package com.zhisland.android.blog.info.eb;

import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.ZHInfo;

/**
 * Created by Mr.Tui on 2016/7/11.
 */
public class EBInfo {

    public static final int ACTION_UPDATA_COUNT = 3;

    public ZHInfo info;

    public int action;

    public EBInfo(int action, ZHInfo info) {
        this.info = info;
        this.action = action;
    }
}
