package com.zptioning.module_widgets.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import androidx.annotation.NonNull;

public class MainViewHolder extends BaseViewHolder {
    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public MainViewHolder(@NonNull View itemView, OperationPopWindow.OnBuyListener onBuyListener, OperationPopWindow.OnSellListener onSellListener) {
        super(itemView, onBuyListener, onSellListener);
    }

    @Override
    public void setOnClickOptionListener(final Context context, TextView textView, final StockEntity stockEntity, int type) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.zption.DetailActivity");
                intent.putExtra("code", stockEntity.code);
                context.startActivity(intent);
            }
        });
    }
}
