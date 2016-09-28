package com.example.sheetal.newyorktimessearch.models;

import java.util.ArrayList;


public class SettingData
{
    private String beginDate;
    private int sortOrderPosition;
    private boolean isCheckedArts;
    private boolean isCheckedFashionStyle;
    private boolean isCheckedSports;

    private ArrayList new_desks_valArray = new ArrayList();

    private static SettingData filterData;

    public static SettingData getInstance() // Singletone instance
    {
        if(filterData == null)
            filterData = new SettingData();

        return  filterData;
    }


    public String getBeginDate() {
        return this.beginDate;
    }

    public int getSortOrderPosition() {
        return this.sortOrderPosition;
    }

    public String isCheckedArts() {
        if(isCheckedArts)
            return  new_desks_valArray.get(0).toString();
        else
            return null;
    }

    public String isCheckedFashionStyle() {
        if(isCheckedFashionStyle)
            return  new_desks_valArray.get(1).toString();
        else
            return null;
    }

    public String isCheckedSports() {
        if(isCheckedSports)
            return  new_desks_valArray.get(2).toString();
        else
            return null;
    }


    public SettingData()
    {

    }

    public void filterSetData(String beginDate,
                       int sortOrderPosition,
                       boolean isCheckedArts,
                       boolean isCheckedFashionStyle,
                       boolean isCheckedSports)
    {

        this.beginDate = beginDate;
        this.sortOrderPosition = sortOrderPosition;
        this.isCheckedArts = isCheckedArts;
        this.isCheckedFashionStyle = isCheckedFashionStyle;
        this.isCheckedSports = isCheckedSports;

        //set new _desk values to arraylist
        new_desks_valArray.add("Arts");
        new_desks_valArray.add("Fashion and Style");
        new_desks_valArray.add("Sports");

    }

}