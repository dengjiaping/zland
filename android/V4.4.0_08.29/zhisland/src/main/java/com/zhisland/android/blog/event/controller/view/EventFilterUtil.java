package com.zhisland.android.blog.event.controller.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.view.flowlayout.FlowLayout;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 活动筛选 popup window
 */
public class EventFilterUtil implements View.OnClickListener, TagFlowLayout.OnTagClickListener {

    //默认的活动类型文案
    private static final String TXT_CATEGORY_DEFAULT = "活动筛选";
    private static final String TXT_ALL_EVENT = "全部活动";
    private static final String TXT_OFFICIAL = "官方";
    // arrow 图标方向类型
    private static final int TYPE_ARROW_UP = 1;
    private static final int TYPE_ARROW_DOWN = 2;

    @InjectView(R.id.llEventFilter)
    LinearLayout llEventFilter;
    @InjectView(R.id.tvEventCategory)
    TextView tvEventCategory;
    @InjectView(R.id.tvEventOrder)
    TextView tvEventOrder;

    // 当前选择的活动类型
    private int curEventType = Event.CATEGORY_ALL;
    private String curEventTag = TXT_CATEGORY_DEFAULT;
    // 上一个选择的活动类型
    private int lastEventType;
    private String lastEventTag;

    private PopupWindow popCategory;
    private TextView tvTagAll;
    private TextView tvTagOfficial;

    // 活动标签position
    private int eventTagPosition;

    private PopupWindow popOrder;
    // 已选择的排序规则
    private String selectOrder;
    // 上一次选择的排序规则
    private String lastSelectOrder;

    private Context context;
    // 筛选监听 listener
    private EventFilterListener listener;

    public static interface EventFilterListener{
        void onClickFilterListener();
    }

