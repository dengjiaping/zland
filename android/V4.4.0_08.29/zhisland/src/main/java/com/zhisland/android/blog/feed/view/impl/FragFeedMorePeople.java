package com.zhisland.android.blog.feed.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.android.blog.feed.model.IIntrestableModel;
import com.zhisland.android.blog.feed.model.ModelFactory;
import com.zhisland.android.blog.feed.presenter.IntrestablePresenter;
import com.zhisland.android.blog.feed.view.IIntrestableView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 更多认识的人
 */
public class FragFeedMorePeople extends FragPullListMvps<RecommendUser> implements IIntrestableView {


    private static final String PAGE_NAME = "FragFeedMorePeople";

    public static final String INK_DATA = "list_data";
    private IntrestablePresenter intrestablePresenter;

    //调起可能认识的人
    public static void Invoke(Context context, List<RecommendUser> users) {
        CommonFragActivity.CommonFragParams paranm = new CommonFragActivity.CommonFragParams();
        paranm.title = "可能感兴趣的人";
        paranm.enableBack = true;
        paranm.clsFrag = FragFeedMorePeople.class;

        Intent intent = CommonFragActivity.createIntent(context, paranm);
        if (users != null) {
            intent.putExtra(INK_DATA, (Serializable) users);
        }
        context.startActivity(intent);
    }

    //region 生命周期&重载
    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenterMap = new HashMap<>(1);

        intrestablePresenter = new IntrestablePresenter();
        IIntrestableModel intrestableModel = ModelFactory.createIntrestableModel();
        intrestablePresenter.setModel(intrestableModel);
        presenterMap.put(IntrestablePresenter.class.getSimpleName(), intrestablePresenter);

        List<RecommendUser> tts = (List<RecommendUser>) getActivity().getIntent().getSerializableExtra(INK_DATA);
        intrestablePresenter.setData(tts);

        return presenterMap;
    }

    @Override
    public void loadNormal() {
        intrestablePresenter.loadNormal();
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


        getPullProxy().getPullView().setBackgroundResource(R.color.color_bg2);
    }


    @Override
    public String getPageName() {
        return PAGE_NAME;
    }
    //endregion


    //region 接口实现
    @Override
    public void updateIntrestable(List<RecommendUser> users) {
        if (users != null) {
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
            getPullProxy().getAdapter().clearItems();
            getPullProxy().getAdapter().add(users);
        } else {
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }

    @Override
    public void gotoUserDetail(User user) {
        ActProfileDetail.invoke(getActivity(), user.uid);
    }

    @Override
    public void removeRecommend(RecommendUser user) {
        getPullProxy().getAdapter().removeItem(user);
    }

    @Override
    public void updateItem(RecommendUser inviteUser) {

    }

    @Override
    public void hideIntrestView() {

    }

    @Override
    public void showIntrestView() {

    }

    @Override
    public void gotoAllIntrest(List<RecommendUser> users) {

    }

    @Override
    public void onLoadSucess(List<RecommendUser> recommendUsers) {
        getPullProxy().onLoadSucessfully(recommendUsers);
    }
    //endregion


    //region adapter

    class UserListAdapter extends BaseListAdapter<RecommendUser> {


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_more_people_user, null);
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
            @InjectView(R.id.more_people_user_icon)
            ImageView icon;

            @InjectView(R.id.more_people_user_name)
            TextView name;

            @InjectView(R.id.more_people_user_tag)
            ImageView starIcon;

            @InjectView(R.id.more_people_user_company)
            TextView company;

            @InjectView(R.id.more_people_user_position)
            TextView position;

            @InjectView(R.id.more_people_user_action_add)
            TextView add;

            @InjectView(R.id.more_people_user_action_cancle)
            ImageView cancle;

            @InjectView(R.id.more_people_user_line)
            View lineView;

            private RecommendUser user;

            public UserHolder(final View convertView) {
                ButterKnife.inject(this, convertView);
                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        System.out.println("convertView click");
                    }
                });
            }

            @OnClick(R.id.more_people_user_action_add)
            void addClick() {
                intrestablePresenter.onFollowClicked(user);
            }

            @OnClick(R.id.more_people_user_action_cancle)
            void cancleClick() {
                intrestablePresenter.onIgnoreClicked(user);
            }


            public void fill(RecommendUser user) {
                this.user = user;
                if (null == user || null == user.user) {
                    return;
                }

                ImageWorkFactory.getCircleFetcher().loadImage(user.user.userAvatar, icon, R.drawable.avatar_default);
                name.setText(user.user.name);
                company.setText(user.user.userCompany);
                position.setText(user.user.userPosition);

                starIcon.setImageResource(user.user.getVipIconId());
            }


            public void recycle() {

            }

        }

    }
    //endregion


}
