package com.zhisland.lib.view.search;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhisland.lib.R;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 搜索条组件。
 * 
 * 调用setSearchBarListener()为组件添加监听，以实现历史和联想的下拉菜单。
 * NOTE: when use this control, if it will be in TabHost, make sure NOT set its'
 * anchorid;
 * 
 */
public class ZHSearchBar extends RelativeLayout {

	/**
	 * 用来创建view
	 */
	protected Context context;
	private ZHAutoCompleteTextView autoCompleteTextView;
	private ImageView btnSearch;

	private ZHSearchListener searchListener;
	protected BaseSearchBarAdapter adapter;

	public ZHSearchBar(Context context) {
		super(context);
		initViews(context);
	}

	public ZHSearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	/**
	 * 设置搜索的所有监听，并生成adapter设置到autotextview
	 * 
	 * @param barListener
	 */
	public void setSearchBarListener(ZHSearchListener barListener) {
		this.searchListener = barListener;
		this.adapter = new DefaultSearchAdapter(context, searchListener);
		this.autoCompleteTextView.setAdapter(adapter);
	}

	private void initViews(Context context) {
		this.context = context;

		RelativeLayout.LayoutParams pSearch = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		pSearch.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		pSearch.addRule(RelativeLayout.CENTER_VERTICAL);
		btnSearch = new ImageView(context);
		btnSearch.setId(R.id.btn_search);
		btnSearch.setImageResource(R.drawable.img_search);
		btnSearch.setPadding(0, 0, 0, 0);

		RelativeLayout.LayoutParams pTv = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		pTv.leftMargin = DensityUtil.dip2px(10);
		pTv.addRule(RelativeLayout.RIGHT_OF, R.id.btn_search);
		pTv.addRule(RelativeLayout.CENTER_VERTICAL);
		autoCompleteTextView = new ZHAutoCompleteTextView(context);
		autoCompleteTextView.setId(R.id.auto_complete);
		autoCompleteTextView.setDropDownWidth(DensityUtil.getWidth());
		autoCompleteTextView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		autoCompleteTextView.setBackgroundResource(R.color.transparent);
		autoCompleteTextView.setHint("搜索");
		autoCompleteTextView.setSingleLine(true);
		autoCompleteTextView.setPadding(0, 0, 0, 0);
		autoCompleteTextView.setTextSize(16);

		autoCompleteTextView.setDropDownBackgroundDrawable(new ColorDrawable(
				Color.WHITE));
		autoCompleteTextView.setDropDownVerticalOffset(DensityUtil.dip2px(8));

		this.addView(btnSearch, pSearch);
		this.addView(autoCompleteTextView, pTv);

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				collapseSoftInputMethod();
				if (searchListener != null) {
					String keyword = autoCompleteTextView.getText().toString()
							.trim();
					if (!StringUtil.isNullOrEmpty(keyword)) {
						searchListener.recordHistory(
								searchListener.getSearchKey(), keyword);
					}
					searchListener.goSearch(searchListener.getSearchKey(),
							keyword);
				}

			}
		});

		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (searchListener != null) {
					if (adapter.isClear(position)) {
						searchListener.clearHistory(searchListener
								.getSearchKey());
						return;
					}
					if (adapter.isShowHistory()) {
						searchListener.onHistoryItemClicked(adapter
								.getItem(position));
					} else {
						searchListener.onIntelligenceItemClicked(adapter
								.getItem(position));
					}
				}

			}
		});

		autoCompleteTextView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					collapseSoftInputMethod();
					if (searchListener != null) {
						String keyword = autoCompleteTextView.getText()
								.toString().trim();
						searchListener.recordHistory(
								searchListener.getSearchKey(), keyword);
						searchListener.goSearch(searchListener.getSearchKey(),
								keyword);
					}
					return true;
				} else {
					return false;
				}

			}
		});
	}
	
	public void setSearchText(String text) {
		autoCompleteTextView.setText(text);
	}

	public void setHint(String text) {
		autoCompleteTextView.setHint(text);
	}

	public void setThreshold(int threshold) {
		autoCompleteTextView.setThreshold(threshold);
	}

	public void setDropDownAnchor(int id) {
		autoCompleteTextView.setDropDownAnchor(this.getId());
	}

	public void setDropDownVerticalOffset(int offset) {
		autoCompleteTextView.setDropDownVerticalOffset(offset);
	}

	public void setDropDownWidth(int width) {
		autoCompleteTextView.setDropDownWidth(width);
	}

	public void setDropDownHeight(int height) {
		autoCompleteTextView.setDropDownHeight(height);
	}

	public void collapseSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);

	}

	public ZHAutoCompleteTextView getTextView() {
		return autoCompleteTextView;
	}

	public void setBGResource(int drawable) {
		this.setBackgroundResource(drawable);
	}

	public void setIcon(int resId) {
		this.btnSearch.setImageResource(resId);
	}

	public void setbtnSearchClickable(boolean clickable){
		btnSearch.setClickable(clickable);
	}
}
