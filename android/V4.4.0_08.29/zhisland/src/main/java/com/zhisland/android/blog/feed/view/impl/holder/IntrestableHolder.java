package com.zhisland.android.blog.feed.view.impl.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by arthur on 2016/9/6.
 */
public class IntrestableHolder {


    @InjectView(R.id.rvIntrested)
    public RecyclerView rvIntrested;


    private Context context;
    private View root;
    private List<RecommendUser> users;
    private IntrestAdapter adapter;
    private IntrestableListener viewListener;

    public IntrestableHolder(Context context, View view, IntrestableListener viewListener) {

        ButterKnife.inject(this, view);

        this.context = context;
        this.root = view;
        this.viewListener = viewListener;

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvIntrested.setLayoutManager(llm);

        adapter = new IntrestAdapter();
        rvIntrested.setAdapter(adapter);

    }

    //更新用户列表
    public void update(List<RecommendUser> users) {
        this.users = users;
        adapter.notifyDataSetChanged();
    }

    //移除某条关注
    public void removeRecommend(RecommendUser inviteUser) {
        if (inviteUser == null || users == null)
            return;

        int index = users.indexOf(inviteUser);
        if (index >= 0) {
            users.remove(inviteUser);
            adapter.notifyItemRangeRemoved(index, 1);
        }

        if (users.size() < 1) {
            viewListener.onRemoveAll();
        }
    }

    //更新某一条
    public void updateItem(RecommendUser inviteUser) {
        if (inviteUser == null || users == null)
            return;

        int index = users.indexOf(inviteUser);
        if (index >= 0) {
            adapter.notifyItemChanged(index);
        }
    }

    //隐藏可能认识的人区域
    public void hideAll() {
        root.setVisibility(View.GONE);
    }

    //展示整个区域
    public void showAll() {
        root.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvIntrestAll)
    public void onViewAllClicked() {
        viewListener.onViewAllClicked();
    }

    class IntrestAdapter extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_intrestable_user, parent, false);
            ItemHolder holder = new ItemHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            if (users != null && users.size() > position) {
                RecommendUser user = users.get(position);
                holder.fill(user, position, getItemCount());
            }
        }

        @Override
        public int getItemCount() {
            return users == null ? 0 : users.size();
        }

        @Override
        public void onViewRecycled(ItemHolder holder) {
            if (!holder.isRecyclable()) {
                holder.recycle();
                holder.setIsRecyclable(true);
            }
        }

    }

    class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ivIntrestAvatar)
        ImageView ivIntrestAvatar;
        @InjectView(R.id.ivIntrestRank)
        ImageView ivIntrestRank;
        @InjectView(R.id.tvIntrestName)
        TextView tvIntrestName;
        @InjectView(R.id.tvIntrestPosition)
        TextView tvIntrestPosition;


        @InjectView(R.id.llIntrestUserFollow)
        LinearLayout llIntrestUserFollow;
        @InjectView(R.id.tvIntrestFollow)
        TextView tvIntrestFollow;
        @InjectView(R.id.tvIntrestIgnore)
        TextView tvIntrestIgnore;
        @InjectView(R.id.tvIntrestFollowed)
        TextView tvIntrestFollowed;

        private View root;
        private int width = DensityUtil.getWidth() * 448 / 1000;
        private RecommendUser inviteUser;

        public ItemHolder(View itemView) {
            super(itemView);
            this.root = itemView;
            ButterKnife.inject(this, itemView);
        }

        //填充数据
        public void fill(RecommendUser inviteUser, int position, int itemCount) {
            if (inviteUser == null || inviteUser.user == null) {
                return;
            }

            this.inviteUser = inviteUser;
            User user = inviteUser.user;

            ImageWorkFactory.getCircleFetcher().loadImage(user.userAvatar, ivIntrestAvatar, R.drawable.avatar_default);
            ivIntrestRank.setImageResource(user.getVipIconId());
            tvIntrestName.setText(user.name);
            tvIntrestPosition.setText(user.combineCompanyAndPosition());


            llIntrestUserFollow.setVisibility(View.VISIBLE);
            tvIntrestFollowed.setVisibility(View.GONE);

            if (position == 0) {
                root.setBackgroundResource(R.drawable.bg_intrest_user_first);
                root.getLayoutParams().width = width + DensityUtil.dip2px(12);
            } else if (position == (itemCount - 1)) {
                root.setBackgroundResource(R.drawable.bg_intrest_user_last);
                root.getLayoutParams().width = width + DensityUtil.dip2px(12);
            } else {
                root.setBackgroundResource(R.drawable.bg_intrest_user);
                root.getLayoutParams().width = width;
            }

        }

        public void recycle() {
            ivIntrestAvatar.setImageBitmap(null);
            ivIntrestRank.setImageResource(R.drawable.rank_transparent);
        }

        @OnClick({R.id.ivIntrestAvatar, R.id.ivIntrestRank, R.id.tvIntrestName, R.id.tvIntrestPosition})
        public void onUserClicked() {
            if (inviteUser == null | inviteUser.user == null)
                return;

            viewListener.onUserClicked(inviteUser.user);
        }

        @OnClick({R.id.tvIntrestFollow})
        public void onFollowClicked() {
            if (inviteUser == null | inviteUser.user == null)
                return;

            viewListener.onFollowClicked(inviteUser);
        }

        @OnClick({R.id.tvIntrestIgnore})
        public void onIgnoreClicked() {
            if (inviteUser == null | inviteUser.user == null)
                return;

            viewListener.onIgnoreClicked(inviteUser);
        }
    }

}

