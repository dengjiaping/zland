package com.zhisland.android.blog.common.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.CustomIcon;

import java.lang.reflect.Type;

/**
 * Created by arthur on 2016/9/8.
 */
public class ReplyGsonAdapter implements JsonDeserializer<Reply> {

    static Gson gson = new Gson();

    @Override
    public Reply deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        Reply reply = gson.fromJson(json, Reply.class);

        if (reply != null) {
            if (reply.publishTime == null && reply.pulblishTime != null) {
                reply.publishTime = reply.pulblishTime;
            }

            if (reply.fromUser == null && reply.publisher != null) {
                reply.fromUser = reply.publisher;
            }

            if (reply.toUser == null && reply.reviewedUid > 0) {
                reply.toUser = new User();
                reply.toUser.uid = reply.reviewedUid;
                reply.toUser.name = reply.reviewedName;
            }

        }


        return reply;
    }
}
