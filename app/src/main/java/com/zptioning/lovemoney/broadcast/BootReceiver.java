package com.zptioning.lovemoney.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.zptioning.lovemoney.MainActivity;
import com.zptioning.module_funds.Datautils;
import com.zptioning.module_widgets.notification.NotificationUtils;

/**
 * @ClassName BootReceiver
 * @Author zptioning
 * @Date 2019-11-27 18:40
 * @Version 1.0
 * @Description <action android:name="android.intent.action.BOOT_COMPLETED"/>
 * <action android:name="android.intent.action.BATTERY_CHANGED"/>
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                Intent intent1 = new Intent(context, MainActivity.class);
                context.startActivity(intent1);
                Toast.makeText(context, "boot", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_TIME_TICK:
                handleMedicine(context);
                break;
        }
    }

    private void handleMedicine(Context context) {
        long timeMillis = System.currentTimeMillis();
        String dateFormat = Datautils.dateFormat("MM-dd HH", timeMillis);
        String hour = Datautils.dateFormat("HH", timeMillis);
        if (hour.equals("20")) {
            // 第一步
            SharedPreferences sp = context.getSharedPreferences(NotificationUtils.SPNAME, Context.MODE_PRIVATE);
            int count = sp.getInt(NotificationUtils.KEY_COUNT, 0);
            String date = sp.getString(NotificationUtils.KEY_TIME1, null);
            if (!TextUtils.equals(dateFormat, date) && count % 4 == 0) {
                new NotificationUtils().sendMedicine(context, dateFormat, "RGXMZ", NotificationUtils.NOTIFICATION_ID_RRXMZ);
            }
            count++;
            sp.edit().putInt(NotificationUtils.KEY_COUNT, count).commit();
            // 第二步
            date = sp.getString(NotificationUtils.KEY_TIME, null);
            if (!TextUtils.equals(dateFormat, date)) {// 当前已经发送通知了 则返回
                new NotificationUtils().sendMedicine(context, dateFormat, NotificationUtils.NOTIFICATION_ID_STOCK);
            }
        }
    }
}
