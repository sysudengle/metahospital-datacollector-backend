package com.metahospital.datacollector.config.configs;

public class NumberItemConfigData {
	private int numberItemTypeId;
    private long defaultValue;
    private long min;
    private long max;
    private int unitDecimal;
    private String unitName;

	public NumberItemConfigData() {
	}

    public NumberItemConfigData(int numberItemTypeId, long defaultValue, long min, long max, int unitDecimal, String unitName) {
        this.numberItemTypeId = numberItemTypeId;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.unitDecimal = unitDecimal;
        this.unitName = unitName;
    }

    public int getNumberItemTypeId() {
        return numberItemTypeId;
    }

    public long getDefaultValue() {
        return defaultValue;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public int getUnitDecimal() {
        return unitDecimal;
    }

    public String getUnitName() {
        return unitName;
    }
}
