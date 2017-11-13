package com.zhisland.android.blog.find.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.zhisland.android.blog.find.api.TaskGetBossCustom;
import com.zhisland.android.blog.find.api.TaskGetBossHot;
import com.zhisland.android.blog.find.dto.FindType;
import com.zhisland.android.blog.find.dto.SearchHistory;
import com.zhisland.android.blog.find.view.SearchHistoryView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 搜索页面，搜索企业家时的默认选择fragment（个性标签，热门标签，历史记录）
 * Created by Mr.Tui on 2016/5/17.
 */
public class FragTagBoss extends FragBase implements SearchHistoryView.OnHistoryClickListener {

    public static final String PAGE_NAME = "FragTagBoss";

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    /**
     * 个性标签随机背景
     * */
    private final int[] customTagBgPool = new int[]{R.drawable.rect_tag_bg1,
            R.drawable.rect_tag_bg2, R.drawable.rect_tag_bg3, R.drawable.rect_tag_bg4,
            R.drawable.rect_tag_bg6, R.drawable.rect_tag_bg7};

    @InjectView(R.id.llCustomTag)
    LinearLayout llCustomTag;
    @InjectView(R.id.tflCustomTag)
    TagFlowLayout tflCustomTag;

    @InjectView(R.id.llHotTag)
    LinearLayout llHotTag;
    @InjectView(R.id.tflHotTag)
    TagFlowLayout tflHotTag;

    @InjectView(R.id.llHistoryTag)
    LinearLayout llHistoryTag;
    @InjectView(R.id.llHistoryContent)
    SearchHistoryView searchHistoryView;

    @InjectView(R.id.vline)
    View vline;

    /**
     * 热门标签数组
     * */
    ArrayList<ZHDicItem> hotTags;
    /**
     * 个性标签数组
     * */
    ArrayList<ZHDicItem> customTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView root = (ScrollView) inflater.inflate(R.layout.frag_tag_boss, null);
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
        refreshHotData();
        refreshCustomData();
    }

    private void initView() {
        searchHistoryView.setOnHistoryClickListener(this);
        setHotTagView();
        setCustomTagView();
        setHistoryView();
    }

    /**
     * 刷新热门标签数据
     */
    private void refreshHotData() {
        ZHApis.getFindApi().getBossHot(getActivity(), new TaskCallback<ArrayList<ZHDicItem>>() {
            @Override
            public void onSuccess(ArrayList<ZHDicItem> content) {
                setHotTagView();
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    /**
     * 刷新个性标签数据
     */
    private void refreshCustomData() {
        ZHApis.getFindApi().getBossCustom(getActivity(), new TaskCallback<ArrayList<ZHDicItem>>() {
            @Override
            public void onSuccess(ArrayList<ZHDicItem> content) {
                setCustomTagView();
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    /**
     * 为个性标签设置随机背景,背景的id暂时放到ZHDicItem的code字段中,
     * 如果以后需要使用code字段,可以把随机背景id放到一个list中保存.
     */
    private void setCustomTagBg() {
        if (customTags == null) {
            return;
        }
        int beforeIndex = -1;
        for (ZHDicItem tag : customTags) {
            int index = new Random().nextInt(customTagBgPool.length);
            while (index == beforeIndex) {
                index = new Random().nextInt(customTagBgPool.length);
            }
            beforeIndex = index;
            tag.code = customTagBgPool[index];
        }
    }

    /**
     * 设置个性标签View
     */
    private void setCustomTagView() {
        customTags = TaskGetBossCustom.getCacheData();
        setCustomTagBg();
        if (customTags == null || customTags.size() == 0) {
            llCustomTag.setVisibility(View.GONE);
        } else {
            llCustomTag.setVisibility(View.VISIBLE);
            TagAdapter customAdapter = new TagAdapter<ZHDicItem>(customTags) {
                @Override
                public View getView(
                        com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
                        int position, ZHDicItem tag) {
                    TextView textView = new TextView(getActivity());
                    textView.setTextColor(getResources().getColor(
                            R.color.white));
                    DensityUtil.setTextSize(textView, R.dimen.txt_14);
                    textView.setBackgroundResource(tag.code);
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
            tflCustomTag.setOnTagClickListener(customTagClickListener);
            tflCustomTag.setAdapter(customAdapter);
        }
        setLineVisibility();
    }

    TagFlowLayout.OnTagClickListener customTagClickListener = new TagFlowLayout.OnTagClickListener() {

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
     * 根据个性标签和热门标签的显示情况,决定是否显示二者之间的线.
     */
    private void setLineVisibility() {
        if (llCustomTag.getVisibility() == View.VISIBLE && llHotTag.getVisibility() == View.VISIBLE) {
            vline.setVisibility(View.VISIBLE);
        } else {
            vline.setVisibility(View.GONE);
        }
    }

    /**
     * 设置热门关键词View
     */
    private void setHotTagView() {
        hotTags = TaskGetBossHot.getCacheData();
        if (hotTags == null || hotTags.size() == 0) {
            llHotTag.setVisibility(View.GONE);
        } else {
            llHotTag.setVisibility(View.VISIBLE);
            TagAdapter hotAdapter = new TagAdapter<ZHDicItem>(hotTags) {
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
            tflHotTag.setOnTagClickListener(hotTagClickListener);
            tflHotTag.setAdapter(hotAdapter);
        }
        setLineVisibility();
    }

    TagFlowLayout.OnTagClickListener hotTagClickListener = new TagFlowLayout.OnTagClickListener() {

        @Override
        public boolean onTagClick(View view, int position, FlowLayout parent) {
            if (hotTags != null && hotTags.size() > position) {
                ZHDicItem tag = hotTags.get(position);
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
        ArrayList<SearchHistory> historyList = SearchHistory.getCacheData(FindType.boss);
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
            ((ActSearch) acitity).search(FindType.boss, word);
        }
    }
}
