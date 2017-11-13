package com.zhisland.android.blog.find.controller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.find.dto.FilterCategory;
import com.zhisland.android.blog.find.dto.FilterItem;
import com.zhisland.android.blog.find.dto.FindType;
import com.zhisland.android.blog.find.dto.SearchFilter;
import com.zhisland.android.blog.find.dto.SearchResult;
import com.zhisland.android.blog.find.view.ResourceHolder;
import com.zhisland.android.blog.find.view.SearchFilterView;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 资源需求搜索结果Fragment
 * Created by Mr.Tui on 2016/5/17.
 */
public class FragResultResource extends FragPullList<Resource> {

    public static final String PAGE_NAME = "DiscoverySupplyDemandSearch";
    public static final String INTENT_KEY_RTYPE = "intent_key_rtype";

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 搜索筛选项
     */
    private SearchFilter searchFilter;

    /**
     * 搜索发起者
     */
    private ISearchInitiated iSearchInitiated;

    /**
     * 搜索结果缓存key。
     * 在搜索页面不需要缓存，该值为null；
     * 在全部企业家和资源需求页面，需要缓存没有筛选项时的结果数据，以备下次进入时默认显示。
     */
    private String cacheKey = null;

    /**
     * 搜索接口的context。每次搜索时new一个实例，搜索结束后置空。页面隐藏时cancel task。
     */
    private Object apiKey;

    @InjectView(R.id.llFilter)
    LinearLayout llFilter;

    @InjectView(R.id.tvRType)
    TextView tvRType;

    @InjectView(R.id.ivRType)
    ImageView ivRType;

    @InjectView(R.id.tvUType)
    TextView tvUType;

    @InjectView(R.id.ivUType)
    ImageView ivUType;

    @InjectView(R.id.tvArea)
    TextView tvArea;

    @InjectView(R.id.ivArea)
    ImageView ivArea;

    /**
     * 资源需求类型筛选项，从外部传进来的
     * */
    String initialRType;

    @InjectView(R.id.filterView)
    SearchFilterView filterView;

