package com.zptioning.module_funds;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * A helper class to manage database creation and version management
 */
public class FundsDBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = FundsDBOpenHelper.class.getSimpleName() + "tag";

    // 数据库名称
    private static final String DB_NAME = "stocks.db";

    // 数据库版本
    private static final int DB_VERSION = 1;

    // 数据库表名称
    public static final String TABLE_NAME_STOCK = "stock";
    public static final String TABLE_NAME_OTHER = "other";


    /**
     * 构造方法
     *
     * @param context
     */
    public FundsDBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableIfNotExist(db, TABLE_NAME_STOCK);
    }

    /**
     * Called when the database needs to be upgraded
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade ==> oldVersion:" + oldVersion + "   newVersion:" + newVersion);
    }



    /*************************************************************************/
    /*********** 一下方法自定义添加提供给外部使用 ************/
    /*************************************************************************/
    /**
     * 查询所有表
     *
     * @param db
     * @return
     */
    public Cursor getAllTables(SQLiteDatabase db) {
        return db.rawQuery("select name from sqlite_master where type='table' order by name",
                null);
    }

    /**
     * 股票的表
     *
     * @return
     */
    public void createTableIfNotExist(SQLiteDatabase db, String tableName) {

        StringBuilder strContent = new StringBuilder();
        strContent.append(" ").append("_id").append(" ").append("INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(" ").append("_index").append(" ").append("int,")// md index前面没有下划线会报错
                .append(" ").append("time").append(" ").append("text,")
                .append(" ").append("code").append(" ").append("text,")
                .append(" ").append("name").append(" ").append("text,")
                .append(" ").append("cost").append(" ").append("text,")// 成本价
                .append(" ").append("price").append(" ").append("text,")// 市场价
                .append(" ").append("rate").append(" ").append("text,")// 价格差
                .append(" ").append("count").append(" ").append("int,")
                .append(" ").append("operation").append(" ").append("int,")// 1:买入  2:卖出
                .append(" ").append("status").append(" ").append("int,")// 1:持有  2:非持有
                .append(" ").append("hold").append(" ").append("int,")// 持有
                .append(" ").append("sold").append(" ").append("int")
                .append(" ");// 卖出

        String createStatement = new StringBuilder().append(" CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" ( ")
                .append(strContent)
                .append(" ); ").toString();

        db.execSQL(createStatement);
        Log.d(TAG, "onCreate ==> " + createStatement);
    }

//    /**
//     * 创建表
//     *
//     * @param db
//     * @param tableName
//     */
//    public void createTable(SQLiteDatabase db, String tableName) {
//        createTableIfNotExist(db, tableName);
//    }

    /**
     * 删除表
     *
     * @param db
     * @param tableName
     */
    public void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("drop table " + tableName);
    }

    /**
     * 清楚表
     * @param db
     * @param tableName
     */
    public void clearTable(SQLiteDatabase db, String tableName) {
        db.execSQL("delete from " + tableName);
    }
}
