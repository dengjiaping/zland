package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.model.impl.TaskContainerModel;
import com.zhisland.android.blog.freshtask.view.ITaskContainerView;

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
 * Created by 向飞 on 2016/5/27.
 */
public class TaskContainerPresenterTest {

    @Mock
    private TaskContainerModel model;
    @Mock
    private ITaskContainerView view;

    private TaskContainerPresenter presenter;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        presenter = new TaskContainerPresenter();
        presenter.setModel(model);
        presenter.bindView(view);
        presenter.setSchedulerObserver(Schedulers.immediate());
        presenter.setSchedulerSubscribe(Schedulers.immediate());

    }

    @Test
    public void testSetTaskWhenBind() {

        TaskCardList cardList = createTasks();

        presenter.setAllTasks(cardList);

        verify(view).setTask(any(List.class));
    }

    @Test
    public void testSwitchNotEnd() {

        TaskCardList cardList = createTasks();

        presenter.setAllTasks(cardList);
        presenter.switchTo(0);

        verify(view).updateStep(any(String.class));
        verify(view).updateSummary(any(String.class));
        verify(view).updateNext("下一步");
    }

    @Test
    public void testSwitchToEnd() {

        TaskCardList cardList = createTasks();

        presenter.setAllTasks(cardList);
        presenter.switchTo(2);

        verify(view).updateStep(any(String.class));
        verify(view).updateSummary(any(String.class));
        verify(view).updateNext("完成");
    }


    private TaskCardList createTasks() {
        TaskCardList cardList = new TaskCardList();

        cardList.display = 1;

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
