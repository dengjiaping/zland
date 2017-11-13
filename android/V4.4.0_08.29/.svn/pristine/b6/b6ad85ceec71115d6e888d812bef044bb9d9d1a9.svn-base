package com.zhisland.android.blog.profilemvp.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserHeatReport;
import com.zhisland.android.blog.profilemvp.model.ModelFactory;
import com.zhisland.android.blog.profilemvp.model.impl.MyHotModel;
import com.zhisland.android.blog.profilemvp.presenter.MyHotPresenter;
import com.zhisland.android.blog.profilemvp.view.IMyHotView;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.mvp.view.FragBaseMvp;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的热度页面
 * Created by Mr.Tui on 2016/9/6.
 */
public class FragMyHot extends FragBaseMvp<MyHotPresenter> implements IMyHotView {

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragMyHot.class;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    //region 声明view
    @InjectView(R.id.tvHot)
    TextView tvHot;

    @InjectView(R.id.tvRank)
    TextView tvRank;

    @InjectView(R.id.tvSeeTimes)
    TextView tvSeeTimes;

    @InjectView(R.id.tvFansNum)
    TextView tvFansNum;

    @InjectView(R.id.tvCommentTimes)
    TextView tvCommentTimes;

    @InjectView(R.id.tvLikeTimes)
    TextView tvLikeTimes;

    @InjectView(R.id.ivAvatar)
    ImageView ivAvatar;

    //endregion

    @Override
    protected MyHotPresenter createPresenter() {
        MyHotPresenter presenter = new MyHotPresenter();
        presenter.setModel(ModelFactory.CreateMyHotModel());
        return presenter;
    }

    @Override
    public String getPageName() {
        return "FragMyHot";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.frag_my_hot, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void fillUserRelation(UserHeatReport heatReport, String userTypeStr) {
        User self = DBMgr.getMgr().getUserDao().getSelfUser();
        if(self != null) {
            int defaultId = R.drawable.avatar_default_man;
            if (self.sex != null && self.sex == User.VALUE_SEX_WOMAN) {
                defaultId = R.drawable.avatar_default_woman;
            }
            ImageWorkFactory.getCircleFetcher().loadImage(self.userAvatar, ivAvatar, defaultId);
        }
        if (heatReport != null) {
            String hotStr = String.format("热度:%d℃", heatReport.heatVal);
            SpannableString ss = new SpannableString(hotStr);
            ss.setSpan(new RelativeSizeSpan(2.0f), 3, hotStr.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvHot.setText(ss);
            tvRank.setText(String.format("%s排名%d名", userTypeStr, heatReport.userRanking));
            tvSeeTimes.setText(String.format("%d次", heatReport.indexPageViews));
            tvFansNum.setText(String.format("%d人", heatReport.fansCount));
            tvCommentTimes.setText(String.format("%d条", heatReport.receiveCommentCount));
            tvLikeTimes.setText(String.format("%d次", heatReport.receiveLikeCount));
        } else {
            //如果用户第一次进来，无数据的情况，都显示为0
            String hotStr = String.format("热度:%d℃", 0);
            SpannableString ss = new SpannableString(hotStr);
            ss.setSpan(new RelativeSizeSpan(1.8f), 3, hotStr.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvHot.setText(ss);
            tvRank.setText(String.format("%s排名%d名", userTypeStr, 0));
            tvSeeTimes.setText(String.format("%d次", 0));
            tvFansNum.setText(String.format("%d人", 0));
            tvCommentTimes.setText(String.format("%d条", 0));
            tvLikeTimes.setText(String.format("%d次", 0));
        }
    }

    @OnClick(R.id.ivBack)
    void onBackClick() {
        getActivity().finish();
    }
}
