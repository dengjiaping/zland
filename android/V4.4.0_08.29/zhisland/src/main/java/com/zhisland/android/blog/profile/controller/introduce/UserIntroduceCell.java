package com.zhisland.android.blog.profile.controller.introduce;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Mr.Tui on 2016/9/9.
 */
public class UserIntroduceCell {

    private final int LINE_MAX_COUNT = 4;

    private Context context;
    private Subscription hideDelayOb;

    @InjectView(R.id.tvContent)
    TextView tvContent;

    @InjectView(R.id.tvSeeAll)
    TextView tvSeeAll;

    public UserIntroduceCell(View view, Context context) {
        this.context = context;
        ButterKnife.inject(this, view);
        tvContent.setMaxLines(Integer.MAX_VALUE);
//        tvContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right,
//                                       int bottom, int oldLeft, int oldTop, int oldRight,
//                                       int oldBottom) {
//                if (oldTop == 0 && oldBottom == 0 && bottom != 0) {
//                    int lineCount = tvContent.getLineCount();
//                    if (lineCount > LINE_MAX_COUNT) {
//                        tvSeeAll.setVisibility(View.VISIBLE);
//                    } else {
//                        tvSeeAll.setVisibility(View.GONE);
//                    }
//                    tvContent.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            tvContent.setMaxLines(LINE_MAX_COUNT);
//                        }
//                    });
//                }
//            }
//        });
    }

    public void fill(String content) {
        tvContent.setText(content);
    }

    @OnClick(R.id.llRoot)
    void onItemClick() {
        tvContent.setMaxLines(Integer.MAX_VALUE);
        tvSeeAll.setVisibility(View.GONE);
    }

}
