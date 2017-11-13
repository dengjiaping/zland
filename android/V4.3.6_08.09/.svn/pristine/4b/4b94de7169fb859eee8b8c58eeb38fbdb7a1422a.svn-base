package com.zhisland.android.blog.tabhome.model.impl;

import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.tabhome.model.IHomeInterceptModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 假数据
 * Created by 向飞 on 2016/5/28.
 */
public class FakeHomeInterceptModel implements IHomeInterceptModel {

    @Override
    public Observable<TaskCardList> checkFreshTask() {
        TaskCardList taskCardList = createTasks(0);
        return Observable.just(taskCardList);
//        Observable.create(new Observable.OnSubscribe<TaskCardList>() {
//            @Override
//            public void call(Subscriber<? super TaskCardList> subscriber) {
//                subscriber.onNext(null);
//                subscriber.onError(null);
//            }
//        });
    }

    @Override
    public Observable<List<UserComment>> checkRecommendComment() {
        List<UserComment> datas = getUserComments();
        return Observable.just(datas);
    }

    @Override
    public long getLastTaskShowTime() {
        return 0;
    }

    @Override
    public void saveTaskShowTime(long time) {

    }

    @Override
    public long getLastPrizeShowTime() {
        return 0;
    }

    @Override
    public void savePrizeShowTime(long time) {

    }

    private TaskCardList createTasks(int display) {
        TaskCardList cardList = new TaskCardList();

        cardList.guideTitle = "欢迎您！\n来自北京的新岛邻";
        cardList.title = "正和岛新手闯关";
        cardList.desc = "完成闯关让自己在岛上获取更多价值";
        cardList.display = display;
        cardList.unfinishedCount = 1;

        cardList.cards = new ArrayList<>(3);

        TaskCard card = new TaskCard();
        card.type = TaskType.CONTACT;
        card.summary = "初步建立好友圈";
        card.title = "你的人脉\n决定你拥有的能量";
        card.desc = "想知道那些朋友已经加入正和岛？";
        card.state = new CustomState();
        card.state.setState(TaskStatus.NORMAL);
        card.state.setIsOperable(1);
        card.state.setStateName("发现已经加入的好友");
        cardList.cards.add(card);

        card = new TaskCard();
        card.type = TaskType.RESOURCE;
        card.summary = "发现商业价值";
        card.title = "资源池\n违逆创造无限价值";
        card.desc = "想让用户找上门？还不赶快晒资源";
        card.state = new CustomState();
        card.state.setState(TaskStatus.FINISHED);
        card.state.setIsOperable(1);
        card.state.setStateName("发布资源");
        cardList.cards.add(card);

        card = new TaskCard();
        card.type = TaskType.RESOURCE;
        card.summary = "发现商业价值";
        card.title = "资源池\n违逆创造无限价值";
        card.desc = "想让用户找上门？还不赶快晒资源";
        card.state = new CustomState();
        card.state.setState(0);
        card.state.setIsOperable(1);
        card.state.setStateName("发布资源");
        cardList.cards.add(card);

        card = new TaskCard();
        card.type = TaskType.FIGURE;
        card.summary = "打造你的商界形象";
        card.title = "打造你的商界形象";
        card.desc = "优质的形象可以给朋友更好的形象";
        card.state = new CustomState();
        card.state.setState(0);
        card.state.setIsOperable(1);
        card.state.setStateName("上传形象照");
        cardList.cards.add(card);

        card = new TaskCard();
        card.type = TaskType.PRICE;
        card.summary = "沉淀好友关系";
        card.title = "知己难寻\n谁是最懂我的人？";
        card.desc = "优质的形象可以给朋友更好的形象";
        card.state = new CustomState();
        card.state.setState(0);
        card.state.setIsOperable(1);
        card.state.setStateName("查看朋友给我的审评");
        cardList.cards.add(card);

        return cardList;
    }

    public ArrayList<UserComment> getUserComments() {
        ArrayList<UserComment> comments = new ArrayList<>();
        UserComment userComment;
        User publisher;
        User toUser;

        userComment = new UserComment();
        userComment.content = "专注社交网站九年，哈哈哈！";
        publisher = new User();
        publisher.name = "比尔盖茨";
        publisher.userCompany = "微软";
        publisher.userPosition = "CEO";
        userComment.publisher = publisher;
        toUser = new User();
        toUser.uid = 2004819;
        toUser.name = "王林男";
        toUser.userCompany = "百度公司";
        toUser.userPosition = "创始人兼CEO";
        toUser.figure = "http://192.168.2.81:8201/impic/T1yFLTBy_T1RXrhCrK.jpg";
        userComment.toUser = toUser;
        comments.add(userComment);

        userComment = new UserComment();
        userComment.content = "专注社交网站十年，哈哈哈！";
        publisher = new User();
        publisher.name = "比尔盖茨";
        publisher.userCompany = "微软";
        publisher.userPosition = "CEO";
        userComment.publisher = publisher;
        toUser = new User();
        toUser.uid = 2004819;
        toUser.name = "王林男";
        toUser.userCompany = "百度公司";
        toUser.zhislandType = 300;
        toUser.userPosition = "创始人兼CEO";
        toUser.figure = "http://192.168.2.81:8201/impic/T1yFLTBy_T1RXrhCrK.jpg";
        userComment.toUser = toUser;
        comments.add(userComment);

        userComment = new UserComment();
        userComment.content = "专注社交网站十一年，哈哈哈！";
        publisher = new User();
        publisher.name = "比尔盖茨";
        publisher.userCompany = "微软";
        publisher.userPosition = "CEO";
        userComment.publisher = publisher;
        toUser = new User();
        toUser.uid = 2004819;
        toUser.name = "王林男";
        toUser.zhislandType = 300;
        toUser.userCompany = "百度公司";
        toUser.userPosition = "创始人兼CEO";
        toUser.figure = "http://192.168.2.81:8201/impic/T1yFLTBy_T1RXrhCrK.jpg";
        userComment.toUser = toUser;
        comments.add(userComment);
        return comments;
    }
}
