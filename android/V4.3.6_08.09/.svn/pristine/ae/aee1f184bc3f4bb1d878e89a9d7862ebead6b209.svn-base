package com.zhisland.android.blog.tabhome.presenter;

import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.tabhome.View.IHomeInterceptView;
import com.zhisland.android.blog.tabhome.model.IHomeInterceptModel;
import com.zhisland.lib.component.adapter.ZHPageData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 向飞 on 2016/5/28.
 */
public class HomeInterceptPresenterTest {


    @Mock
    private IHomeInterceptModel model;
    @Mock
    private IHomeInterceptView view;

    private HomeInterceptPresenter presenter;

    @Before
    public void setupMocksAndView() {

        MockitoAnnotations.initMocks(this);

        presenter = new HomeInterceptPresenter();

        presenter.setModel(model);
        presenter.bindView(view);
        presenter.setSchedulerObserver(Schedulers.immediate());
        presenter.setSchedulerSubscribe(Schedulers.immediate());

    }

    //已经显示过引导
    @Test
    public void testNotShowTaskAndPrize() {

        when(model.getLastTaskShowTime()).thenReturn(System.currentTimeMillis());
        when(model.getLastPrizeShowTime()).thenReturn(System.currentTimeMillis());

        presenter.checkIntercept(false);

    }

    //无引导并且有新手任务
    @Test
    public void testNoGuideAndHasTask() {

        TaskCardList cardList = createTasks(0, 1);
        when(model.getLastTaskShowTime()).thenReturn(1l);
        when(model.checkFreshTask()).thenReturn(Observable.just(cardList));

        presenter.checkIntercept(false);

        verify(view).showTaskCards(any(TaskCardList.class), 0);
    }

    /**
     * 有引导并且有新手任务
     */
    @Test
    public void testHasGuideAndHasTask() {

        TaskCardList cardList = createTasks(1, 1);
        when(model.checkFreshTask()).thenReturn(Observable.just(cardList));

        presenter.checkIntercept(false);

        verify(view).showTaskGuide(any(TaskCardList.class));
    }

    /**
     * 无新手任务并且有评论推荐
     */
    @Test
    public void testNoTaskAndHasComment() {

        TaskCardList cardList = null;
        List<UserComment> comments = this.createComment();
        when(model.checkFreshTask()).thenReturn(Observable.just(cardList));

        presenter.checkIntercept(false);

        verify(view).showRecommendComment(any(List.class));
    }

    /**
     * 无新手任务并且无评论推荐
     */
    @Test
    public void testNoTaskAndComment() {

        TaskCardList cardList = null;
        List<UserComment> comments = null;
        when(model.checkFreshTask()).thenReturn(Observable.just(cardList));

        presenter.checkIntercept(false);

    }

    List<UserComment> createComment() {
        List<UserComment> comments= new ArrayList<>();
        return comments;
    }

    private TaskCardList createTasks(int display, int unfinished) {
        TaskCardList cardList = new TaskCardList();

        cardList.display = display;
        cardList.unfinishedCount = unfinished;

        cardList.cards = new ArrayList<>(3);

        TaskCard card = new TaskCard();
        card.type = TaskType.CONTACT;
        card.summary = "初步建立好友圈";
        card.title = "你的人脉\n决定你拥有的能量";
        card.desc = "想知道那些朋友已经加入正和岛？";
        card.state = new CustomState();
        card.state.setState(0);
        card.state.setStateName("发现已经加入的好友");
        cardList.cards.add(card);

        card = new TaskCard();
        card.type = TaskType.FIGURE;
        card.summary = "打造你的商界形象";
        card.title = "打造你的商界形象";
        card.desc = "优质的形象可以给朋友更好的形象";
        card.state = new CustomState();
        card.state.setState(0);
        card.state.setStateName("上传形象照");
        cardList.cards.add(card);

        card = new TaskCard();
        card.type = TaskType.PRICE;
        card.summary = "沉淀好友关系";
        card.title = "知己难寻\n谁是最懂我的人？";
        card.desc = "优质的形象可以给朋友更好的形象";
        card.state = new CustomState();
        card.state.setState(0);
        card.state.setStateName("查看朋友给我的审评");
        cardList.cards.add(card);

        return cardList;
    }

}
