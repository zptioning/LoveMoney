package com.zptioning.module_funds;

import android.content.Context;
import android.util.TypedValue;

import java.util.EnumSet;

public class AppUtils {
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取某个列在枚举中的索引
     *
     * @param title
     * @return
     */
    public static int getEnumIndex(String title) {
        if (null == title) {
            return -1;
        }
        EnumSet<StockInterface.ENUM_TITLES> enum_titles = EnumSet.allOf(StockInterface.ENUM_TITLES.class);
        for (StockInterface.ENUM_TITLES enum_title : enum_titles) {
            String name = enum_title.name();
            String value = enum_title.getValue();
            int ordinal = enum_title.ordinal();
            if (title.equals(name)) {
                return ordinal;
            }
        }
        return -1;
    }
}
