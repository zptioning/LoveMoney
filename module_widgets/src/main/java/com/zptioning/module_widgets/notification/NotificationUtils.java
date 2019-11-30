package com.zptioning.module_widgets.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.zptioning.module_funds.DataUtils;

/**
 * 通知管理工具类
 */
public class NotificationUtils {

    public static final int NOTIFICATION_ID_STOCK = 1;
    public static final int NOTIFICATION_ID_FOREGROUND_SERVICE = 2;
    public static final int NOTIFICATION_ID_RRXMZ = 3;

    public static final String SPNAME = "zption_sp";
    public static final String KEY_DD = "dd";
    public static final String KEY_TIME = "time";
    public static final String KEY_TIME1 = "time1";
    public static final String VALUE_ZZJN = "ZZJN";
    public static final String VALUE_HLS = "HLS";

    public void sendNotification(Context context,
                                 String contentTitle,
                                 String contentText,
                                 int id) {
        // 获取系统 通知管理 服务
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 构建 Notification
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);
        // 兼容 api26、Android 8.0
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            // 第三个参数表示通知的重要程度
            NotificationChannel notificationChannel
                    = new NotificationChannel("dao_id", "dao_name", NotificationManager.IMPORTANCE_DEFAULT);
            // 注册通道，注册后除非卸载再安装否则不改变
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("dao_id");
        }

        // 手动删不掉
        builder.setOngoing(true);
        // 添加默认声音提醒
        builder.setDefaults(Notification.DEFAULT_SOUND);
        // 添加默认呼吸灯提醒，自动添加FLAG_SHOW_LIGHTS
        builder.setDefaults(Notification.DEFAULT_LIGHTS);

        // 点击通知后跳转到MainActivity
        try {
            Intent intent = new Intent(context, Class.forName("com.zptioning.lovemoney.MainActivity"));
            int requestCode = 0;
            int flags = PendingIntent.FLAG_ONE_SHOT;
            PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, flags);
            builder.setContentIntent(pendingIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 发出通知
        notificationManager.notify(id, builder.build());
    }

    public Notification.Builder getBuilder(Context context,
                                           String contentTitle,
                                           String contentText) {
        // 获取系统 通知管理 服务
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 构建 Notification
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);
        // 兼容 api26、Android 8.0
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            // 第三个参数表示通知的重要程度
            NotificationChannel notificationChannel
                    = new NotificationChannel("dao_id", "dao_name", NotificationManager.IMPORTANCE_DEFAULT);
            // 注册通道，注册后除非卸载再安装否则不改变
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("dao_id");
        }

        // 点击通知后跳转到MainActivity
        try {
            Intent intent = new Intent(context, Class.forName("com.zptioning.lovemoney.MainActivity"));
            int requestCode = 0;
            int flags = PendingIntent.FLAG_ONE_SHOT;
            PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, flags);
            builder.setContentIntent(pendingIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return builder;
    }

    /**
     * 发送通知
     *
     * @param context
     * @param dateFormat
     * @param context
     * @param notificationIdStock
     */
    public void sendMedicine(Context context, String dateFormat, long day, String strContext, int notificationIdStock) {
        SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(KEY_TIME1, day).commit();
        sendNotification(context, dateFormat, strContext, notificationIdStock);
    }

    /**
     * @param context
     * @param dateFormat
     * @param notificationIdStock
     */
    public void sendMedicine(Context context, String dateFormat, int notificationIdStock) {
        SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        String dd = sp.getString(KEY_DD, null);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_TIME, dateFormat);
        if (TextUtils.equals(dd, VALUE_ZZJN)) {
            sendNotification(context, dateFormat, "HLS", notificationIdStock);
            edit.putString(KEY_DD, VALUE_HLS);
        } else {
            sendNotification(context, dateFormat, "ZZJN", notificationIdStock);
            edit.putString(KEY_DD, VALUE_ZZJN);
        }
        edit.commit();
    }

    public void handleMedicineImmediately(Context context) {
        long timeMillis = System.currentTimeMillis();
        long today = timeMillis / 1000 / 60 / 60 / 24;
        // 第一步
        SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        long lastDay = sp.getLong(KEY_TIME1, today - 4);
        String dateFormat = DataUtils.dateFormat("MM-dd HH", timeMillis);
        if ((today - lastDay) % 4 == 0) {
            sendNotification(context, dateFormat, "RGXMZ", NOTIFICATION_ID_RRXMZ);
        }

        // 第二步
        String lastDate = sp.getString(KEY_TIME, null);
        String dd = sp.getString(KEY_DD, null);
        if (!TextUtils.equals(dateFormat, lastDate)) {
            if (TextUtils.equals(dd, VALUE_ZZJN)) {
                sendNotification(context, dateFormat, "HLS", NOTIFICATION_ID_STOCK);
            } else {
                sendNotification(context, dateFormat, "ZZJN", NOTIFICATION_ID_STOCK);
            }
        } else {
            sendNotification(context, dateFormat, dd, NOTIFICATION_ID_STOCK);
        }
    }

    public void handleMedicine(Context context) {
        long timeMillis = System.currentTimeMillis();
        String dateFormat = DataUtils.dateFormat("MM-dd HH", timeMillis);
        String hour = DataUtils.dateFormat("HH", timeMillis);
        String minite = DataUtils.dateFormat("mm", timeMillis);
        long today = timeMillis / 1000 / 60 / 60 / 24;
        if (minite.equals("00")) {
            // 第一步
            SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
            long lastDay = sp.getLong(KEY_TIME1, today - 4);
            if (today != lastDay && (today - lastDay) % 4 == 0) {
                sendMedicine(context, dateFormat, today, "RGXMZ", NOTIFICATION_ID_RRXMZ);
            }
            // 第二步
            String date = sp.getString(KEY_TIME, null);
            if (!TextUtils.equals(dateFormat, date)) {// 当前已经发送通知了 则返回
                sendMedicine(context, dateFormat, NOTIFICATION_ID_STOCK);
            }
        }
    }
}
