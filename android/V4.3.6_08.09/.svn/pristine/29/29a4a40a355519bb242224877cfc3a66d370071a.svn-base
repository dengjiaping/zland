package com.zhisland.android.blog.find.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.find.dto.FilterItem;
import com.zhisland.android.blog.find.dto.FindType;
import com.zhisland.android.blog.find.dto.SearchFilter;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 搜索筛选View
 * Created by Mr.Tui on 2016/5/19.
 */
public class SearchFilterView extends LinearLayout {

    private SearchFilter searchFilter;
    private FindType findType;

    @InjectView(R.id.llUType)
    LinearLayout llUType;
    @InjectView(R.id.tvUType)
    TextView tvUType;
    @InjectView(R.id.tflUType)
    FilterView tflUType;

    @InjectView(R.id.llRType)
    LinearLayout llRType;
    @InjectView(R.id.tvRType)
    TextView tvRType;
    @InjectView(R.id.tflRType)
    FilterView tflRType;

    @InjectView(R.id.llArea)
    LinearLayout llArea;
    @InjectView(R.id.tvArea)
    TextView tvArea;
    @InjectView(R.id.tflArea)
    FilterView tflArea;

    TextView okBtn;

    public SearchFilterView(Context context) {
        super(context);
        init(context);
    }

    public SearchFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchFilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        View contentView = LayoutInflater.from(context).inflate(R.layout.content_search_filter, null);
        ButterKnife.inject(this, contentView);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        addView(contentView, lp);

