package com.zhisland.android.blog.common.base;

import android.app.Activity;
import android.content.Context;

import com.zhisland.lib.async.http.task.TaskManager;

/**
 * 接口引擎基类
 */
public abstract class ApiBase {

    protected void addTask(Object context, TaskBase<?, ?> task) {
        TaskManager.addTask(task, context);
    }

    public void cancelTask(Object context) {
        TaskManager.cancelTask(context);
    }

}