    /**
     * 筛选页弹出的起因是哪个筛选类型。用来实现筛选title右侧小三角的变化。
     */
    FilterCategory filterCategory;

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().needRefreshOnResume = false;
        getPullProxy().setAdapter(new ResourceAdapter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.frag_result_resource, null);
        RelativeLayout rlResult = (RelativeLayout) root.findViewById(R.id.rlResult);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        View pullList = super.onCreateView(inflater, container,
                savedInstanceState);
        pullList.setLayoutParams(lp);
        rlResult.addView(pullList, 0);
        ButterKnife.inject(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(refreshRunnable);
        if (apiKey != null) {
            ZHApis.getFindApi().cancelTask(apiKey);
            apiKey = null;
        }
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && apiKey != null) {
            ZHApis.getFindApi().cancelTask(apiKey);
            apiKey = null;
            getPullProxy().onRefreshFinished();
        }
    }

    /**
     * 设置筛选条View
     */
    private void setFilterView(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
        filterView.setFilter(searchFilter, FindType.resource);
        setFilterViewColor();
    }

    /**
     * 设置筛选View控制条的颜色和右侧三角
     */
    private void setFilterViewColor() {
        FilterItem activeUType = null;
        FilterItem activeRType = null;
        FilterItem activeArea = null;
        if (searchFilter != null) {
            activeUType = searchFilter.getActiveUType();
            activeRType = searchFilter.getActiveRType();
            activeArea = searchFilter.getActiveArea();
        }
        if (activeUType == null && activeRType == null && activeArea == null && getPullProxy().getAdapter().getCount() <= 0) {
            //如果没有搜索结果且没有进行筛选，不显示筛选条
            llFilter.setVisibility(View.GONE);
        } else {
            llFilter.setVisibility(View.VISIBLE);
            setColorAndArrow(tvUType, ivUType, activeUType != null, filterCategory == FilterCategory.uType);
            setColorAndArrow(tvRType, ivRType, activeRType != null, filterCategory == FilterCategory.rType);
            setColorAndArrow(tvArea, ivArea, activeArea != null, filterCategory == FilterCategory.area);
        }
    }

    private void setColorAndArrow(TextView textView, ImageView imageView, boolean green, boolean up) {
        if (green) {
            textView.setTextColor(getResources().getColor(R.color.color_dc));
            if (up) {
                imageView.setImageResource(R.drawable.img_search_arrow_green_up);
            } else {
                imageView.setImageResource(R.drawable.img_search_arrow_green_down);
            }
        } else {
            textView.setTextColor(getResources().getColor(R.color.color_f1));
            if (up) {
                imageView.setImageResource(R.drawable.img_arrow_gray_up);
            } else {
                imageView.setImageResource(R.drawable.img_arrow_gray_down);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.transparent));
        internalView.setFastScrollEnabled(false);
        internalView.setDividerHeight(0);
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        internalView.setHeaderDividersEnabled(false);
        internalView.setFooterDividersEnabled(false);
        internalView.setBackgroundColor(getResources().getColor(
                R.color.transparent));

        EmptyView ev = new EmptyView(getActivity());
        ev.setImgRes(R.drawable.img_emptybox);
        ev.setPrompt("没有找到相关结果");
        ev.setBtnVisibility(View.INVISIBLE);
        getPullProxy().getPullView().setEmptyView(ev);

        initialRType = getActivity().getIntent().getStringExtra(INTENT_KEY_RTYPE);
        if (cacheKey != null && StringUtil.isNullOrEmpty(initialRType)) {
            //如果cacheKey不为空，是全部企业家或资源需求页面，取缓存设置到view中
            Object obj = DBMgr.getMgr().getCacheDao().get(cacheKey);
            if (obj != null && obj instanceof SearchResult) {
                getPullProxy().onLoadSucessfully((SearchResult<Resource>) obj);
                setFilterView(((SearchResult<Resource>) obj).filter);
            }
        }
    }

    private void initView() {
        filterView.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点确定的时候将筛选View的选项赋值过来，进行搜索。
                searchFilter = filterView.getSearchFilter();
                hideFilterView();
                setFilterViewColor();
                getPullProxy().pullDownToRefresh(true);
            }
        });
    }

    /**
     * 对外提供的延时搜索方法。
     * 延迟原因：防止fragment没显示出来时，下拉效果出不来。
     * @param delayed 延迟时间，单位毫秒
     */
    public synchronized void searchDelayed(int delayed) {
        //正在搜索,上次搜索未执行完
        if (apiKey != null) {
            return;
        }
        if (filterView.getVisibility() == View.VISIBLE) {
            //如果筛选View显示时，不进行搜索。
            return;
        }
        handler.removeCallbacks(refreshRunnable);
        handler.postDelayed(refreshRunnable, delayed);
    }

    Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            getPullProxy().pullDownToRefresh(true);
        }
    };

    /**
     * 清除筛选状态
     */
    public void cleanStatue() {
        hideFilterView();
        getPullProxy().getAdapter().clearItems();
        setFilterView(null);
    }

    @Override
    public void loadNormal() {
        refreshKeyword();
        search(keyword, null);
    }

    /**
     * 刷新当前的搜索关键字
     */
    private void refreshKeyword() {
        if (iSearchInitiated != null) {
            keyword = iSearchInitiated.provideSearchKey();
        }
    }

    public void hideFilterView() {
        filterCategory = null;
        filterView.setVisibility(View.GONE);
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        search(keyword, nextId);
    }

    /**
     * 根据searchWord和maxId，获取搜索结果
     */
    private synchronized void search(final String searchWord, final String maxId) {
        if (apiKey != null) {
            return;
        }
        hideFilterView();
        setFilterViewColor();
        FilterItem activeUType = null;
        FilterItem activeRType = null;
        FilterItem activeArea = null;
        if (searchFilter != null) {
            activeUType = searchFilter.getActiveUType();
            activeRType = searchFilter.getActiveRType();
            activeArea = searchFilter.getActiveArea();
        }
        String codeUType = activeUType == null ? null : activeUType.code;
        String codeRType = activeRType == null ? null : activeRType.code;
        String codeArea = activeArea == null ? null : activeArea.code;
        apiKey = new Object();
        if(!StringUtil.isNullOrEmpty(initialRType)){
            codeRType = initialRType;
        }
        final boolean noFilter = codeUType == null && codeRType == null && codeArea == null;
        ZHApis.getFindApi().searchRes(apiKey, maxId, searchWord, codeUType, codeRType, codeArea, new TaskCallback<SearchResult<Resource>>() {
            @Override
            public void onSuccess(SearchResult<Resource> content) {
                if(!StringUtil.isNullOrEmpty(initialRType)){
                    initialRType = null;
                }
                //cacheKey在搜索act为空，在搜索企业家act和搜索资源act不为空。在搜索企业家act和搜索资源act、没有筛选项、maxid为空的情况下缓存数据。
                if (cacheKey != null && noFilter && maxId == null && StringUtil.isNullOrEmpty(searchWord)) {
                    DBMgr.getMgr().getCacheDao().set(cacheKey, content);
                }
                getPullProxy().onLoadSucessfully(content);
                setFilterView(content.filter);
            }

            @Override
            public void onFailure(Throwable error) {
                getPullProxy().onLoadFailed(error);
            }


            @Override
            public void onFinish() {
                super.onFinish();
                apiKey = null;
            }
        });
    }

    @OnClick(R.id.llUType)
    void uTypeClick() {
        if (getPullProxy().getPullView().isRefreshing()) {
            //如果正在搜索，不能进行筛选
            return;
        }
        if (filterCategory == null) {
            filterCategory = FilterCategory.uType;
            filterView.setFilter(searchFilter, FindType.boss);
            filterView.setVisibility(View.VISIBLE);
        } else if (filterCategory == FilterCategory.uType) {
            hideFilterView();
        } else {
            filterCategory = FilterCategory.uType;
        }
        setFilterViewColor();
    }

    @OnClick(R.id.llRType)
    void rTypeClick() {
        if (getPullProxy().getPullView().isRefreshing()) {
            //如果正在搜索，不能进行筛选
            return;
        }
        if (filterCategory == null) {
            filterCategory = FilterCategory.rType;
            filterView.setFilter(searchFilter, FindType.boss);
            filterView.setVisibility(View.VISIBLE);
        } else if (filterCategory == FilterCategory.rType) {
            hideFilterView();
        } else {
            filterCategory = FilterCategory.rType;
        }
        setFilterViewColor();
    }

    @OnClick(R.id.llArea)
    void areaClick() {
        if (getPullProxy().getPullView().isRefreshing()) {
            //如果正在搜索，不能进行筛选
            return;
        }
        if (filterCategory == null) {
            filterCategory = FilterCategory.area;
            filterView.setFilter(searchFilter, FindType.boss);
            filterView.setVisibility(View.VISIBLE);
        } else if (filterCategory == FilterCategory.area) {
            hideFilterView();
        } else {
            filterCategory = FilterCategory.area;
        }
        setFilterViewColor();
    }

    class ResourceAdapter extends BaseListAdapter<Resource> {

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (getCount() == 0) {
                getPullProxy().getPullView().setBackgroundColor(getResources().getColor(R.color.color_bg1));
            } else {
                getPullProxy().getPullView().setBackgroundColor(getResources().getColor(R.color.color_bg2));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ResourceHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_search_resource, null);
                holder = new ResourceHolder(convertView, getActivity());
                convertView.setTag(holder);
            } else {
                holder = (ResourceHolder) convertView.getTag();
            }
            holder.fill(getItem(position), keyword);
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

    @Override
    public boolean onBackPressed() {
        if (filterView.getVisibility() == View.VISIBLE) {
            hideFilterView();
            setFilterViewColor();
            return true;
        }
        return super.onBackPressed();
    }

    /**
     * 设置搜索发起者
     */
    public void setiSearchInitiated(ISearchInitiated iSearchInitiated) {
        this.iSearchInitiated = iSearchInitiated;
    }

    /**
     * 设置搜索结果缓存key。
     * 在搜索页面不需要缓存，该值为null；
     * 在全部企业家和资源需求页面，需要缓存没有筛选项时的结果数据，以备下次进入时默认显示。
     */
    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}