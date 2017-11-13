package com.zhisland.android.blog.find.controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.find.dto.FindType;
import com.zhisland.android.blog.find.dto.SearchHistory;
import com.zhisland.android.blog.find.view.FindSearchLayout;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 搜索页面
 * Created by Mr.Tui on 2016/5/17.
 */
public class ActSearch extends FragBaseActivity implements FindSearchLayout.CallBack, ISearchInitiated {

    public static final String INTENT_KEY_TYPE = "intent_key_type";

    /**
     * ActSearch搜索类型。既可搜索企业家，又可搜索资源需求，可在二者之间切换。
     */
    public static final int TYPE_ALL = 0;
    /**
     * ActSearch搜索类型。只可搜索企业家。
     */
    public static final int TYPE_BOSS = 1;
    /**
     * ActSearch搜索类型。只可搜索资源需求。
     */
    public static final int TYPE_RES = 2;

    /**
     * 启动搜索页面。
     *
     * @param type 搜索页面的搜索类型。
     *             备选值：ActSearch.TYPE_ALL 既可搜索企业家，又可搜索资源需求，可在二者之间切换。
     *             ActSearch.TYPE_BOSS 只可搜索企业家。
     *             ActSearch.TYPE_RES 只可搜索资源需求。
     */
    public static void invoke(Context context, int type) {
        Intent intent = new Intent(context, ActSearch.class);
        intent.putExtra(INTENT_KEY_TYPE, type);
        context.startActivity(intent);
    }

    @InjectView(R.id.searchView)
    FindSearchLayout searchView;

    FragResultBoss fragResultBoss;
    FragResultResource fragResultResource;
    FragTagBoss fragTagBoss;
    FragTagResource fragTagResource;

    /**
     * ActSearch搜索类型。
     */
    private int type;

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        type = getIntent().getIntExtra(INTENT_KEY_TYPE, TYPE_ALL);
        ButterKnife.inject(this);
        setSwipeBackEnable(true);
        getTitleBar().addBackButton();
        getTitleBar().setTitle(getTitleTxt());
        searchView.setCallBack(this);
        searchView.setSearchType(type);
        fragResultBoss = new FragResultBoss();
        fragResultBoss.setiSearchInitiated(this);
        fragResultResource = new FragResultResource();
        fragResultResource.setiSearchInitiated(this);
        fragTagBoss = new FragTagBoss();
        fragTagResource = new FragTagResource();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragResultBoss);
        ft.add(R.id.frag_container, fragResultResource);
        ft.add(R.id.frag_container, fragTagBoss);
        ft.add(R.id.frag_container, fragTagResource);
        ft.commit();
        if (type == TYPE_RES) {
            showFragTagRes();
        } else {
            showFragTagBoss();
        }

        handler.postDelayed(showKeyboardDelayed, 500);
    }

    Runnable showKeyboardDelayed = new Runnable() {
        @Override
        public void run() {
            SoftInputUtil.showKeyboard(getCurrentFocus());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(showKeyboardDelayed);
    }

    private String getTitleTxt() {
        if (type == ActSearch.TYPE_BOSS) {
            return "搜索企业家";
        } else if (type == ActSearch.TYPE_RES) {
            return "搜索资源需求";
        } else {
            return "搜索";
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
        return R.layout.act_search;
    }

    @Override
    public void toSearch(FindType type, String searchKey) {
        search(type, searchKey);
    }

    @Override
    public void onSearchTypeChanged(FindType type, String searchKey) {
        //当搜索类型切换时，清空之前的筛选，根据当前类型和当前关键字搜索。
        fragResultBoss.cleanStatue();
        fragResultResource.cleanStatue();
        search(type, searchKey);
    }

    /**
     * 搜索.根据searchKey和type进行搜索.
     */
    public void search(FindType type, String searchKey) {
        if (StringUtil.isNullOrEmpty(searchKey)) {
            fragResultBoss.cleanStatue();
            fragResultResource.cleanStatue();
            SoftInputUtil.showKeyboard(getCurrentFocus());
            //搜索关键字为空时，显示默认选项。
            if (type == FindType.boss) {
                showFragTagBoss();
                fragTagBoss.setHistoryView();
            } else if (type == FindType.resource) {
                showFragTagRes();
                fragTagResource.setHistoryView();
            }
        } else {
            searchView.setSearchWord(searchKey);
            SearchHistory.cacheData(type, searchKey);
            SoftInputUtil.hideInput(this);
            if (type == FindType.boss) {
                showFragResultBoss(searchKey);
                fragResultBoss.searchDelayed(300);
            } else if (type == FindType.resource) {
                showFragResultRes(searchKey);
                fragResultResource.searchDelayed(300);
            }
        }
    }

    private void showFragTagBoss() {
        hideAllFrag();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragTagBoss);
        ft.commitAllowingStateLoss();
    }

    private void showFragTagRes() {
        hideAllFrag();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragTagResource);
        ft.commitAllowingStateLoss();
    }

    private void showFragResultBoss(String searchKey) {
        hideAllFrag();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragResultBoss);
        ft.commitAllowingStateLoss();
    }

    private void showFragResultRes(String searchKey) {
        hideAllFrag();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragResultResource);
        ft.commitAllowingStateLoss();
    }

    private void hideAllFrag() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragTagBoss);
        ft.hide(fragTagResource);
        ft.hide(fragResultBoss);
        ft.hide(fragResultResource);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragResultBoss.onBackPressed()) {
            return;
        }
        if (fragResultResource.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public String provideSearchKey() {
        return searchView.getSearchWord();
    }
}
