package com.zhisland.android.blog.find.controller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 可能认识的人页面fragment
 * Created by Mr.Tui on 2016/5/24.
 */
public class FragContactFriend extends FragPullList<User> {

    public static final String PAGE_NAME = "DiscoveryPossibleFriend";

    EmptyView ev;

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().needRefreshOnResume = false;
        getPullProxy().setAdapter(new UserAdapter());
        getPullProxy().setRefreshKey(PAGE_NAME + PrefUtil.Instance().getUserId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View pullList = super.onCreateView(inflater, container,
                savedInstanceState);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        pullList.setLayoutParams(lp);
        return pullList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.white));
        internalView.setFastScrollEnabled(false);
        internalView.setDividerHeight(0);
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        internalView.setHeaderDividersEnabled(false);
        internalView.setFooterDividersEnabled(false);
        internalView.setBackgroundColor(getResources().getColor(
                R.color.transparent));
        makeEmptyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshRunnable);
    }

    private void makeEmptyView() {
        ev = new EmptyView(getActivity());
        ev.setImgRes(R.drawable.img_find_glass);
        ev.setBtnVisibility(View.INVISIBLE);
        SpannableStringBuilder style = new SpannableStringBuilder("抱歉，没有找到您可能认识的人\n好友加入正和岛时，我们会通知您");
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_f1)),
                0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new RelativeSizeSpan(1.2f),
                0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ev.setPrompt(style);
    }

    @Override
    public void loadNormal() {
        uploadContact();
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        getContactFriend(nextId);
    }

    public void refreshData() {
        handler.postDelayed(refreshRunnable, 700);
    }

    Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            getPullProxy().pullDownToRefresh(true);
        }
    };

    /**
     * 上传通讯录
     */
    private void uploadContact() {
        PhoneContactUtil.ContactResult<String> contacts = PhoneContactUtil.getChangeContact();
        final Long timestamp = contacts == null ? null : contacts.timestamp;
        //如果有变更的通讯录，则上传通讯录，上传成功后，获取可能认识的人
        if (contacts != null && !StringUtil.isNullOrEmpty(contacts.result)) {
            ZHApis.getCommonApi().UploadContact(getActivity(), contacts.result, new TaskCallback<Object>() {
                @Override
                public void onSuccess(Object content) {
                    if (timestamp != null) {
                        PhoneContactUtil.setLastTimestamp(timestamp);
                    }
                    getContactFriend(null);
                }

                @Override
                public void onFailure(Throwable error) {
                    getContactFriend(null);
                }

            });
        } else {
            getContactFriend(null);
        }
    }

    /**
     * 获取可能认识的人
     */
    private void getContactFriend(String maxId) {
        ZHApis.getFindApi().getContactFriend(getActivity(), maxId, 20, new TaskCallback<ZHPageData<User>>() {
            @Override
            public void onSuccess(ZHPageData<User> content) {
                PrefUtil.Instance().setContactFriendCount(PrefUtil.Instance().getUserId(), content.count);
                getPullProxy().onLoadSucessfully(content);
            }

            @Override
            public void onFailure(Throwable error) {
                getPullProxy().onLoadFailed(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                getPullProxy().getPullView().setEmptyView(ev);
            }
        });
    }

    class UserAdapter extends BaseListAdapter<User> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UserHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_search_user, null);
                holder = new UserHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (UserHolder) convertView.getTag();
            }
            holder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

    class UserHolder {

        User user;

        @InjectView(R.id.ivAvatar)
        AvatarView avatarView;

        @InjectView(R.id.tvName)
        TextView tvName;

        @InjectView(R.id.tvComAndPos)
        TextView tvComAndPos;

        @InjectView(R.id.tvRight)
        TextView tvRight;

        @InjectView(R.id.ivRight)
        ImageView ivRight;

        View item;

        public UserHolder(View v) {
            item = v;
            ButterKnife.inject(this, v);
        }

        public void fill(User user) {
            this.user = user;
            avatarView.fill(user, true);
            String name = user.name == null ? "" : user.name;
            String desc = (user.userCompany == null ? ""
                    : (user.userCompany + " "))
                    + (user.userPosition == null ? "" : user.userPosition);
            tvName.setText(name);
            tvComAndPos.setText(desc);
            ivRight.setVisibility(View.GONE);
            tvRight.setVisibility(View.VISIBLE);
            if (user.sendApplyFriendRequest) {
                tvRight.setText("已发送");
                tvRight.setEnabled(false);
            } else {
                tvRight.setText("加好友");
                tvRight.setEnabled(true);
            }
            item.setOnClickListener(itemClickListener);
        }

        View.OnClickListener itemClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActProfileDetail.invoke(getActivity(), user.uid);
            }
        };

        @OnClick(R.id.tvRight)
        void rightClick() {
            if (!user.sendApplyFriendRequest) {
                if (PermissionsMgr.getInstance().canAddFriend(user)) {
                    boolean result = IMUtil.addFriend(user.uid);
                    if (result) {
                        ToastUtil.showShort("好友请求已发送");
                        user.sendApplyFriendRequest = true;
                        tvRight.setText("已发送");
                        tvRight.setEnabled(false);
                    } else {
                        ToastUtil.showShort("发送好友申请失败，请重新发送");
                    }
                } else {
                    DialogUtil.showPermissionsDialog(getActivity(), getPageName());
                }
            }
        }
    }

}