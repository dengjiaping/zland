package com.zhisland.android.blog.profile.controller.comment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.TimeUtil;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.android.blog.profile.api.TaskTopUserComment;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.profile.eb.EBUserComment;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.dialog.AProgressDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 个人页神评论 cell
 */
public class UserCommentCell {

    @InjectView(R.id.tvUcContent)
    TextView tvUcContent;

    @InjectView(R.id.tvUcInfo)
    TextView tvUcInfo;

    private UserComment comment;
    private Context context;

    public UserCommentCell(View view, final Context context) {
        this.context = context;
        ButterKnife.inject(this, view);
    }

    public void fill(UserComment comment) {
        if (comment == null) {
            return;
        }
        this.comment = comment;
        tvUcContent.setText(comment.content);
        StringBuffer sbUcInfo = new StringBuffer();
        User publisher = comment.publisher;
        if (publisher != null) {
            if (!StringUtil.isNullOrEmpty(publisher.name)) {
                sbUcInfo.append("－ ").append(publisher.name);
            }
            if (!StringUtil.isNullOrEmpty(publisher.userCompany)) {
                sbUcInfo.append(" ").append(publisher.userCompany);
            }
            if (!StringUtil.isNullOrEmpty(publisher.userPosition)) {
                sbUcInfo.append(" ").append(publisher.userPosition);
            }
        }
        String str = sbUcInfo.toString();
        tvUcInfo.setText(str);
    }

    @OnClick(R.id.llRoot)
    public void onClickRootView() {
        User publisher = comment.publisher;
        if (publisher != null) {
            ActProfileDetail.invokeNoHistory(context, publisher.uid);
        }
    }

}
