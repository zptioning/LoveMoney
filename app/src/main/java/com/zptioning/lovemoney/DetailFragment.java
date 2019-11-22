package com.zptioning.lovemoney;


import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ClassName DetailFragment
 * @Author zptioning
 * @Date 2019-11-04 11:37
 * @Version 1.0
 * @Description
 */
public class DetailFragment extends BaseFragment {

    public static DetailFragment getInstance() {
        return new DetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mType = TYPE_DETAIL_FRAGEMNT;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initTopWidgets() {
        mLLTop.setVisibility(View.GONE);
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
                viewById.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 详情页
                        new OperationPopWindow(_mActivity).show(_mActivity,new StockEntity());
                    }
                });
                break;
            case "SOLD":
                viewById.setVisibility(View.GONE);
                break;
            case "HOLD":
                viewById.setVisibility(View.GONE);
                break;
            case "STATUS":
                break;
            default:
                break;
        }
    }

    @Override
    protected void queryResult(ContentResolver contentResolver, Uri stockContentUri) {

    }
}
