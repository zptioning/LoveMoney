package com.zptioning.lovemoney;

import android.os.Bundle;

public class HistoryActivity extends BaseActivity {

    private String mCode;
    private String mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCode = getIntent().getStringExtra("code");
        mIndex = getIntent().getStringExtra("index");
        super.onCreate(savedInstanceState);

    }

    @Override
    protected BaseFragment getFragment() {
        return HistoryFragment.getInstance(mCode, mIndex);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
