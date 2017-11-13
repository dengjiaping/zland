package com.zhisland.android.blog.contacts.eb;

import com.zhisland.android.blog.contacts.dto.Loc;
import com.zhisland.lib.OrmDto;

public class EBLoc extends OrmDto{

	public Loc loc;

	public long time;

	public int errorCode;

	public EBLoc(Loc loc, int errorCode, long time) {
		this.loc = loc;
		this.time = time;
		this.errorCode = errorCode;
	}
}
