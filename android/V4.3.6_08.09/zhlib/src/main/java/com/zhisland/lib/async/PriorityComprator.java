package com.zhisland.lib.async;

import java.util.Comparator;

public class PriorityComprator implements Comparator<Runnable> {

	@Override
	public int compare(Runnable arg0, Runnable arg1) {
		if (arg0 == null && arg1 == null) {
			return 0;
		} else if (arg0 == null) {
			return -1;
		} else if (arg1 == null) {
			return 1;
		}

		if (arg0 instanceof PriorityFutureTask<?>
				&& arg1 instanceof PriorityFutureTask<?>) {

			PriorityFutureTask<?> pft0 = (PriorityFutureTask<?>) arg0;
			PriorityFutureTask<?> pft1 = (PriorityFutureTask<?>) arg1;
			if (pft0.priority > pft1.priority) {
				return 1;
			}
			if (pft0.priority < pft1.priority) {
				return -1;
			}

		}
		return 0;
	}

}
