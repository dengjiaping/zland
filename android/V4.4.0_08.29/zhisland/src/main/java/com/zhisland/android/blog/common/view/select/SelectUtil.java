package com.zhisland.android.blog.common.view.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectUtil {

	/**
	 * 将一个普通对象list转变为serializable list
	 * 
	 * @param from
	 * @return
	 */
	public static ArrayList<Serializable> converTo(List<?> from) {
		if (from == null)
			return null;
		ArrayList<Serializable> results = new ArrayList<Serializable>(
				from.size());
		for (Object obj : from) {
			results.add((Serializable) obj);
		}
		return results;
	}

	/**
	 * 判断列表是否包含指定的数据，如果有则返回匹配到的列表中的数据
	 * 
	 * @param data
	 * @param item
	 * @param listener
	 * @return
	 */
	public static Serializable findEqualObj(List<Serializable> data,
			Serializable item, SelectDataListener listener) {

		if (data == null || item == null || listener == null)
			return null;

		for (Serializable s : data) {
			if (listener.isEqual(s, item)) {
				return s;
			}
		}
		return null;

	}

}
