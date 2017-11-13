package com.zhisland.android.blog.im.authority;

import android.content.Context;
import android.content.Intent;

import com.zhisland.lib.authority.IAuthorityHandler;

/**
 * 绘画页面的权限拦截处理
 * <p>
 * Created by arthur on 2016/9/12.
 */
public class AuthChatDetail implements IAuthorityHandler {

    @Override
    public boolean handle(Context context, Intent intent) {
        //TODO 处理权限判断
        return true;
    }
}
