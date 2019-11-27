package com.zptioning.lovemoney.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.zptioning.module_widgets.notification.NotificationUtils;

public class ForegroundService extends Service {
    private final String TAG = ForegroundService.class.getSimpleName() + "_tag";

    public ForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,TAG + "onCreate !!!!");
        Notification.Builder builder = new NotificationUtils().getBuilder(this, "service", "foreground");
        startForeground(NotificationUtils.NOTIFICATION_ID_FOREGROUND_SERVICE, builder.build());
    }
}
