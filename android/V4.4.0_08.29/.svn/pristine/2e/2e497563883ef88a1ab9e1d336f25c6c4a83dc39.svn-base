package com.zhisland.android.blog.feed.model.impl;

import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.feed.bean.Attach;
import com.zhisland.android.blog.feed.bean.AttachImg;
import com.zhisland.android.blog.feed.bean.AttachPraise;
import com.zhisland.android.blog.feed.bean.AttachResource;
import com.zhisland.android.blog.feed.bean.CustomIcon;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.android.blog.feed.bean.RecommendData;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.android.blog.feed.bean.TimelineData;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by arthur on 2016/9/7.
 */
public class MockFeedCreator {

    private static Random random = new Random(50);

    //创建假数据
    public static TimelineData createMockTimeLineData() {
        TimelineData timelineData = new TimelineData();

        timelineData.feeds = new ZHPageData<>();
        timelineData.feeds.pageIsLast = true;

        timelineData.feeds.data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Feed item = createMockFeed(i);
            timelineData.feeds.data.add(item);
        }

        timelineData.intrestableUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RecommendUser iu = new RecommendUser();
            iu.user = DBMgr.getMgr().getUserDao().getSelfUser();
            iu.user.name = iu.user.name + (i + 1);
            timelineData.intrestableUsers.add(iu);
        }

        timelineData.recommendData = new RecommendData();
        timelineData.recommendData.uri = "zhisland://com.zhisland/news/432";
        timelineData.recommendData.imgrl = "http://192.168.2.151:8095/pic/19.jpg";

        return timelineData;
    }

    public static Feed createMockFeed(int index) {
        Feed feed = new Feed();
        if (index % 3 == 0) {
            feed.forwardUser = DBMgr.getMgr().getUserDao().getSelfUser();
        }
        feed.feedId = UUID.randomUUID().toString();
        feed.recommendTag = index % 4 == 0 ? "正和岛推荐" : null;
        feed.title = "这是一个牛逼的feed";
        feed.type = ((index % 5) + 1) * 100;
        feed.attach = createAttach(feed.type);
        feed.user = DBMgr.getMgr().getUserDao().getSelfUser();
        feed.forward = createMockIcon("转播", random.nextInt(100), index % 2);
        feed.comment = createMockIcon("评论", random.nextInt(100), index % 3);
        feed.like = createMockIcon("赞", random.nextInt(100), index % 2);
        feed.time = System.currentTimeMillis() - index * 2000000;

        return feed;
    }

    public static CustomIcon createMockIcon(String name, int quantity, int operable) {
        CustomIcon icon = new CustomIcon();
        icon.name = name;
        icon.quantity = quantity;
        icon.operable = operable;
        return icon;
    }

    public static Serializable createAttach(int type) {
        switch (type) {
            case FeedType.IMG: {
                AttachImg attach = new AttachImg();
                attach.pictures = new ArrayList<>(3);

                FeedPicture fp = new FeedPicture();
                fp.localPath = "http://192.168.2.151:8095/pic/1.jpg";
                fp.height = 300;
                fp.width = 400;
                attach.pictures.add(fp);

                fp = new FeedPicture();
                fp.localPath = "http://192.168.2.151:8095/pic/2.jpg";
                fp.height = 700;
                fp.width = 400;
                attach.pictures.add(fp);

                return attach;
            }
            case FeedType.COMMENT: {
                AttachPraise attach = new AttachPraise();
                attach.title = "这是一个靠谱的小青年TT TT";
                attach.toUser = new User();
                attach.toUser.name = "姓名测试";
                attach.toUser.uid = 6135626192415358979l;
                attach.toUser.userAvatar = "http://192.168.2.151:8095/pic/6.jpg";
                return attach;
            }
            case FeedType.INFO: {
                Attach attach = new Attach();
                attach.title = "如何在绝境中带领企业杀出来。。";
                return attach;
            }
            case FeedType.EVENT: {
                Attach attach = new Attach();
                attach.title = "2050正和岛新年家宴";
                return attach;
            }
            case FeedType.RESOURCE: {
                AttachResource attach = new AttachResource();
                attach.title = "梦幻一样的庄园，只要15个亿";
                attach.tags = new ArrayList<>(2);
                attach.tags.add(new ZHDicItem(null, "地产"));
                attach.tags.add(new ZHDicItem(null, "金融"));
                return attach;
            }
        }
        return null;

    }
}
