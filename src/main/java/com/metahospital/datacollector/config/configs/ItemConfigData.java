package com.metahospital.datacollector.config.configs;

/**
 * Created on 2021/12/28.
 */
public class ItemConfigData {
	private int itemId;
	private String itemName;
	private String description;
	private int itemType;
	private int itemTypeId;
	private int departmentId;

	public ItemConfigData() {
	}

    public ItemConfigData(int itemId, String itemName, String description, int itemType, int itemTypeId, int departmentId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.itemType = itemType;
        this.itemTypeId = itemTypeId;
        this.departmentId = departmentId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public int getItemType() {
        return itemType;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public int getDepartmentId() {
        return departmentId;
    }
}
