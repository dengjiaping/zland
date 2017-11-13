package com.zhisland.android.blog.im.controller.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.im.eb.EBMessage;
import com.zhisland.android.blog.message.view.impl.FragFansMessageList;
import com.zhisland.android.blog.message.view.impl.FragInteractionMessage;
import com.zhisland.android.blog.message.view.impl.FragSystemMessageList;
import com.zhisland.lib.rxjava.RxBus;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 对话列表的头部
 * <p/>
 * Created by arthur on 2016/9/13.
 */
public class HolderChatSessionHeader {

    private Context context;
    private View fansView, interactionView, systemView;
    public ChatHeaderItemHolder fansHolder, interactionHolder, systemHolder;

    public HolderChatSessionHeader(final Context context, View headerView) {
        this.context = context;
        fansView = headerView.findViewById(R.id.vChatFans);
        fansHolder = new ChatHeaderItemHolder(fansView);

        interactionView = headerView.findViewById(R.id.vChatInteraction);
        interactionHolder = new ChatHeaderItemHolder(interactionView);

        systemView = headerView.findViewById(R.id.vChatSystem);
        systemHolder = new ChatHeaderItemHolder(systemView);

        fansView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragFansMessageList.Invoke(context);
                PrefUtil.Instance().setNewlyAddedFansCount(0);
                RxBus.getDefault().post(new EBMessage(EBMessage.TYPE_MESSAGE_REFRESH, null));
            }
        });
        interactionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragInteractionMessage.Invoke(context);
                PrefUtil.Instance().setInteractiveCount(0);
                RxBus.getDefault().post(new EBMessage(EBMessage.TYPE_MESSAGE_REFRESH, null));
            }
        });
        systemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragSystemMessageList.Invoke(context);
                PrefUtil.Instance().setSystemMsgCount(0);
                RxBus.getDefault().post(new EBMessage(EBMessage.TYPE_MESSAGE_REFRESH, null));
            }
        });

        fansHolder.fill(R.drawable.sel_im_addfans, "新增粉丝", PrefUtil.Instance().getNewlyAddedFansCount());
        interactionHolder.fill(R.drawable.sel_im_interaction, "互动消息", PrefUtil.Instance().getInteractiveCount());
        systemHolder.fill(R.drawable.sel_im_inform, "系统通知", PrefUtil.Instance().getSystemMsgCount());
        systemHolder.vChatHeaderLine.setVisibility(View.GONE);

    }


    public static class ChatHeaderItemHolder {
        @InjectView(R.id.ivChatHeaderAvatar)
        ImageView ivChatHeaderAvatar;
        @InjectView(R.id.tvChatHeaderName)
        TextView tvChatHeaderName;
        @InjectView(R.id.ivChatHeaderArrow)
        ImageView ivChatHeaderArrow;
        @InjectView(R.id.tvChatHeaderNotify)
        TextView tvChatHeaderNotify;
        @InjectView(R.id.vChatHeaderLine)
        View vChatHeaderLine;

        ChatHeaderItemHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void fill(int resId, String title, long num) {
            ivChatHeaderAvatar.setImageResource(resId);
            tvChatHeaderName.setText(title);
            if (num < 1) {
                ivChatHeaderArrow.setVisibility(View.VISIBLE);
                tvChatHeaderNotify.setVisibility(View.GONE);
            } else {
                ivChatHeaderArrow.setVisibility(View.GONE);
                tvChatHeaderNotify.setVisibility(View.VISIBLE);
                String notifyText = num > 99 ? "99+" : num + "";
                tvChatHeaderNotify.setText(notifyText);
            }
        }

    }
}
