package com.zhisland.android.blog.info.view.impl.holder;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.view.impl.ActInfoDetail;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/7/25.
 */
public class RecommendInfoHolder {

    ZHInfo info;

    @InjectView(R.id.tvFrom)
    TextView tvFrom;

    @InjectView(R.id.tvInfoTitle)
    TextView tvInfoTitle;

    @InjectView(R.id.tvRead)
    TextView tvRead;

    @InjectView(R.id.tvTime)
    TextView tvTime;

    @InjectView(R.id.tvComment)
    TextView tvComment;

    View root;

    //下面的View在三个布局独有的，不能用ButterKnife
    ImageView ivInfoImg;
    TextView tvInfoLead;

    int colorF1 = ZHApplication.APP_CONTEXT.getResources().getColor(R.color.color_f1);
    int colorRead = Color.parseColor("#939393");
    int colorDc = ZHApplication.APP_CONTEXT.getResources().getColor(R.color.color_dc);

    int curType;

    public RecommendInfoHolder(View v) {
        root = v;
        ButterKnife.inject(this, v);
    }

    /**
     * 将info的数据填充到view中
     *
     * @param info 资讯对象
     */
    public void fill(ZHInfo info, int curType) {
        this.info = info;
        this.curType = curType;
        tvInfoTitle.setText(info.title);
        boolean isRead = DBMgr.getMgr().getInfoCacheDao().hasInfo(info.newsId);
        if (curType == 0) {
            ivInfoImg = (ImageView) root.findViewById(R.id.ivInfoImg);
            ImageWorkFactory.getFetcher().loadImage(info.coverLarge, ivInfoImg,
                    R.drawable.img_info_default_pic,
                    ImageWorker.ImgSizeEnum.MIDDLE);
            tvInfoTitle.setTextColor(isRead ? colorRead : colorF1);
        } else if (curType == 1) {
            ivInfoImg = (ImageView) root.findViewById(R.id.ivInfoImg);
            ImageWorkFactory.getFetcher().loadImage(info.coverSmall, ivInfoImg,
                    R.drawable.img_info_default_pic,
                    ImageWorker.ImgSizeEnum.SMALL);
            tvInfoTitle.setTextColor(isRead ? colorRead : colorF1);
        } else {
            tvInfoLead = (TextView) root.findViewById(R.id.tvInfoLead);
            if (!StringUtil.isNullOrEmpty(info.recommendText)) {
                tvInfoLead.setVisibility(View.VISIBLE);
                tvInfoLead.setText(info.recommendText);
            } else {
                tvInfoLead.setVisibility(View.GONE);
            }
            tvInfoTitle.setTextColor(isRead ? colorRead : colorF1);
        }
        setFromView();

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

        tvTime.setText(info.displayTime);
    }

    private void setFromView() {
        if (info.officialTag == ZHInfo.VALUE_OFFICIAL_TAG_NULL) {
            tvFrom.setTextColor(Color.parseColor("#99000000"));
            if (info.recommendUser != null) {
                tvFrom.setVisibility(View.VISIBLE);
                SpannableString ss = new SpannableString("来自 " + info.recommendUser.name + " 推荐");
                ss.setSpan(new ForegroundColorSpan(colorDc), 3, info.recommendUser.name.length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvFrom.setText(ss);
            } else {
                tvFrom.setVisibility(View.GONE);
            }
        } else {
            tvFrom.setVisibility(View.VISIBLE);
            tvFrom.setTextColor(colorDc);
            if (info.officialTag == ZHInfo.VALUE_OFFICIAL_TAG_ORIGINAL) {
                tvFrom.setText("正和岛原创");
            } else {
                tvFrom.setText("正和岛推荐");
            }
        }
    }

    @OnClick(R.id.itemView)
    public void onClickInfoItem() {
        if (curType == 0) {
            tvInfoTitle.setAlpha(0.7f);
        } else {
            tvInfoTitle.setTextColor(colorRead);
        }
        ActInfoDetail.invoke(root.getContext(), info.newsId);
    }

    @OnClick(R.id.tvFrom)
    public void onFromClick() {
        if (info.officialTag == ZHInfo.VALUE_OFFICIAL_TAG_NULL && info.recommendUser != null) {
            ActProfileDetail.invoke(root.getContext(), info.recommendUser.uid);
        } else {
            onClickInfoItem();
        }
    }
}
