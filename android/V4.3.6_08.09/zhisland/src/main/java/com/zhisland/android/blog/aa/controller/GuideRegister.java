package com.zhisland.android.blog.aa.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.HomeUtil;
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
 * 注册引导流程
 * Created by 向飞 on 2016/5/20.
 */
public class GuideRegister implements GuideStepViewCreator {

    private static GuideRegister INSTANCE() {
        return InstanceHolder.INSTANCE;
    }

    public static void invoke(Context context) {
        List<GuideStepItem> steps = INSTANCE().createSteps();
        FragGuide.invoke(context, steps, INSTANCE(), R.drawable.guide_register_bg);
    }

    private GuideRegister() {
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
        step.title = "我们一生都在寻找…\n\n同伟大的智者一样\n我们都心怀希望…";
        steps.add(step);

        step = new GuideStepItem();
        step.title = "“我多么希望在世界上有个小岛，上面居住的全是智慧和善良的人们”";
        step.desc = "—爱因斯坦";
        steps.add(step);

        step = new GuideStepItem();
        step.title = "加入正和岛\n与智慧而善良的人们同行..                                                                 ";//增加空格是为了撑开textview高度
        step.actionTitle = "好的";
        steps.add(step);

        return steps;
    }

    @Override
    public View createStepView(Context context, GuideStepItem stepItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide_register_step, null);
        ViewHolder holder = new ViewHolder();
        ButterKnife.inject(holder, view);
        holder.fill(context, stepItem);
        return view;

    }

    //信息引导的步骤viewholder
    static class ViewHolder {
        @InjectView(R.id.tvRegisterGuideTitle)
        TextView tvTitle;
        @InjectView(R.id.tvRegisterGuideDesc)
        TextView tvDesc;
        @InjectView(R.id.tvRegisterGuideAction)
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
                tvTitle.setTextSize(20);
            } else {
                tvAction.setVisibility(View.VISIBLE);
                tvAction.setText(step.actionTitle);
                tvTitle.setTextSize(22);
            }
        }

        @OnClick(R.id.tvRegisterGuideAction)
        public void onClickGuideAction(){
            PrefUtil.Instance().setShowGuideRegister();
            if (context instanceof Activity) {
                HomeUtil.dispatchJump((Activity) context);
            }
        }
    }

    static class InstanceHolder {
        static GuideRegister INSTANCE = new GuideRegister();
    }

}
