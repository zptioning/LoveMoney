package com.zptioning.lovemoney;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zptioning.module_funds.Datautils;
import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.adapter.HistoryAdapter;
import com.zptioning.module_widgets.custom_view.CustomItemDecoration;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFragment extends BaseFragment {

    private static String sCode;
    private static String sIndex;
    private static BigDecimal sPrice;

    public static HistoryFragment getInstance(String code, String index, BigDecimal price) {
        sCode = code;
        sIndex = index;
        sPrice = price;
        return new HistoryFragment();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_history;
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
    protected void initRvStocks() {
        mRvStocks = mRootView.findViewById(R.id.rv_stocks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvStocks.setLayoutManager(linearLayoutManager);
        mRvStocks.addItemDecoration(new CustomItemDecoration());
        initAdapter();
        mRvStocks.setAdapter(mBaseAdapter);
    }

    @Override
    protected void updateAllData() {
        super.updateAllData();
        List<StockEntity> stockEntities = Datautils.queryAllStocks(mContentResolver,
                Datautils.addOtherFragment(sCode + "_" + sIndex));

//        StockEntity remoteData = Datautils.getRemoteData(sCode);
//        if (null != remoteData) {
//            for (int i = 0; i < stockEntities.size(); i++) {
//                StockEntity stockEntity = stockEntities.get(i);
//                stockEntity.price = remoteData.price;
//                stockEntity.rate = Datautils.getRate(stockEntity.cost, stockEntity.price);
//            }
//        }
        mBaseAdapter.replaceData(stockEntities);
    }

    @Override
    protected void setColumnTitle(String value, int ordinal, TextView viewById) {
        viewById.setText(value);
        viewById.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
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
                viewById.setVisibility(View.GONE);
                break;
            case "NAME":
                viewById.setVisibility(View.GONE);
                break;
            case "COST":
                break;
            case "PRICE":
                viewById.setVisibility(View.GONE);
                break;
            case "RATE":
                viewById.setVisibility(View.GONE);
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
                break;
            default:
                break;
        }
    }

}
