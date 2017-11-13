package com.zhisland.lib.component.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * an abstract base class of listview adapter
 * 
 * @author Xiangfeiy
 * 
 * @param <T>: List item's data entity
 */
public abstract class BaseListFilterAdapter<T> extends BaseListAdapter<T> {

	private ArrayList<Integer> mappedIndexes = null;
	protected String keyword = null;

	public BaseListFilterAdapter() {
		this(null);
	}

	public BaseListFilterAdapter(final List<T> data) {
		super(data);
		this.mappedIndexes = new ArrayList<Integer>();
	}

	@Override
	public void notifyDataSetChanged() {
		this.filter();
		super.notifyDataSetChanged();
	}

	public void filter(String keyword) {
		this.keyword = keyword;
	}

	private void filter() {
		mappedIndexes.clear();
		if (data != null) {
			int idx = 0;
			for (T item : data) {
				if (isItemMatched(item, this.keyword)) {
					mappedIndexes.add(idx);
				}
				idx++;
			}

		}
	}

	protected boolean isItemMatched(T item, String keyword) {
		return true;
	}

	@Override
	public int getCount() {
		return mappedIndexes.size();
	}

	/**
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public T getItem(final int position) {

		T item = null;
		if (this.data != null) {
			item = this.data.get(mappedIndexes.get(position));
		}
		return item;
	}

}
