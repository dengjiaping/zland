package com.zhisland.android.blog.feed.bean;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonExclude;

import java.io.Serializable;

/**
 * Created by arthur on 2016/8/31.
 */
public class Feed extends OrmDto {

    @GsonExclude
    public EbAction action;

    // 动态ID
    public String feedId;
    // 标题
    public String title;
    // 类型
    public Integer type;
    // 发起者
    public User user;
    // 内容
    @GsonExclude
    public Serializable attach;

    //只是用来做解析使用
    @Deprecated
    public String content;

    // 点赞
    public CustomIcon like;
    // 评论
    public CustomIcon comment;
    // 转播
    public CustomIcon forward;
    // 转播人
    public User forwardUser;

    // 推荐标签
    public String recommendTag;

    //发布时间
    public Long time = 0l;

}
