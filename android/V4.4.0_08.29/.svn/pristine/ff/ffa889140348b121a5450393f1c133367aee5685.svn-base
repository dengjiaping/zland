package com.zhisland.android.blog.common.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

import java.util.ArrayList;

/**
 * 城市对象 省/市
 */
public class City extends OrmDto {

    /**
     * 城市 code
     */
    @SerializedName("tagId")
    public int code;

    /**
     * 城市 name
     */
    @SerializedName("tagName")
    public String name;

    /**
     * 父城市 code，如果没有则为0
     */
    @SerializedName("parentId")
    public int parentCode;

    @SerializedName("shortName")
    public String shortName;

    public City(int code, String name, int parentCode) {
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }

    /**
     * 通过code 获取 name
     */
    public static String getNameByCode(int code) {
        ArrayList<City> cities = Dict.getInstance().getCities();
        for (City city : cities) {
            if (code == city.code) {
                return city.name;
            }
        }
        return "";
    }

}
