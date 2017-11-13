package com.zhisland.android.blog.feed.view.impl.holder;

import android.content.Context;

import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.android.blog.feed.bean.FeedViewType;

/**
 * 附件视图的工厂类
 * Created by arthur on 2016/9/1.
 */
public class AttachCreator {

    /**
     *
     * @param context
     * @param type {@code FeedViewType}  feed 类型：图片、评论、资源、资讯、活动
     * @param isShare 是否为分享样式
     * @return
     */
    public AttachHolder getAttachHolder(Context context, int type, boolean isShare) {

        switch (type) {
            case FeedViewType.IMG: {
                return new ImageHolder(context);
            }
            case FeedViewType.COMMENT: {
                return new CommentHolder(context);
            }
            case FeedViewType.INFO: {
                return new EIHolder(context, isShare);
            }
            case FeedViewType.RESOURCE: {
                return new ResourceHolder(context);
            }
        }

        return null;
    }

    /**
     * 根据feed类型返回视图的类型
     *
     * @param feed
     * @return
     */
    public int getViewType(Feed feed) {
        switch (feed.type) {
            case FeedType.IMG:
                return FeedViewType.IMG;
            case FeedType.COMMENT:
                return FeedViewType.COMMENT;
            case FeedType.INFO:
            case FeedType.EVENT:
                return FeedViewType.INFO;
            case FeedType.RESOURCE:
                return FeedViewType.RESOURCE;
        }
        return FeedViewType.IMG;
    }


    //feed构造器单例
    public static AttachCreator Instance() {
        return InstanceHolder.Instance;
    }

    private static class InstanceHolder {
        static AttachCreator Instance = new AttachCreator();
    }
}
