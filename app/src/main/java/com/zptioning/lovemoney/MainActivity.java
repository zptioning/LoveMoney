package com.zptioning.lovemoney;

/**
 * @ClassName MainActivity
 * @Author zptioning
 * @Date 2019-11-01 17:25
 * @Version 1.0
 * @Description
 */
public class MainActivity extends BaseActivity {

    @Override
    protected BaseFragment getFragment() {
        return MainFragment.getInstance();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
