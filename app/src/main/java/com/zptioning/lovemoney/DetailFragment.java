package com.zptioning.lovemoney;


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
import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.adapter.DetailAdapter;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import java.math.BigDecimal;
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
    private static BigDecimal sPrice;
    private Uri mUri = Datautils.addOtherFragment(sCode);
    private StockObserver mStockObserver;

    public static DetailFragment getInstance(String code, BigDecimal price) {
        sCode = code;
        sPrice = price;
        return new DetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mType = StockConstants.TYPE_DETAIL;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initTopWidgets() {
        mRootView.findViewById(R.id.rg_parent).setVisibility(View.GONE);
        mRootView.findViewById(R.id.et_code).setVisibility(View.GONE);
        Button btnInsert = mRootView.findViewById(R.id.btn_insert);
        btnInsert.setText("新买一组");
        btnInsert.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                StockEntity stockEntity = new StockEntity();
                int itemCount = mBaseAdapter.getItemCount();
                if (0 == itemCount) {
                    stockEntity.index = 1;
                } else {
                    stockEntity.index = itemCount + 1;
                }
                stockEntity.code = sCode;
                stockEntity.price = sPrice;
                new OperationPopWindow(_mActivity, stockEntity)
                        .setOnBuyListener(new OperationPopWindow.OnBuyListener() {
                            @Override
                            public void onBuy(StockEntity stockEntity) {
                                // 新买入操作
                                Datautils.insert(mContentResolver,
                                        Datautils.addOtherFragment(stockEntity.code), stockEntity);
                                Datautils.insert(mContentResolver,
                                        Datautils.addOtherFragment(stockEntity.code + "_" + stockEntity.index), stockEntity);
                            }
                        })
                        .setOnlyBuy(true).show(_mActivity);
                return true;
            }
        });

        mStockObserver = new StockObserver(new Handler());
        mContentResolver.registerContentObserver(mUri, true, mStockObserver);
    }

    @Override
    protected void initAdapter() {
        mBaseAdapter = new DetailAdapter(mType, null,
                new OperationPopWindow.OnBuyListener() {
                    @Override
                    public void onBuy(StockEntity stockEntity) {
                        // 买入操作
                        Datautils.update(mContentResolver, mUri, stockEntity);
                        Datautils.insert(mContentResolver,
                                Datautils.addOtherFragment(stockEntity.code + "_" + stockEntity.index), stockEntity);
                    }
                },
                new OperationPopWindow.OnSellListener() {
                    @Override
                    public void onSell(StockEntity stockEntity) {
                        // 卖出操作
                        Datautils.update(mContentResolver, mUri, stockEntity);
                        Datautils.insert(mContentResolver,
                                Datautils.addOtherFragment(stockEntity.code + "_" + stockEntity.index), stockEntity);
                    }
                });
    }

    /**
     * 更新所有数据的涨跌幅
     */
    @Override
    protected void updateAllData() {
        super.updateAllData();
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


        mBaseAdapter.replaceData(stockEntities);
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
