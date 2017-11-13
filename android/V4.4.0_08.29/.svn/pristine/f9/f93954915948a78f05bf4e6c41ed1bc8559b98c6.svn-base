package com.zhisland.android.blog.profilemvp.model;

import com.zhisland.android.blog.profilemvp.bean.UserPhoto;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * 个人主页  相册model
 * Created by Mr.Tui on 2016/9/6.
 */
public interface IMyPhotoModel extends IMvpModel {

    /**
     * 分页获取相册
     */
    Observable<ZHPageData<UserPhoto>> getPhoto(final long userId, final String nextId);

    /**
     * 删除相册中的图片
     */
    Observable<Void> deletePhoto(final long photoId);

}
