package com.zhisland.android.blog.common.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.comment.view.ReplyAdapter;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.async.http.task.DateDeserializer;
import com.zhisland.lib.async.http.task.GsonExclusionStrategy;

import java.util.Date;

/**
 * Created by arthur on 2016/9/10.
 */
public class GsonCreater {

    public static Gson CreateGson(){
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setExclusionStrategies(new GsonExclusionStrategy())
                .serializeSpecialFloatingPointValues();
        gsonBuilder.registerTypeAdapter(Date.class,
                new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Feed.class, new FeedGsonAdapter());
        gsonBuilder.registerTypeAdapter(Comment.class, new CommentGsonAdapter());
        gsonBuilder.registerTypeAdapter(Reply.class, new ReplyGsonAdapter());

        return gsonBuilder.create();
    }

}
