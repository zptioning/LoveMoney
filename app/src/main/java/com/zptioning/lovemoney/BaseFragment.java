package com.zptioning.lovemoney;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zptioning.module_funds.Datautils;
import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockInterface.ENUM_TITLES;
import com.zptioning.module_widgets.adapter.BaseAdapter;
import com.zptioning.module_widgets.custom_view.CustomItemDecoration;

import java.util.EnumSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @ClassName BaseFragment
 * @Author zptioning
 * @Date 2019-11-04 11:31
 * @Version 1.0
 * @Description
 */
public abstract class BaseFragment extends Fragment {

    // 主页标记
    protected static final int TYPE_MAIN_FRAGEMNT = 0;
    // 详情页标记
    protected static final int TYPE_DETAIL_FRAGEMNT = 1;

    protected Activity _mActivity;
    // 根布局
    protected View mRootView;

    // 顶部父布局
    protected LinearLayout mLLTop;

    // 代码输入框
    protected EditText mEtCode;

    // 第一次进入 resume 时不需要拉接口
    protected boolean mBFirst = true;

    /**
     * 0:mainfragment  1:detailfragment
     */
    protected int mType = StockConstants.TYPE_MAIN;

    protected RecyclerView mRvStocks;
    protected String strExchange = "";

    protected BaseAdapter mBaseAdapter;

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
    protected ContentResolver mContentResolver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 将layout布局转换成View
        _mActivity = getActivity();
        mRootView = inflater.inflate(getLayoutID(), null);
        mLLTop = mRootView.findViewById(R.id.ll_top);
        return mRootView;
    }

    protected abstract int getLayoutID();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 查询当前有哪些表
        mContentResolver = _mActivity.getContentResolver();
        initTopWidgets();
        initRvStocks();
        initEnums();
        updateAllData();
    }

    /**
     * 初始化控件
     */
    protected abstract void initTopWidgets();

    /**
     * 初始化列表
     */
    protected void initRvStocks() {
        mRvStocks = mRootView.findViewById(R.id.rv_stocks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRvStocks.setLayoutManager(linearLayoutManager);
        mRvStocks.addItemDecoration(new CustomItemDecoration());
        initAdapter();
        mRvStocks.setAdapter(mBaseAdapter);
    }

    private void initEnums() {
        EnumSet<ENUM_TITLES> enum_titles = EnumSet.allOf(ENUM_TITLES.class);
        for (ENUM_TITLES enum_title : enum_titles) {
            String name = enum_title.name();
            String value = enum_title.getValue();
            int ordinal = enum_title.ordinal();
            if (ordinal < resIDs.length) {
                TextView viewById = mRootView.findViewById(resIDs[ordinal]);

                viewById.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                viewById.setVisibility(View.VISIBLE);
                if (null == enum_title || null == name || null == viewById) {
                    continue;
                }

                setColumnTitle(value, ordinal, viewById);

                updateLines(viewById, enum_title);
            }
        }
    }

    protected void setColumnTitle(String value, int ordinal, TextView viewById) {
        viewById.setText(String.format("%02d", ordinal) + ":" + value);
    }

    protected abstract void initAdapter();

    /**
     * 更新页面所有数据
     */
    protected  void updateAllData(){
        Datautils._QueryAllTables(mContentResolver);
    };


    /**
     * 更新列
     *
     * @param viewById
     * @param enum_title
     */
    protected abstract void updateLines(TextView viewById, ENUM_TITLES enum_title);



    /**
     * 取消编辑框 选中状态
     */
    public void clearEtFocus() {
        if (null != mEtCode) {
            mEtCode.clearFocus();
        }
    }
}
