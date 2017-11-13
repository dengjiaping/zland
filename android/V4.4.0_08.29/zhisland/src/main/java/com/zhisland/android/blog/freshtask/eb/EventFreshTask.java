package com.zhisland.android.blog.freshtask.eb;

/**
 * 新手任务通知
 * Created by 向飞 on 2016/5/31.
 */
public class EventFreshTask {

    /**
     * 移动到下个卡片
     */
    public static final int TYPE_SWITCH_TO_NEXT_CARD = 100;

    /**
     * 关闭FragAddFriend
     */
    public static final int TYPE_CLOSE_FRAG_ADD_FRIEND = 200;

    /**
     * 显示 新手任务卡片 postion
     */
    public static final int TYPE_SHOW_CARD = 300;

    /**
     * 刷新列表或者卡片
     */
    public static final int TYPE_FRESH_TASK_LIST = 400;

    /**
     * 获取新手任务数据
     */
    public static final int TYPE_GET_FRESH_TASK = 500;


    public EventFreshTask(int taskType, int status) {
        this.taskType = taskType;
        this.status = status;
    }

    public EventFreshTask(int taskType, Object obj) {
        this.taskType = taskType;
        this.obj = obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int taskType;//任务类型
    public int status;//任务状态

    public Object obj;

}
