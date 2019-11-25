package com.zptioning.module_widgets.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_widgets.R;

/**
 * @ClassName OperationPopWindow
 * @Author zptioning
 * @Date 2019-11-08 17:07
 * @Version 1.0
 * @Description
 */
public class OperationPopWindow extends PopupWindow {

    private Context mContext;
    private StockEntity mStockEntity;
    private boolean mBNewBuy;
    private final View mView;

    public OperationPopWindow(Context context, StockEntity stockEntity) {
        super(context);
        mContext = context;
        mStockEntity = stockEntity;
        mView = LayoutInflater.from(context).inflate(R.layout.pop_window_operation, null);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mView);
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        initListeners();
    }

    private void initListeners() {
        ((EditText) mView.findViewById(R.id.et_code1)).setText(mStockEntity.code);
        mView.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mView.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 显示
     */
    public void show(Activity activity) {
        if (!activity.isFinishing() && !isShowing()) {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    public OperationPopWindow setNewBuy(boolean bNewBuy) {
        mBNewBuy = bNewBuy;
        if (mBNewBuy) {
            mView.findViewById(R.id.btn_sold).setVisibility(View.GONE);
        }
        return this;
    }
}
