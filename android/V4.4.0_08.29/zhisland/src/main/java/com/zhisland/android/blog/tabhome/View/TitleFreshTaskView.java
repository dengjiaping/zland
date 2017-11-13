package com.zhisland.android.blog.tabhome.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * titlebar title
 */
public class TitleFreshTaskView {

    @InjectView(R.id.rlFreshTask)
    RelativeLayout rlFreshTask;

    @InjectView(R.id.ivRetDot)
    ImageView ivRetDot;

    @InjectView(R.id.ivRefreshTask)
    ImageView ivRefreshTask;

    private View freshTaskView;

    private Context context;

    public TitleFreshTaskView(Context context) {
        this.context = context;
        createRootView();
    }

    private void createRootView() {
        freshTaskView = LayoutInflater.from(context).inflate(R.layout.layout_fresh_task, null);
        ButterKnife.inject(this, freshTaskView);
    }

    public void addLeftTitle(TitleBarProxy titleBar, int tagId) {
        if (titleBar != null) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT);
            param.leftMargin = DensityUtil.dip2px(6);
            titleBar.addLeftTitleButton(freshTaskView, tagId,
                    param);

            refreshTitleFreshTask(PrefUtil.Instance().getIsShowTitleFreshTask(), PrefUtil.Instance().getIsShowTitleFreshTaskRedDot());
        }
    }

    /**
     * 刷新新手任务title
     *
     * @param showFreshTask 是否显示新手任务入口
     * @param showRedDot    是否显示红点
     */
    public void refreshTitleFreshTask(boolean showFreshTask, boolean showRedDot) {
        if (rlFreshTask != null) {
            rlFreshTask.setVisibility(showFreshTask ? View.VISIBLE : View.GONE);
        }
        if (ivRetDot != null) {
            ivRetDot.setVisibility(showRedDot ? View.VISIBLE : View.GONE);
        }
    }

    public void setIconBg(int resId) {
        ivRefreshTask.setBackgroundResource(resId);
    }
}
