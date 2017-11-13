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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profilemvp.model.ModelFactory;
import com.zhisland.android.blog.profilemvp.presenter.MyFansPresenter;
import com.zhisland.android.blog.profilemvp.view.IMyFansView;
import com.zhisland.android.blog.profilemvp.view.impl.holder.InviteUserHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;
import com.zhisland.lib.util.DensityUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的粉丝页面
 * Created by Mr.Tui on 2016/9/6.
 */
public class FragMyFans extends FragPullListMvps<InviteUser> implements IMyFansView, InviteUserHolder.OnRightClickListener {

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragMyFans.class;
        param.enableBack = true;
        param.title = "我的粉丝";
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    MyFansPresenter myFansPresenter;
    UserAdapter userAdapter;

    @Override
    public String getPageName() {
        return "ProfileMyFansList";
    }

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenterMap = new HashMap<>(1);
        myFansPresenter = new MyFansPresenter();
        myFansPresenter.setModel(ModelFactory.CreateMyFansModel());
        presenterMap.put(MyFansPresenter.class.getSimpleName(), myFansPresenter);
        return presenterMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View pullView = super.onCreateView(inflater, container, savedInstanceState);
        pullView.setBackgroundColor(getResources().getColor(R.color.color_bg1));
        ViewGroup.LayoutParams rootLp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pullView.setLayoutParams(rootLp);
        return pullView;
    }

    ClickableSpan clickableSpan = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            ActProfileDetail.invoke(getActivity(), PrefUtil.Instance().getUserId());
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
    }

    @Override
    public void loadNormal() {
        super.loadNormal();
        myFansPresenter.loadFansUser(null);
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        myFansPresenter.loadFansUser(nextId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().setRefreshKey(getPageName() + PrefUtil.Instance().getUserId());
        userAdapter = new UserAdapter();
        getPullProxy().setAdapter(userAdapter);
    }

    @Override
    public void onLoadSucessfully(ZHPageData<InviteUser> dataList, boolean showSeeVisitors) {
        userAdapter.showSeeVisitors = showSeeVisitors;
        super.onLoadSucessfully(dataList);
    }

    @Override
    public void refreshList() {
        getPullProxy().getAdapter().notifyDataSetChanged();
    }

    @Override
    public void pullDownToRefresh(boolean show) {
        getPullProxy().pullDownToRefresh(show);
    }

    View.OnClickListener seeVisitorsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            getPullProxy().getPullView().setRefreshing(true);
        }
    };

    @Override
    public void onRightClick(int position, InviteUser inviteUser) {
        myFansPresenter.onFollowClicked(inviteUser);
    }

    class UserAdapter extends BaseListAdapter<InviteUser> {

        boolean showSeeVisitors = false;

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getCount() - 1) {
                return 0;
            }
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getItemViewType(position);
            if (viewType == 0) {
                if (showSeeVisitors) {
                    TextView tv = new TextView(getActivity());
                    int padding = DensityUtil.dip2px(30);
                    tv.setPadding(padding, padding, padding, padding);
                    DensityUtil.setTextSize(tv, R.dimen.txt_16);
                    tv.setTextColor(getResources().getColorStateList(R.color.sel_color_dc_f3));
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("查看访客粉丝");
                    tv.setOnClickListener(seeVisitorsListener);
                    return tv;
                } else {
                    if(getCount() == 1){
                        EmptyView ev = new EmptyView(getActivity());
                        ev.setImgVisibility(View.INVISIBLE);
                        ev.setBtnVisibility(View.INVISIBLE);
                        SpannableString ss = new SpannableString("暂时还没有人关注到你\n完善个人主页可有效提高被关注度");
                        ss.setSpan(clickableSpan, 11, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ev.setPrompt(ss);
                        ev.setPromptTextColor(R.color.color_f3);
                        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.getHeight() - DensityUtil.dip2px(80));
                        ev.setLayoutParams(lp);
                        return ev;
                    }else{
                        View v = new View(getActivity());
                        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1);
                        v.setLayoutParams(lp);
                        return v;
                    }
                }
            } else {
                InviteUserHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_my_attention, null);
                    holder = new InviteUserHolder(convertView, getActivity(), FragMyFans.this);
                    convertView.setTag(holder);
                } else {
                    holder = (InviteUserHolder) convertView.getTag();
                }
                holder.fill(getItem(position), position);
                return convertView;
            }
        }

        @Override
        protected void recycleView(View view) {

        }
    }

}
