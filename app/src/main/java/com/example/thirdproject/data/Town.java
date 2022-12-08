package com.example.thirdproject.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Town {
    @SerializedName("Country")
    @Expose
    public String country;

    @SerializedName("Name")
    @Expose
    public String name;

    @SerializedName("language")
    @Expose
    public String language;

    @SerializedName("Population")
    @Expose
    public Integer population;

    @SerializedName("square")
    @Expose
    public Integer square;
}
