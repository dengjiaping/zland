package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.model.impl.CommentRecommendModel;
import com.zhisland.android.blog.freshtask.presenter.CommentRecommendPresenter;
import com.zhisland.android.blog.freshtask.view.ICommentRecommendView;
import com.zhisland.android.blog.freshtask.view.impl.holder.CommentCardHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.CommentCardHolderListener;
import com.zhisland.android.blog.freshtask.view.impl.holder.CommentRecommendHolder;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 神评论精选
 */
public class FragCommentRecommend extends FragBaseMvp<CommentRecommendPresenter>
        implements ICommentRecommendView, ViewPager.OnPageChangeListener, CommentCardHolderListener {

    private static final String INK_DATA = "INK_DATA";

    private CommentRecommendHolder viewHolder = new CommentRecommendHolder();
    private AdapterCard adapter;

    public static void invoke(Context context, List<UserComment> cards) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragCommentRecommend.class;
        param.enableBack = false;
        param.swipeBackEnable = false;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INK_DATA, (Serializable) cards);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Object obj = getActivity().getIntent().getSerializableExtra(INK_DATA);
        if (obj == null || !(obj instanceof List)) {
            getActivity().finish();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_comment_recommend, container, false);
        ButterKnife.inject(viewHolder, view);
        ButterKnife.inject(this, view);
        adapter = new AdapterCard();
        viewHolder.vp.setAdapter(adapter);
        viewHolder.vp.setOffscreenPageLimit(3);
        viewHolder.vp.addOnPageChangeListener(this);
        viewHolder.vp.setPageMargin((DensityUtil.dip2px(20)));
        return view;
    }

    @Override
    protected String getPageName() {
        return "InterceptCommentTerse";
    }

    @Override
    public void setData(List<UserComment> task) {
        adapter.setItems(task);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateNext(String next) {
        viewHolder.tvNextStep.setText(next);
    }

    @Override
    public void updateHolder(int position) {
        viewHolder.vp.setCurrentItem(position);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter().switchTo(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //==============viewpager listener end============


    //================UI event start================

    @OnClick(R.id.tvNextStep)
    public void onClickNextStep() {
        presenter().nextClicked();
    }

    @OnClick(R.id.viewClose)
    public void onClickClose() {
        finish();
    }

    @Override
    public void onCardClicked(User user) {
        presenter().onCardItemClicked(getActivity(), user);
    }

    @Override
    protected CommentRecommendPresenter createPresenter() {
        CommentRecommendPresenter presenter = new CommentRecommendPresenter();
        CommentRecommendModel model = new CommentRecommendModel();
        presenter.setModel(model);
        Object obj = getActivity().getIntent().getSerializableExtra(INK_DATA);
        presenter.setData((List<UserComment>) obj);
        return presenter;
    }

    //===============UI event end=============

    //==========viewpager adapter============
    class AdapterCard extends PagerAdapter {

        private List<UserComment> items;


        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View convertView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.item_price, container, false);
            CommentCardHolder holder = new CommentCardHolder();
            holder.setCardHolderListener(FragCommentRecommend.this);
            ButterKnife.inject(holder, convertView);
            UserComment item = getItem(position);
            holder.fill(getActivity(), item);
            container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            return convertView;
        }

        public UserComment getItem(int position) {
            return items.get(position);
        }

        public void setItems(List<UserComment> items) {
            this.items = items;
        }
    }

}
