package com.zhisland.lib.component.adapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAutoSectionAdapter<T> extends BaseListAdapter<T> {

	private ArrayList<T> sections;

	public BaseListAutoSectionAdapter(List<T> data) {
		super(data);
		sections = new ArrayList<T>();
	}

	@Override
	public void insert(List<T> items) {
		if (items == null || items.size() < 1)
			return;

		if (data == null) {
			data = new ArrayList<T>();
		}

		int i = 0;
		T sectionStart = null;
		for (T item : items) {
			if (needStartNewSection(sectionStart, item)) {
				T sectionItem = createSectionTitle(item);
				data.add(i++, sectionItem);
				sections.add(sectionItem);
			}
			data.add(i++, item);
			sectionStart = item;
		}
		notifyDataSetChanged();
	}

	@Override
	public void add(List<T> items) {
		if (items == null || items.size() < 1)
			return;

		if (data == null) {
			data = new ArrayList<T>();
		}

		T sectionStart = getLastItem();

		for (T item : items) {
			if (needStartNewSection(sectionStart, item)) {
				T sectionItem = createSectionTitle(item);
				data.add(sectionItem);
				sections.add(sectionItem);
			}
			data.add(item);
			sectionStart = item;
		}
		notifyDataSetChanged();
	}

	@Override
	public void add(T item) {
		if (item == null)
			return;

		if (data == null) {
			data = new ArrayList<T>();
		}

		T sectionStart = getLastItem();
		if (needStartNewSection(sectionStart, item)) {
			T sectionItem = createSectionTitle(item);
			data.add(sectionItem);
			sections.add(sectionItem);
		}
		data.add(item);
		notifyDataSetChanged();
	}

	public T getFirstItem() {
		return getCount() > 1 ? getItem(1) : null;
	}

	public T getLastItem() {
		return getCount() > 0 ? getItem(getCount() - 1) : null;
	}

	@Override
	public void removeItem(T item) {

		if (data == null)
			return;
		int index = data.indexOf(item);
		if (index < 0)
			return;

		T pre = index > 0 ? getItem(index - 1) : null;
		T next = index < getCount() - 1 ? getItem(index + 1) : null;
		if (sections.contains(pre) && (sections.contains(next) || next == null)) {
			data.remove(pre);
			data.remove(item);
		} else if (sections.contains(next) || next == null) {
			data.remove(item);
		} else {
			data.remove(item);
			if (needStartNewSection(pre, next)) {
				T sectionItem = createSectionTitle(next);
				data.add(index, sectionItem);
			}
		}

		notifyDataSetChanged();
	}

	/**
	 * if retur true, a new sction title will be added
	 */
	protected abstract boolean needStartNewSection(T pre, T next);

	/**
	 * create section title view
	 */
	protected abstract T createSectionTitle(T item);

}
