package com.zhisland.lib.async;

import java.util.concurrent.FutureTask;

public class PriorityFutureTask<V> extends FutureTask<V> {

	public int priority;

	public PriorityFutureTask(Runnable runnable, V result, int priority) {
		super(runnable, result);

		this.priority = priority;
	}

}
