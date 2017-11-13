package com.zhisland.android.blog.find.controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.profile.controller.resource.FragUserResource;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 资源需求页面
 * Created by Mr.Tui on 2016/5/17.
 */
public class ActAllRes extends FragBaseActivity implements ISearchInitiated {

    public static final String INTENT_KEY_KEYWORD = "intent_key_keyword";
    private static final int TAG_ADD = 80;

    public static void invoke(Context context,String rType,String keyword) {
        Intent intent = new Intent(context, ActAllRes.class);
        if (!StringUtil.isNullOrEmpty(rType)) {
            intent.putExtra(FragResultResource.INTENT_KEY_RTYPE, rType);
        }
        if (!StringUtil.isNullOrEmpty(keyword)) {
            intent.putExtra(INTENT_KEY_KEYWORD, keyword);
        }
        context.startActivity(intent);
    }

    FragResultResource fragResultResource;

    @InjectView(R.id.tvSearch)
    TextView tvSearch;

    boolean isFirstResume = true;

    String keyword = "";

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        ButterKnife.inject(this);
        setSwipeBackEnable(true);
        getTitleBar().addBackButton();
        getTitleBar().setTitle("全部资源需求");
        TextView addBtn = TitleCreator.Instance().createTextButton(this, "添加",
                R.color.color_dc);
        getTitleBar().addRightTitleButton(addBtn, TAG_ADD);
        keyword = getIntent().getStringExtra(INTENT_KEY_KEYWORD);
        if (!StringUtil.isNullOrEmpty(keyword)) {
            tvSearch.setText(keyword);
            tvSearch.setTextColor(getResources().getColor(R.color.color_f1));
        } else {
            tvSearch.setText(getResources().getString(R.string.find_resource_search_hint));
        }
        fragResultResource = new FragResultResource();
        fragResultResource.setiSearchInitiated(this);
        fragResultResource.setCacheKey(this.getClass().getName());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragResultResource);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            fragResultResource.searchDelayed(700);
        }
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        if (tagId == TitleBarProxy.TAG_BACK) {
            finish();
            return;
        } else if (tagId == TAG_ADD) {
            ZhislandApplication.trackerClickEvent(fragResultResource.getPageName(), TrackerAlias.CLICK_ADD_DEMAND_SUPPLY);
            FragUserResource.invoke(this, null);
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
        if (fragResultResource.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @OnClick(R.id.llSearch)
    void onSearchClick() {
        fragResultResource.hideFilterView();
        ActSearch.invoke(this, ActSearch.TYPE_RES);
    }

    @Override
    public String provideSearchKey() {
        return keyword;
    }
}
