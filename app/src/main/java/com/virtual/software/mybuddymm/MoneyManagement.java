package com.virtual.software.mybuddymm;

import java.util.ArrayList;
import java.util.List;

public class MoneyManagement {

    public static final String OSCAR = "Oscar's Grind";
    public static final String OSCAR_DESCRIPTION = "+1 unit if W, Stay current unit if L";

    public static final String ORC = "Orc mm";
    public static final String ORC_DESCRIPTION = "Total of 12 units, Steps 6";


    public static final String MOON = "Moon Grind";
    public static final String MOON_DESCRIPTION = "+1 unit if L, Stay to current unit if W";

    public static final String Mang_B = "Mang B";
    public static final String Mang_B_DESCRIPTION = "Stay to current unit if L, get the total of  L if W";


    private String fieldName;
    private String description;
    private int UnitsRequired;


    private boolean selected;


    public MoneyManagement() {

    }

    public int getUnitsRequired() {
        return UnitsRequired;
    }

    public void setUnitsRequired(int unitsRequired) {
        UnitsRequired = unitsRequired;
    }

    public MoneyManagement(String fieldName, String description, int unitsRequired, boolean selected) {
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

    public static List<MoneyManagement> getMoneyManagementList() {
        List<MoneyManagement> itemList = new ArrayList<>();
        itemList.add(new MoneyManagement(MoneyManagement.ORC, MoneyManagement.ORC_DESCRIPTION, 0, false));
        itemList.add(new MoneyManagement(MoneyManagement.OSCAR, MoneyManagement.OSCAR_DESCRIPTION, 0, false));
        itemList.add(new MoneyManagement(MoneyManagement.MOON, MoneyManagement.MOON_DESCRIPTION, 0, false));
        itemList.add(new MoneyManagement(MoneyManagement.Mang_B, MoneyManagement.Mang_B_DESCRIPTION, 0, false));

        return itemList;
    }

}
