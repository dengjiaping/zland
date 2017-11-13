package com.zhisland.android.blog.find.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/**
 * 搜索结果配套的筛选项集合
 * Created by Mr.Tui on 2016/5/20.
 */
public class SearchFilter extends OrmDto {

    /**
     * 搜索关键字
     * */
    @SerializedName("keyword")
    public String keyword;

    /**
     * 搜索筛选集合，筛选资源需求类型
     * */
    @SerializedName("rType")
    public ArrayList<FilterItem> rType;

    /**
     * 搜索筛选集合，筛选用户类型
     * */
    @SerializedName("uType")
    public ArrayList<FilterItem> uType;

    /**
     * 搜索筛选集合，筛选地区
     * */
    @SerializedName("area")
    public ArrayList<FilterItem> area;

    /**
     * 获取用户类型激活的筛选项
     * */
    public FilterItem getActiveUType() {
        if (uType == null) {
            return null;
        }
        for (FilterItem filterItem : uType) {
            if (filterItem.active == FilterItem.STATUS_ACTIVE_TRUE) {
                return filterItem;
            }
        }
        return null;
    }

    /**
     * 获取资源需求类型激活的筛选项
     * */
    public FilterItem getActiveRType() {
        if (rType == null) {
            return null;
        }
        for (FilterItem filterItem : rType) {
            if (filterItem.active == FilterItem.STATUS_ACTIVE_TRUE) {
                return filterItem;
            }
        }
        return null;
    }

    /**
     * 获取地区激活的筛选项
     * */
    public FilterItem getActiveArea() {
        if (area == null) {
            return null;
        }
        for (FilterItem filterItem : area) {
            if (filterItem.active == FilterItem.STATUS_ACTIVE_TRUE) {
                return filterItem;
            }
        }
        return null;
    }

    /**
     * 深度克隆本实例
     * */
    public SearchFilter deepClone() {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream objOut = null;
        ByteArrayInputStream byteIn = null;
        ObjectInputStream objIn = null;

        try {
            byteOut = new ByteArrayOutputStream();
            objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(this);

            byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            objIn = new ObjectInputStream(byteIn);

            return (SearchFilter) objIn.readObject();
        } catch (Exception e) {

        } finally {
            try {
                if (objOut != null) objOut.close();
                if (objIn != null) objIn.close();
                if (byteIn != null) byteIn.close();
                if (byteOut != null) byteOut.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

}
