package com.zhisland.android.blog.common.view.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典选择器配置参数，每一级别的选择器对应着一个参数配置 TODO 可以获取新增加选项，反选掉的选项
 * 
 * @author 向飞
 *
 */
public class FragParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param selectedItems
	 */
	public FragParam(List<Serializable> selectedItems) {
		this.selectedItems = selectedItems;
		if (selectedItems != null) {
			allSelectedItems.addAll(selectedItems);
		}
	}

	// 下一级选择参数,如果设置了这个参数，当选中一条数据后会优先跳转到下一级别选择器
	public FragParam nextFrag;
	// 数据接口
	public SelectDataListener dataListener;
	// 标题
	public String title;
	/**
	 * 最多选择书，当nextFrag不为空时，此参数将失效
	 */
	public int max = 1;

	public boolean showFilterBar = true;

	// 初始选中的数据
	private List<Serializable> selectedItems;

	// 最终选中数据
	private List<Serializable> allSelectedItems = new ArrayList<Serializable>();

	List<Serializable> getAllSelectedItems() {
		return allSelectedItems;
	}

	/**
	 * 选择某一条数据，可能是新增的选择，也可能是取消的选择
	 * 
	 * @param item
	 */
	public void select(Serializable item) {
		Serializable matched = SelectUtil.findEqualObj(allSelectedItems, item,
				dataListener);
		if (matched != null) {
			allSelectedItems.remove(matched);
		} else {
			allSelectedItems.add(item);
		}

	}

	/**
	 * 重置选择器参数
	 */
	public void reset() {
		allSelectedItems.clear();
		if (selectedItems != null)
			allSelectedItems.addAll(selectedItems);
	}
}
