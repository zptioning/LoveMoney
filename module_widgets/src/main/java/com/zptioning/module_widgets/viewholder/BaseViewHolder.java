package com.zptioning.module_widgets.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_widgets.R;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected OperationPopWindow.OnBuyListener mOnBuyListener;
    protected OperationPopWindow.OnSellListener mOnSellListener;

    protected int[] resIDs = {
            R.id.tv_0,
            R.id.tv_1,
            R.id.tv_2,
            R.id.tv_3,
            R.id.tv_4,
            R.id.tv_5,
            R.id.tv_6,
            R.id.tv_7,
            R.id.tv_8,
            R.id.tv_9,
            R.id.tv_10,
            R.id.tv_11,
    };

    public TextView[] mTextViews = new TextView[12];

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        for (int i = 0; i < resIDs.length; i++) {
            mTextViews[i] = itemView.findViewById(resIDs[i]);
        }
    }

    public BaseViewHolder(@NonNull View itemView, OperationPopWindow.OnBuyListener onBuyListener,
                          OperationPopWindow.OnSellListener onSellListener) {
        super(itemView);
        mOnBuyListener = onBuyListener;
        mOnSellListener = onSellListener;
        for (int i = 0; i < resIDs.length; i++) {
            mTextViews[i] = itemView.findViewById(resIDs[i]);
        }
    }


    public void setText(TextView textView, String content) {
        if (null == textView) {
            return;
        }

        textView.setText("0".equals(content) ? "-" : content);
    }

    /**
     * 点击操作时的监听响应
     *
     * @param context
     * @param textView
     * @param stockEntity
     * @param type
     */
    public void setOnClickOptionListener(final Context context, TextView textView,
                                         final StockEntity stockEntity, final int type) {
    }

    /**
     * 点击索引 跳转历史操作页面
     *
     * @param stockEntity
     * @param context
     * @param textView
     */
    public void setonClickIndexListener(final Context context, TextView textView,
                                        final StockEntity stockEntity) {
    }
}
