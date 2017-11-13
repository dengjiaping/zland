package com.zhisland.android.blog.freshtask.model.impl;

import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.mvp.model.IMvpModel;

/**
 * Created by 向飞 on 2016/5/30.
 */
public class TaskGuideModel implements IMvpModel {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public User getSelf() {
        return DBMgr.getMgr().getUserDao().getSelfUser();
    }

}
