package com.zhisland.android.blog.invitation.view.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.ContactRefuseView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.model.IInvitationConfirmModel;
import com.zhisland.android.blog.invitation.model.IInvitationRequestModel;
import com.zhisland.android.blog.invitation.model.InvitationModelFactory;
import com.zhisland.android.blog.invitation.presenter.InvitationConfirmPresenter;
import com.zhisland.android.blog.invitation.presenter.InvitationRequestPresenter;
import com.zhisland.android.blog.invitation.view.IInvitationRequestView;
import com.zhisland.android.blog.invitation.view.IInvitatoinConfirmView;
import com.zhisland.android.blog.invitation.view.impl.holder.InviteUserHolder;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;
import com.zhisland.lib.util.DensityUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 认证海客主页面
 * Created by arthur on 2016/8/10.
 */
public class FragHaikeConfirm extends FragPullListMvps<InviteUser> implements IInvitationRequestView, IInvitatoinConfirmView {


    //region 头部UI接口实现

    @Override
    public void setCount(int totalCount, int leftCount) {
        tvApproveableNum.setText("/" + totalCount + " 剩余人数");
        tvApproveableRemainNum.setText(leftCount + "");
    }

    @Override
    public void hideRequest() {
        tvViewAll.setVisibility(View.GONE);
        tvApprovedReqNum.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }

    @Override
    public void setRequestData(List<InviteUser> requestUsers) {
        tvViewAll.setVisibility(View.VISIBLE);
        tvApprovedReqNum.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);


        cardPagerAdapter.setDatas(requestUsers);
        cardPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRequestTitle(String format) {
        tvApprovedReqNum.setText(format);
    }

    @Override
    public void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gotoDealedList() {
        FragInvitationDealedList.invoke(getActivity(), null);
    }

    @Override
    public void gotoRequestList(List<InviteUser> users) {
        FragReqApproveFriends.invoke(getActivity(), users);
    }

    @Override
    public void gotoAllowHaike(InviteUser user) {
        FragApproveHaiKe.invoke(getActivity(), user);
    }


    @Override
    public boolean removeRequest(InviteUser user) {
        return cardPagerAdapter.removeItem(user);
    }


    //endregion


    //region 可批准的UI接口

    @Override
    public void gotoConfirmSearch() {
        FragSearchInvite.invoke(getActivity());
    }

    @Override
    public void showListEmptyView() {
        getPullProxy().getAdapter().clearItems();
        getPullProxy().getAdapter().add(emptyItem);
    }

    @Override
    public void showContactDenyView() {
        getPullProxy().getAdapter().clearItems();
        getPullProxy().getAdapter().add(contactDenyItem);
    }

    @Override
    public void hideContactDenyView() {
        getPullProxy().getAdapter().removeItem(contactDenyItem);
    }

    @Override
    public void hideEmptyView() {
        getPullProxy().getAdapter().removeItem(emptyItem);
    }

    @Override
    public void disablePullUp() {
        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    public void enablePullUp() {
        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    @Override
    public void showListHeaderView() {
        mHeaderView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFirstDlg() {
        ZhislandApplication.trackerPageStart(TrackerAlias.CLICK_APPROVE_HAIKE_README);
        View guestView = LayoutInflater.from(getActivity()).inflate(R.layout.dlg_first_allow_haike, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.UpdateDialog);
        TextView tvOk = (TextView) guestView.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialog && dialog.isShowing()) {
                    dialog.dismiss();
                    ZhislandApplication.trackerPageEnd(TrackerAlias.CLICK_APPROVE_HAIKE_README);
                }
            }
        });
        dialog.setContentView(guestView);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmlp.width = DensityUtil.getWidth() - DensityUtil.dip2px(64); // 宽度
        dialog.show();
    }

    @Override
    public void finishSelf() {
        getActivity().finish();
    }

    @Override
    public void gotoActProfile(long uid) {
        ActProfileDetail.invoke(getActivity(), uid);
    }

    //endregion


    //region 同步UI事件以及控件

    @InjectView(R.id.tvViewAll)
    TextView tvViewAll;
    @InjectView(R.id.tvApprovedReqNum)
    TextView tvApprovedReqNum;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.tvApproveable)
    TextView tvApproveable;

    @InjectView(R.id.tvApproveableNum)
    TextView tvApproveableNum;
    @InjectView(R.id.tvApproveableRemainNum)
    TextView tvApproveableRemainNum;
    @InjectView(R.id.ivSearchHeader)
    ImageView ivSearchHeader;

    View mHeaderView;

    @OnClick(R.id.tvViewAll)
    public void onViewAllClick() {
        requestPresenter.onAllRequestClicked();
    }

    @OnClick(R.id.laySearch)
    public void onsSearchClick() {
        confirmPresenter.onSearchClicked();
    }

    @OnClick(R.id.ivSearchHeader)
    public void onsSearchHeaderClick() {
        confirmPresenter.onSearchClicked();
        ZhislandApplication.trackerClickEvent(getPageName(),TrackerAlias.CLICK_APPROVE_SEARCH);
    }


    @OnClick({R.id.tvViewApproved})
    public void onViewDealedClick(View view) {
        requestPresenter.onDealedClicked();
    }

    @OnClick({R.id.ivBack})
    public void onBackClick(View view) {
        getActivity().finish();
    }
