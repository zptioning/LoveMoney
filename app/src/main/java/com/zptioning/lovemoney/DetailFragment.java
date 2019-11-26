package com.zptioning.lovemoney;


import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zptioning.module_funds.Datautils;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.adapter.StocksAdapter;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import java.util.List;

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

    // 当前页面 股票代码 也是对应股票 数据库中表的名字
    private static String sCode;
    private Uri mUri = Datautils.addOtherFragment(sCode);
    private StockObserver mStockObserver;

    public static DetailFragment getInstance(String code) {
        sCode = code;
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
        mRootView.findViewById(R.id.rg_parent).setVisibility(View.GONE);
        mRootView.findViewById(R.id.et_code).setVisibility(View.GONE);
        Button btnInsert = mRootView.findViewById(R.id.btn_insert);
        btnInsert.setText("新买一组");
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockEntity stockEntity = new StockEntity();
                int itemCount = mStocksAdapter.getItemCount();
                if (0 == itemCount) {
                    stockEntity.index = 1;
                } else {
                    stockEntity.index = itemCount + 1;
                }
                stockEntity.code = sCode;
                new OperationPopWindow(_mActivity, stockEntity)
                        .setOnBuyListener(new OperationPopWindow.OnBuyListener() {
                            @Override
                            public void onBuy(StockEntity stockEntity) {
                                // 新买入操作
                                Datautils.insert(mContentResolver, mUri, stockEntity);
                            }
                        })
                        .setOnlyBuy(true).show(_mActivity);
            }
        });

        mStockObserver = new StockObserver(new Handler());
        mContentResolver.registerContentObserver(mUri, true, mStockObserver);
    }

    @Override
    protected void initAdapter() {
        mStocksAdapter = new StocksAdapter(mType, null,
                new OperationPopWindow.OnBuyListener() {
                    @Override
                    public void onBuy(StockEntity stockEntity) {
                        // 买入操作
                        Datautils.update(mContentResolver, mUri, stockEntity);
                    }
                },
                new OperationPopWindow.OnSellListener() {
                    @Override
                    public void onSell(StockEntity stockEntity) {
                        // 卖出操作
                        Datautils.update(mContentResolver, mUri, stockEntity);
                    }
                });
    }

    /**
     * 更新所有数据的涨跌幅
     */
    @Override
    protected void updateAllData() {
        List<StockEntity> stockEntities = Datautils.queryAllStocks(mContentResolver, mUri);
        if (null == stockEntities || stockEntities.size() == 0) {
            return;
        }
        Log.e("updateAllData_tag", stockEntities.toString());
        StockEntity remoteData = Datautils.getRemoteData(sCode);
        if (null != remoteData) {
            for (int i = 0; i < stockEntities.size(); i++) {
                StockEntity stockEntity = stockEntities.get(i);
                stockEntity.price = remoteData.price;
                stockEntity.rate = Datautils.getRate(stockEntity.cost, stockEntity.price);
            }
        }


        mStocksAdapter.replaceData(stockEntities);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentResolver.unregisterContentObserver(mStockObserver);
    }

    class StockObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public StockObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            updateAllData();
        }
    }
}
