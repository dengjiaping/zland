/**
 * 
 */
package com.zhisland.lib.component.adapter;

import android.view.View;
import android.view.ViewGroup.OnHierarchyChangeListener;

import com.zhisland.lib.util.MLog;

/**
 *
 *
 */
public abstract class CatchableOnHierarchyChangeListener implements
		OnHierarchyChangeListener {
	public abstract void intlOnChildViewAdded(View parent, View child);

	public abstract void intlOnChildViewRemoved(View parent, View child);

	@Override
	public void onChildViewAdded(View parent, View child) {
		try {
			intlOnChildViewAdded(parent, child);
		} catch (final Error t) {
			MLog.e("CatchableOnHierarchyChangeListener", t.getMessage(), t);

			throw (Error) t.fillInStackTrace();
		}
	}

	@Override
	public void onChildViewRemoved(View parent, View child) {
		try {
			intlOnChildViewRemoved(parent, child);
		} catch (final Error t) {
			MLog.e("CatchableOnHierarchyChangeListener", t.getMessage(), t);

			throw (Error) t.fillInStackTrace();
		}
	}
}
