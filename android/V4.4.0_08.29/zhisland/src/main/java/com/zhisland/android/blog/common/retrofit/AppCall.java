package com.zhisland.android.blog.common.retrofit;

import com.squareup.okhttp.Headers;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.contacts.api.TaskGetFriendListByUid;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.retrofit.AppCallBase;
import com.zhisland.lib.util.StringUtil;

import de.greenrobot.event.EventBus;

public abstract class AppCall<T> extends AppCallBase<T> {

    private static final String MARKS = "marks";
    private static final String MARKS_YES = "1";//head 中的标示为1代表“是”，表示肯定。


    /**
     * 处理客户端错误状态码
     */
    @Override
    protected void handlerError(int code, String message) {
        ApiErrorHandle.INSTANCE().handleApiError(code, message);
    }

    @Override
    protected void handlerHeaders(Headers headers) {
        String marks = headers.get(MARKS);
        if (StringUtil.isNullOrEmpty(marks))
            return;
        String firstMark = String.valueOf(marks.charAt(0));
        String secondMark = String.valueOf(marks.charAt(1));
        if (firstMark.equals(MARKS_YES)) {
            //重新拉取用户信息，成功后再去拉取权限，以及新手任务列表。不然如果uc出问题，userDetail拉不到，任务列表能拉到，这样会一直访问接口，造成客户端卡。
            ZHApis.getUserApi().getUserDetail(null,
                    PrefUtil.Instance().getUserId(), new TaskCallback<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail content) {
                            // 拉取权限
                            ZHApis.getCommonApi().getPermissions(ZhislandApplication.APP_CONTEXT,
                                    null);
                            EventBus.getDefault().post(new EventFreshTask(EventFreshTask.TYPE_GET_FRESH_TASK, null));
                        }

                        @Override
                        public void onFailure(Throwable error) {

                        }
                    });
        } else if (secondMark.equals(MARKS_YES)) {
            TaskGetFriendListByUid task = new TaskGetFriendListByUid(ZhislandApplication.APP_CONTEXT,
                    null);
            task.execute();
        }
    }
}
