package com.zptioning.module_funds;

public interface StockInterface {
    public enum ENUM_TITLES {
        INDEX("索引"),
        TIME("时间"),
        CODE("代码"),
        NAME("名称"),
        RATE("涨幅"),
        COST("成本"),
        PRICE("现价"),
        COUNT("数量"),
        OPTION("操作"),
        SOLD("卖出"),
        HOLD("持有"),
        STATUS("状态"),
        ;
        private String value;

        ENUM_TITLES(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
