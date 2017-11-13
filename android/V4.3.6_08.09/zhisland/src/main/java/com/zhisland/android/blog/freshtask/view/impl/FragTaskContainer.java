package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.model.impl.TaskContainerModel;
import com.zhisland.android.blog.freshtask.presenter.TaskContainerPresenter;
import com.zhisland.android.blog.freshtask.view.ITaskContainerView;
import com.zhisland.android.blog.freshtask.view.impl.holder.CardHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.CardHolderListener;
import com.zhisland.android.blog.freshtask.view.impl.holder.TaskContainerHolder;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 好友任务框架类
 * Created by 向飞 on 2016/5/26.
 */
public class FragTaskContainer extends FragBaseMvp<TaskContainerPresenter>
        implements ITaskContainerView, ViewPager.OnPageChangeListener, CardHolderListener {

    public static final int REQ_IMAGE = 100;

    private static final String INK_TASK = "ink_task";
    private static final String INK_POSITION = "ink_position";
    // view pager margin
    private static final int VP_MARGIN = DensityUtil.dip2px(20);
    //形象照 top margin
    private static final int FIGURE_TOP_MARGIN = DensityUtil.dip2px(25);

    private TaskContainerHolder viewHolder = new TaskContainerHolder();
    private AdapterCard adapter;//new AdapterCard(context, items)
    private CardHolder clickedHolder;
    private AProgressDialog dialog;

    // 形象照高度
    private int figureHeight = 0;

    public static void invoke(Context context, TaskCardList cards, int position) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragTaskContainer.class;
        param.enableBack = false;
        param.swipeBackEnable = false;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INK_TASK, cards);
        bundle.putSerializable(INK_POSITION, position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Object obj = getActivity().getIntent().getSerializableExtra(INK_TASK);
        if (obj == null || !(obj instanceof TaskCardList)) {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_task_container, container, false);
        ButterKnife.inject(viewHolder, view);
        ButterKnife.inject(this, view);
        adapter = new AdapterCard();
        viewHolder.vp.setAdapter(adapter);
        viewHolder.vp.addOnPageChangeListener(this);
        viewHolder.vp.setPageMargin((DensityUtil.dip2px(20)));
        viewHolder.vp.setOffscreenPageLimit(3);
        viewHolder.pc.setColors(getResources().getColor(R.color.white), getResources().getColor(R.color.color_white_70));
        initViewPagerHeight();
        return view;
    }

    /**
     * 设置 卡片高度 以及 形象照高度
     */
    private void initViewPagerHeight() {
        // 形象照高度计算公式： （屏幕宽度 - ViewPager左右距屏幕Margin - ViewPager内部padding) * (3/4)
        figureHeight = (int) ((DensityUtil.getWidth() - VP_MARGIN * 2 - getResources().getDimensionPixelOffset(R.dimen.fresh_task_card_padding) * 2) * 0.75);
        // 卡片整个高度等于 形象照高度 + 170dp
        int cardHeight = figureHeight + DensityUtil.dip2px(170);
        // 设置ViewPager params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, cardHeight);
        params.leftMargin = VP_MARGIN;
        params.rightMargin = VP_MARGIN;
        viewHolder.vp.setLayoutParams(params);
    }

    @Override
    protected String getPageName() {
        return "RookieTaskCard";
    }

    @Override
    public void setTask(List<TaskCard> task) {
        adapter.setItems(task);
        adapter.notifyDataSetChanged();
        viewHolder.pc.setPageCount(task.size());
    }

    @Override
    public void updateStep(String text) {
        viewHolder.tvStep.setText(text);
    }

    @Override
    public void updateSummary(String summary) {
        viewHolder.tvSummary.setText(summary);
    }

    @Override
    public void updateNext(String next) {
        viewHolder.tvNextStep.setText(next);
    }

    @Override
    public void updateHolder(int position) {
        viewHolder.vp.setCurrentItem(position);
        viewHolder.pc.setCurrentPage(position);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void gotoContact() {
        FragFriendAdd.invoke(getActivity());
    }

    @Override
    public void gotoFigure() {
        MultiImgPickerActivity.invoke(getActivity(),
                DensityUtil.getWidth(), DensityUtil.getWidth() * 3 / 4,
                REQ_IMAGE);
    }

    @Override
    public void gotoIntroduction() {
        FragIntroduction.invoke(getActivity());
    }

    @Override
    public void gotoResource() {
        FragAddResource.invoke(getActivity());
    }

    @Override
    public void gotoPrice() {
        FragFriendComment.invoke(getActivity());
    }

    @Override
    public void gotToUpdateHaiKe() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_TASK_CARD_FIND_FRIEND);
        // 跳转升级为海客页面
        FragInviteRequest.invoke(getActivity(), true);
    }

    @Override
    public void switchTo(int current) {

    }

    @Override
    public void refreshCurrentItem() {
        if (clickedHolder != null) {
            clickedHolder.refresh();
        }
    }

    @Override
    public void showProgress() {
        if (dialog == null) {
            dialog = new AProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    presenter().cancelUpload();
                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
            dialog.setText("正在上传您的图片...");
        }
    }

    @Override
    public void hideProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showShortToast(String toast) {
        ToastUtil.showShort(toast);
    }

    @Override
    public void showGuideFigure() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogGuest);
        dialog.setContentView(R.layout.guide_figure);
        dialog.setCancelable(false);
        final LinearLayout llIKnow = (LinearLayout) dialog
                .findViewById(R.id.llIKnow);
        llIKnow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    presenter().clickGuideFigure();
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtil.getWidth(); // 宽度
        lp.height = DensityUtil.getHeight(); // 高度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        dialog.show();
    }

    @Override
    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateListTitle(String title) {

    }

    @Override
    public void updateListDesc(String desc) {

    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public void showContactTask() {
        DialogUtil.showFreshTaskContactDlg(getActivity());
    }

    //=============viewpager listener start==============
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter().switchTo(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //==============viewpager listener end============


    //================UI event start================

    @OnClick(R.id.tvNextStep)
    public void onClickNextStep() {
        presenter().nextClicked();
    }

    @OnClick(R.id.viewClose)
    public void onClickClose() {
        presenter().closeClicked();
    }

    //===============UI event end=============

    //===============Card Item UI event start===============

    /**
     * 底部按钮点击
     */
    @Override
    public void onBtnClicked(CardHolder holder) {
        clickedHolder = holder;
        presenter().onCardItemClicked(holder.item);
    }

    @Override
    protected TaskContainerPresenter createPresenter() {
        TaskContainerPresenter presenter = new TaskContainerPresenter();
        TaskContainerModel model = new TaskContainerModel();
        presenter.setModel(model);
        Object obj = getActivity().getIntent().getSerializableExtra(INK_TASK);
        presenter.setAllTasks((TaskCardList) obj);
        presenter.setCurrentPosition(getActivity().getIntent().getIntExtra(INK_POSITION, 0));
        return presenter;
    }

    //===============Card Item UI event end===============

    //==========viewpager adapter============
    class AdapterCard extends PagerAdapter {

        private List<TaskCard> items;


        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View convertView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.item_fresh_task, null);
            CardHolder holder = new CardHolder();

            holder.setCardHolderListener(FragTaskContainer.this);
            ButterKnife.inject(holder, convertView);
            TaskCard item = getItem(position);
            holder.fill(item);

            initViewParamsByType(holder, item);

            container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            return convertView;
        }

        /**
         * 根据 type值 设置控件的 LayoutParams
         */
        private void initViewParamsByType(CardHolder holder, TaskCard item) {
            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (item.type == TaskType.FIGURE) {
                titleParams.topMargin = FIGURE_TOP_MARGIN + figureHeight + DensityUtil.dip2px(10);

                RelativeLayout.LayoutParams figureParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, figureHeight);
                figureParams.topMargin = FIGURE_TOP_MARGIN;
                // 设置形象照的 params
                holder.ivStepFigure.setLayoutParams(figureParams);
            } else {
                titleParams.topMargin = DensityUtil.dip2px(20);
            }
            // 设置标题的 params
            titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            holder.tvStepTitle.setLayoutParams(titleParams);
        }

        public TaskCard getItem(int position) {
            return items.get(position);
        }

        public void setItems(List<TaskCard> items) {
            this.items = items;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_IMAGE:
                    List<String> paths = (List<String>) data
                            .getSerializableExtra(MultiImgPickerActivity.RLT_PATHES);
                    String localUrl = paths.get(0);
                    ImageWorkFactory.getFetcher().loadImage(localUrl, clickedHolder.ivStepFigure);
                    presenter().loadFigure(localUrl);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onBackPressed() {
        presenter().closeClicked();
        return true;
    }
}
