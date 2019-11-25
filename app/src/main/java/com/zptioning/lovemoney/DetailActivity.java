package com.zptioning.lovemoney;

import android.os.Bundle;

public class DetailActivity extends BaseActivity {

    private String mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCode = getIntent().getStringExtra("code");
        super.onCreate(savedInstanceState);
        setTitle(mCode);
    }

    @Override
    protected BaseFragment getFragment() {
        return DetailFragment.getInstance(mCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }
}
