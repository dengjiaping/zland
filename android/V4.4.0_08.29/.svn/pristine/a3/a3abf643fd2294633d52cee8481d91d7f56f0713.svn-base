package com.zhisland.lib.async.http.task;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * maintain whether gson serialize the fields
 * 
 * @author Xiangfeiy
 * 
 */
public class GsonExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(GsonExclude.class) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}
}