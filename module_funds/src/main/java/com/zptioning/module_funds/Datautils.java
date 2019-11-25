package com.zptioning.module_funds;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.zptioning.module_funds.net_work.HttpUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Datautils
 * @Author zptioning
 * @Date 2019-11-04 14:28
 * @Version 1.0
 * @Description strContent.append(" _id ").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
 * .append(" _index ").append(" int, ")// md index前面没有下划线会报错
 * .append(" time ").append(" text, ")
 * .append(" code ").append(" text, ")
 * .append(" name ").append(" text, ")
 * .append(" cost ").append(" text, ")// 成本价
 * .append(" price ").append(" text, ")// 市场价
 * .append(" rate ").append(" text, ")// 价格差
 * .append(" count ").append(" int,")
 * .append(" operation ").append(" int, ")// 1:买入  2:卖出
 * .append(" status ").append(" int ")// 1:持有  2:非持有
 * .append(" hold ").append(" int ")// 持有
 * .append(" sold ").append(" int ");// 卖出
 */
public class Datautils {

    private static final String TAG = Datautils.class.getSimpleName() + "_tag";

    private static final String COL_ID = "_id";
    private static final String COL_INDEX = "_index";
    private static final String COL_TIME = "time";
    private static final String COL_CODE = "code";
    private static final String COL_NAME = "name";
    private static final String COL_COST = "cost";
    private static final String COL_PRICE = "price";
    private static final String COL_RATE = "rate";
    private static final String COL_COUNT = "count";
    private static final String COL_OPERATION = "operation";
    private static final String COL_STATUS = "status";
    private static final String COL_HOLD = "hold";
    private static final String COL_SOLD = "sold";

    /* 查询地址 */
    private static final String codeAddress = "http://hq.sinajs.cn/list=";

    public static StockEntity getRemoteData(String strCode) {
        String url = codeAddress + strCode;
        String strResult = HttpUtils.getInstance().httpGetResult(url);
        return handleResult(strResult, strCode);
    }

    /**
     * 处理查询股票结果
     *
     * @param strResult
     * @param strCode   var hq_str_sh600100=
     *                  "同方股份,8.180,8.240,8.200,8.290,8.140,8.190,
     *                  8.200,20270689,166541422.000,206000,8.190,252600,8.180,163800,8.170,83800,
     *                  8.160,111800,8.150,200,8.200,71539,8.210,59700,8.220,79000,8.230,243300,
     */
    private static StockEntity handleResult(String strResult, String strCode) {
        if (TextUtils.isEmpty(strResult)) {
            return null;
        }
        int start = strResult.indexOf("\"");
        int end = strResult.lastIndexOf("\"");
        String realResult = strResult.substring(start + 1, end);
        if (TextUtils.isEmpty(realResult)) {
            return null;
        }
        String[] split = realResult.split(",");
        if (null == split || split.length <= 0) {
            return null;
        }

        StockEntity stockEntity = new StockEntity();
        stockEntity.index = 0;
        stockEntity.time = split[30];
        stockEntity.code = strCode;
        stockEntity.name = split[0];
        stockEntity.cost = new BigDecimal("0");
        stockEntity.price = new BigDecimal(split[3]);
        stockEntity.rate = new BigDecimal("0");
        stockEntity.count = 0;
        stockEntity.operation = 0;
        stockEntity.status = 2;
        stockEntity.hold = 0;
        stockEntity.sold = 0;

        return stockEntity;
    }


    /**
     * 增：插入数据 到总表中
     *
     * @param contentResolver
     * @param stockEntity     return null 如果股票已经存在
     */
    public static Uri insert(ContentResolver contentResolver, Uri uri, StockEntity stockEntity) {
        ContentValues values = new ContentValues();
        List<StockEntity> stockEntities = queryAllStocks(contentResolver, uri);

        for (StockEntity entity : stockEntities) {
            if (TextUtils.equals(stockEntity.code, entity.code)) {
                return null;
            }
        }

        values.put(COL_INDEX, null == stockEntities ? 0 : stockEntities.size() + 1);
        values.put(COL_TIME, stockEntity.time);
        values.put(COL_CODE, stockEntity.code);
        values.put(COL_NAME, stockEntity.name);
        values.put(COL_COST, stockEntity.cost.toString());
        values.put(COL_PRICE, stockEntity.price.toString());
        values.put(COL_RATE, stockEntity.rate.toString());
        values.put(COL_COUNT, stockEntity.count);
        values.put(COL_OPERATION, stockEntity.operation);
        values.put(COL_STATUS, stockEntity.status);
        values.put(COL_HOLD, stockEntity.hold);
        values.put(COL_SOLD, stockEntity.sold);
        Uri insert = contentResolver.insert(uri, values);
        Log.d(TAG, insert.toString());
        return insert;
    }

