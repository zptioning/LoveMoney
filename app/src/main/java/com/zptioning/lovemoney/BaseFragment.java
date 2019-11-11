package com.zptioning.lovemoney;

import android.app.Activity;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zptioning.module_funds.Datautils;
import com.zptioning.module_funds.FundsProvider;
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
public class BaseFragment extends Fragment {


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
    protected int mType = 0;

    private RecyclerView mRvStocks;
    private String strExchange = "";


    private StocksAdapter mStocksAdapter;


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

    /* 数据处理工具 */
//    private Datautils mDatautils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 将layout布局转换成View
        _mActivity = getActivity();
        mRootView = inflater.inflate(R.layout.fragment_main, null);
        mLLTop = mRootView.findViewById(R.id.ll_top);
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (0 == mType) {
            initTopWidgets();
        } else {
            mLLTop.setVisibility(View.GONE);
        }
        initRvStocks();
        initEnums();
        updateAllData();
    }

    /**
     * 更新页面所有数据
     */
    private void updateAllData() {
        if (0 == mType) {
            List<StockEntity> stockEntities = Datautils.queryAllStocks(_mActivity.getContentResolver(),
                    FundsProvider.STOCK_CONTENT_URI);
            mStocksAdapter.replaceData(stockEntities);
        } else {

        }
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
                addClickListener(viewById, enum_title, ordinal);
            }
        }
    }

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
        viewById.setText(String.format("%02d", ordinal) + ":" + titles[ordinal]);

        if (0 == mType) {
            switch (enum_title.name()) {
                // 执行买卖操作
                case "INDEX":
                    break;
                case "TIME":
                    viewById.setVisibility(View.GONE);
                    break;
                case "CODE":
                    break;
                case "NAME":
                    break;
                case "COST":
                    break;
                case "PRICE":
                    break;
                case "RATE":
                    break;
                case "COUNT":
                    break;
                case "OPTION":
                    break;
                case "SOLD":
                    break;
                case "HOLD":
                    break;
                case "STATUS":
                    viewById.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            switch (enum_title.name()) {
                // 执行买卖操作
                case "INDEX":
                    break;
                case "TIME":
                    break;
                case "CODE":
                    break;
                case "NAME":
                    break;
                case "COST":
                    break;
                case "PRICE":
                    break;
                case "RATE":
                    break;
                case "COUNT":
                    break;
                case "OPTION":
                    break;
                case "SOLD":
                    viewById.setVisibility(View.GONE);
                    break;
                case "HOLD":
                    viewById.setVisibility(View.GONE);
                    break;
                case "STATUS":
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 初始化列表
     */
    private void initRvStocks() {
        mRvStocks = mRootView.findViewById(R.id.rv_stocks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRvStocks.setLayoutManager(linearLayoutManager);
        mRvStocks.addItemDecoration(new CustomItemDecoration());

        mStocksAdapter = new StocksAdapter(mType, null);
        mRvStocks.setAdapter(mStocksAdapter);
    }

    /**
     * 初始化控件
     */
    private void initTopWidgets() {
        /** 单选框初始化 和 监听 */
        RadioGroup radioGroup = mRootView.findViewById(R.id.rg_parent);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sh:
                        strExchange = "sh";
                        break;
                    case R.id.rb_sz:
                        strExchange = "sz";
                        break;
                    default:
                        strExchange = "";
                        break;
                }
            }
        });
        radioGroup.check(R.id.rb_sh);
        strExchange = "sh";

        /** 输入框初始化和监听 */
        mEtCode = mRootView.findViewById(R.id.et_code);
        mEtCode.setText("510300");

        /** 插入按钮初始化监听 */
        Button btnInsert = mRootView.findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(strExchange)) {
                    Toast.makeText(_mActivity, "请先选交易所！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String strCode = mEtCode.getText().toString();
                if (TextUtils.isEmpty(strCode)) {
                    Toast.makeText(_mActivity, "请输入股票代码！！", Toast.LENGTH_SHORT).show();
                    return;
                }

                StockEntity stockEntity = Datautils.getRemoteData(strExchange, strCode);
                insertStock(stockEntity);
            }
        });
    }

    /**
     * 插入指定股票信息
     *
     * @param stockEntity
     */
    private void insertStock(StockEntity stockEntity) {
        Uri uri = Datautils.insert(_mActivity.getContentResolver(), FundsProvider.STOCK_CONTENT_URI,
                stockEntity);
        if (null == uri) {
            Toast.makeText(_mActivity, "股票已存在！！", Toast.LENGTH_SHORT).show();
            return;
        }
        long id = ContentUris.parseId(uri);
        if (-1 == id) {
            Toast.makeText(_mActivity, "插入失败", Toast.LENGTH_SHORT).show();
        } else {
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
