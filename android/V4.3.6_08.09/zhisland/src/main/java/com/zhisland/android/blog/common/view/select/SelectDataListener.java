package com.zhisland.android.blog.common.view.select;

import java.io.Serializable;

import com.zhisland.android.blog.common.view.select.FragSelect.SelectCallback;

/**
 * 选择fragment的数据加载器
 * 
 * @author 向飞
 *
 */
public interface SelectDataListener extends Serializable {

	/**
	 * 加载要选择的列表数据
	 * 
	 * @param parentArg
	 *            多级选择的时候，上一级的选项
	 * @param callback
	 */
	void loadNormal(Serializable parentArg, SelectCallback callback);

	/**
	 * 将列表数据中的某个item转化为string
	 * 
	 * @param item
	 *            列表中的某条数据
	 * @return
	 */
	String convertToString(Serializable item);

	/**
	 * 判断两个数据是否相等
	 * 
	 * @param s1
	 *            预选项
	 * @param s2
	 *            列表中的某一项
	 */
	boolean isEqual(Serializable s1, Serializable s2);

}