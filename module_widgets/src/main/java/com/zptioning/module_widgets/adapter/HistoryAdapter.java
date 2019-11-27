package com.zptioning.module_widgets.adapter;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;

import com.zptioning.module_funds.Datautils;
import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.R;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;
import com.zptioning.module_widgets.viewholder.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;

public class HistoryAdapter extends BaseAdapter {
    public HistoryAdapter(int type, List<StockEntity> dataList) {
        super(type, dataList);
    }

    public HistoryAdapter(int type, List<StockEntity> dataList,
                          OperationPopWindow.OnBuyListener onBuyListener, OperationPopWindow.OnSellListener onSellListener) {
        super(type, dataList, onBuyListener, onSellListener);
    }

    @Override
    protected void updateView(@NonNull BaseViewHolder holder, StockEntity stockEntity,
                              StockInterface.ENUM_TITLES enum_title, int ordinal, int position) {

        if (StockConstants.hold == stockEntity.status) {
            holder.itemView.setBackgroundColor(Color.parseColor(mContext.getString(R.string.hold)));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(mContext.getString(R.string.sold)));
        }
        switch (enum_title.name()) {
            case "INDEX":
                holder.setText(holder.mTextViews[ordinal], String.valueOf(position + 1));
                break;
            case "TIME":
                holder.mTextViews[ordinal].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                holder.setText(holder.mTextViews[ordinal], Datautils.dateFormat(stockEntity.time));
                break;
            case "CODE":
                holder.mTextViews[ordinal].setVisibility(View.GONE);
                holder.setText(holder.mTextViews[ordinal], stockEntity.code);
                break;
            case "NAME":
                holder.mTextViews[ordinal].setVisibility(View.GONE);
                holder.setText(holder.mTextViews[ordinal], stockEntity.name);
                break;
            case "COST":
                holder.setText(holder.mTextViews[ordinal], stockEntity.cost.toString());
                break;
            case "PRICE":
                holder.mTextViews[ordinal].setVisibility(View.GONE);
                holder.setText(holder.mTextViews[ordinal], stockEntity.price.toString());
                break;
            case "RATE":
                holder.mTextViews[ordinal].setVisibility(View.GONE);
                BigDecimal multiply = stockEntity.rate.multiply(new BigDecimal(100)).stripTrailingZeros();
                holder.setText(holder.mTextViews[ordinal], multiply.toString() + "%");
                break;
            case "COUNT":
                holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.count));
                break;
            case "OPTION":
                holder.mTextViews[ordinal].setVisibility(View.GONE);
                break;
            case "SOLD":
                holder.mTextViews[ordinal].setVisibility(View.GONE);
                holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.sold));
                break;
            case "HOLD":
                holder.mTextViews[ordinal].setVisibility(View.GONE);
                holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.hold));
                break;
            case "STATUS":
                if (StockConstants.hold == stockEntity.status) {
                    holder.setText(holder.mTextViews[ordinal], "买入");
                } else {
                    holder.setText(holder.mTextViews[ordinal], "卖出");
                }
                break;
            default:
                break;
        }
    }

}
