package com.zhisland.android.blog.invitation.view.impl;

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
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.contacts.dto.InviteStructure;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.model.IInvitationDealedModel;
import com.zhisland.android.blog.invitation.model.InvitationModelFactory;
import com.zhisland.android.blog.invitation.presenter.InvitationDealedListPresenter;
import com.zhisland.android.blog.invitation.view.IInvitationDealedView;
import com.zhisland.android.blog.invitation.view.impl.holder.InviteUserHolder;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 查看历史记录列表
 * Created by arthur on 2016/8/9.
 */
public class FragInvitationDealedList extends FragPullListMvps<InviteUser> implements IInvitationDealedView {

    private static final String INK_DATA = "ink_data";
    InvitationDealedListPresenter presenter;

    @InjectView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        IInvitationDealedModel model = InvitationModelFactory.createInvitationDealedModel();
        presenter = new InvitationDealedListPresenter();
        presenter.setModel(model);

        HashMap<String, BasePresenter> presenters = new HashMap<>(1);
        presenters.put(getClass().getSimpleName(), presenter);
        return presenters;
    }

    /**
     * 启动这个页面
     *
     * @param context
     */
    public static void invoke(Context context, InviteStructure inviteStructure) {
        CommonFragActivity.CommonFragParams params
                = new CommonFragActivity.CommonFragParams();
        params.clsFrag = FragInvitationDealedList.class;
        params.title = "查看历史记录";
        params.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, params);
        intent.putExtra(INK_DATA, inviteStructure);
        context.startActivity(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        InvitationAdapter adapter = new InvitationAdapter();
        getPullProxy().setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        getPullProxy().getPullView().getRefreshableView().setDividerHeight(0);
        EmptyView emptyView = new EmptyView(getActivity());
        emptyView.setPrompt("没有历史记录");
        emptyView.setImgRes(R.drawable.img_emptybox);
        getPullProxy().getPullView().setEmptyView(emptyView);
    }

    @Override
    public void loadNormal() {
        presenter.loadData();
    }


    @Override
    protected View createDefaultFragView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.frag_invitation_dealed_list, null);
    }

    @Override
    public void updateTitleString(String titleString) {
        tvTitle.setText(titleString);
    }

    @Override
    public void gotoUserDetail(User user) {
        ActProfileDetail.invoke(getActivity(), user.uid);
    }

    /**
     * 列表适配器
     */
    private class InvitationAdapter extends BaseListAdapter<InviteUser> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_invite_search, null);
                InviteUserHolder holder = new InviteUserHolder(convertView);
                convertView.setTag(holder);
            }

            InviteUserHolder holder = (InviteUserHolder) convertView.getTag();
            InviteUser user = getItem(position);
            holder.fill(user);

            return convertView;
        }

        @Override
        protected void recycleView(View view) {
            InviteUserHolder holder = (InviteUserHolder) view.getTag();
            holder.recycle();
        }
    }


    @Override
    protected String getPageName() {
        return "FragInvitationDealedList";
    }
}
