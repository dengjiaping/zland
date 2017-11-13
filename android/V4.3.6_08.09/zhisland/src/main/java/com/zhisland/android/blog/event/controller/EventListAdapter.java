package com.zhisland.android.blog.event.controller;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 活动列表 adapter
 */
public class EventListAdapter extends BaseListAdapter<Event> {

    // 正常活动
    public static final int TYPE_EVENT = 0;
    // 往期活动分割
    public static final int TYPE_TXT_PAST_EVENT = 1;
    // 当前活动空白页
    public static final int TYPE_CURRENT_EMPTY_VIEW = 2;
    // 无活动空白页
    public static final int TYPE_EVENT_EMPTY_VIEW = 3;

    private Context context;

    public EventListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return getData().get(position).localType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_EVENT:
                EventHolder holder;
                final Event item = getItem(position);
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_event, parent, false);
                    holder = new EventHolder(convertView, context);
                    convertView.setTag(holder);
                } else {
                    holder = (EventHolder) convertView.getTag();
                }
                holder.fill(item);
                break;
            case TYPE_TXT_PAST_EVENT:
                convertView = inflater.inflate(R.layout.event_txt_past, parent, false);
                break;
            case TYPE_CURRENT_EMPTY_VIEW:
                convertView = inflater.inflate(R.layout.event_past_empty_view, parent, false);
                break;
            case TYPE_EVENT_EMPTY_VIEW:
                convertView = inflater.inflate(R.layout.event_empty_view, parent, false);
                break;
        }
        return convertView;
    }

    @Override
    protected void recycleView(View view) {
        Object tag = view.getTag();
        if (tag instanceof EventHolder) {
            EventHolder holder = (EventHolder) tag;
            holder.recycle();
        }
    }

    static class EventHolder {
        // 活动 icon
        @InjectView(R.id.ivEventIcon)
        public ImageView ivEventIcon;
        // 限时优惠
        @InjectView(R.id.ivEventDiscount)
        public ImageView ivEventDiscount;
        // 活动状态 : 已开始/已结束
        @InjectView(R.id.tvEventState)
        public TextView tvEventState;
        // 活动标题
        @InjectView(R.id.tvEventTitle)
        public TextView tvEventTitle;
        // 活动时间和活动地点
        @InjectView(R.id.tvEventTimeAndLocation)
        public TextView tvEventTimeAndLocation;
        // 参加人数
        @InjectView(R.id.tvEventAttend)
        public TextView tvEventAttend;

        private Context context;
        private Event event;

        public EventHolder(View v, final Context context) {
            ButterKnife.inject(this, v);
            this.context = context;
        }

        public void fill(Event event) {
            this.event = event;

            ImageWorkFactory.getFetcher().loadImage(event.imgUrl, ivEventIcon,
                    R.drawable.img_info_default_pic, ImageWorker.ImgSizeEnum.MIDDLE);
            // 限时优惠显示
            if (event.isDiscountEvent()) {
                ivEventDiscount.setVisibility(View.VISIBLE);
            } else {
                ivEventDiscount.setVisibility(View.GONE);
            }
            // 活动状态
            if (!StringUtil.isNullOrEmpty(event.statusName)) {
                tvEventState.setVisibility(View.VISIBLE);
                tvEventState.setText(event.statusName);
            } else {
                tvEventState.setVisibility(View.GONE);
            }
            // 官方活动
            if (event.type == Event.CATEGORY_TYPE_OFFICIAL) {
                SpannableString spanString = new SpannableString("  " + event.eventTitle);
                Drawable drawable = context.getResources().getDrawable(R.drawable.campaign_img_guanfang);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvEventTitle.setText(spanString);
            } else {
                tvEventTitle.setText(event.eventTitle);
            }
            // 时间与地点
            tvEventTimeAndLocation.setText(event.period);

            if (event.signedCount > 0) {
                tvEventAttend.setVisibility(View.VISIBLE);
                if (!StringUtil.isNullOrEmpty(event.friend)) {
                    String preString = "好友";
                    String sufString = "等" + event.signedCount + "人已报名";
                    String joinString = preString + event.friend + sufString;
                    SpannableString spanString = new SpannableString(joinString);
                    ForegroundColorSpan span = new ForegroundColorSpan(
                            context.getResources().getColor(R.color.color_dc));
                    spanString.setSpan(span, preString.length(),
                            preString.length() + event.friend.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvEventAttend.setText(spanString);
                } else {
                    String joinString = event.signedCount + "人已报名";
                    tvEventAttend.setText(joinString);
                }
            } else {
                tvEventAttend.setVisibility(View.INVISIBLE);
            }
        }

        public void recycle() {
            this.event = null;
        }

        @OnClick(R.id.rlEvent)
        public void onClickEvent() {
            ActEventDetail.invoke(context, event.eventId, false);
        }
    }

}
