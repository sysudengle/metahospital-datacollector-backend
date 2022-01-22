package com.metahospital.datacollector.config.configs.customtype;

/**
 * @author wanghaoyuan
 */
public class SelectionItemOptionCO {
    private int value;
    private String desc;

    public SelectionItemOptionCO() {
    }

    public SelectionItemOptionCO(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
