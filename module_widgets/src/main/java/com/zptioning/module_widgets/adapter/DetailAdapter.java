package com.zptioning.module_widgets.adapter;

import android.util.TypedValue;
import android.view.View;

import com.zptioning.module_funds.DataUtils;
import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;
import com.zptioning.module_widgets.viewholder.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;

public class DetailAdapter extends BaseAdapter {
    public DetailAdapter(int type, List<StockEntity> dataList) {
        super(type, dataList);
    }

    public DetailAdapter(int type, List<StockEntity> dataList, OperationPopWindow.OnBuyListener onBuyListener, OperationPopWindow.OnSellListener onSellListener) {
        super(type, dataList, onBuyListener, onSellListener);
    }

    @Override
    protected void updateView(@NonNull BaseViewHolder holder, StockEntity stockEntity, StockInterface.ENUM_TITLES enum_title, int ordinal, int position) {

        switch (enum_title.name()) {
            // 执行买卖操作
            case "INDEX":
                holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.index));
                holder.setonClickIndexListener(mContext, holder.mTextViews[ordinal], stockEntity);
                break;
            case "TIME":
                holder.mTextViews[ordinal].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                holder.setText(holder.mTextViews[ordinal], DataUtils.dateFormat(stockEntity.time));
                break;
            case "CODE":
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
                holder.setText(holder.mTextViews[ordinal], stockEntity.price.toString());
                break;
            case "RATE":
                BigDecimal multiply = stockEntity.rate.multiply(new BigDecimal(100)).stripTrailingZeros();
                holder.setText(holder.mTextViews[ordinal], multiply.toString() + "%");
                break;
            case "COUNT":
                holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.count));
                break;
            case "OPTION":
                holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.operation));
                holder.mTextViews[ordinal].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                if (StockConstants.hold == stockEntity.status) {
                    holder.mTextViews[ordinal].setText("想卖吗");
                } else {
                    holder.mTextViews[ordinal].setText("想买吗");
                }
                holder.setOnClickOptionListener(mContext, holder.mTextViews[ordinal], stockEntity, mType);
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
                    holder.mTextViews[ordinal].setText("持有");
                } else {
                    holder.mTextViews[ordinal].setText("卖出");
                }                break;
            default:
                break;
        }
    }


}
