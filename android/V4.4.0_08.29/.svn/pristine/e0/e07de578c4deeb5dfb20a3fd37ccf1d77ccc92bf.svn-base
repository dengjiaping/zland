package com.zhisland.android.blog.profile.controller.comment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.common.view.UserCommonHead;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.DensityUtil;

import org.apache.http.client.HttpResponseException;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 全部评价列表
 */
@SuppressLint("InflateParams")
public class FragUserCommentList extends FragPullList<UserComment> {

    private static final String PAGE_NAME = "ProfileCommentList";

    private User userFrom;

    /**
     * 是否可以评论，默认为可以，只有接口返回987才置为不可以。
     */
    private boolean commentEnable = true;

    public FragUserCommentList() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        userFrom = (User) getActivity().getIntent().getSerializableExtra(ActUserCommentList.INK_FROM_USER);
        getPullProxy().setRefreshKey(PAGE_NAME + userFrom.uid);
        boolean isUserSelf = userFrom.uid == PrefUtil.Instance().getUserId();
        getPullProxy().setAdapter(
                new UserCommentAdapter(getActivity(),
                        UserCommentHolder.FROM_LIST, isUserSelf));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EBProfile eb) {
        getUserCommentListTask(null);
        getCommentEnable();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.white));
        internalView.setBackgroundColor(getResources().getColor(R.color.white));
        internalView.setPadding(DensityUtil.dip2px(20), 0,
                DensityUtil.dip2px(20), 0);
        ColorDrawable drawable = new ColorDrawable(getResources().getColor(
                R.color.white));
        internalView.setDivider(drawable);
        internalView.setDividerHeight(DensityUtil.dip2px(15));
        internalView.setHeaderDividersEnabled(false);
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        internalView.setVerticalScrollBarEnabled(false);
        internalView.setFastScrollEnabled(false);

        addHeadrView();

        refreshRightEnable();
        getCommentEnable();

        EmptyView ev = new EmptyView(getActivity());
        ev.setImgRes(R.drawable.img_emptybox);
        ev.setPrompt("暂时没有评价");
        ev.setBtnVisibility(View.INVISIBLE);
        getPullProxy().getPullView().setEmptyView(ev);
    }

    /**
     * 添加header view
     */
    private void addHeadrView() {
        UserCommonHead headerView = new UserCommonHead(getActivity());
        headerView.setHeadImage(R.drawable.profile_img_comment);

        if (((ActUserCommentList) getActivity()).isUserSelf) {
            headerView.setDesc("这里汇聚了我的评论");
            headerView.setBtnVisiable(View.GONE);
        } else {
            final User userFrom = ((ActUserCommentList) getActivity()).userFrom;
            headerView.setDesc("给 " + userFrom.name + " 一句深情的评论");
            headerView.setBtnVisiable(View.VISIBLE);
            headerView.setBtnText("去评价");
            headerView.setBtnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (commentEnable) {
                        ActWriteUserComment.invoke(getActivity(), userFrom);
                    } else {
                        showToast("已经写过神评价，不能重复评价");
                    }
                }
            });
        }
        internalView.addHeaderView(headerView);
    }

    @Override
    public void loadNormal() {
        super.loadNormal();
        getUserCommentListTask(null);
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        getUserCommentListTask(nextId);
    }

    /**
     * 获取评论列表task
     */
    private void getUserCommentListTask(String maxId) {
        ZHApis.getProfileApi().getUserCommentList(getActivity(), userFrom.uid, maxId,
                new TaskCallback<ZHPageData<UserComment>>() {

                    @Override
                    public void onSuccess(ZHPageData<UserComment> content) {
                        getPullProxy().onLoadSucessfully(content);
                        refreshRightEnable();
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        getPullProxy().onLoadFailed(error);
                    }
                });
    }

    public List<UserComment> getDatas() {
        return getPullProxy().getAdapter().getData();
    }

    /**
     * 刷新右边的按钮状态
     */
    private void refreshRightEnable() {
        boolean showBtn = false;
        List<UserComment> datas = getDatas();
        if (datas != null && datas.size() > 0) {
            showBtn = true;
        }
        if (getActivity() != null) {
            ((ActUserCommentList) getActivity()).setRightBtnEnable(showBtn);
        }
    }

    private void getCommentEnable() {
        if (userFrom != null) {
            ZHApis.getProfileApi().getCommentEnable(getActivity(), userFrom.uid, new TaskCallback<Object>() {
                @Override
                public void onSuccess(Object content) {
                    commentEnable = true;
                }

                @Override
                public void onFailure(Throwable error) {
                    if (error != null && error instanceof HttpResponseException) {
                        HttpResponseException exception = (HttpResponseException) error;
                        int code = exception.getStatusCode();
                        if (code == CodeUtil.CODE_UNENABLE_COMMENT) {
                            commentEnable = false;
                        }
                    }
                }
            });
        }
    }

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

}
