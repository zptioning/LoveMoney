package com.zptioning.lovemoney;

import android.os.Bundle;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetailActivity extends BaseActivity {

    private String mCode;
    private BigDecimal mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCode = getIntent().getStringExtra("code");
        mPrice = (BigDecimal) getIntent().getSerializableExtra("price");
        super.onCreate(savedInstanceState);
        setTitle(mCode);
    }

    @Override
    protected BaseFragment getFragment() {
        return DetailFragment.getInstance(mCode,mPrice);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }
}
