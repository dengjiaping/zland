package com.zhisland.android.blog.immvp.bean;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

/**
 * 受到消息检查结果
 * <p/>
 * Created by arthur on 2016/9/18.
 */
public class ChatReceiveVerify extends OrmDto {

    public int status;//是否可接收聊天消息 0 不可接收  1 可接收
    public AuthVerifyStatus authVerifyStatus;//权限检查状态
    public String msg;//显示文案
    public User user;//聊天发起方用户信息
}
