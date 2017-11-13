package com.zhisland.lib.component.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ZHPageData<T> implements Serializable {

    private static final long serialVersionUID = -5607262592780784855L;

    //是否最后一页
    @SerializedName("lastPage")
    public boolean pageIsLast = true;

    //下拉传值
    @SerializedName("previousId")
    public String preId;

    //上拉传值
    @SerializedName("nextId")
    public String nextId;

    //未读数
    @SerializedName("unread")
    public Integer unread;


    @SerializedName("totalCount")
    public long count;

    @SerializedName("data")
    public ArrayList<T> data;

}
