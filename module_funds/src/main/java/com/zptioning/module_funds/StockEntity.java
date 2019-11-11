package com.zptioning.module_funds;

import java.math.BigDecimal;

/**
 * .append(" _id ").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
 * .append(" index ").append(" int, ")
 * .append(" time ").append(" text, ")
 * .append(" code ").append(" text, ")
 * .append(" name ").append(" text, ")
 * .append(" price ").append(" decimal, ")
 * .append(" count ").append(" int,")
 * .append(" operation ").append(" int, ")// 1:买入  2:卖出
 * .append(" status ").append(" int ");// 1:持有  2:非持有
 * .append(" hold ").append(" int ")// 持有数量
 * .append(" sold ").append(" int ");// 卖出数量
 *
 *    "索引",
 *             "时间",
 *             "代码",
 *             "名称",
 *             "成本",
 *             "现价",
 *             "涨幅",
 *             "数量",
 *             "操作",
 *             "买卖",
 *             "持有",
 *             "状态",
 */
public class StockEntity {
    public int _id;// 主键
    public int index;// "索引",
    public String time;// "时间",
    public String code;// 代码
    public String name;// 名称
    public BigDecimal cost;// 成本价
    public BigDecimal price;// 市场价
    public BigDecimal rate;// 涨幅
    public int count;// 数量
    public int operation;// 操作   1:买入  2:卖出
    public int status;// 状态   1:持有  2:非持有
    public int hold;// 持有 数量
    public int sold;// 已经卖出  数量
}
