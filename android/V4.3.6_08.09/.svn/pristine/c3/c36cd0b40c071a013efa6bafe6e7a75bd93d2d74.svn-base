/**
 * 
 */
package com.zhisland.lib.component.adapter;

import android.view.View;
import android.widget.AbsListView.RecyclerListener;

import com.zhisland.lib.util.MLog;

/**
 *
 *
 */
public abstract class CatchableRecyclerListener implements RecyclerListener {
	public abstract void intlOnMovedToScrapHeap(View view);

	@Override
	public void onMovedToScrapHeap(View view) {
		try {
			intlOnMovedToScrapHeap(view);
		} catch (final Error t) {
			MLog.e("CatchableRecyclerListener", t.getMessage(), t);

			throw (Error) t.fillInStackTrace();
		}
	}
}
