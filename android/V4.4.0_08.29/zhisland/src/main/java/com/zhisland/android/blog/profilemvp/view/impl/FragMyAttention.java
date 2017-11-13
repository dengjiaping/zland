package com.zhisland.android.blog.profilemvp.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.feed.view.impl.FragFeedMorePeople;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profilemvp.model.ModelFactory;
import com.zhisland.android.blog.profilemvp.presenter.MyAttentionPresenter;
import com.zhisland.android.blog.profilemvp.view.IMyAttentionView;
import com.zhisland.android.blog.profilemvp.view.impl.adapter.InviteUserAdapter;
import com.zhisland.android.blog.profilemvp.view.impl.holder.InviteUserHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我关注的人页面
 * Created by Mr.Tui on 2016/9/6.
 */
public class FragMyAttention extends FragPullListMvps<InviteUser> implements IMyAttentionView {

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragMyAttention.class;
        param.enableBack = true;
        param.title = "我关注的人";
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    MyAttentionPresenter myAttentionPresenter;

    @Override
    public String getPageName() {
        return "FragMyAttention";
    }

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenterMap = new HashMap<>(1);
        myAttentionPresenter = new MyAttentionPresenter();
        myAttentionPresenter.setModel(ModelFactory.CreateMyAttentionModel());
        presenterMap.put(MyAttentionPresenter.class.getSimpleName(), myAttentionPresenter);
        return presenterMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View pullView = super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.frag_my_attention, container, false);
        LinearLayout.LayoutParams listLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        listLp.weight = 1;
        rootView.addView(pullView, listLp);

        ViewGroup.LayoutParams rootLp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(rootLp);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    ClickableSpan clickableSpan = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            FragFeedMorePeople.Invoke(getActivity(), null);
            getActivity().finish();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.color_dc));
            ds.setUnderlineText(false);
        }

    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        internalView.setFastScrollEnabled(false);
        pullView.getRefreshableView().setDividerHeight(0);
        pullView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));

        EmptyView ev = new EmptyView(getActivity());
        ev.setImgVisibility(View.INVISIBLE);
        ev.setBtnVisibility(View.INVISIBLE);
        SpannableString ss = new SpannableString("你还没关注过任何人\n先去关注一些感兴趣的人吧");
        ss.setSpan(clickableSpan, 16, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ev.setPrompt(ss);
        ev.setPromptTextColor(R.color.color_f3);
        getPullProxy().getPullView().setEmptyView(ev);
    }

    @Override
    public void loadNormal() {
        super.loadNormal();
        myAttentionPresenter.loadAttentionUser(null);
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        myAttentionPresenter.loadAttentionUser(nextId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().setRefreshKey(getPageName() + PrefUtil.Instance().getUserId());
        InviteUserAdapter userAdapter = new InviteUserAdapter(getActivity(), null);
        getPullProxy().setAdapter(userAdapter);
    }

    @Override
    public void pullDownToRefresh(boolean show) {
        getPullProxy().pullDownToRefresh(show);
    }
}
