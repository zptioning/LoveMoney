package com.zptioning.module_widgets.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_widgets.R;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StocksViewHolder extends RecyclerView.ViewHolder {

    private OperationPopWindow.OnBuyListener mOnBuyListener;
    private OperationPopWindow.OnSellListener mOnSellListener;

    public int[] resIDs = {
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

    public StocksViewHolder(@NonNull View itemView) {
        super(itemView);
        for (int i = 0; i < resIDs.length; i++) {
            mTextViews[i] = itemView.findViewById(resIDs[i]);
        }
    }

    public StocksViewHolder(@NonNull View itemView, OperationPopWindow.OnBuyListener onBuyListener,
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
    public void setOnClickOptionListener(final Context context, TextView textView, final StockEntity stockEntity, final int type) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == type) {
                    // 主页
                    Intent intent = new Intent("com.zption.DetailActivity");
                    intent.putExtra("code", stockEntity.code);
                    context.startActivity(intent);
                } else if (1 == type) {
                    // 详情页
                    new OperationPopWindow(context, stockEntity)
                            .setOnBuyListener(new OperationPopWindow.OnBuyListener() {
                                @Override
                                public void onBuy(StockEntity stockEntity) {
                                    if (null != mOnBuyListener) {
                                        mOnBuyListener.onBuy(stockEntity);
                                    }
                                }
                            })
                            .setOnSellListener(new OperationPopWindow.OnSellListener() {
                                @Override
                                public void onSell(StockEntity stockEntity) {
                                    if (null != mOnSellListener) {
                                        mOnSellListener.onSell(stockEntity);
                                    }
                                }
                            })
                            .show((Activity) context);
                }
            }
        });
    }
}
