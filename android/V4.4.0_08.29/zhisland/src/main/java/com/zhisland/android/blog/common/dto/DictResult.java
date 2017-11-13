package com.zhisland.android.blog.common.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

import java.util.List;

/**
 * 用于接收服务器返回的字段对象
 */
public class DictResult extends OrmDto {

    @SerializedName("dict")
    public List<Dict> dicts;

    @SerializedName("notEmpty")
    public boolean notEmpty;

}
