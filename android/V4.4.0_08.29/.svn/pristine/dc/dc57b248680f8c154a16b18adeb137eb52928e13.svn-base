package com.zhisland.android.blog.common.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.feed.bean.CustomIcon;

import java.lang.reflect.Type;

/**
 * Created by arthur on 2016/9/8.
 */
public class CommentGsonAdapter implements JsonDeserializer<Comment> {

    static Gson gson = CommentGsonCreater.CreateGson();

    @Override
    public Comment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        Comment comment = gson.fromJson(json, Comment.class);

        //兼容资讯数据结构
        if (comment != null && comment.commentId <= 0 && comment.viewpointId > 0) {
            comment.commentId = comment.viewpointId;
        }

        if(comment != null && comment.replys != null && comment.replyList == null){
            comment.replyList = comment.replys;
        }

        if (comment.likeCustomIcon == null) {
            comment.likeCustomIcon = new CustomIcon();
            comment.likeCustomIcon.operable = 1;
        }

        if (comment.countCollect != null) {
            comment.likeCustomIcon.clickState = comment.countCollect.likedState;
            if (comment.likeCustomIcon.clickState == 1) {
                comment.likeCustomIcon.operable = 0;
            } else {
                comment.likeCustomIcon.operable = 1;
            }
            comment.likeCustomIcon.quantity = comment.countCollect.likeCount;
            comment.replyCount = comment.countCollect.replyCount;
        }

        return comment;
    }
}
