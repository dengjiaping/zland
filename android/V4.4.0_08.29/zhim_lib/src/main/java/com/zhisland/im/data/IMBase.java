package com.zhisland.im.data;

import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonExclude;

public class IMBase extends OrmDto {

	public static final int ADD = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	private static final long serialVersionUID = 1L;

	/**
	 * 用户event bus notify
	 */
	@GsonExclude
	public int action;

}
