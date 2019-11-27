package com.zptioning.lovemoney;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.zptioning.lovemoney.broadcast.BootReceiver;
import com.zptioning.module_widgets.notification.NotificationUtils;

/**
 * @ClassName MainActivity
 * @Author zptioning
 * @Date 2019-11-01 17:25
 * @Version 1.0
 * @Description
 */
public class MainActivity extends BaseActivity {

    private BootReceiver mBootReceiver;

    @Override
    protected BaseFragment getFragment() {
        return MainFragment.getInstance();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new NotificationUtils().handleMedicineImmediately(this);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化

        mBootReceiver = new BootReceiver();
        registerReceiver(mBootReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBootReceiver);
    }
}

