package com.zhisland.android.blog.freshtask.model;

import com.zhisland.android.blog.freshtask.model.impl.FriendCallModel;

/**
 * Created by arthur on 2016/6/21.
 */
public class ModelFactory {

    /**
     * 构造召唤好友的MOdel
     *
     * @return
     */
    public static IFriendCallModel CreateFriendCallModel() {
        return new FriendCallModel();
    }
}
