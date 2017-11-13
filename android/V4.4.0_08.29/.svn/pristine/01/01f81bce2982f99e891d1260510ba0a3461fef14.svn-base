package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.util.StringUtil;

import java.util.List;

/**
 * 活动详情页，报名Listview
 */
public class SignUpListview extends LinearLayout {

    private Context context;

    private List<User> mUsers;

    public static final int mDefaultCount = 4;

    private int mTotalCount;

    private LayoutInflater mInflater;

    OnSignUpClickListener onClickListener;

    public SignUpListview(Context context) {
        this(context, null);
    }

    public SignUpListview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignUpListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setSignUpUsers(List<User> users, int totalCount) {
        this.mUsers = users;
        this.mTotalCount = totalCount;
        setUpViews();
    }

    private void setUpViews() {
        if (null == mUsers || mUsers.size() == 0) {
            return;
        }
        setOrientation(VERTICAL);
        removeAllViews();
        int showListCount = mUsers.size() >= mDefaultCount ? mDefaultCount : mUsers.size();

        for (int i = 0; i < showListCount; i++) {
            final int index = i;
            RelativeLayout item = (RelativeLayout) mInflater.inflate(R.layout.event_details_list_item, null);
            AvatarView avatarView = (AvatarView) item.findViewById(R.id.ivAuthorAvatar);
            TextView tvAuthorName = (TextView) item.findViewById(R.id.tvAuthorName);
            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            TextView tvFriend = (TextView) item.findViewById(R.id.tvFriend);
            TextView tvTime = (TextView) item.findViewById(R.id.tvTime);
            User user = mUsers.get(i);
            if (user.isFriend != null && user.isFriend == 1) {
                tvFriend.setVisibility(View.VISIBLE);
            }
            if(!StringUtil.isNullOrEmpty(user.customTime)){
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(user.customTime);
            }else{
                tvTime.setVisibility(View.GONE);
            }
            avatarView.setVisibility(View.VISIBLE);
            avatarView.fill(user, false);
            tvAuthorName.setText(user.name);
            tvPosition.setText(user.combineCompanyAndPosition());
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SignUpListview.this.onClickListener != null) {
                        SignUpListview.this.onClickListener.onItemClick(index, v);
                    }
                }
            });
            if (i == showListCount - 1) {
                View bottomLine = item.findViewById(R.id.view_bottom);
                bottomLine.setVisibility(GONE);
            }
            addView(item);
        }
//        makeMoreView();
    }


    private void makeMoreView() {
        //报名人数大于4添加查看更多导航
        if (mTotalCount > mDefaultCount) {
            RelativeLayout rlMore = (RelativeLayout) mInflater.inflate(R.layout.event_detail_all_member, null);
            TextView tvMore = (TextView) rlMore.findViewById(R.id.tv_more);
            tvMore.setText("全部报名用户(" + mTotalCount + ")" + " ");
            rlMore.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (SignUpListview.this.onClickListener != null) {
                        SignUpListview.this.onClickListener.moreClick(v);
                    }
                }
            });

            addView(rlMore);
        } else {
            //报名数小于4时,隐藏最后一个item的底线
            int childrenCount = this.getChildCount();
            View bottomView = getChildAt(mTotalCount - 1);
            View bottomLine = bottomView.findViewById(R.id.view_bottom);
            bottomLine.setVisibility(GONE);

        }
    }

    public void setOnSignUpClickListener(OnSignUpClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnSignUpClickListener {

        public void onItemClick(int index, View v);

        public void moreClick(View v);

    }

}
