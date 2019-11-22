package com.zptioning.lovemoney;

import android.app.Activity;
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
import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface;
import com.zptioning.module_widgets.popupwindow.OperationPopWindow;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        mType = TYPE_MAIN_FRAGEMNT;
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

                StockEntity stockEntity = Datautils.getRemoteData(strExchange + strCode);
                insertStock(stockEntity, FundsProvider.STOCK_CONTENT_URI);
            }
        });
    }

    /**
     * 查询数据库中 存储了哪些股票的信息
     * 并且 根据股票代码 查询数据库中 每个股票的详细信息 再计算出结果 展示出来。
     */
    @Override
    protected void updateAllData() {
        List<StockEntity> stockEntities = Datautils.queryAllStocks(_mActivity.getContentResolver(),
                FundsProvider.STOCK_CONTENT_URI);
        // TODO: 2019-11-22  读取到所有的股票信息后，根据股票名，查询每个股票对应的表中的详细信息。
        mStocksAdapter.replaceData(stockEntities);
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
        Datautils.queryAllTables(contentResolver);
    }
}
