package com.shankaryadav.www.showweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City_pojo {

    @SerializedName("main_cond")
    @Expose
    private String main_cond;
    @SerializedName("city_name")
    @Expose
    private String city_name;

    @SerializedName("city_temp")
    @Expose
    private String city_temp;


    public String getMain_cond() {
        return main_cond;
    }

    public void setMain_cond(String main_cond) {
        this.main_cond = main_cond;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_temp() {
        return city_temp;
    }

    public void setCity_temp(String city_temp) {
        this.city_temp = city_temp;
    }

    public City_pojo() {
    }

    public City_pojo(String main_cond, String city_name, String city_temp) {
        this.main_cond = main_cond;
        this.city_name = city_name;
        this.city_temp = city_temp;
    }

    public City_pojo(String main_cond, String city_name) {
        this.main_cond = main_cond;
        this.city_name = city_name;
    }

    public City_pojo(String city_temp) {
        this.city_temp = city_temp;
    }
}
