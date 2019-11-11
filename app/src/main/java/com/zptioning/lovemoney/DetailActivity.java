package com.zptioning.lovemoney;

import android.os.Bundle;

public class DetailActivity extends BaseActivity {

    private String mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCode = getIntent().getStringExtra("code");
        setTitle(mCode);
    }

    @Override
    protected BaseFragment getFragment() {
        return DetailFragment.getInstance();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }
}
