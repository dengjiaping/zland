package com.zhisland.android.blog.invitation.view.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.trello.rxlifecycle.FragmentEvent;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.common.view.ContactRefuseView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.view.impl.holder.InviteRegistHolder;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.search.ZHSearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr.Tui on 2016/8/11.
 */
public class FragInviteRegist extends FragPullList<InviteUser> {

    private static final int TAG_ID_RIGHT = 100;

    @InjectView(R.id.searchBar)
    ZHSearchBar searchBar;

    //服务器返回的所有数据
    private List<InviteUser> allDatas = new ArrayList<>();
    //搜索结果数据
    private List<InviteUser> searchDatas = new ArrayList<>();

    private List<InviteUser> joinedList;

    private Subscription hideDelayOb;

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragInviteRegist.class;
        param.title = "邀请注册";
        param.enableBack = true;
        param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
        param.runnable = titleRunnable;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = "手机号邀请";
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc);
        param.titleBtns.add(tb);
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            FragInviteByPhone.invoke(context);
            ZhislandApplication.trackerClickEvent("FragInviteRegist",TrackerAlias.CLICK_INVITE_MOBILE_CLICK);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getPullProxy().needRefreshOnResume = false;
        getPullProxy().setAdapter(new UserAdapter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.frag_invite_contacts, container, false);
        FrameLayout flContainer = (FrameLayout) view.findViewById(R.id.flContainer);
        View pullList = super.onCreateView(inflater, container,
                savedInstanceState);
        flContainer.addView(pullList);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        internalView.setFastScrollEnabled(false);
        initView();
        addSearchListener();
    }

    private void initView() {
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.color_bg2));
        internalView.setDividerHeight(0);
        internalView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void refreshData() {
        if (!PhoneContactUtil.hasContactData()) {
            ContactRefuseView refuseView = new ContactRefuseView(getActivity());
            refuseView.setContent("未授权通讯录\n无法邀请通讯录好友");
            refuseView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            refuseView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            refuseView.setPadding(0, DensityUtil.dip2px(130), 0, DensityUtil.dip2px(20));
            getPullProxy().getPullView().setEmptyView(refuseView);
            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            getPullProxy().getPullView().setEmptyView(null);
            List<InviteUser> data = getPullProxy().getAdapter().getData();
            if (data == null || data.size() == 0) {
                if (hideDelayOb != null && (!hideDelayOb.isUnsubscribed())) {
                    hideDelayOb.unsubscribe();
                }
                hideDelayOb = Observable.timer(700, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<Long>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                                getPullProxy().pullDownToRefresh(true);
                            }
                        });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * 搜索监听
     */
    private void addSearchListener() {
        searchBar.getTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();
                if (StringUtil.isNullOrEmpty(searchText)) {
                    getPullProxy().getAdapter().clearItems();
                    getPullProxy().onLoadSucessfully(allDatas);
                    getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
                } else {
                    searchDatas.clear();
                    for (InviteUser local : allDatas) {
                        if (local.user.name.contains(searchText) || local.user.userMobile.contains(searchText)) {
                            searchDatas.add(local);
                        }
                    }
                    getPullProxy().getAdapter().clearItems();
                    getPullProxy().onLoadSucessfully(searchDatas);
                    getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
                }
            }
        });
    }

    @Override
    public void loadNormal() {
        super.loadNormal();
        loadLocalContact();
        sysContacts();
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        getPullProxy().onRefreshFinished();
    }

    /**
     * 同步通讯录
     */
    private void sysContacts() {
        final PhoneContactUtil.ContactResult<String> newContact = PhoneContactUtil.getChangeContact();
        ZHApis.getUserApi().matchContactsNormal(getActivity(), newContact.result, null,
                new TaskCallback<ZHPageData<InviteUser>>() {

                    @Override
                    public void onSuccess(ZHPageData<InviteUser> content) {
                        //修改时间戳
                        PhoneContactUtil.setLastTimestamp(newContact.timestamp);
                        if (content != null) {
                            joinedList = content.data;
                            refreshJoinedstatus();
                        }
                    }

                    @Override
                    public void onFailure(Throwable error) {
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void loadLocalContact() {
        Observable.create(new Observable.OnSubscribe<List<InviteUser>>() {
            @Override
            public void call(Subscriber<? super List<InviteUser>> subscriber) {
                List<InviteUser> users = PhoneContactUtil.getAllContact();
                subscriber.onNext(users);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InviteUser>>() {
                    @Override
                    public void call(List<InviteUser> inviteUsers) {
                        if (inviteUsers != null) {
                            allDatas = inviteUsers;
                            getPullProxy().onLoadSucessfully(allDatas);
                            getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
                            refreshJoinedstatus();
                        }
                    }
                });
    }

    private void refreshJoinedstatus() {
        if (allDatas == null || allDatas.size() == 0 || joinedList == null || joinedList.size() == 0) {
            return;
        }
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                long time = System.currentTimeMillis();
                int count = 0;
                for (InviteUser all : allDatas) {
                    for (InviteUser joined : joinedList) {
                        count++;
                        if (count % 200000 == 0) {
                            //循环200000大约1秒
                            subscriber.onNext(null);
                        }
                        if (all.user != null && joined.user != null && all.user.userMobile != null
                                && joined.user.userMobile != null && all.user.userMobile.equals(joined.user.userMobile)) {
                            all.state = joined.state;
                            all.user = joined.user;
                            joinedList.remove(joined);
                            break;
                        }
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        getPullProxy().getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object content) {
                        getPullProxy().getAdapter().notifyDataSetChanged();
                    }
                });
    }

    class UserAdapter extends BaseListAdapter<InviteUser> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_invite_search, parent, false);
                InviteRegistHolder holder = new InviteRegistHolder(convertView);
                holder.setCallBack(callback);
                convertView.setTag(holder);
            }
            InviteRegistHolder holder = (InviteRegistHolder) convertView.getTag();
            holder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {
            InviteRegistHolder holder = (InviteRegistHolder) view.getTag();
            holder.recycle();
        }

    }

    InviteRegistHolder.CallBack callback = new InviteRegistHolder.CallBack() {
        @Override
        public void onRightBtnClick(final InviteUser inviteUser) {
            if (StringUtil.isNullOrEmpty(inviteUser.user.userMobile)) {
                ToastUtil.showLong("不是有效的手机号码");
                return;
            }
            ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_INVITE_BUTTON_CLICK);
            showProgressDlg();
            ZHApis.getUserApi().inviteByPhone(getActivity(), inviteUser.user.name, Country.CHINA_CODE, inviteUser.user.userMobile, new TaskCallback<String>() {
                @Override
                public void onSuccess(String content) {
                    ToastUtil.showShort("已发送");
                    inviteUser.state.setIsOperable(CustomState.CAN_NOT_OPERABLE);
                    inviteUser.state.setStateName("已邀请");
                    getPullProxy().getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Throwable error) {
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    hideProgressDlg();
                }
            });
        }
    };

    @Override
    public String getPageName() {
        return "InviteContactInvitation";
    }

}
