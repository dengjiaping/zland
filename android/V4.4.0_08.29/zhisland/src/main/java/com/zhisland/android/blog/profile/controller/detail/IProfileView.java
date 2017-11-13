package com.zhisland.android.blog.profile.controller.detail;

import com.zhisland.android.blog.profile.dto.UserDetail;

/**
 * Created by Mr.Tui on 2016/9/8.
 */
public interface IProfileView {

    void onUpdate(UserDetail userDetail);

    void onLoadError(Throwable error);

}
