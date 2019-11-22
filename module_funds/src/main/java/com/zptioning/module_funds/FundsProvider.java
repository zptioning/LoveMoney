package com.zptioning.module_funds;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 提供操作数据app
 * <p>
 * String uri = "http://www.zpan.com:8080/lujing/path.htm?id=10&name=zhangsan&old=24#zuihoude";
 * Uri mUri = Uri.parse(uri);
 * <p>
 * // 协议
 * String scheme = mUri.getScheme(); // http
 * // 域名+端口号+路径+参数
 * String scheme_specific_part = mUri.getSchemeSpecificPart();// //www.zpan.com:8080/lujing/path.htm?id=10&name=zhangsan&old=24
 * // 域名+端口号
 * String authority = mUri.getAuthority();// www.zpan.com:8080
 * // fragment
 * String fragment = mUri.getFragment();// zuihoude
 * // 域名
 * String host = mUri.getHost();// www.zpan.com
 * // 端口号
 * int port = mUri.getPort();// 8080
 * // 路径
 * String path = mUri.getPath();// /lujing/path.htm
 * // 参数
 * String query = mUri.getQuery();// id=10&name=zhangsan&old=24
 * <p>
 * Log.e("zpan", "======协议===scheme ==" + scheme);
 * E/zpan: ======协议===scheme ==http
 * Log.e("zpan", "======域名+端口号+路径+参数==scheme_specific_part ===" + scheme_specific_part);
 * E/zpan: ======域名+端口号+路径+参数==scheme_specific_part ===//www.zpan.com:8080/lujing/path.htm?id=10&name=zhangsan&old=24
 * Log.e("zpan", "======域名+端口号===authority ==" + authority);
 * E/zpan: ======域名+端口号===authority ==www.zpan.com:8080
 * Log.e("zpan", "======fragment===fragment ==" + fragment);
 * E/zpan: ======fragment===fragment ==zuihoude
 * Log.e("zpan", "======域名===host ==" + host);
 * E/zpan: ======域名===host ==www.zpan.com
 * Log.e("zpan", "======端口号===port ==" + port);
 * E/zpan: ======端口号===port ==8080
 * Log.e("zpan", "======路径===path ==" + path);
 * E/zpan: ======路径===path ==/lujing/path.htm
 * Log.e("zpan", "======参数===query ==" + query);
 * E/zpan: ======参数===query ==id=10&name=zhangsan&old=24
 * <p>
 * // 依次提取出Path的各个部分的字符串，以字符串数组的形式输出
 * List<String> pathSegments = mUri.getPathSegments();
 * for (String str : pathSegments) {
 * Log.e("zpan", "======路径拆分====path ==" + str);
 * E/zpan: ======路径拆分====path ==lujing
 * E/zpan: ======路径拆分====path ==path.htm
 * }
 * // 获得所有参数 key
 * Set<String> params = mUri.getQueryParameterNames();
 * for(String param: params) {
 * Log.e("zpan","=====params=====" + param);
 * E/zpan: =====params=====id
 * E/zpan: =====params=====name
 * E/zpan: =====params=====old
 * }
 * <p>
 * // 根据参数的 key，取出相应的值
 * String id = mUri.getQueryParameter("id");
 * String name = mUri.getQueryParameter("name");
 * String old = mUri.getQueryParameter("old");
 * <p>
 * Log.e("zpan", "======参数===id ==" + id);
 * E/zpan: ======参数===id ==10
 * Log.e("zpan", "======参数===name ==" + name);
 * E/zpan: ======参数===name ==zhangsan
 * Log.e("zpan", "======参数===old ==" + old);
 * E/zpan: ======参数===old ==24
 */
public class FundsProvider extends ContentProvider {


    static {
        String uri = "http://www.zpan.com:8080/lujing/path.htm?id=10&name=zhangsan&old=24#zuihoude";
        Uri mUri = Uri.parse(uri);
        // 协议
        String scheme = mUri.getScheme(); // http
        // 域名+端口号+路径+参数
        String scheme_specific_part = mUri.getSchemeSpecificPart();//
        // www.zpan.com:8080/lujing/path.htm?id=10&name=zhangsan&old=24

        // 域名+端口号
        String authority = mUri.getAuthority();// www.zpan.com:8080
        // fragment
        String fragment = mUri.getFragment();// zuihoude
        // 域名
        String host = mUri.getHost();// www.zpan.com
        // 端口号
        int port = mUri.getPort();// 8080
        // 路径
        String path = mUri.getPath();// /lujing/path.htm
        // 参数
        String query = mUri.getQuery();// id=10&name=zhangsan&old=24
    }


    private static final String TAG = FundsProvider.class.getSimpleName() + "_tag";

    /* authority */
    public static final String AUTHORITY = "com.zption.funds.provider";
    /* path */
    private static final String STOCK_PATH = "stock";
    private static final String OTHER_PATH = "other";

