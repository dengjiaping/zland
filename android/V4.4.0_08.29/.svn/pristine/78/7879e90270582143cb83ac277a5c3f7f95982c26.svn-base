package com.zhisland.android.blog.common.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.zhisland.android.blog.feed.bean.Attach;
import com.zhisland.android.blog.feed.bean.AttachImg;
import com.zhisland.android.blog.feed.bean.AttachPraise;
import com.zhisland.android.blog.feed.bean.AttachResource;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by arthur on 2016/9/8.
 */
public class FeedGsonAdapter implements JsonDeserializer<Feed> {

    static Gson gson = new Gson();

    @Override
    public Feed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        Feed feed = gson.fromJson(json, Feed.class);

        if (feed.content == null)
            return feed;

        switch (feed.type) {
            case FeedType.IMG: {
                feed.attach = gson.fromJson(feed.content, AttachImg.class);
                break;
            }
            case FeedType.EVENT:
            case FeedType.INFO: {
                feed.attach = gson.fromJson(feed.content, Attach.class);
                break;
            }
            case FeedType.COMMENT: {
                feed.attach = gson.fromJson(feed.content, AttachPraise.class);
                break;
            }
            case FeedType.RESOURCE: {
                feed.attach = gson.fromJson(feed.content, AttachResource.class);
                break;
            }
        }

        return feed;
    }
}
