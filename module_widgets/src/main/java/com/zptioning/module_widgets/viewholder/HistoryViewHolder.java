package com.zptioning.module_widgets.viewholder;

import android.view.View;

import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import androidx.annotation.NonNull;

public class HistoryViewHolder extends BaseViewHolder {
    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public HistoryViewHolder(@NonNull View itemView, OperationPopWindow.OnBuyListener onBuyListener, OperationPopWindow.OnSellListener onSellListener) {
        super(itemView, onBuyListener, onSellListener);
    }
}
