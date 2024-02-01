package com.virtual.software.mybuddymm;

import java.util.ArrayList;
import java.util.List;

public class ListItemMoneymanagement {
    private String fieldName;
    private String description;
    private int UnitsRequired;


    private boolean selected;


    public ListItemMoneymanagement() {

    }

    public int getUnitsRequired() {
        return UnitsRequired;
    }

    public void setUnitsRequired(int unitsRequired) {
        UnitsRequired = unitsRequired;
    }

    public ListItemMoneymanagement(String fieldName, String description, int unitsRequired, boolean selected) {
        this.fieldName = fieldName;
        this.description = description;
        UnitsRequired = unitsRequired;
        this.selected = selected;
    }


    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static List<ListItemMoneymanagement> getMoneyManagementList() {
        List<ListItemMoneymanagement> itemList = new ArrayList<>();
        itemList.add(new ListItemMoneymanagement("ORC", "Steps: 6 Units: 12", 12, false));
        itemList.add(new ListItemMoneymanagement("Moon", "Steps: 35 Units: 126", 126, false));
        itemList.add(new ListItemMoneymanagement(MoneyManagement.OSCAR, MoneyManagement.OSCAR_DESCRIPTION, 40, true));
        return itemList;
    }

}
