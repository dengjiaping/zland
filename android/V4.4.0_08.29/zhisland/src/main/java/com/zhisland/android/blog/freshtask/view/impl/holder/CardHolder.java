package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.lib.bitmap.ImageWorkFactory;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 新手任务 ViewPager holder
 */
public class CardHolder {

    @InjectView(R.id.tvStepTitle)
    public TextView tvStepTitle;

    @InjectView(R.id.ivStepIcon)
    public ImageView ivStepIcon;

    @InjectView(R.id.llStepIcon)
    public LinearLayout llStepIcon;

    @InjectView(R.id.ivStepFigure)
    public ImageView ivStepFigure;

    @InjectView(R.id.tvStepDesc)
    public TextView tvStepDesc;

    @InjectView(R.id.tvCompleteStep)
    public TextView tvCompleteStep;

    @InjectView(R.id.ivAlreadyComplete)
    public ImageView ivAlreadyComplete;

    public TaskCard item;
    private CardHolderListener cardHolderListener;

    public void fill(TaskCard item) {
        this.item = item;
        tvStepTitle.setText(item.tips);
        tvStepDesc.setText(item.summary);
        if (item.type == TaskType.FIGURE) {
            ivStepFigure.setVisibility(View.VISIBLE);
            llStepIcon.setVisibility(View.GONE);
        } else {
            ivStepFigure.setVisibility(View.GONE);
            llStepIcon.setVisibility(View.VISIBLE);
        }
        if (item.type == TaskType.FIGURE) {
            ImageWorkFactory.getFetcher().loadImage(item.figureUrl, ivStepFigure, item.iconCardRes);
        } else {
            ivStepIcon.setImageResource(item.iconCardRes);
        }

        boolean isFinished = item.state.getState() == TaskStatus.FINISHED;
        tvCompleteStep.setEnabled(!isFinished);
        if (isFinished) {
            ivAlreadyComplete.setVisibility(View.VISIBLE);
            tvCompleteStep.setText("已完成");
        } else {
            ivAlreadyComplete.setVisibility(View.GONE);
            tvCompleteStep.setText(item.state.getStateName());
        }
    }

    /**
     * 更新UI
     */
    public void refresh() {
        if (item != null) {
            fill(item);
        }
    }

    @OnClick(R.id.tvCompleteStep)
    public void onClickComplete() {
        if (cardHolderListener != null) {
            cardHolderListener.onBtnClicked(this);
        }
    }


    public void setCardHolderListener(CardHolderListener cardHolderListener) {
        this.cardHolderListener = cardHolderListener;
    }
}
