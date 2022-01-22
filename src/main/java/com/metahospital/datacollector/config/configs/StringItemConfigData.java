package com.metahospital.datacollector.config.configs;

public class StringItemConfigData {
	private int stringItemTypeId;
    private String defaultValue;
    private int maxLength;

	public StringItemConfigData() {
	}

    public StringItemConfigData(int stringItemTypeId, String defaultValue, int maxLength) {
        this.stringItemTypeId = stringItemTypeId;
        this.defaultValue = defaultValue;
        this.maxLength = maxLength;
    }

    public int getStringItemTypeId() {
        return stringItemTypeId;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
