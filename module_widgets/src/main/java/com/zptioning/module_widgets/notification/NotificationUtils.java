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
//    public static final String KEY_COUNT = "count";
    public static final String VALUE_ZZJN = "zzjn";
    public static final String VALUE_HLS = "hls";

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

        builder.setOngoing(true);

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
    public void sendMedicine(Context context, String dateFormat,long day, String strContext, int notificationIdStock) {
        SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(KEY_TIME1, day).commit();
        sendNotification(context, dateFormat, strContext, notificationIdStock);
    }

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
}
