package com.zhisland.android.blog.find.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.find.controller.ActSearch;
import com.zhisland.android.blog.find.dto.FindType;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.zhisland.android.blog.find.dto.FindType.boss;
import static com.zhisland.android.blog.find.dto.FindType.resource;

/**
 * 搜索bar
 * Created by Mr.Tui on 2016/5/19.
 */
public class FindSearchLayout extends LinearLayout {

    Context context;

    @InjectView(R.id.llSwitch)
    LinearLayout llSwitch;

    @InjectView(R.id.tvType)
    TextView tvType;

    @InjectView(R.id.etSearch)
    EditText etSearch;

    @InjectView(R.id.ivClean)
    ImageView ivClean;

    /**
     * 当前搜索类型
     */
    FindType currentType;

    /**
     * 搜索相关callback
     */
    private CallBack callBack;

    /**
     * 搜索类型切换ViewHolder
     */
    TypeHolder typeHolder;

    public FindSearchLayout(Context context) {
        super(context);
        init(context);
    }

    public FindSearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FindSearchLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.content_find_search_bar, null);
        ButterKnife.inject(this, contentView);
        int paddingLeftRight = DensityUtil.dip2px(10);
        int paddingTopBottom = DensityUtil.dip2px(8);
        setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);
        addView(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        EditTextUtil.setKeyBoard(etSearch, EditTextUtil.ID_SEARCH,
                new EditTextUtil.IKeyBoardAction() {

                    @Override
                    public void action() {
                        onSearchClick();
                    }
                });
    }

    @OnTextChanged(R.id.etSearch)
    void OnSearchTextChanged() {
        if (etSearch.getText().length() > 0) {
            ivClean.setVisibility(View.VISIBLE);
        } else {
            if (callBack != null) {
                //当搜索内容为空时，回调toSearch。为了显示默认选项。
                callBack.toSearch(currentType, null);
            }
            ivClean.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.llSwitch)
    void onSwitchClick() {
        if (typeHolder == null) {
            typeHolder = new TypeHolder();
        }
        typeHolder.show();
    }

    /**
     * 改变当前搜索类型,会回调CallBack.onSearchTypeChanged
     */
    private void changeSearchType(FindType type) {
        if (currentType != type) {
            this.currentType = type;
            setTypeText(tvType, type);
            String searchKey = etSearch.getText().toString().trim();
            if (callBack != null) {
                callBack.onSearchTypeChanged(currentType, searchKey);
            }
        }
    }

    /**
     * 设置当前搜索类型,不会回调CallBack.onSearchTypeChanged
     */
    public void setCurrentSearchType(FindType type) {
        this.currentType = type;
        setTypeText(tvType, type);
    }

    /**
     * 设置可以搜索类型。
     * 备选值：ActSearch.TYPE_ALL 既可搜索企业家，又可搜索资源需求，可在二者之间切换。
     * ActSearch.TYPE_BOSS 只可搜索企业家。
     * ActSearch.TYPE_RES 只可搜索资源需求。
     */
    public void setSearchType(int type) {
        if (type == ActSearch.TYPE_BOSS) {
            llSwitch.setVisibility(View.GONE);
            setCurrentSearchType(FindType.boss);
        } else if (type == ActSearch.TYPE_RES) {
            llSwitch.setVisibility(View.GONE);
            setCurrentSearchType(FindType.resource);
        } else {
            //其他视为搜索页面，可以在企业家和资源间切换，默认值为企业家
            llSwitch.setVisibility(View.VISIBLE);
            setCurrentSearchType(FindType.boss);
        }
    }

    /**
     * 设置TextView的显示搜索类型的名字
     */
    private void setTypeText(TextView tv, FindType type) {
        if (tv != null) {
            switch (type) {
                case boss:
                    tv.setText(context.getString(R.string.find_people));
                    etSearch.setHint(context.getString(R.string.find_people_search_hint));
                    break;
                case resource:
                    tv.setText(context.getString(R.string.find_resource));
                    etSearch.setHint(context.getString(R.string.find_resource_search_hint));
                    break;
                default:
                    tv.setText("");
            }
        }
    }

    public synchronized void setSearchWord(String word) {
        if (word == null) {
            word = "";
        }
        etSearch.setText(word);
        etSearch.setSelection(word.length());
    }

    /**
     * 获取当前搜索框内的字
     */
    public String getSearchWord() {
        return etSearch.getText().toString().trim();
    }

    @OnClick(R.id.ivClean)
    void onCleanClick() {
        etSearch.setText("");
    }

    @OnClick(R.id.ivSearch)
    void onSearchClick() {
        String searchKey = etSearch.getText().toString().trim();
        //业务逻辑:如果搜索内容为空,不执行搜索
        if (callBack != null && !StringUtil.isNullOrEmpty(searchKey)) {
            callBack.toSearch(currentType, searchKey);
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {

        /**
         * 搜索按钮被点击
         */
        void toSearch(FindType type, String searchKey);

        /**
         * 搜索类型切换
         */
        void onSearchTypeChanged(FindType type, String searchKey);

    }

    /**
     * 搜索类型切换，选择PopupWindow
     */
    class TypeHolder {

        private PopupWindow typeDlg;

        public TypeHolder() {
            View view = LayoutInflater.from(context).inflate(R.layout.dlg_switch_type, null);
            typeDlg = new PopupWindow(view, DensityUtil.dip2px(99),
                    DensityUtil.dip2px(80), true);
            typeDlg.setBackgroundDrawable(new BitmapDrawable());
            typeDlg.setOutsideTouchable(true);
            typeDlg.setFocusable(false);
            ButterKnife.inject(this, view);
        }

        @OnClick(R.id.tvTypeBoss)
        void onTypeBossClick() {
            changeSearchType(boss);
            etSearch.setHint(context.getString(R.string.find_people_search_hint));
            typeDlg.dismiss();
        }

        @OnClick(R.id.tvTypeResource)
        void onTypeResourceClick() {
            changeSearchType(resource);
            etSearch.setHint(context.getString(R.string.find_resource_search_hint));
            typeDlg.dismiss();
        }

        void show() {
            typeDlg.showAsDropDown(llSwitch);
        }
    }
}
