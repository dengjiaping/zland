package com.zhisland.android.blog.aa.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.view.FragGuide.FragGuide;
import com.zhisland.android.blog.common.view.FragGuide.GuideStepItem;
import com.zhisland.android.blog.common.view.FragGuide.GuideStepViewCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 基本信息引导流程
 * Created by 向飞 on 2016/5/20.
 */
public class GuideInfo implements GuideStepViewCreator {

    private static GuideInfo INSTANCE() {
        return InstanceHolder.INSTANCE;
    }

    public static void invoke(Context context) {
        List<GuideStepItem> steps = INSTANCE().createSteps();
        FragGuide.invoke(context, steps, INSTANCE(), R.drawable.guide_info_bg);
    }

    private GuideInfo() {
    }

    /**
     * 创建基本信息步骤
     *
     * @return
     */
    private List<GuideStepItem> createSteps() {
        List<GuideStepItem> steps = new ArrayList<>(3);
        GuideStepItem step;

        step = new GuideStepItem();
        step.title = "连接成功…";
        step.desc = "欢迎您来到正和岛\n先让我们彼此介绍一下";
        steps.add(step);

        step = new GuideStepItem();
        step.title = "正和岛就是这样一座小岛";
        step.desc = "链接有信用的企业家，让商业世界更值得信任；因为信任，所以简单…";
        step.actionTitle = "介绍自己";
        step.actionUri = "";//TODO 替换成跳转到基本信息的URI
        steps.add(step);

        return steps;
    }

    @Override
    public View createStepView(Context context, GuideStepItem stepItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide_info_step, null);
        ViewHolder holder = new ViewHolder();
        ButterKnife.inject(holder, view);
        holder.fill(context, stepItem);
        return view;

    }


    //信息引导的步骤viewholder
    static class ViewHolder {
        @InjectView(R.id.tvGuideInfoTitle)
        TextView tvTitle;
        @InjectView(R.id.tvGuideInfoDesc)
        TextView tvDesc;
        @InjectView(R.id.tvGuideInfoAction)
        TextView tvAction;

        private Context context;

        void fill(Context context, GuideStepItem step) {
            this.context = context;
            tvTitle.setText(step.title);
            tvDesc.setText(step.desc);

            if (TextUtils.isEmpty(step.desc)) {
                tvDesc.setVisibility(View.GONE);
            } else {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(step.desc);
            }

            if (TextUtils.isEmpty(step.actionTitle)) {
                tvAction.setVisibility(View.GONE);
            } else {
                tvAction.setVisibility(View.VISIBLE);
                tvAction.setText(step.actionTitle);
            }
        }

        @OnClick(R.id.tvGuideInfoAction)
        public void onClickAction() {
            PrefUtil.Instance().setShowGuideBasicInfo();
            FragCreateBasicInfo.invoke(context, false);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }

    static class InstanceHolder {
        static GuideInfo INSTANCE = new GuideInfo();
    }

}
