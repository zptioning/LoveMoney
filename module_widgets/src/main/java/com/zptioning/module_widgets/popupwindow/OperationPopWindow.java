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

import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_widgets.R;

import java.math.BigDecimal;

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
    private boolean mBNewBuy;// 是否是新买的一组股票
    private final View mView;
    private OnBuyListener mOnBuyListener;
    private OnSellListener mOnSellListener;
    private EditText mEtCount;
    private EditText mEtPrice;

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
        mEtCount = mView.findViewById(R.id.et_count);
        mEtPrice = mView.findViewById(R.id.et_price);

        /* 买入股票 */
        mView.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnBuyListener) {
                    mStockEntity.time = System.currentTimeMillis();
                    mStockEntity.status = StockConstants.hold;
                    if (0 == mStockEntity.count) {// 新买入的
                        mStockEntity.cost = new BigDecimal(mEtPrice.getText().toString());
                        try {
                            mStockEntity.count = Integer.parseInt(mEtCount.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 非新买入的

                    }
                    mOnBuyListener.onBuy(mStockEntity);
                    dismiss();
                }
            }
        });

        /* 卖出股票 */
        mView.findViewById(R.id.btn_sell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSellListener) {
                    // 卖出 记录卖出的价格
                    mStockEntity.time = System.currentTimeMillis();
                    mStockEntity.cost = new BigDecimal(mEtPrice.getText().toString());
                    mStockEntity.status = StockConstants.sold;
                    mOnSellListener.onSell(mStockEntity);
                    dismiss();
                }
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

    public OperationPopWindow setOnlyBuy(boolean bNewBuy) {
        mBNewBuy = bNewBuy;
        if (mBNewBuy) {
            mView.findViewById(R.id.btn_sell).setVisibility(View.GONE);
        } else {
            mView.findViewById(R.id.btn_sell).setVisibility(View.GONE);
            mEtCount.setText(mStockEntity.count + "");
            mEtPrice.setText(mStockEntity.price.toString());
        }
        return this;
    }

    /**
     * 只能卖
     *
     * @return
     */
    public OperationPopWindow setOnlySell() {
        mView.findViewById(R.id.btn_buy).setVisibility(View.GONE);
        mEtCount.setText(mStockEntity.count + "");
        mEtPrice.setText(mStockEntity.cost.toString());
        return this;
    }

    public OperationPopWindow setOnBuyListener(OnBuyListener onBuyListener) {
        mOnBuyListener = onBuyListener;
        return this;
    }

    public OperationPopWindow setOnSellListener(OnSellListener onSellListener) {
        mOnSellListener = onSellListener;
        return this;
    }

    public interface OnBuyListener {
        void onBuy(StockEntity stockEntity);
    }

    public interface OnSellListener {
        void onSell(StockEntity stockEntity);
    }
}
