package com.zhisland.android.blog.message.bean;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

/**
 * Created by arthur on 2016/9/13.
 */
public class Message extends OrmDto {

    public Long msgId; //    消息ID
    public String uri;
    public String content;//
    public long time;//
    public long type;
    public int clueDataType;
    public String clue;
    public User fromUser;
    public User toUser;
}
