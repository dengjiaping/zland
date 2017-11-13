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
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 报名人员列表页面(活动详情过来的)
 */
public class FragSignedList extends FragPullList<User> {

    private static final String PAGE_NAME = "EventSignList";

    public static final String INTENT_KEY_EVENT = "intent_key_event";

    private Event event;

    /**
     * 记录已经审核通过的uid
     */
    private HashMap<Long, Integer> okUidsMap = new HashMap<Long, Integer>();

    /**
     * 启动报名用户列表页面。三种情况，通过type判断。
     *
     * @param event 活动
     */
    public static void invoke(Context context, Event event) {
        if (event == null) {
            return;
        }
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragSignedList.class;
        param.enableBack = true;
        param.title = "已报名人员";
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_EVENT, event);
        context.startActivity(intent);
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        event = (Event) getActivity().getIntent().getSerializableExtra(
                INTENT_KEY_EVENT);

        getPullProxy().setAdapter(new UserAdapter());
        getPullProxy().setRefreshKey(PAGE_NAME + event.eventId);
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
        ev.setPrompt("暂时还没有报名人员");
        ev.setBtnVisibility(View.INVISIBLE);
        getPullProxy().getPullView().setEmptyView(ev);
    }

    @Override
    public void loadNormal() {
        getDataFromInternet(null);
    }

    private void getDataFromInternet(String maxId) {
        getSignedData(maxId);
    }

    /**
     * 获取报名人列表数据
     */
    private void getSignedData(String maxId) {
        ZHApis.getEventApi().getSignedMembers(getActivity(), event.eventId, maxId,
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
                        R.layout.event_sign_list, null);
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

        @InjectView(R.id.tvFriend)
        TextView tvFriend;

        View item;

        public UserHolder(View v, final Context context) {
            this.context = context;
            item = v;
            ButterKnife.inject(this, v);
        }

        public void fill(User user, int position) {
            if (user == null) {
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

            if (user.isFriend != null && user.isFriend.intValue() == 1) {
                tvFriend.setVisibility(View.VISIBLE);
            } else {
                tvFriend.setVisibility(View.GONE);
            }
            tvComAndPos.setVisibility(View.VISIBLE);
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

    }
}