    /**
     * 删：删除数据
     *
     * @param contentResolver
     * @param code
     */
    public static void delete(ContentResolver contentResolver, Uri uri, String code) {
        int delete = contentResolver.delete(uri,
                "code = ?", new String[]{code});
    }

    /**
     * 改：更新数据
     *
     * @param contentResolver
     * @param stockEntity
     */
    public static void update(ContentResolver contentResolver, Uri uri, StockEntity stockEntity) {
        ContentValues values = new ContentValues();
        values.put(COL_INDEX, stockEntity.index);
        values.put(COL_TIME, stockEntity.time);
        values.put(COL_NAME, stockEntity.name);
        values.put(COL_COST, stockEntity.cost.toString());
        values.put(COL_PRICE, stockEntity.price.toString());
        values.put(COL_RATE, stockEntity.rate.toString());
        values.put(COL_COUNT, stockEntity.count);
        values.put(COL_OPERATION, stockEntity.operation);
        values.put(COL_STATUS, stockEntity.status);
        values.put(COL_HOLD, stockEntity.hold);
        values.put(COL_SOLD, stockEntity.sold);

        int update = contentResolver.update(uri, values,
                "code = ?", new String[]{stockEntity.code});
    }

    /**
     * 查：查询数据
     *
     * @param contentResolver
     * @return
     */
    public static List<StockEntity> queryAllStocks(ContentResolver contentResolver, Uri uri) {

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (null == cursor) {
            return null;
        }

        ArrayList<StockEntity> stocksList = null;
        stocksList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            int index = cursor.getInt(cursor.getColumnIndex(COL_INDEX));
            String time = cursor.getString(cursor.getColumnIndex(COL_TIME));
            String code = cursor.getString(cursor.getColumnIndex(COL_CODE));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String cost = cursor.getString(cursor.getColumnIndex(COL_COST));
            String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
            String rate = cursor.getString(cursor.getColumnIndex(COL_RATE));
            int count = cursor.getInt(cursor.getColumnIndex(COL_COUNT));
            int operation = cursor.getInt(cursor.getColumnIndex(COL_OPERATION));
            int status = cursor.getInt(cursor.getColumnIndex(COL_STATUS));
            int hold = cursor.getInt(cursor.getColumnIndex(COL_HOLD));
            int sold = cursor.getInt(cursor.getColumnIndex(COL_SOLD));

            StockEntity stockEntity = new StockEntity();
            stockEntity.index = index;
            stockEntity.time = time;
            stockEntity.code = code;
            stockEntity.name = name;
            stockEntity.cost = new BigDecimal(cost);
            stockEntity.price = new BigDecimal(price);
            stockEntity.rate = new BigDecimal(rate);
            stockEntity.count = count;
            stockEntity.operation = operation;
            stockEntity.status = status;
            stockEntity.hold = hold;
            stockEntity.sold = sold;

            stocksList.add(stockEntity);
        }

        cursor.close();
        return stocksList;
    }

    /**
     * 创建数据表
     *
     * @param contentResolver
     */
    public static void createTable(ContentResolver contentResolver, /*Uri uri,*/ String tableName) {
        try {
            Bundle bundle = new Bundle();

            Uri uri = Uri.parse("content://" + FundsProvider.AUTHORITY);
            Bundle callBundle = contentResolver.call(uri, "createTable", tableName, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给uri 添加 fragment
     *
     * @param uri
     * @param fragment
     * @return
     */
    public static Uri addFragment(Uri uri, String fragment) {
        return Uri.parse(uri.toString() + "#" + fragment);
    }

    /**
     * 查询所有的表
     *
     * @param contentResolver
     */
    public static void queryAllTables(ContentResolver contentResolver) {
        try {
            Bundle bundle = new Bundle();
            Uri uri = Uri.parse("content://" + FundsProvider.AUTHORITY);
            Bundle callBundle = contentResolver.call(uri, "_queryAllTables", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
