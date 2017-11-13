package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.eb.EBEvent;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 报名人员列表页面（包括两个逻辑页面：已确认的报名用户页面、待审核的报名用户页面）
 */
public class FragSignUpMembers extends FragPullList<User> {

    private static final String PAGE_NAME = "frag_sign_up_member";

    public static final String INTENT_KEY_EVENT = "intent_key_event";
    public static final String INTENT_KEY_TYPE = "intent_key_type";

    /**
     * 对应type的常量，已确认的报名用户页面。（我发起的活动页面跳转过来的）
     */
    public static final String TYPE_AUDITED = "type_audited";
    /**
     * 对应type的常量，待审核的报名用户页面。（我发起的活动页面跳转过来的）
     */
    public static final String TYPE_AUDITING = "type_auditing";

    private Event event;

    private String type;

    /**
     * 记录已经审核通过的uid
     */
    private HashMap<Long, Integer> okUidsMap = new HashMap<Long, Integer>();

    /**
     * 启动报名用户列表页面。两种情况，通过type判断。
     *
     * @param event 活动
     * @param type  两种情况.已确认报名人员、待审核报名人员
     */
    public static void invoke(Context context, Event event, String type) {
        if (event == null || type == null) {
            return;
        }
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragSignUpMembers.class;
        param.enableBack = true;
        if (type.equals(TYPE_AUDITED)) {
            param.title = "已确认";
        } else if (type.equals(TYPE_AUDITING)) {
            param.title = "待审核";
        }
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_EVENT, event);
        intent.putExtra(INTENT_KEY_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    public String getPageName() {
        return "";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        event = (Event) getActivity().getIntent().getSerializableExtra(
                INTENT_KEY_EVENT);
        type = getActivity().getIntent().getStringExtra(INTENT_KEY_TYPE);

        getPullProxy().setAdapter(new UserAdapter());
        getPullProxy().setRefreshKey(PAGE_NAME + type + event.eventId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.color_bg2));

        internalView.setFastScrollEnabled(false);
        internalView.setDividerHeight(0);
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        internalView.setHeaderDividersEnabled(false);
        internalView.setFooterDividersEnabled(false);
        internalView.setBackgroundColor(getResources().getColor(
                R.color.color_bg1));

        EmptyView ev = new EmptyView(getActivity());
        ev.setImgRes(R.drawable.img_campaign_empty);
        if (type.equals(TYPE_AUDITED)) {
            ev.setPrompt("暂时没有已通过人员");
        } else if (type.equals(TYPE_AUDITING)) {
            ev.setPrompt("暂时没有新报名人员");
        }
        ev.setBtnVisibility(View.INVISIBLE);
        getPullProxy().getPullView().setEmptyView(ev);
    }

    @Override
    public void loadNormal() {
        getDataFromInternet(null);
    }

    private void getDataFromInternet(String maxId) {
        if (type.equals(TYPE_AUDITED)) {
            getAuditedData(maxId);
        } else if (type.equals(TYPE_AUDITING)) {
            getAuditingData(maxId);
        }
    }

    /**
     * 获取审核通过列表数据
     */
    private void getAuditedData(String maxId) {
        ZHApis.getEventApi().getAuditedMembers(getActivity(), event.eventId, maxId,
                new TaskCallback<ZHPageData<User>>() {

                    @Override
                    public void onSuccess(ZHPageData<User> content) {
                        getPullProxy().onLoadSucessfully(content);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        getPullProxy().onLoadFailed(error);
                    }
                });
    }

    /**
     * 获取未审核列表数据
     */
    private void getAuditingData(String maxId) {
        ZHApis.getEventApi().getAuditingMembers(getActivity(), event.eventId, maxId,
                new TaskCallback<ZHPageData<User>>() {

                    @Override
                    public void onSuccess(ZHPageData<User> content) {
                        getPullProxy().onLoadSucessfully(content);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        getPullProxy().onLoadFailed(error);
                    }
                });
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        getDataFromInternet(nextId);
    }