//endregion


//region 生命周期以及MVP事件

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams params = new CommonFragActivity.CommonFragParams();
        params.noTitle = true;
        params.clsFrag = FragHaikeConfirm.class;
        CommonFragActivity.invoke(context, params);
    }

    private static final String ITEM_EMPTY = "zhisland_item_empty_woshi2";
    private static final String ITEM_DENY = "zhisland_item_deny_woshi2";
    InvitationRequestPresenter requestPresenter;
    InvitationConfirmPresenter confirmPresenter;
    CardPagerAdapter cardPagerAdapter;
    InviteUser emptyItem, contactDenyItem;

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenters = new HashMap<>(2);
        requestPresenter = new InvitationRequestPresenter();
        IInvitationRequestModel requestModel = InvitationModelFactory.createInvitationRequestModel();
        requestPresenter.setModel(requestModel);
        presenters.put(InvitationRequestPresenter.class.getSimpleName(), requestPresenter);

        confirmPresenter = new InvitationConfirmPresenter();
        IInvitationConfirmModel confirmModel = InvitationModelFactory.createInvitationConfirmModel();
        confirmPresenter.setModel(confirmModel);
        presenters.put(InvitationConfirmPresenter.class.getSimpleName(), confirmPresenter);
        return presenters;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ConfirmableAdapter adapter = new ConfirmableAdapter();
        emptyItem = new InviteUser();
        emptyItem.requestExplanation = ITEM_EMPTY;
        contactDenyItem = new InviteUser();
        contactDenyItem.requestExplanation = ITEM_DENY;
        getPullProxy().setAdapter(adapter);
        getPullProxy().needRefreshOnResume = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_approved_req_guest, null);
        mHeaderView.setVisibility(View.GONE);
        getPullProxy().getPullView().getRefreshableView().addHeaderView(mHeaderView);
        ButterKnife.inject(this, root);

        getPullProxy().getPullView().setBackgroundColor(Color.WHITE);
        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        cardPagerAdapter = new CardPagerAdapter(getActivity());
        viewPager.setAdapter(cardPagerAdapter);
        viewPager.setPageMargin(DensityUtil.dip2px(10));
        viewPager.setOffscreenPageLimit(3);
        return root;
    }

    @Override
    protected View createDefaultFragView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.frag_invitation_confirm, null);
    }


    @Override
    public void loadMore(String nextId) {
        confirmPresenter.loadMore(nextId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


//endregion


    private class ConfirmableAdapter extends BaseListAdapter<InviteUser> {


        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            InviteUser item = getItem(position);
            if (item.requestExplanation != null && item.requestExplanation.equals(ITEM_DENY)) {
                return 0;
            } else if (item.requestExplanation != null && item.requestExplanation.equals(ITEM_EMPTY)) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);

            switch (type) {
                case 0: {
                    if (convertView == null) {
                        ContactRefuseView refuseView = new ContactRefuseView(getActivity());
                        refuseView.setPadding(0, DensityUtil.dip2px(20), 0, DensityUtil.dip2px(20));
                        convertView = refuseView;
                    }
                    break;
                }
                case 1: {
                    if (convertView == null) {
                        ZhislandApplication.trackerPageStart(TrackerAlias.CLICK_APPROVE_DATA_EMPTY);
                        tvApproveable.setVisibility(View.GONE);
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_approveble_empty, null);
                        TextView tvshengjihaike = (TextView) convertView.findViewById(R.id.tvshengjihaike);
                        tvshengjihaike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoConfirmSearch();
                                ZhislandApplication.trackerClickEvent(getPageName(),TrackerAlias.CLICK_APPROVE_DATA_EMPTY_CLICK);
                            }
                        });
                    }
                    break;
                }
                default: {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_invite_search, null);
                        TextView tvApprove = (TextView) convertView.findViewById(R.id.tvApprove);
                        InviteUserHolder holder = new InviteUserHolder(convertView);
                        convertView.setTag(holder);
                    }
                    InviteUserHolder holder = (InviteUserHolder) convertView.getTag();
                    holder.setCallBack(new InviteUserHolder.CallBack() {
                        @Override
                        public void onRightBtnClick(InviteUser inviteUser) {
                            confirmPresenter.onConfirmClicked(inviteUser);
                            ZhislandApplication.trackerClickEvent(getPageName(),TrackerAlias.CLICK_APPROVE_HELP_HIM);
                        }
                    });
                    InviteUser user = getItem(position);
                    holder.fill(user);
                }
            }

            return convertView;
        }

        @Override
        protected void recycleView(View view) {
            InviteUserHolder holder = (InviteUserHolder) view.getTag();
            if (holder != null)
                holder.recycle();
        }
    }


    /**
     * 认证请求的adapter
     */
    class CardPagerAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;
        Context mContext;
        List<InviteUser> inviteUsers;

        public CardPagerAdapter(Context context) {
            mContext = context;
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public void setDatas(List<InviteUser> inviteUsers) {
            this.inviteUsers = inviteUsers;
        }

        @Override
        public int getCount() {
            return inviteUsers == null ? 0 : inviteUsers.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View cardView = layoutInflater.inflate(R.layout.item_approved_req, null);
            container.addView(cardView);

            InviteUser user = this.inviteUsers.get(position);

            CardHolder holder = new CardHolder(cardView);
            holder.fill(user);

            return cardView;
        }

        /**
         * 删除某条数据
         *
         * @param user
         * @return
         */
        public boolean removeItem(InviteUser user) {
            boolean result = inviteUsers.remove(user);
            this.notifyDataSetChanged();
            return result;
        }

        //重写这个方法强制每个view都冲刷
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    class CardHolder {
        @InjectView(R.id.ivReqAvatar)
        AvatarView ivReqAvatar;
        @InjectView(R.id.tvUserName)
        TextView tvUserName;
        @InjectView(R.id.tvCompany)
        TextView tvCompany;
        @InjectView(R.id.tvDate)
        TextView tvDate;
        @InjectView(R.id.tvDes)
        TextView tvDes;
        @InjectView(R.id.tvApproved)
        TextView tvApproved;
        @InjectView(R.id.tvIgnore)
        TextView tvIgnore;

        private InviteUser request;

        CardHolder(View view) {
            ButterKnife.inject(this, view);
        }

        void fill(InviteUser user) {

            this.request = user;

            ivReqAvatar.fill(user.user, false);
            tvCompany.setText(user.user.combineCompanyAndPosition());
            tvUserName.setText(user.user.name);
            tvDate.setText(user.requestTime);
            if (!TextUtils.isEmpty(user.requestExplanation)) {
                tvDes.setText(user.requestExplanation);
            } else {
                tvDes.setText("--");
            }

        }

        @OnClick(R.id.tvIgnore)
        public void onIgoneClicked() {
            requestPresenter.onIgnoreClicked(request);
            ZhislandApplication.trackerClickEvent(getPageName(),TrackerAlias.CLICK_APPROVE_IGNORE,"忽略");
        }

        @OnClick(R.id.tvApproved)
        public void onApproveClicked() {
            requestPresenter.onAllowClicked(request);
            ZhislandApplication.trackerClickEvent(getPageName(),TrackerAlias.CLICK_APPROVE_AGREE,"批准");
        }

        @OnClick(R.id.rlItemRoot)
        public void onRootClicked() {
            requestPresenter.onCardClick(request);
        }
    }

    @Override
    protected String getPageName() {
        return "FragHaikeConfirm";
    }
}
