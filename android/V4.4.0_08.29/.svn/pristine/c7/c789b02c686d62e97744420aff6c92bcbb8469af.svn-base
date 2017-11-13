package com.zhisland.lib.async.http.task;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class TaskManager {
	private static WeakHashMap<Object, ArrayList<HttpTask<?>>> taskMap = new WeakHashMap<Object, ArrayList<HttpTask<?>>>();

	public static void addTask(HttpTask<?> task, Object context) {
		ArrayList<HttpTask<?>> tasks = taskMap.get(context);
		if (tasks == null) {
			tasks = new ArrayList<HttpTask<?>>();
			taskMap.put(context, tasks);
		}
		tasks.add(task);
		task.execute();
	}

	public static void cancelTask(Object context) {

		ArrayList<HttpTask<?>> tasks = taskMap.get(context);
		if (tasks != null) {
			for (HttpTask<?> task : tasks) {
				task.cancel();
			}
			taskMap.remove(tasks);
		}
	}

	public static void cancelAll() {
		for (ArrayList<HttpTask<?>> tasks : taskMap.values()) {
			for (HttpTask<?> task : tasks) {
				task.cancel();
			}
			tasks.clear();
		}
		taskMap.clear();
	}

	public static void removeTask(Object context, HttpTask<?> task) {
		List<HttpTask<?>> tasks = taskMap.get(context);
		if (tasks != null) {
			if (tasks.contains(task))
				tasks.remove(task);
		}
	}

}
