package com.zhisland.lib.async;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zhisland.lib.util.MLog;

/**
 * all async threads should
 * 
 */
public class ThreadManager {

	public static final int THREAD_PRIORITY_HIGH = 0;
	public static final int THREAD_PRIORITY_NORMAL = 1;
	public static final int THREAD_PRIORITY_LOW = 2;
	public static final int THREAD_PRIORITY_LOWER = 3;
	
	private static final String TAG = "queue";

	private static Object lockObj = new Object();
	private static ThreadManager instance = null;
	private ThreadPoolExecutor threadPool;
	private PriorityBlockingQueue<Runnable> queue;
	private WeakHashMap<Object, LinkedList<WeakReference<FutureTask<?>>>> futureTasks;

	public static ThreadManager instance() {
		if (instance == null) {
			synchronized (lockObj) {
				if (instance == null) {
					instance = new ThreadManager();
				}
			}
		}
		return instance;
	}

	private ThreadManager() {
		futureTasks = new WeakHashMap<Object, LinkedList<WeakReference<FutureTask<?>>>>();
		queue = new PriorityBlockingQueue<Runnable>(10, new PriorityComprator());
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();
		threadPool = new ThreadPoolExecutor(numberOfProcessors + 1, numberOfProcessors + 1, 0L, TimeUnit.MILLISECONDS,
				queue);
	}

	public void execute(PriorityFutureTask<?> task) {
		execute(task, null);
	}

	public void execute(PriorityFutureTask<?> task, Object contextKey) {
		threadPool.execute(task);
		LinkedList<WeakReference<FutureTask<?>>> requestList = futureTasks
				.get(contextKey);
		if (requestList == null) {
			requestList = new LinkedList<WeakReference<FutureTask<?>>>();
			futureTasks.put(contextKey, requestList);
		}

		requestList.add(new WeakReference<FutureTask<?>>(task));
	}

	public void cancelTask(Object contextKey, boolean mayInterruptIfRunning) {
		LinkedList<WeakReference<FutureTask<?>>> requestList = futureTasks
				.get(contextKey);
		if (requestList != null) {
			for (WeakReference<FutureTask<?>> requestRef : requestList) {
				Future<?> request = requestRef.get();
				if (request != null) {
					request.cancel(mayInterruptIfRunning);
					MLog.d(TAG, "cancel  reuqest: " + request.toString());
				}
			}
			futureTasks.remove(contextKey);
		}

	}
}
