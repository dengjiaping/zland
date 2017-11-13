package com.zhisland.android.blog.common.view.select;

import java.io.Serializable;

/**
 * 选择器内部导航类
 * 
 * @author 向飞
 *
 */
interface NavListener {

	/**
	 * 当选中某个item时，会触发callback
	 * 
	 * @param item
	 */
	boolean onSelected(Serializable item);
}
