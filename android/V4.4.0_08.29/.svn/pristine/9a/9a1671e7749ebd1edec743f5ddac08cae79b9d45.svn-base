package com.zhisland.android.blog.message.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.TimeUtil;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.android.blog.message.model.IInteractionModel;
import com.zhisland.android.blog.message.model.impl.InteractionModel;
import com.zhisland.android.blog.message.presenter.InteractionMessagePresenter;
import com.zhisland.android.blog.message.view.IInteractionView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 互动消息
 */
public class FragInteractionMessage extends FragPullListMvps<Message> implements IInteractionView {


    private static final String PAGE_NAME = "FragInteractionMessage";

    public static final String INK_DATA = "list_data";
    private InteractionMessagePresenter intrestablePresenter;

    //调起可能认识的人
    public static void Invoke(Context context) {
        CommonFragActivity.CommonFragParams paranm = new CommonFragActivity.CommonFragParams();
        paranm.title = "互动消息";
        paranm.enableBack = true;
        paranm.clsFrag = FragInteractionMessage.class;

        Intent intent = CommonFragActivity.createIntent(context, paranm);
        //intent.putExtra(INK_DATA, (Serializable) users);
        context.startActivity(intent);
    }

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenterMap = new HashMap<>(1);

        intrestablePresenter = new InteractionMessagePresenter();
        IInteractionModel intrestableModel = new InteractionModel();
        intrestablePresenter.setModel(intrestableModel);
        presenterMap.put(InteractionMessagePresenter.class.getSimpleName(), intrestablePresenter);


        return presenterMap;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        UserListAdapter adapter = new UserListAdapter();
        getPullProxy().setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.BOTH);
        getPullProxy().getPullView().setBackgroundResource(R.color.color_bg2);
        TextView emptyView = new TextView(getActivity());

        getPullProxy().getPullView().getRefreshableView().setEmptyView(emptyView);

        ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText("暂无新的互动消息");
    }


    @Override
    public String getPageName() {
        return PAGE_NAME;
    }


    @Override
    public void updateInteractionMessage(List<Message> users) {
        getPullProxy().getAdapter().clearItems();
        getPullProxy().getAdapter().add(users);
    }

    @Override
    public void gotoUser(User user) {
        ActProfileDetail.invoke(getActivity(), user.uid);
    }

    @Override
    public void gotoFeed(Feed feed) {
        //TODO
        //ActProfileDetail.invoke(getActivity(), feed);
    }

    @Override
    public void loadNormal() {
        intrestablePresenter.loadNormal();
    }

    @Override
    public void loadMore(String nextId) {
        intrestablePresenter.loadMore(nextId);
    }

//    public void onLoadSucessfully(ZHPageData<Message> data) {
//        System.out.println("FragInteractionMessage onLoadSucessfully");
//        //getPullProxy().onLoadSucessfully(data);
//    }


    //region adapter

    class UserListAdapter extends BaseListAdapter<Message> {


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_interaction_message, null);
                UserHolder holder = new UserHolder(convertView);
                convertView.setTag(holder);
            }
            UserHolder holder = (UserHolder) convertView.getTag();
            boolean showLine = (getCount() - 1) > position;
            holder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }


        class UserHolder {
            @InjectView(R.id.interaction_user_icon)
            ImageView icon;

            @InjectView(R.id.interaction_comment_user_name)
            TextView name;

            @InjectView(R.id.interaction_comment_user_tag)
            ImageView starIcon;

            @InjectView(R.id.interaction_comment_content)
            TextView content;

            @InjectView(R.id.interaction_comment_time)
            TextView time;

            @InjectView(R.id.interaction_comment_feed_title)
            TextView feedTitle;

            @InjectView(R.id.interaction_comment_feed_image)
            ImageView feedImage;


            @InjectView(R.id.more_people_user_line)
            View lineView;

            private Message message;

            public UserHolder(final View convertView) {
                ButterKnife.inject(this, convertView);
                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                });
            }

            @OnClick({R.id.interaction_user_icon, R.id.interaction_comment_user_name, R.id.interaction_comment_user_tag})
            void addClick() {
                intrestablePresenter.onUserClicked(message.fromUser);
            }

            @OnClick({R.id.interaction_comment_feed_layout, R.id.interaction_comment_content, R.id.interaction_comment_time})
            void cancleClick() {
                intrestablePresenter.onFeedClicked(null);
            }


            public void fill(Message message) {
                this.message = message;
                if (null == message) {
                    return;
                }

                content.setText(message.content);
                time.setText(TimeUtil.getTimeString(message.time));

                if (null == message.fromUser) {
                    return;
                }

                ImageWorkFactory.getCircleFetcher().loadImage(message.fromUser.userAvatar, icon, R.drawable.avatar_default);
                name.setText(message.fromUser.name);
                starIcon.setImageResource(message.fromUser.getVipIconId());
                //TODO
                //如果类型是新鲜事,可能没有主题,如果没有主题,则显示图片
                if (1 == message.clueDataType) {
                    feedImage.setVisibility(View.GONE);
                    feedTitle.setText(message.clue);
                } else {
                    ImageWorkFactory.getFetcher().loadImage(message.uri, feedImage);
                    feedTitle.setVisibility(View.GONE);
                    feedImage.setVisibility(View.VISIBLE);
                }


            }


            public void recycle() {

            }

        }

    }
    //endregion


}
