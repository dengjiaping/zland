package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.eb.EBNotify;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.eb.EBEvent;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 我发起的活动页面
 */
public class FragInitiatedEvents extends FragPullList<Event> {

    private static final String PAGE_NAME = "EventMyCreateList";

    /**
     * 保持对EventHolder的引用。
     */
    private SparseArray<EventHolder> eventHolders = new SparseArray<EventHolder>();

    /**
     * 启动取消活动，编辑活动页面时的request code。返回时，如果是result ok，则更新页面。
     */
    public static final int REQ_NEED_FOR_LOAD_AGAIN = 2001;

    public static void invoke(Context context) {
        context.startActivity(getInvokeIntent(context));
    }

    public static Intent getInvokeIntent(Context context) {
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragInitiatedEvents.class;
        param.enableBack = true;
        param.title = "发起的活动";
        Intent intent = CommonFragActivity.createIntent(context, param);
        return intent;
    }

    @Override
    public String getPageName() {
        return "FragInitiatedEvents";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().setAdapter(new EventAdapter());
        getPullProxy().setRefreshKey(
                PAGE_NAME + PrefUtil.Instance().getUserId());
    }

    @Override
    public void onDetach() {
        eventHolders.clear();
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.color_bg2));

        internalView.setFastScrollEnabled(false);
        ColorDrawable drawable = new ColorDrawable(getResources().getColor(
                R.color.color_bg2));
        internalView.setDivider(drawable);
        internalView.setDividerHeight(DensityUtil.dip2px(10));
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        internalView.setBackgroundColor(getResources().getColor(
                R.color.color_bg2));

        EmptyView ev = new EmptyView(getActivity());
        ev.setImgRes(R.drawable.img_campaign_empty);
        ev.setPrompt("您还没有发起任何活动");
        ev.setBtnVisibility(View.INVISIBLE);
        getPullProxy().getPullView().setEmptyView(ev);

        // 我发起的活动列表被查看，个人页tab的红点消失
        PrefUtil.Instance().setKeyValue(NotifyTypeConstants.PREF_MY_EVENT, 0);
        EBNotify notify = new EBNotify();
        notify.notifyType = NotifyTypeConstants.EventBusEventInitiatedList;
        EventBus.getDefault().post(notify);
    }

    @Override
    public void loadNormal() {
        getDataFromInternet(null);
    }

    private void getDataFromInternet(String maxId) {
        ZHApis.getEventApi().getInitiatedEvents(getActivity(), maxId,
                new TaskCallback<ZHPageData<Event>>() {

                    @Override
                    public void onSuccess(ZHPageData<Event> content) {
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

    class EventAdapter extends BaseListAdapter<Event> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EventHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_initiated_event, null);
                holder = new EventHolder(convertView, getActivity());
                eventHolders.put(holder.hashCode(), holder);
                convertView.setTag(holder);
            } else {
                holder = (EventHolder) convertView.getTag();
            }
            holder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

    class EventHolder {

        Event event;

        Context context;

        @InjectView(R.id.tvTitle)
        TextView tvTitle;

        @InjectView(R.id.tvChecked)
        TextView tvChecked;

        @InjectView(R.id.tvToCheck)
        TextView tvToCheck;

        @InjectView(R.id.tvEdit)
        TextView tvEdit;

        @InjectView(R.id.tvCancel)
        TextView tvCancel;

        @InjectView(R.id.tvCancelOrOver)
        TextView tvCancelOrOver;

        private CommonDialog commonDialog;

        public EventHolder(View v, final Context context) {
            this.context = context;
            ButterKnife.inject(this, v);
        }

        public void fill(Event event) {
            this.event = event;
            tvTitle.setText(event.eventTitle);

            SpannableString checkedString = new SpannableString("已确认 "
                    + event.auditedCount);
            ForegroundColorSpan checkSpan = new ForegroundColorSpan(
                    getResources().getColor(R.color.color_f1));
            checkedString.setSpan(checkSpan, 4, checkedString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvChecked.setText(checkedString);

            SpannableString toCheckString = new SpannableString("待审核 "
                    + event.auditingCount);
            ForegroundColorSpan toCheckSpan = new ForegroundColorSpan(
                    getResources().getColor(R.color.red));
            toCheckString.setSpan(toCheckSpan, 4, toCheckString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvToCheck.setText(toCheckString);

            if (event.eventStatus == Event.STATUS_SIGNING) {
                tvEdit.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                tvCancelOrOver.setVisibility(View.GONE);
            } else {
                tvEdit.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                tvCancelOrOver.setVisibility(View.VISIBLE);
                if (event.eventStatus == Event.STATUS_END) {
                    tvCancelOrOver.setText("活动已结束");
                } else if (event.eventStatus == Event.STATUS_CANCEL) {
                    tvCancelOrOver.setText("活动已取消");
                } else if (event.eventStatus == Event.STATUS_PROGRESSING) {
                    tvCancelOrOver.setText("活动进行中");
                } else if (event.eventStatus == Event.STATUS_SIGN_OVER) {
                    // 个人活动不存在报名截止，该分支不会被执行。先写着，以防万一。
                    tvCancelOrOver.setText("活动报名已截止");
                }
            }
        }

        @OnClick(R.id.tvTitle)
        void titleClick() {
            ActEventDetail.invoke(getActivity(), event.eventId, false);
        }

        @OnClick(R.id.tvChecked)
        void checkedClick() {
            FragSignUpMembers.invoke(getActivity(), event,
                    FragSignUpMembers.TYPE_AUDITED);
        }

        @OnClick(R.id.tvToCheck)
        void toCheckClick() {
            FragSignUpMembers.invoke(getActivity(), event,
                    FragSignUpMembers.TYPE_AUDITING);
        }

        @OnClick(R.id.tvEdit)
        void editClick() {
            ActEventCreate.invokeForResult(getActivity(), event,
                    FragInitiatedEvents.REQ_NEED_FOR_LOAD_AGAIN, -1);
        }

        @OnClick(R.id.tvCancel)
        void cancelClick() {
            if (commonDialog == null) {
                commonDialog = new CommonDialog(context);
            }
            if (!commonDialog.isShowing()) {
                commonDialog.show();

                commonDialog.setTitle("取消此活动");
                commonDialog.setContent("取消活动后，报名人将收到取消通知");
                commonDialog.setLeftBtnContent("再想想");
                commonDialog.setRightBtnContent("确定");
                commonDialog.setRightBtnColor(context.getResources().getColor(
                        R.color.color_ac));
                commonDialog.tvDlgConfirm
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                commonDialog.dismiss();
                                ActEventCancelReasion.invoke(context,
                                        event.eventId);
                            }
                        });
            }
        }

    }

    public void onEventMainThread(EBEvent eb) {
        switch (eb.getType()) {
            case EBEvent.TYPE_EVENT_APPROVED:
                // 通知 : 审核通过一位报名我的活动的用户 ,更新item
                int key = 0;
                for (int i = 0; i < eventHolders.size(); i++) {
                    key = eventHolders.keyAt(i);
                    try {
                        EventHolder holder = eventHolders.get(key);
                        if (holder.event.eventId == eb.getEvent().eventId) {
                            holder.fill(eb.getEvent());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FragInitiatedEvents.REQ_NEED_FOR_LOAD_AGAIN) {
                // 启动取消活动，编辑活动页面时的request code。返回时，如果是result ok，则更新页面。
                getDataFromInternet(null);
            }
        }
    }
}
