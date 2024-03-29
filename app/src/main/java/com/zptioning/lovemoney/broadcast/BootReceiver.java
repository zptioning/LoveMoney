package com.zptioning.lovemoney.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zptioning.lovemoney.MainActivity;
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
                new NotificationUtils().handleMedicine(context);
                break;
        }
    }


}
