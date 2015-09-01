package edu.umbc.cs.ebiquity.mithril.hma.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AppListJson {	 
    @SerializedName("appList") 
    public List<String> appList;
}