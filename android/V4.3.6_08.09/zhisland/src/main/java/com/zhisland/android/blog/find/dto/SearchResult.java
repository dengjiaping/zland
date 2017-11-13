package com.zhisland.android.blog.find.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.component.adapter.ZHPageData;

/**
 * 搜索结果
 * Created by Mr.Tui on 2016/5/20.
 */
public class SearchResult<T> extends ZHPageData<T> {

    /**
     * 搜索结果配套的筛选项
     * */
    @SerializedName("filter")
    public SearchFilter filter;

}
