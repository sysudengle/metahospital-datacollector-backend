package com.metahospital.datacollector.dao.data;

import java.util.HashSet;
import java.util.Set;

public class Combo {
    private int comboId;
    private String comboName;

    public Combo(){
    }

    public Combo(int comboId, String comboName){
        this.comboId = comboId;
        this.comboName = comboName;
    }

    public int getComboId() {
        return comboId;
    }

    public void setComboId(int comboId) {
        this.comboId = comboId;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }
}
