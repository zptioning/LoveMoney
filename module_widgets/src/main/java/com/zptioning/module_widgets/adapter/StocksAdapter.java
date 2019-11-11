package com.zptioning.module_widgets.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zptioning.module_funds.StockEntity;
import com.zptioning.module_funds.StockInterface.ENUM_TITLES;
import com.zptioning.module_widgets.R;
import com.zptioning.module_widgets.viewholder.StocksViewHolder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @ClassName StocksAdapter
 * @Author zptioning
 * @Date 2019-11-01 11:08
 * @Version 1.0
 * @Description
 */
public class StocksAdapter extends RecyclerView.Adapter<StocksViewHolder> {

    private int mType;
    private List<StockEntity> mDataList;
    private Context mContext;

    public StocksAdapter(int type, List<StockEntity> dataList) {
        mType = type;
        mDataList = dataList;
    }

    @NonNull
    @Override
    public StocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_stock, parent, false);
        return new StocksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder holder, int position) {
        StockEntity stockEntity = mDataList.get(position);

        EnumSet<ENUM_TITLES> enum_titles = EnumSet.allOf(ENUM_TITLES.class);
        for (ENUM_TITLES enum_title : enum_titles) {
            String name = enum_title.name();
            String value = enum_title.getValue();
            int ordinal = enum_title.ordinal();

            holder.mTextViews[ordinal].setVisibility(View.VISIBLE);
            holder.mTextViews[ordinal].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            if (0 == mType) {
                switch (enum_title.name()) {
                    // 执行买卖操作
                    case "INDEX":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.index));
                        break;
                    case "TIME":
                        holder.mTextViews[ordinal].setVisibility(View.GONE);
                        holder.setText(holder.mTextViews[ordinal], stockEntity.time);
                        break;
                    case "CODE":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.code);
                        break;
                    case "NAME":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.name);
                        break;
                    case "COST":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.cost.toString());
                        break;
                    case "PRICE":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.price.toString());
                        break;
                    case "RATE":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.rate.toString());
                        break;
                    case "COUNT":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.count));
                        break;
                    case "OPTION":
                        holder.setText(holder.mTextViews[ordinal], "just click, baby!"/*String.valueOf(stockEntity.operation)*/);
                        holder.mTextViews[ordinal].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        holder.setOnClickOptionListener(mContext, holder.mTextViews[ordinal], stockEntity, mType);
                        break;
                    case "SOLD":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.sold));
                        break;
                    case "HOLD":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.hold));
                        break;
                    case "STATUS":
                        holder.mTextViews[ordinal].setVisibility(View.GONE);
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.status));
                        break;
                    default:
                        break;
                }
            } else {
                switch (enum_title.name()) {
                    // 执行买卖操作
                    case "INDEX":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.index));
                        break;
                    case "TIME":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.time);
                        break;
                    case "CODE":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.code);
                        break;
                    case "NAME":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.name);
                        break;
                    case "COST":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.cost.toString());
                        break;
                    case "PRICE":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.price.toString());
                        break;
                    case "RATE":
                        holder.setText(holder.mTextViews[ordinal], stockEntity.rate.toString());
                        break;
                    case "COUNT":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.count));
                        break;
                    case "OPTION":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.operation));
                        holder.mTextViews[ordinal].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        holder.setOnClickOptionListener(mContext, holder.mTextViews[ordinal], stockEntity, mType);
                        break;
                    case "SOLD":
                        holder.mTextViews[ordinal].setVisibility(View.GONE);
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.sold));
                        break;
                    case "HOLD":
                        holder.mTextViews[ordinal].setVisibility(View.GONE);
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.hold));
                        break;
                    case "STATUS":
                        holder.setText(holder.mTextViews[ordinal], String.valueOf(stockEntity.status));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    /**
     * 更新所有的数据
     *
     * @param stockEntities
     */
    public void replaceData(List<StockEntity> stockEntities) {
        if (null == mDataList) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();
        mDataList.addAll(stockEntities);
        notifyDataSetChanged();
    }
}
