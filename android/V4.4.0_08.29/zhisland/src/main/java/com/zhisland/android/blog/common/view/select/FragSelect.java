package com.zhisland.android.blog.common.view.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.lib.component.adapter.BaseListFilterAdapter;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.StringUtil;

/**
 * 通用选择fragment
 * 
 * @author 向飞
 */
public class FragSelect extends FragPullList<Serializable> {

	private SelectDataListener dataListener;// 数据存储接口
	private NavListener navListener;// 数据选导航接口
	private Serializable parentArg;// 选中的父级参数

	private boolean showFilter = true;// 是否显示筛选器

	private SelectAdapter adapter;// 选择器的view适配器

	@InjectView(R.id.etFilter)
	EditText etFilter;
	@InjectView(R.id.btnCancelFilter)
	TextView btnFilter;
	@InjectView(R.id.llFilter)
	LinearLayout llFilter;
	@InjectView(R.id.vLine)
	View line;

	public FragSelect() {
		adapter = new SelectAdapter();
	}

	/**
	 * 
	 * @param dataListener
	 *            数据加载器
	 * @param navListener
	 *            选择导航器
	 * @param parentArg
	 *            父级参数
	 * @param max
	 *            最大可选数
	 */
	public void set(SelectDataListener dataListener, NavListener navListener,
					Serializable parentArg, int max){
		this.dataListener = dataListener;
		this.navListener = navListener;
		this.parentArg = parentArg;
		adapter.setMax(max);
	}

	/**
	 * 隐藏条目后面的箭头导航
	 */
	public void hideArrow() {
		adapter.hideArrow();
	}

	/**
	 * 隐藏筛选器
	 */
	public void hideFilter() {
		showFilter = false;
		if (llFilter != null) {
			llFilter.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置选中的条目
	 * 
	 * @param selectedItems
	 */
	public void setSelectedItems(List<Serializable> selectedItems) {
		this.adapter.setSelects(selectedItems);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		getPullProxy().setAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getPullProxy().getPullView().setBackgroundResource(R.color.white);
		internalView.setDividerHeight(0);
		adapter.setAbsView(internalView);

		if (!showFilter) {
			hideFilter();
		}

	}

	@Override
	protected View createDefaultFragView() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.frag_select, null);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void loadNormal() {
		dataListener.loadNormal(parentArg, new SelectCallback());
	}

	/**
	 * 筛选器文字变更事件
	 * 
	 * @param text
	 */
	@OnTextChanged(R.id.etFilter)
	public void onFilterChanged(CharSequence text) {
		String keyword = text == null ? "" : text.toString();
		adapter.filter(keyword);
		adapter.notifyDataSetChanged();
		if (keyword.length() > 0) {
			btnFilter.setVisibility(View.VISIBLE);
		} else {
			btnFilter.setVisibility(View.INVISIBLE);
		}
	}

	@OnClick(R.id.btnCancelFilter)
	public void onCancelClicked(View view) {
		SoftInputUtil.hideInput(getActivity(), view);
		etFilter.setText(null);
	}

	private class SelectAdapter extends BaseListFilterAdapter<Serializable> {

		private List<Serializable> selectedItems = new ArrayList<Serializable>();
		private boolean hideArrow = false;
		private int max = 1;

		/**
		 * 将会清除之前的所有选择数据，并刷新最新的选择数据
		 * 
		 * @param selectedItems
		 *            新的选择数据
		 */
		private void setSelects(List<Serializable> selectedItems) {
			this.selectedItems.clear();
			this.selectedItems.addAll(selectedItems);
		}

		/**
		 * 设置最大选择数
		 * 
		 * @param max
		 */
		public void setMax(int max) {
			this.max = max;
		}

		void hideArrow() {
			hideArrow = true;
		}

		void select(Serializable it) {
			Serializable matched = SelectUtil.findEqualObj(selectedItems, it,
					dataListener);
			if (matched != null) {
				selectedItems.remove(matched);
			} else {
				selectedItems.add(it);
			}
			notifyDataSetChanged();
		}

		@Override
		protected boolean isItemMatched(Serializable it, String keyword) {
			return StringUtil.isNullOrEmpty(keyword)
					|| dataListener.convertToString(it).contains(keyword);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = this.inflater.inflate(R.layout.item_select, null);
				Holder holder = new Holder(convertView);
				convertView.setTag(holder);
			}

			Holder holder = (Holder) convertView.getTag();
			Serializable item = this.getItem(position);
			boolean isSelected = false;
			if (selectedItems != null) {
				Serializable matched = SelectUtil.findEqualObj(selectedItems,
						item, dataListener);
				isSelected = matched != null;
			}
			holder.fill(item, isSelected, hideArrow, max);
			return convertView;
		}

		@Override
		protected void recycleView(View view) {
		}

	}

	/**
	 * adpter的view holder
	 * 
	 * @author 向飞
	 * 
	 */
	final class Holder implements OnClickListener {

		@InjectView(R.id.select_title)
		protected TextView title;
		@InjectView(R.id.select_item_icon)
		protected ImageView icon;
		@InjectView(R.id.select_item_arrow)
		protected View arrow;

		private Serializable item;

		Holder(View converView) {
			ButterKnife.inject(this, converView);
			converView.setOnClickListener(this);
		}

		/**
		 * 设置当前显示的数据
		 * 
		 * @param item
		 */
		public void fill(Serializable item, boolean isSelected,
				boolean hideArrow, int max) {
			this.item = item;
			arrow.setVisibility(hideArrow ? View.GONE : View.VISIBLE);
			title.setText(dataListener.convertToString(item));

			//if (max > 1) {
				// 多选
				if (isSelected) {
					icon.setImageResource(R.drawable.chk_checked);
				} else {
					icon.setImageResource(R.drawable.chk_default);
				}
			//} else {
				// 单选不需要显示复选框
			//	icon.setVisibility(View.GONE);
			//}
		}

		@Override
		public void onClick(View v) {

			if (navListener.onSelected(item)) {
				adapter.select(item);
			}
		}
	}

	public class SelectCallback {

		public void onLoadSuccessFully(List<Serializable> datas) {
			getPullProxy().onLoadSucessfully(datas);
			getPullProxy().getPullView().setMode(Mode.DISABLED);
		}

		public void onLoadFailture(Throwable failture) {
			getPullProxy().onLoadFailed(failture);
		}
	}

}
