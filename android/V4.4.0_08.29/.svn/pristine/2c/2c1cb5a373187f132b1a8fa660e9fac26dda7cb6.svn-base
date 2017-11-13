package com.zhisland.android.blog.info.view.impl.holder;

import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.view.impl.FragLinkEdit;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 资讯推荐提示气泡
 * Created by Mr.Tui on 2016/7/9.
 */
public class AddLinkPrompt {

    @InjectView(R.id.tvFirst)
    TextView tvFirst;

    @InjectView(R.id.tvSecond)
    TextView tvSecond;

    View llPrompt;

    public AddLinkPrompt(View view) {
        llPrompt = view;
        llPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragLinkEdit.TryInvoke(v.getContext());
            }
        });
        ButterKnife.inject(this, view);
    }

    public void show(String url) {
        tvFirst.setText("点击推荐的复制链");
        tvSecond.setText("接" + url);
        llPrompt.setVisibility(View.VISIBLE);
    }

    public void hide() {
        llPrompt.setVisibility(View.GONE);
    }
}
