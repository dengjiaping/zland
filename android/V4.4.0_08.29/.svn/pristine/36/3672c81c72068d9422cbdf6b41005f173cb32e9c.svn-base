package com.zhisland.android.blog.profilemvp.bean;

/**
 * Created by arthur on 2016/9/7.
 */
public class RelationConstants {


    public static final int UNFOLLOW_TA = -1;//两个人没有任何关系
    public static final int BE_FOLLOWED = 21;//登录用户己关注此用户
    public static final int FOLLOWED_TA = 12;//此用户己关注登录用户
    public static final int BOTH_FLLOWED = 22;//互相关注

    //关注了ta
    public static boolean hadFollowedTa(int state) {
        return state == FOLLOWED_TA || state == BOTH_FLLOWED;
    }

    //关注了我
    public static boolean hadFollowedMe(int state){
        return state == BE_FOLLOWED || state == BOTH_FLLOWED;
    }

}
