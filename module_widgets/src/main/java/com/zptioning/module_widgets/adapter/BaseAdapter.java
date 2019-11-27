package com.zptioning.module_widgets.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface.ENUM_TITLES;
import com.zptioning.module_widgets.R;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;
import com.zptioning.module_widgets.viewholder.BaseViewHolder;
import com.zptioning.module_widgets.viewholder.DetailViewHolder;
import com.zptioning.module_widgets.viewholder.HistoryViewHolder;
import com.zptioning.module_widgets.viewholder.MainViewHolder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @ClassName BaseAdapter
 * @Author zptioning
 * @Date 2019-11-01 11:08
 * @Version 1.0
 * @Description
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final String TAG = this.getClass().getSimpleName() + "_tag";

    protected int mType;
    private List<StockEntity> mDataList;
    protected Context mContext;

    private OperationPopWindow.OnBuyListener mOnBuyListener;
    private OperationPopWindow.OnSellListener mOnSellListener;

    public BaseAdapter(int type, List<StockEntity> dataList) {
        mType = type;
        mDataList = dataList;
    }

    public BaseAdapter(int type, List<StockEntity> dataList,
                       OperationPopWindow.OnBuyListener onBuyListener,
                       OperationPopWindow.OnSellListener onSellListener) {
        mType = type;
        mDataList = dataList;
        mOnBuyListener = onBuyListener;
        mOnSellListener = onSellListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_stock, parent, false);
        if (mType == StockConstants.TYPE_MAIN) {
            return new MainViewHolder(view);
        } else if (StockConstants.TYPE_DETAIL == mType) {
            return new DetailViewHolder(view, mOnBuyListener, mOnSellListener);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_view_stock_horizontal, parent, false);
            return new HistoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        StockEntity stockEntity = mDataList.get(position);

        EnumSet<ENUM_TITLES> enum_titles = EnumSet.allOf(ENUM_TITLES.class);
        for (ENUM_TITLES enum_title : enum_titles) {
            String name = enum_title.name();
            String value = enum_title.getValue();
            int ordinal = enum_title.ordinal();

            holder.mTextViews[ordinal].setVisibility(View.VISIBLE);
            holder.mTextViews[ordinal].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            updateView(holder, stockEntity, enum_title, ordinal,position);
        }
    }

    protected abstract void updateView(@NonNull BaseViewHolder holder, StockEntity stockEntity, ENUM_TITLES enum_title, int ordinal, int position);

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    /**
     * 更新所有的数据
     *
     * @param stockEntities
     */
    public void replaceData(List<StockEntity> stockEntities) {
        if (null == stockEntities) {
            return;
        }
        for (StockEntity entity :
                stockEntities) {
            Log.w(TAG, entity.toString());
        }
        if (null == mDataList) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();
        mDataList.addAll(stockEntities);
        notifyDataSetChanged();
    }
}
