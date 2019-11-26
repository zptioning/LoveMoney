package com.zptioning.lovemoney;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zptioning.module_funds.Datautils;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface.ENUM_TITLES;
import com.zptioning.module_widgets.adapter.StocksAdapter;
import com.zptioning.module_widgets.custom_view.CustomItemDecoration;

import java.util.EnumSet;
import java.util.List;

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

    /**
     * 0:mainfragment  1:detailfragment
     */
    protected int mType = TYPE_MAIN_FRAGEMNT;

    private RecyclerView mRvStocks;
    protected String strExchange = "";


    protected StocksAdapter mStocksAdapter;


    private String[] titles = {
            "索引",
            "时间",
            "代码",
            "名称",
            "成本",
            "现价",
            "涨幅",
            "数量",
            "操作",
            "已卖",
            "持有",
            "状态",
    };

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

    /* 数据处理工具 */
//    private Datautils mDatautils;

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
        Datautils._QueryAllTables(mContentResolver);
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
    private void initRvStocks() {
        mRvStocks = mRootView.findViewById(R.id.rv_stocks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRvStocks.setLayoutManager(linearLayoutManager);
        mRvStocks.addItemDecoration(new CustomItemDecoration());
        initAdapter();

//        if(mType == 0) {
//            mStocksAdapter = new StocksAdapter(mType, null);
//        }else {
//            mStocksAdapter = new StocksAdapter(mType,null);
//        }
        mRvStocks.setAdapter(mStocksAdapter);
    }

    private void initEnums() {
        EnumSet<ENUM_TITLES> enum_titles = EnumSet.allOf(ENUM_TITLES.class);
        for (ENUM_TITLES enum_title : enum_titles) {
            String name = enum_title.name();
            String value = enum_title.getValue();
            int ordinal = enum_title.ordinal();
            if (ordinal < resIDs.length) {
                TextView viewById = (TextView) mRootView.findViewById(resIDs[ordinal]);

                viewById.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//                addClickListener(viewById, enum_title, ordinal);
                if (null == enum_title || null == name || null == viewById) {
                    continue;
                }

                viewById.setVisibility(View.VISIBLE);
                viewById.setText(String.format("%02d", ordinal) + ":" + /*titles[ordinal]*/enum_title.getValue());

                updateLines(viewById, enum_title);
            }
        }
    }

    protected abstract void initAdapter();

    /**
     * 更新页面所有数据
     */
    protected abstract void updateAllData();

    /**
     * @param viewById
     * @param enum_title
     * @param ordinal
     */
    private void addClickListener(TextView viewById, ENUM_TITLES enum_title, int ordinal) {
        if (null == enum_title || null == enum_title.name() || null == viewById) {
            return;
        }

        viewById.setVisibility(View.VISIBLE);
        viewById.setText(String.format("%02d", ordinal) + ":" + /*titles[ordinal]*/enum_title.getValue());

        updateLines(viewById, enum_title);
    }

    /**
     * 更新列
     *
     * @param viewById
     * @param enum_title
     */
    protected abstract void updateLines(TextView viewById, ENUM_TITLES enum_title);

    protected abstract void queryResult(ContentResolver contentResolver, Uri stockContentUri);

    /**
     * 插入指定股票信息
     *
     * @param stockEntity
     * @param stockContentUri
     */
    protected void insertStock(StockEntity stockEntity, Uri stockContentUri) {
        List<StockEntity> stockEntities = Datautils.queryAllStocks(mContentResolver, stockContentUri);
        stockEntity.index = stockEntities.size() + 1;
        Uri uri = Datautils.insert(mContentResolver, stockContentUri, stockEntity);
        if (null == uri) {
            Toast.makeText(_mActivity, "股票已存在！！", Toast.LENGTH_SHORT).show();
            return;
        }
        long id = ContentUris.parseId(uri);
        if (-1 == id) {
            Toast.makeText(_mActivity, "插入失败", Toast.LENGTH_SHORT).show();
        } else {
            // 查询当前数据库 有 哪些表
            queryResult(mContentResolver, stockContentUri);
            updateAllData();
        }
    }

    /**
     * 取消编辑框 选中状态
     */
    public void clearEtFocus() {
        if (null != mEtCode) {
            mEtCode.clearFocus();
        }
    }
}
