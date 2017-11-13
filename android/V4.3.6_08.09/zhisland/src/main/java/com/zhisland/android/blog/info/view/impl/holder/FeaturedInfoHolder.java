package com.zhisland.android.blog.info.view.impl.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.view.impl.ActInfoDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/7/25.
 */
public class FeaturedInfoHolder {


    @InjectView(R.id.llDate)
    LinearLayout llDate;

    @InjectView(R.id.llBody)
    RelativeLayout llBody;

    @InjectView(R.id.tvDate)
    TextView tvDate;

    @InjectView(R.id.ivInfoImg)
    ImageView ivInfoImg;

    @InjectView(R.id.tvInfoTitle)
    TextView tvInfoTitle;

    @InjectView(R.id.tvRead)
    TextView tvRead;

    @InjectView(R.id.tvComment)
    TextView tvComment;

    @InjectView(R.id.vBottomDiv)
    View vBottomDiv;

    int colorF1 = ZHApplication.APP_CONTEXT.getResources().getColor(R.color.color_f1);
    int colorRead = Color.parseColor("#939393");;

    ZHInfo info;

    public FeaturedInfoHolder(View v) {
        ButterKnife.inject(this, v);
    }

    public void fill(ZHInfo info) {
        this.info = info;
        if (info.isFirst) {
            llDate.setVisibility(View.VISIBLE);
            if (isToday(info.day)) {
                tvDate.setText(" 今 日 ");
            } else {
                String dateStr = info.day;
                SimpleDateFormat sdf = new SimpleDateFormat(ZHInfo.dayFormat);
                try {
                    Date date = sdf.parse(info.day);
                    SimpleDateFormat df = new SimpleDateFormat("M月d日", Locale.getDefault());
                    dateStr = df.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tvDate.setText(dateStr);
            }
            if (info.isLast) {
                vBottomDiv.setVisibility(View.GONE);
                llBody.setBackgroundResource(R.drawable.bg_info_item_complete);
            } else {
                vBottomDiv.setVisibility(View.VISIBLE);
                llBody.setBackgroundResource(R.drawable.bg_info_item_top);
            }
        } else {
            llDate.setVisibility(View.GONE);
            if (info.isLast) {
                vBottomDiv.setVisibility(View.GONE);
                llBody.setBackgroundResource(R.drawable.bg_info_item_bottom);
            } else {
                vBottomDiv.setVisibility(View.VISIBLE);
                llBody.setBackgroundResource(R.drawable.bg_info_item_middle);
            }
        }
        String cover = StringUtil.isNullOrEmpty(info.coverSmall) ? info.coverLarge : info.coverSmall;
        ImageWorkFactory.getFetcher().loadImage(cover, ivInfoImg,
                R.drawable.img_info_default_pic,
                ImageWorker.ImgSizeEnum.SMALL);
        tvInfoTitle.setText(info.title);

        if (info.countCollect != null && info.countCollect.readedCount > 0) {
            tvRead.setText(String.valueOf(info.countCollect.readedCount));
            tvRead.setVisibility(View.VISIBLE);
        } else {
            tvRead.setVisibility(View.GONE);
        }

        if (info.countCollect != null && info.countCollect.viewpointCount > 0) {
            tvComment.setVisibility(View.VISIBLE);
            tvComment.setText(String.valueOf(info.countCollect.viewpointCount));
        } else {
            tvComment.setVisibility(View.GONE);
        }

        boolean isRead = DBMgr.getMgr().getInfoCacheDao().hasInfo(info.newsId);
        tvInfoTitle.setTextColor(isRead ? colorRead : colorF1);
    }

    @OnClick(R.id.llBody)
    public void onClickInfoItem() {
        tvInfoTitle.setTextColor(colorRead);
        ActInfoDetail.invoke(llBody.getContext(), info.newsId);
    }

    private boolean isToday(String date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(ZHInfo.dayFormat, Locale.getDefault());
            String curDate = df.format(new Date(System.currentTimeMillis()));
            return date.equals(curDate);
        }
        return false;
    }
}
