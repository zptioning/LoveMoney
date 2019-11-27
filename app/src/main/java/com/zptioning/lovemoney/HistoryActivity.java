package com.zptioning.lovemoney;

import android.os.Bundle;

import java.math.BigDecimal;

public class HistoryActivity extends BaseActivity {

    private String mCode;
    private int mIndex;
    private BigDecimal mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCode = getIntent().getStringExtra("code");
        mIndex = getIntent().getIntExtra("index", -1);
        mPrice = (BigDecimal) getIntent().getSerializableExtra("price");
        super.onCreate(savedInstanceState);
        setTitle(mCode + "_" + mIndex);
    }

    @Override
    protected BaseFragment getFragment() {
        return HistoryFragment.getInstance(mCode, String.valueOf(mIndex), mPrice);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
