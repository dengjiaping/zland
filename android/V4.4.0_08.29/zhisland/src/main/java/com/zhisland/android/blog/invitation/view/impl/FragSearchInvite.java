package com.zhisland.android.blog.invitation.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.model.impl.SearchModel;
import com.zhisland.android.blog.invitation.presenter.SearchPresenter;
import com.zhisland.android.blog.invitation.view.ISearchInvite;
import com.zhisland.android.blog.invitation.view.impl.adapter.SearchAdapter;
import com.zhisland.android.blog.invitation.view.impl.holder.InviteUserHolder;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.FragPullListMvp;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 搜索用户for邀请
 * Created by Mr.Tui on 2016/8/9.
 */
public class FragSearchInvite extends FragPullListMvp<InviteUser, SearchPresenter> implements ISearchInvite {

    @InjectView(R.id.etSearch)
    EditText etSearch;

    @InjectView(R.id.ivClean)
    ImageView ivClean;

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragSearchInvite.class;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Override
    protected SearchPresenter createPresenter() {
        SearchPresenter presenter = new SearchPresenter();
        SearchModel model = new SearchModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public String getPageName() {
        return "SearchInvite";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getPullProxy().needRefreshOnResume = false;
        SearchAdapter adapter = new SearchAdapter();
        adapter.setCallBack(callBack);
        getPullProxy().setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.frag_invite_search, null);
        root.addView(super.onCreateView(inflater, container, savedInstanceState), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        EditTextUtil.setKeyBoard(etSearch, EditTextUtil.ID_SEARCH, new EditTextUtil.IKeyBoardAction() {
            @Override
            public void action() {
                onSearchClick();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        internalView.setDividerHeight(0);
        internalView.setFastScrollEnabled(false);
        internalView.setBackgroundColor(getResources().getColor(
                R.color.color_bg2));
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.color_bg2));
        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);

    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        presenter().loadResultMore(nextId);
    }

    InviteUserHolder.CallBack callBack = new InviteUserHolder.CallBack() {
        @Override
        public void onRightBtnClick(InviteUser inviteUser) {
            presenter().onItemRightBtnClick(inviteUser);
        }
    };

    @OnClick(R.id.tvCancel)
    void onCancelClick() {
        getActivity().finish();
    }

    @OnClick(R.id.ivSearch)
    void onSearchClick() {
        String searchKey = etSearch.getText().toString().trim();
        if (StringUtil.isNullOrEmpty(searchKey)) {
            ToastUtil.showShort("请输入搜索关键字");
            return;
        }
        SoftInputUtil.hideInput(getActivity());
        presenter().onSearchClick(searchKey);
    }

    @OnTextChanged(R.id.etSearch)
    void OnSearchTextChanged() {
        if (etSearch.getText().length() > 0) {
            ivClean.setVisibility(View.VISIBLE);
        } else {
            ivClean.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ivClean)
    void onCleanClick() {
        etSearch.setText("");
    }

    @Override
    public void onSearchFailed(Throwable error) {
        getPullProxy().onLoadFailed(error);
        if (getPullProxy().isLastPage) {
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        }
    }

    @Override
    public void onSearchSuccess(ZHPageData<InviteUser> data) {
        getPullProxy().onLoadSucessfully(data);
        if (getPullProxy().isLastPage) {
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        }
    }

    @Override
    public void setEmptyView() {
        EmptyView ev = new EmptyView(getActivity());
        ev.setImgRes(R.drawable.img_emptybox);
        ev.setPrompt("没有找到相关结果");
        ev.setBtnVisibility(View.INVISIBLE);
        getPullProxy().getPullView().setEmptyView(ev);
    }

    @Override
    public void goToApproveHaiKe(InviteUser inviteUser){
        FragApproveHaiKe.invoke(getActivity(), inviteUser);
    }

    @Override
    public void showKeyboard(){
        SoftInputUtil.showKeyboard(getActivity().getCurrentFocus());
    }

}
