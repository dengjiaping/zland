package com.zhisland.android.blog.find.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.view.flowlayout.FlowLayout;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.find.api.TaskGetResCategory;
import com.zhisland.android.blog.find.dto.FindType;
import com.zhisland.android.blog.find.dto.SearchHistory;
import com.zhisland.android.blog.find.view.SearchHistoryView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 搜索页面，搜索资源需求时的默认选择fragment（类别标签，历史记录）
 * Created by Mr.Tui on 2016/5/17.
 */
public class FragTagResource extends FragBase implements SearchHistoryView.OnHistoryClickListener {

    public static final String PAGE_NAME = "FragTagResource";

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    @InjectView(R.id.llCategoryTag)
    LinearLayout llCategoryTag;
    @InjectView(R.id.tflCategoryTag)
    TagFlowLayout tflCategoryTag;

    @InjectView(R.id.llHistoryTag)
    LinearLayout llHistoryTag;
    @InjectView(R.id.llHistoryContent)
    SearchHistoryView searchHistoryView;

    /**
     * 类别标签数组
     */
    ArrayList<ZHDicItem> customTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView root = (ScrollView) inflater.inflate(R.layout.frag_tag_resource, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        initView();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshCategoryData();
    }

    private void initView() {
        searchHistoryView.setOnHistoryClickListener(this);
        setCategoryTagView();
        setHistoryView();
    }

    /**
     * 刷新个性标签数据
     */
    private void refreshCategoryData() {
        ZHApis.getFindApi().getResCategory(getActivity(), new TaskCallback<ArrayList<ZHDicItem>>() {
            @Override
            public void onSuccess(ArrayList<ZHDicItem> content) {
                setCategoryTagView();
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    /**
     * 设置类别标签View
     */
    private void setCategoryTagView() {
        customTags = TaskGetResCategory.getCacheData();
        if (customTags == null || customTags.size() == 0) {
            llCategoryTag.setVisibility(View.GONE);
        } else {
            llCategoryTag.setVisibility(View.VISIBLE);
            TagAdapter hotAdapter = new TagAdapter<ZHDicItem>(customTags) {
                @Override
                public View getView(
                        com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
                        int position, ZHDicItem tag) {
                    TextView textView = new TextView(getActivity());
                    textView.setTextColor(getResources().getColor(
                            R.color.color_f1));
                    DensityUtil.setTextSize(textView, R.dimen.txt_14);
                    textView.setBackgroundResource(R.drawable.rect_sf2_clarge);
                    int dpi7 = DensityUtil.dip2px(7);
                    int dpi12 = DensityUtil.dip2px(14);
                    textView.setPadding(dpi12, dpi7, dpi12, dpi7);
                    textView.setText(tag.name);
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                            ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                            ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                    params.rightMargin = DensityUtil.dip2px(9);
                    params.topMargin = DensityUtil.dip2px(12);
                    textView.setLayoutParams(params);
                    return textView;
                }
            };
            tflCategoryTag.setOnTagClickListener(categoryTagClickListener);
            tflCategoryTag.setAdapter(hotAdapter);
        }
    }

    TagFlowLayout.OnTagClickListener categoryTagClickListener = new TagFlowLayout.OnTagClickListener() {

        @Override
        public boolean onTagClick(View view, int position, FlowLayout parent) {
            if (customTags != null && customTags.size() > position) {
                ZHDicItem tag = customTags.get(position);
                if (tag != null && !StringUtil.isNullOrEmpty(tag.name)) {
                    goToSearch(tag.name);
                }
            }
            return false;
        }
    };

    /**
     * 设置历史搜索View
     */
    public void setHistoryView() {
        ArrayList<SearchHistory> historyList = SearchHistory.getCacheData(FindType.resource);
        if (historyList == null || historyList.size() == 0) {
            llHistoryTag.setVisibility(View.GONE);
        } else {
            llHistoryTag.setVisibility(View.VISIBLE);
            searchHistoryView.setHistory(historyList);
        }
    }

    @Override
    public void onItemClick(int index, SearchHistory searchHistory) {
        if (searchHistory != null && !StringUtil.isNullOrEmpty(searchHistory.searchWord)) {
            goToSearch(searchHistory.searchWord);
        }
    }

    private void goToSearch(String word) {
        Activity acitity = getActivity();
        if (acitity != null && acitity instanceof ActSearch) {
            ((ActSearch) acitity).search(FindType.resource, word);
        }
    }
}