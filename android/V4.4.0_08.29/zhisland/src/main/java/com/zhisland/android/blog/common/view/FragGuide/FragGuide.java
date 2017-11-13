package com.zhisland.android.blog.common.view.FragGuide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.view.AnimatedSlideGroup;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 向飞 on 2016/5/19.
 */
public class FragGuide extends FragBase {

    private static String TAG = "slide";
    private static String INK_STEP = "ink_guide_step_data";
    private static String INK_CREATOR = "ink_guide_step_creator";
    private static String INK_BACKGROUND = "ink_guide_background";

    private List<GuideStepItem> steps;//步骤数据
    private GuideStepViewCreator viewCreator;//步骤试图构造器
    private int bg;

    long delay = 500;
    long slideIn = 2000;
    long slideStay = 3000;
    long slideOut = 1200;

    private AnimatedSlideGroup group;

    public static void invoke(Context context, List<GuideStepItem> steps, GuideStepViewCreator viewCreator, int bg) {
        MLog.d(TAG, "invoke frag slide");
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.swipeBackEnable = false;
        param.clsFrag = FragGuide.class;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INK_STEP, (Serializable) steps);
        intent.putExtra(INK_CREATOR, (Serializable) viewCreator);
        intent.putExtra(INK_BACKGROUND, bg);
        context.startActivity(intent);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        steps = (List<GuideStepItem>) activity.getIntent().getSerializableExtra(INK_STEP);
        viewCreator = (GuideStepViewCreator) activity.getIntent().getSerializableExtra(INK_CREATOR);
        bg = activity.getIntent().getIntExtra(INK_BACKGROUND, R.id.invalidResId);
        if (steps == null || viewCreator == null) {
            throw new RuntimeException("引导步骤或者试图构造器不能为空");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MLog.d(TAG, "frag slide create view");
        group = new AnimatedSlideGroup(getActivity());

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        group.setLayoutParams(params);
        if (bg != R.id.invalidResId) {
            group.setBackgroundResource(bg);
        }
        return group;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MLog.d(TAG, "frag slide activity create");

        for (int i = 0, count = steps.size(); i < count; i++) {

            final GuideStepItem step = steps.get(i);
            final View view = viewCreator.createStepView(getActivity(), step);

            final boolean out = !(i == (count - 1));//最后一步不用再划出


            Observable.timer(delay + i * (slideIn + slideStay), TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            startOne(view, out);
                        }
                    });

        }

//
//        Observable.timer(delay, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        startOne("我们一生都在寻找…\n\n同伟大的智者一样/n我们都心怀希望…");
//                    }
//                });
//
//        Observable.timer(delay + slideIn + slideStay, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        startOne("“我多么希望在世界上有个小岛，\n上面居住的全是智慧和善良的人们”\n                                             —爱因斯坦");
//                    }
//                });
//
//        Observable.timer(delay + 2 * slideIn + 2 * slideStay, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        startOne("加入正和岛，\n帮您高效找到对的人");
//                    }
//                });
    }


    //开始一个滑动
    private void startOne(final View view, boolean out) {

        final AnimatedSlideGroup.SlideRange rangIn = new AnimatedSlideGroup.SlideRange(DensityUtil.getHeight(), DensityUtil.getHeight() / 3, slideIn);
        group.startSlide(view, rangIn);

        //不需要划出view则返回
        if (!out)
            return;

        final AnimatedSlideGroup.SlideRange rangOut = new AnimatedSlideGroup.SlideRange(DensityUtil.getHeight() / 3, 0 - 500, slideOut);
        rangOut.setStayInGroup(false);
        Observable.timer(slideIn + slideStay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                               @Override
                               public void call(Long aLong) {
                                   MLog.d(TAG, "start slide");
                                   group.startSlide(view, rangOut);
                               }
                           }
                );
    }

    @Override
    public String getPageName() {
        return "引导页";
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
