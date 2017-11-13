package com.zhisland.android.blog.setting.eb;

public class EBSetting {

	/**
	 * 刷新版本icon
	 */
	public static final int TYPE_REFRESH_VERSION_ICON = 1;
	
	/**
	 * 隐私设置
	 */
	public static final int TYPE_VISIBLE_SETTING = 2;

	private int type;
	private Object obj;

	public EBSetting(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public Object getData() {
		return obj;
	}

}