    // 主页面
    public static final Uri STOCK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + STOCK_PATH);
    // 详情页
    public static final Uri OTHER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OTHER_PATH);

    public static final int STOCK_URI_CODE = 1;
    public static final int OTHER_URI_CODE = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, STOCK_PATH, STOCK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, OTHER_PATH, OTHER_URI_CODE);
    }

    private Context mContext;
    /**
     * A helper to manage database creation and version management
     */
    private FundsDBOpenHelper mDBHelper;
    private SQLiteDatabase mDb;

    /**
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate ==> ");
        mContext = getContext();
        mDBHelper = new FundsDBOpenHelper(mContext);
        mDb = mDBHelper.getReadableDatabase();
        return true;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record, or
     * <code>vnd.android.cursor.dir/</code> for multiple items.
     * <p>
     * 关于MIME类型，android 是这么规定的。
     * 必须以vnd开头
     * 如果是多条记录，后面接android.cursor.dir/,
     * 如果是单条记录，后面接android.cursor.item/
     * 最后 加上"vnd.<authority>.<path>"
     *
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType ==> " + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case STOCK_URI_CODE:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + STOCK_PATH;
            case OTHER_URI_CODE:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + OTHER_PATH;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    /**
     * 添加
     *
     * @return The URI for the newly inserted item
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);

        if (TextUtils.isEmpty(tableName) || null == values) {
            return null;
        }

//        // 如果要处理的是Other表 则加这个操作，如果表不存在，先创建表
//        if (TextUtils.equals(tableName, FundsDBOpenHelper.TABLE_NAME_OTHER)) {
//
//            String code = uri.getFragment();
//            if (!TextUtils.isEmpty(code)) {
//                _CreateTableIfNotExist(code);
//                tableName = code;
//            } else {
//                // code 为空的话 返回id 设置为 -2；
//                Uri uri1 = ContentUris.withAppendedId(uri, -2);
//                return uri1;
//            }
//        }

        long insert = mDb.insert(tableName, null, values);
        if (-1 != insert) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        Uri uri1 = ContentUris.withAppendedId(uri, insert);
        Log.d(TAG, "insert ==> tablename: " + tableName + " : " + uri1.toString());
        return uri1;
    }

    /**
     * 删除
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);

        int delete = mDb.delete(tableName, selection, selectionArgs);
        if (delete > 0) {
            // 删除数据后通知改变
            mContext.getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "delete ==> " + delete);

        return delete;
    }

    /**
     * 更新
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);

        int update = mDb.update(tableName, values, selection, selectionArgs);
        if (update > 0) {
            // 更新数据通知改变
            mContext.getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "update ==> " + update);

        return update;
    }

    /**
     * 查询
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        String tableName = getTableName(uri);

        Cursor queryCursor = mDb.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                null);

        if (null != queryCursor) {
            Log.d(TAG, "query ==> " + queryCursor.getCount());
        }

        return queryCursor;
    }

    /**
     * 根据uri 决定操作哪张表
     *
     * @param uri
     * @return 表name
     */
    private String getTableName(Uri uri) {
        if (null == uri) {
            return null;
        }
        switch (sUriMatcher.match(uri)) {
            case STOCK_URI_CODE:
                return FundsDBOpenHelper.TABLE_NAME_STOCK;
            case OTHER_URI_CODE:
                return FundsDBOpenHelper.TABLE_NAME_OTHER;
        }

        long parseId = ContentUris.parseId(uri);
        return String.valueOf(parseId);
    }

    /**
     * 提供的远程调用的方法 都以下划线开头第一个字母大写
     *
     * @param method
     * @param arg
     * @param extras
     * @return
     */
    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle bundle = new Bundle();
        String strContent = null;

        switch (method) {
            case "_queryAllTables":
                _IterateDatabase();
                break;
            case "_CreateTableIfNotExist":
                if (null != extras) {
                    String table_name = extras.getString("table_name");
                    _CreateTableIfNotExist(table_name);
                }
                break;
            case "_DeleteTable":
                if (null != extras) {
                    String table_name = extras.getString("table_name");
                    _DeleteTable(table_name);
                }
                break;
            case "_ClearTable":
                if (null != extras) {
                    String table_name = extras.getString("table_name");
                    _ClearTable(table_name);
                }
                break;
        }


        bundle.putString("content", strContent);
        return bundle;
    }

    /**
     * 查询所有表名称
     *
     * @return
     */
    private Cursor _getAllTables() {
        return mDBHelper.getAllTables(mDb);
    }

    /**
     * 遍历表名
     */
    private void _IterateDatabase() {
        Cursor cursor = _getAllTables();
        Log.i(TAG, "_IterateDatabase ==> begin >>>>>>>>>>>");
        while (cursor.moveToNext()) {
            //遍历出表名
            String name = cursor.getString(0);
            Log.i(TAG, "_IterateDatabase ==> name: " + name + ".db");
        }
        cursor.close();
        Log.i(TAG, "_IterateDatabase ==> end <<<<<<<<<<<<");
    }

    /**
     * 创建表
     *
     * @param tableName
     */
    public void _CreateTableIfNotExist(String tableName) {
        mDBHelper.createTableIfNotExist(mDb, tableName);
    }

    /**
     * 删除表
     *
     * @param tableName
     */
    public void _DeleteTable(String tableName) {
        mDBHelper.deleteTable(mDb, tableName);
    }

    /**
     * 清空表
     *
     * @param tableName
     */
    public void _ClearTable(String tableName) {
        mDBHelper.clearTable(mDb, tableName);
    }

}
