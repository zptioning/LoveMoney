package com.zptioning.lovemoney;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.adapter.HistoryAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HistoryFragment extends BaseFragment {

    private static String sCode;
    private static String sIndex;

    public static HistoryFragment getInstance(String code, String index) {
        sCode = code;
        sIndex = index;
        return new HistoryFragment();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mType = StockConstants.TYPE_HISTORY;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initTopWidgets() {
        mLLTop.setVisibility(View.GONE);
    }

    @Override
    protected void initAdapter() {
        mBaseAdapter = new HistoryAdapter(mType, null);
    }

    @Override
    protected void updateAllData() {

    }

    @Override
    protected void updateLines(TextView viewById, StockInterface.ENUM_TITLES enum_title) {
        switch (enum_title.name()) {
            // 执行买卖操作
            case "INDEX":
                break;
            case "TIME":
                break;
            case "CODE":
                break;
            case "NAME":
                viewById.setVisibility(View.GONE);
                break;
            case "COST":
                break;
            case "PRICE":
                break;
            case "RATE":
                break;
            case "COUNT":
                break;
            case "OPTION":
                viewById.setVisibility(View.GONE);
                break;
            case "SOLD":
                viewById.setVisibility(View.GONE);
                break;
            case "HOLD":
                viewById.setVisibility(View.GONE);
                break;
            case "STATUS":
                viewById.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void queryResult(ContentResolver contentResolver, Uri stockContentUri) {

    }
}
