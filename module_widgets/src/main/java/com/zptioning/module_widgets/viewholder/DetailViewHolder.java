package com.zptioning.module_widgets.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import androidx.annotation.NonNull;

public class DetailViewHolder extends BaseViewHolder {
    public DetailViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public DetailViewHolder(@NonNull View itemView, OperationPopWindow.OnBuyListener onBuyListener, OperationPopWindow.OnSellListener onSellListener) {
        super(itemView, onBuyListener, onSellListener);
    }

    @Override
    public void setOnClickOptionListener(final Context context, TextView textView, final StockEntity stockEntity, int type) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 详情页
                OperationPopWindow operationPopWindow = new OperationPopWindow(context, stockEntity)
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
                        });
                if (stockEntity.status == StockConstants.hold) {
                    operationPopWindow.setOnlySell();
                } else {
                    operationPopWindow.setOnlyBuy(false);
                }
                operationPopWindow.show((Activity) context);
            }
        });
    }

    @Override
    public void setonClickIndexListener(final Context context, TextView textView, final StockEntity stockEntity) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.zption.HistoryActivity");
                intent.putExtra("code", stockEntity.code);
                intent.putExtra("index", stockEntity.index);
                intent.putExtra("price", stockEntity.price);
                context.startActivity(intent);
            }
        });
    }
}