        okBtn = new TextView(getContext());
        okBtn.setText("确定");
        okBtn.setGravity(Gravity.CENTER);
        int padding = DensityUtil.dip2px(12);
        okBtn.setPadding(padding, padding, padding, padding);
        okBtn.setTextColor(getResources().getColor(R.color.white));
        okBtn.setBackgroundResource(R.drawable.rect_bdc_clarge);
        DensityUtil.setTextSize(okBtn, R.dimen.txt_16);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int marginTop = DensityUtil.dip2px(13);
        int marginLeft = DensityUtil.dip2px(67);
        lp.setMargins(marginLeft, marginTop, marginLeft, marginTop);
        lp.weight = 0;
        addView(okBtn, lp);
    }

    /**
     * 设置筛选数据
     */
    public void setFilter(SearchFilter searchFilter, FindType findType) {
        this.searchFilter = searchFilter == null ? null : searchFilter.deepClone();
        this.findType = findType;
        fillUTypeView();
        fillRTypeView();
        fillAreaView();
    }

    /**
     * 设置会员类型View
     */
    private void fillUTypeView() {
        if (searchFilter == null || searchFilter.uType == null || searchFilter.uType.size() == 0) {
            llUType.setVisibility(View.GONE);
        } else {
            llUType.setVisibility(View.VISIBLE);
            FilterItem filterItem = getActiveUTypeItem();
            if (filterItem == null) {
                setTitleText(tvUType, getUTypeTitle(), "不限");
            } else {
                setTitleText(tvUType, getUTypeTitle(), filterItem.name);
            }
            tflUType.setFilterAdapter(new FilterView.FilterAdapter<FilterItem>(searchFilter.uType) {

                @Override
                public View getView(LinearLayout row, int position, FilterItem data) {
                    return makeFilterItemView(row, position, data);
                }

                @Override
                public void onItemClick(View Item, FilterItem data) {
                    //选项为单选
                    int valueBefore = data.active;
                    for (FilterItem filterItem : searchFilter.uType) {
                        filterItem.active = FilterItem.STATUS_ACTIVE_FALSE;
                    }
                    if (valueBefore == FilterItem.STATUS_ACTIVE_FALSE) {
                        data.active = FilterItem.STATUS_ACTIVE_TRUE;
                        setTitleText(tvUType, getUTypeTitle(), data.name);
                    } else {
                        setTitleText(tvUType, getUTypeTitle(), "不限");
                    }
                    //刷新每个Item的选中状态。
                    toRefreshItem();
                }

                @Override
                public void refreshView(View v, FilterItem data) {
                    fillItemView((TextView) v, data);
                }

            });
        }
    }

    private String getUTypeTitle() {
        if (findType == FindType.boss) {
            return "会员类型：";
        } else {
            return "发布人类型：";
        }
    }

    /**
     * 设置信息类别View
     */
    private void fillRTypeView() {
        if (searchFilter == null || searchFilter.rType == null || searchFilter.rType.size() == 0) {
            llRType.setVisibility(View.GONE);
        } else {
            llRType.setVisibility(View.VISIBLE);
            FilterItem filterItem = getActiveRTypeItem();
            if (filterItem == null) {
                setTitleText(tvRType, "信息类别：", "不限");
            } else {
                setTitleText(tvRType, "信息类别：", filterItem.name);
            }
            tflRType.setFilterAdapter(new FilterView.FilterAdapter<FilterItem>(searchFilter.rType) {

                @Override
                public View getView(LinearLayout row, int position, FilterItem data) {
                    return makeFilterItemView(row, position, data);
                }

                @Override
                public void onItemClick(View Item, FilterItem data) {
                    //选项为单选
                    int valueBefore = data.active;
                    for (FilterItem filterItem : searchFilter.rType) {
                        filterItem.active = FilterItem.STATUS_ACTIVE_FALSE;
                    }
                    if (valueBefore == FilterItem.STATUS_ACTIVE_FALSE) {
                        data.active = FilterItem.STATUS_ACTIVE_TRUE;
                        setTitleText(tvRType, "信息类别：", data.name);
                    } else {
                        setTitleText(tvRType, "信息类别：", "不限");
                    }
                    //刷新每个Item的选中状态。
                    toRefreshItem();
                }

                @Override
                public void refreshView(View v, FilterItem data) {
                    fillItemView((TextView) v, data);
                }

            });
        }
    }

    /**
     * 设置所在地区View
     */
    private void fillAreaView() {
        if (searchFilter == null || searchFilter.area == null || searchFilter.area.size() == 0) {
            llArea.setVisibility(View.GONE);
        } else {
            llArea.setVisibility(View.VISIBLE);
            FilterItem filterItem = getActiveAreaItem();
            if (filterItem == null) {
                setTitleText(tvArea, "所在地区：", "不限");
            } else {
                setTitleText(tvArea, "所在地区：", filterItem.name);
            }
            tflArea.setFilterAdapter(new FilterView.FilterAdapter<FilterItem>(searchFilter.area) {

                @Override
                public View getView(LinearLayout row, int position, FilterItem data) {
                    return makeFilterItemView(row, position, data);
                }

                @Override
                public void onItemClick(View v, FilterItem data) {
                    //选项为单选
                    int valueBefore = data.active;
                    for (FilterItem filterItem : searchFilter.area) {
                        filterItem.active = FilterItem.STATUS_ACTIVE_FALSE;
                    }
                    if (valueBefore == FilterItem.STATUS_ACTIVE_FALSE) {
                        data.active = FilterItem.STATUS_ACTIVE_TRUE;
                        setTitleText(tvArea, "所在地区：", data.name);
                    } else {
                        setTitleText(tvArea, "所在地区：", "不限");
                    }
                    //刷新每个Item的选中状态。
                    toRefreshItem();
                }

                @Override
                public void refreshView(View v, FilterItem data) {
                    fillItemView((TextView) v, data);
                }

            });
        }
    }

    /**
     * 设置titleView，value为绿色
     */
    private void setTitleText(TextView v, String name, String value) {
        SpannableStringBuilder style = new SpannableStringBuilder(name + value);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_dc)),
                name.length(), name.length() + value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        v.setText(style);
    }

    /**
     * 生成筛选项View
     */
    private View makeFilterItemView(View parent, int position, FilterItem item) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tag_text, null);
        textView.setTextColor(getContext().getResources().getColorStateList(R.color.sel_color_find_filter));
        DensityUtil.setTextSize(textView, R.dimen.txt_14);
        textView.setBackgroundResource(R.drawable.sel_btn_find_filter);
        int dpi10 = DensityUtil.dip2px(8);
        textView.setPadding(dpi10, dpi10, dpi10, dpi10);
        fillItemView(textView, item);
        return textView;
    }

    /**
     * 设置筛选项View
     */
    private void fillItemView(TextView textView, FilterItem item) {
        textView.setText(item.name);
        textView.setEnabled(item.enabled == FilterItem.STATUS_ENABLED_TRUE);
        textView.setSelected(item.active == FilterItem.STATUS_ACTIVE_TRUE);
    }

    public void setOnOkClickListener(OnClickListener onClickListener) {
        okBtn.setOnClickListener(onClickListener);
    }

    /**
     * 获取激活的用户筛选项
     */
    public FilterItem getActiveUTypeItem() {
        if (searchFilter == null || searchFilter.uType == null) {
            return null;
        }
        for (FilterItem filterItem : searchFilter.uType) {
            if (filterItem.active == FilterItem.STATUS_ACTIVE_TRUE) {
                return filterItem;
            }
        }
        return null;
    }

    /**
     * 获取激活的资源类型筛选项
     */
    public FilterItem getActiveRTypeItem() {
        if (searchFilter == null || searchFilter.rType == null) {
            return null;
        }
        for (FilterItem filterItem : searchFilter.rType) {
            if (filterItem.active == FilterItem.STATUS_ACTIVE_TRUE) {
                return filterItem;
            }
        }
        return null;
    }

    /**
     * 获取激活的地区筛选项
     */
    public FilterItem getActiveAreaItem() {
        if (searchFilter == null || searchFilter.area == null) {
            return null;
        }
        for (FilterItem filterItem : searchFilter.area) {
            if (filterItem.active == FilterItem.STATUS_ACTIVE_TRUE) {
                return filterItem;
            }
        }
        return null;
    }

    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

}