    class UserAdapter extends BaseListAdapter<User> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UserHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_sign_up_member, null);
                holder = new UserHolder(convertView, getActivity());
                convertView.setTag(holder);
            } else {
                holder = (UserHolder) convertView.getTag();
            }
            holder.fill(getItem(position), position);
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

    class UserHolder {

        User user;

        Context context;

        int position;

        @InjectView(R.id.ivAvatar)
        AvatarView avatarView;

        @InjectView(R.id.tvName)
        TextView tvName;

        @InjectView(R.id.tvComAndPos)
        TextView tvComAndPos;

        @InjectView(R.id.tvCheckOk)
        TextView tvCheckOk;

        @InjectView(R.id.tvSignOk)
        TextView tvSignOk;

        @InjectView(R.id.tvMobile)
        TextView tvMobile;

        View item;

        public UserHolder(View v, final Context context) {
            this.context = context;
            item = v;
            ButterKnife.inject(this, v);
        }

        public void fill(User user, int position) {
            if (user == null) {
                tvSignOk.setVisibility(View.GONE);
                tvCheckOk.setVisibility(View.GONE);
                return;
            }
            this.user = user;
            this.position = position;
            avatarView.fill(user, true);
            tvName.setText(user.name);
            tvComAndPos.setText((user.userCompany == null ? ""
                    : user.userCompany)
                    + " "
                    + (user.userPosition == null ? "" : user.userPosition));

            // 待审核人员页面
            if (type.equals(TYPE_AUDITING)) {
                Integer signStatus = okUidsMap.get(user.uid);
                if (signStatus != null
                        && signStatus == Event.STATUS_AUDIT_ACCEPT) {
                    // 已审核
                    tvSignOk.setVisibility(View.VISIBLE);
                    tvCheckOk.setVisibility(View.INVISIBLE);
                } else {
                    // 未审核
                    if (event.canAudit()) {
                        // 可以审核
                        tvSignOk.setVisibility(View.INVISIBLE);
                        tvCheckOk.setVisibility(View.VISIBLE);
                        tvCheckOk.setEnabled(true);
                    } else {
                        // 不可以审核
                        tvSignOk.setVisibility(View.GONE);
                        tvCheckOk.setVisibility(View.VISIBLE);
                        tvCheckOk.setEnabled(false);
                    }
                }
            } else {
                // 已报名页面和已审核页面 不显示审核按钮、审核状态、联系方式。
                tvSignOk.setVisibility(View.GONE);
                tvCheckOk.setVisibility(View.GONE);

            }

            tvMobile.setText(user.userMobile);
            tvMobile.setVisibility(View.VISIBLE);

            item.setOnClickListener(itemClickListener);
        }

        OnClickListener itemClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user == null) {
                    return;
                }
                ActProfileDetail.invoke(getActivity(), user.uid);
            }
        };

        @OnClick(R.id.tvCheckOk)
        void checkOkClick() {
            tvCheckOk.setClickable(false);
            final User userCheck = user;
            showProgressDlg("提交中...");
            ZHApis.getEventApi().Approved(getActivity(), event.eventId, userCheck.uid,
                    new TaskCallback<Object>() {

                        @Override
                        public void onFinish() {
                            hideProgressDlg();
                            super.onFinish();
                            tvCheckOk.setClickable(true);
                        }

                        @Override
                        public void onSuccess(Object content) {
                            event.auditedCount += 1;
                            event.auditingCount -= 1;
                            EventBus.getDefault().post(
                                    new EBEvent(EBEvent.TYPE_EVENT_APPROVED,
                                            event));
                            if (isAdded() && !isDetached()) {
                                okUidsMap.put(userCheck.uid,
                                        Event.STATUS_AUDIT_ACCEPT);
                                getPullProxy().getAdapter()
                                        .notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Throwable error) {
                        }
                    });
        }

        public boolean leftMoreThanOneDay(long time) {
            long sysTime = System.currentTimeMillis() / 1000l;
            return time - sysTime > 60 * 60 * 24;
        }
    }
}
