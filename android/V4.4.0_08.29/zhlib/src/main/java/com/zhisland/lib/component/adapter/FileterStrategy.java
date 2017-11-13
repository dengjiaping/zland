package com.zhisland.lib.component.adapter;

import java.util.List;

interface FileterStrategy<T> {

	boolean isMatched(T item, CharSequence prefix);

	CharSequence getCharSequence(Object resultValue);

	void onDataChanged(List<T> newData);
}
