package com.zhisland.lib.bitmap;

import java.util.ArrayList;
import java.util.WeakHashMap;

import com.zhisland.lib.bitmap.ImageWorker.AsyncBitmap;

import android.content.Context;

public class AsyncBitmapManager {
	private static WeakHashMap<Context, ArrayList<AsyncBitmap>> bitmapMap = new WeakHashMap<Context, ArrayList<AsyncBitmap>>();

	public static void addAsyncBitmap(AsyncBitmap task, Context context) {
		ArrayList<AsyncBitmap> tasks = (ArrayList<AsyncBitmap>) bitmapMap
				.get(context);
		if (tasks == null) {
			tasks = new ArrayList<AsyncBitmap>();
			bitmapMap.put(context, tasks);
		}
		tasks.add(task);
	}

	public static void cancelTask(Context context) {

		ArrayList<AsyncBitmap> bitmapTasks = (ArrayList<AsyncBitmap>) bitmapMap
				.get(context);
		if (bitmapTasks != null) {
			for (AsyncBitmap task : bitmapTasks) {
				task.cancel();
			}
			bitmapMap.remove(bitmapTasks);
		}

	}
}
