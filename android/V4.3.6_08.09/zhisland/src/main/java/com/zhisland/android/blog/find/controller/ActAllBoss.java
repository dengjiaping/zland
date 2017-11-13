package com.zhisland.android.blog.find.controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.lib.component.act.TitleType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 全部企业家页面
 * Created by Mr.Tui on 2016/5/17.
 */
public class ActAllBoss extends FragBaseActivity implements ISearchInitiated {

    public static void invoke(Context context) {
        Intent intent = new Intent(context, ActAllBoss.class);
        context.startActivity(intent);
    }

    FragResultBoss fragResultBoss;

    @InjectView(R.id.tvSearch)
    TextView tvSearch;

    boolean isFirstResume = true;

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        ButterKnife.inject(this);
        setSwipeBackEnable(true);
        getTitleBar().addBackButton();
        getTitleBar().setTitle("全部企业家");
        tvSearch.setText(getString(R.string.find_people_search_hint));
        fragResultBoss = new FragResultBoss();
        fragResultBoss.setiSearchInitiated(this);
        fragResultBoss.setCacheKey(this.getClass().getName());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragResultBoss);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            fragResultBoss.searchDelayed(700);
        }
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        if (tagId == TitleBarProxy.TAG_BACK) {
            finish();
            return;
        }
        super.onTitleClicked(view, tagId);
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

    @Override
    protected int layResId() {
        return R.layout.act_all_boss;
    }

    @Override
    public void onBackPressed() {
        if (fragResultBoss.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @OnClick(R.id.llSearch)
    void onSearchClick() {
        fragResultBoss.hideFilterView();
        ActSearch.invoke(this, ActSearch.TYPE_BOSS);
    }

    @Override
    public String provideSearchKey() {
        return "";
    }
}