    public EventFilterUtil(Context context, EventFilterListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * 获取 活动筛选 view
     */
    public View getFilterView() {
        View filterView = LayoutInflater.from(context).inflate(R.layout.view_event_filter, null);
        ButterKnife.inject(this, filterView);
        setCategoryText(curEventTag, TYPE_ARROW_DOWN);
        setOrderText(TYPE_ARROW_DOWN);
        return filterView;
    }

    /**
     * 获取当前选中的活动类型 id
     */
    public int getCurEventType() {
        return curEventType;
    }

    /**
     * 获取当前选中的活动标签 名字
     */
    public String getCurEventTag() {
        return curEventTag;
    }

    /**
     * 获取当前排序规则
     */
    public String getCurOrderId() {
        String result = "";
        ArrayList<ZHDicItem> eventOrders = getEventOrders();
        if (StringUtil.isNullOrEmpty(selectOrder)) {
            // 取默认第一个
            ZHDicItem dicItem = eventOrders.get(0);
            result = dicItem.key;
        } else {
            for (ZHDicItem dicItem : eventOrders) {
                if (StringUtil.isEquals(dicItem.name, selectOrder)) {
                    result = dicItem.key;
                    break;
                }
            }
        }
        return result;
    }

    @OnClick(R.id.llEventCategory)
    void onClickCategory() {
        //点击活动类型
        showCategory();
    }

    @OnClick(R.id.llEventOrder)
    void onClickOrder() {
        //点击活动排序
        showOrder();
    }

    /**
     * 活动类型 Dialog
     */
    public void showCategory() {
        if (popCategory != null && popCategory.isShowing()) {
            popCategory.dismiss();
            return;
        }
        if (popCategory == null) {
            popCategory = createFilterDialog();
        }
        switch (curEventType) {
            case Event.CATEGORY_ALL:
                tvTagAll.setSelected(true);
                tvTagOfficial.setSelected(false);
                tagAdapter.clearChecked();
                break;
            case Event.CATEGORY_TYPE_OFFICIAL:
                tvTagAll.setSelected(false);
                tvTagOfficial.setSelected(true);
                tagAdapter.clearChecked();
                break;
            default:
                tvTagAll.setSelected(false);
                tvTagOfficial.setSelected(false);
                tagAdapter.clearChecked();
                tagAdapter.setSelectedList(eventTagPosition);
                break;
        }
        setCategoryText(curEventTag, TYPE_ARROW_UP);
        popCategory.showAsDropDown(llEventFilter);
    }

    /**
     * 创建 标签选择dialog
     */
    private PopupWindow createFilterDialog() {
        //============= 全部活动 和 官方活动 view ===============
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.pop_event_category, null);
        tvTagAll = (TextView) rootView.findViewById(R.id.tagAll);
        tvTagAll.setText(TXT_ALL_EVENT);
        tvTagAll.setTextColor(context.getResources().getColorStateList(R.color.sel_font_color_green));
        tvTagAll.setOnClickListener(this);
        tvTagOfficial = (TextView) rootView.findViewById(R.id.tagOffical);
        tvTagOfficial.setText(TXT_OFFICIAL);
        tvTagOfficial.setTextColor(context.getResources().getColorStateList(R.color.sel_font_color_green));
        tvTagOfficial.setOnClickListener(this);
        //============= 全部活动 和 官方活动 view ===============

        //============= 其它活动类型 标签 view===============
        TagFlowLayout flowLayout = (TagFlowLayout) rootView.findViewById(R.id.id_flowlayout);
        flowLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        flowLayout.setPadding(DensityUtil.dip2px(20), DensityUtil.dip2px(5), DensityUtil.dip2px(10), DensityUtil.dip2px(30));
        flowLayout.setMaxSelectCount(1);
        flowLayout.setOnTagClickListener(this);
        flowLayout.setAdapter(tagAdapter);
        //============= 其它活动类型 标签 view===============

        //============= pop剩余填充 view ===============
        View viewTransparent = rootView.findViewById(R.id.viewTransparent);
        viewTransparent.setOnClickListener(this);
        //============= pop剩余填充 view ===============

        rootView.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        popCategory = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popCategory.setBackgroundDrawable(new BitmapDrawable());
        popCategory.setOutsideTouchable(true);
        popCategory.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                setCategoryText(curEventTag, TYPE_ARROW_DOWN);
            }
        });
        setCategoryText(curEventTag, TYPE_ARROW_UP);
        return popCategory;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 点击全部活动
            case R.id.tagAll:
                setLastTag();
                curEventType = Event.CATEGORY_ALL;
                curEventTag = TXT_ALL_EVENT;
                onClickEventCategory();
                popCategory.dismiss();
                break;
            // 点击官方活动
            case R.id.tagOffical:
                setLastTag();
                curEventType = Event.CATEGORY_TYPE_OFFICIAL;
                curEventTag = TXT_OFFICIAL;
                onClickEventCategory();
                popCategory.dismiss();
                break;
            // 点击popwindow 底部填充view
            case R.id.viewTransparent:
                popCategory.dismiss();
                break;
            case R.id.viewOrderFill:
                popOrder.dismiss();
                break;
        }
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        setLastTag();
        curEventType = Event.CATEGORY_TYPE_TAG;
        curEventTag = getEventTags().get(position);
        eventTagPosition = position;
        onClickEventCategory();
        popCategory.dismiss();
        return false;
    }

    /**
     * 设置上一次点击的 tag
     */
    private void setLastTag() {
        this.lastEventType = curEventType;
        this.lastEventTag = curEventTag;
    }

    /**
     * 标签 adapter
     */
    TagAdapter<String> tagAdapter = new TagAdapter<String>(getEventTags()) {
        @Override
        public View getView(FlowLayout parent, int position, String tag) {
            TextView textView = (TextView) LayoutInflater.from(
                    context).inflate(R.layout.tag_text, null);
            int tagHeight = context.getResources().getDimensionPixelSize(
                    R.dimen.tag_height);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT, tagHeight);
            params.rightMargin = DensityUtil.dip2px(10);
            params.topMargin = DensityUtil.dip2px(10);
            textView.setTextColor(context.getResources().getColorStateList(
                    R.color.sel_font_color_green));
            textView.setLayoutParams(params);
            textView.setText(tag);
            return textView;
        }
    };

    /**
     * 获取活动标签列表
     */
    private List<String> getEventTags() {
        List<String> eventTags = Dict.getInstance().getEventTags();
        return eventTags;
    }

    /**
     * 获取活动排序列表
     */
    private ArrayList<ZHDicItem> getEventOrders() {
        return Dict.getInstance().getEventOrder();
    }

    /**
     * 获取默认第一个活动排序
     */
    private String getFirstOrderName() {
        return getEventOrders().get(0).name;
    }

    /**
     * 设置活动类型 名字和图标
     *
     * @param arrowType 图标方向
     */
    private void setCategoryText(String categoryName, int arrowType) {
        tvEventCategory.setText(categoryName);
        int resId;
        if (StringUtil.isEquals(categoryName, TXT_CATEGORY_DEFAULT)) {
            if (arrowType == TYPE_ARROW_UP) {
                resId = R.drawable.img_arrow_gray_up;
            } else {
                resId = R.drawable.img_arrow_gray_down;
            }
        } else {
            tvEventCategory.setTextColor(context.getResources().getColor(R.color.color_dc));
            if (arrowType == TYPE_ARROW_UP) {
                resId = R.drawable.img_filter_arrow_up;
            } else {
                resId = R.drawable.img_filter_arrow_down;
            }
        }
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        tvEventCategory.setCompoundDrawables(null, null, drawable, null);
        int drawablePadding = context.getResources().getDimensionPixelSize(R.dimen.event_filter_padding);
        tvEventCategory.setCompoundDrawablePadding(drawablePadding);
    }

    /**
     * 点击某个活动标签
     */
    private void onClickEventCategory() {
        if (curEventType != lastEventType || !StringUtil.isEquals(curEventTag, lastEventTag)) {
            listener.onClickFilterListener();
        }
    }

    /**
     * 显示活动排序 popup window
     */
    private void showOrder() {
        if (popOrder != null && popOrder.isShowing()) {
            return;
        }
        View orderRootView = LayoutInflater.from(context).inflate(R.layout.pop_event_order, null);

        ListView listView = (ListView) orderRootView.findViewById(R.id.llEventOrder);
        listView.setAdapter(new OrderAdapter());
        View viewOrderFill = orderRootView.findViewById(R.id.viewOrderFill);
        viewOrderFill.setOnClickListener(this);

        popOrder = new PopupWindow(orderRootView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popOrder.setBackgroundDrawable(new BitmapDrawable());
        popOrder.setOutsideTouchable(true);
        popOrder.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                setOrderText(TYPE_ARROW_DOWN);
            }
        });
        setOrderText(TYPE_ARROW_UP);
        popOrder.showAsDropDown(llEventFilter);
    }

    /**
     * 活动排序 adapter
     */
    class OrderAdapter extends BaseAdapter {

        ArrayList<ZHDicItem> eventOrders;

        public OrderAdapter() {
            eventOrders = getEventOrders();
        }

        @Override
        public int getCount() {
            return eventOrders.size();
        }

        @Override
        public Object getItem(int i) {
            return eventOrders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            OrderHolder holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_event_order, parent, false);
                holder = new OrderHolder(view);
                view.setTag(holder);
            } else {
                holder = (OrderHolder) view.getTag();
            }
            final ZHDicItem dicItem = eventOrders.get(position);
            boolean isSelect;
            if (StringUtil.isNullOrEmpty(EventFilterUtil.this.selectOrder)) {
                isSelect = StringUtil.isEquals(dicItem.name, getFirstOrderName());
            } else {
                isSelect = StringUtil.isEquals(dicItem.name, EventFilterUtil.this.selectOrder);
            }
            holder.fillView(dicItem, isSelect);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventFilterUtil.this.selectOrder = dicItem.name;
                    popOrder.dismiss();
                    if (!StringUtil.isEquals(selectOrder, lastSelectOrder)) {
                        listener.onClickFilterListener();
                    }
                    lastSelectOrder = selectOrder;
                }
            });
            return view;
        }
    }

    public static class OrderHolder {

        @InjectView(R.id.tvEventOrder)
        TextView tvEventOrder;

        @InjectView(R.id.ivEventOrder)
        ImageView ivEventOrder;

        public OrderHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void fillView(ZHDicItem dicItem, boolean isSelect) {
            tvEventOrder.setText(dicItem.name);
            ivEventOrder.setVisibility(isSelect ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置活动排序 名字和图标
     *
     * @param arrowType 图标方向
     */
    private void setOrderText(int arrowType) {
        if (StringUtil.isNullOrEmpty(selectOrder)) {
            tvEventOrder.setText(getFirstOrderName());
        } else {
            tvEventOrder.setText(selectOrder);
        }
        int resId;
        if (arrowType == TYPE_ARROW_UP) {
            resId = R.drawable.img_arrow_gray_up;
        } else {
            resId = R.drawable.img_arrow_gray_down;
        }
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        tvEventOrder.setCompoundDrawables(null, null, drawable, null);
        int drawablePadding = context.getResources().getDimensionPixelSize(R.dimen.event_filter_padding);
        tvEventOrder.setCompoundDrawablePadding(drawablePadding);
    }
}
