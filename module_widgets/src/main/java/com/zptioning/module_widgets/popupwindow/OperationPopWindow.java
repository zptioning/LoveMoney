package com.zptioning.module_widgets.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    public OperationPopWindow(Context context) {
        super(context);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_window_operation, null);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 显示
     */
    public void show(Activity activity) {
        if (!activity.isFinishing() && !isShowing()) {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    public void show(Activity context, StockEntity stockEntity) {
        mStockEntity = stockEntity;
        if (!(context instanceof Activity)) {
            return;
        }
        if (null == stockEntity) {
            return;
        }
        show(context);
    }
}
