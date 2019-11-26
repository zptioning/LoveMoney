package com.zptioning.lovemoney;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zptioning.module_funds.Datautils;
import com.zptioning.module_funds.FundsProvider;
import com.zptioning.module_funds.StockConstants;
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.adapter.MainAdapter;

import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.zptioning.module_funds.Datautils.ROUNDING_MODE;
import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @ClassName MainFragment
 * @Author zptioning
 * @Date 2019-11-04 11:08
 * @Version 1.0
 * @Description
 */
public class MainFragment extends BaseFragment {


    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mType = StockConstants.TYPE_MAIN;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }


    protected void initTopWidgets() {
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
        btnInsert.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(strExchange)) {
                    Toast.makeText(_mActivity, "请先选交易所！！", Toast.LENGTH_SHORT).show();
                    return false;
                }
                String strCode = mEtCode.getText().toString();
                if (TextUtils.isEmpty(strCode)) {
                    Toast.makeText(_mActivity, "请输入股票代码！！", Toast.LENGTH_SHORT).show();
                    return false;
                }

                StockEntity stockEntity = Datautils.getRemoteData(strExchange + strCode);
                insertStock(stockEntity, FundsProvider.STOCK_CONTENT_URI);
                return true;
            }
        });
    }

    @Override
    protected void initAdapter() {
        mBaseAdapter = new MainAdapter(mType, null);
    }

    /**
     * 查询数据库中 存储了哪些股票的信息
     * 并且 根据股票代码 查询数据库中 每个股票的详细信息 再计算出结果 展示出来。
     */
    @Override
    protected void updateAllData() {
        List<StockEntity> stockEntities = Datautils.queryAllStocks(_mActivity.getContentResolver(),
                FundsProvider.STOCK_CONTENT_URI);
        updateAllDataWithRemoteData(stockEntities);
        calculateWithLocalData(stockEntities);
        mBaseAdapter.replaceData(stockEntities);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAllData();
    }

    /**
     * 查询本地数据库，计算每个股票的具体数据
     * 成本、涨幅、数量、已卖数量、持有数量
     *
     * @param stockEntities
     */
    private void calculateWithLocalData(List<StockEntity> stockEntities) {
        for (int i = 0; i < stockEntities.size(); i++) {
            StockEntity stockEntity = stockEntities.get(i);
            List<StockEntity> detailList = Datautils.queryAllStocks(mContentResolver,
                    Datautils.addOtherFragment(stockEntity.code));
            if (null == detailList || detailList.size() == 0) {
                continue;
            }
            // 计算 成本 总数量 持有数量 已卖数量 涨幅
            BigDecimal cost = new BigDecimal("0");// 成本
            int allCount = 0;// 总数量
            int allHold = 0;// 持有数量
            int allSold = 0;// 卖出数量
            BigDecimal rate = new BigDecimal("0");// 涨跌幅
            for (int j = 0; j < detailList.size(); j++) {
                StockEntity detailEntity = detailList.get(j);
                if (detailEntity.status == 1)// 持有
                {
                    allHold += detailEntity.count;
                    cost = cost.add(detailEntity.cost.multiply(new BigDecimal(detailEntity.count)));
                } else {
                    allSold += detailEntity.count;
                }
            }
            stockEntity.hold = allHold;
            stockEntity.sold = allSold;
            stockEntity.count = allHold + allSold;
            if (0 == allHold) {
                continue;
            }
            stockEntity.cost = cost.divide(new BigDecimal(allHold), ROUNDING_MODE, ROUND_HALF_UP);
            stockEntity.rate = Datautils.getRate(stockEntity.cost, stockEntity.price);
        }
    }

    /**
     * 查询所存股票的价格并计算相关数据
     *
     * @param stockEntities
     */
    private void updateAllDataWithRemoteData(List<StockEntity> stockEntities) {
        if (null == stockEntities) {
            return;
        }

        for (int i = 0; i < stockEntities.size(); i++) {
            StockEntity stockEntity = stockEntities.get(i);
            StockEntity remoteData = Datautils.getRemoteData(stockEntity.code);
            if (null == remoteData) {
                continue;
            }
            stockEntity.price = remoteData.price;
        }
    }


    protected void updateLines(TextView viewById, StockInterface.ENUM_TITLES enum_title) {
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
    }

    @Override
    protected void queryResult(ContentResolver contentResolver, Uri stockContentUri) {
        Datautils._QueryAllTables(contentResolver);
    }
}
