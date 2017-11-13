package com.zhisland.android.blog.tabhome.eb;

/**
 * tab home event bus
 */
public class EBTabHome {

    // 主页 tab 位置, start 0
    public static final int TYPE_SEL_TAB_POSITION = 1;

    // 主页 tab 类别 start 1
    public static final int TYPE_SEL_TAB_CATEGORY = 2;

    // 主页 tab 红点 start 1
    public static final int TYPE_TAB_RED_DOT = 3;

    public int type;
    public Object object;

    public EBTabHome(int type, Object object) {
        this.type = type;
        this.object = object;
    }

}
