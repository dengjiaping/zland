package com.zhisland.android.blog.info.view.impl.holder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 发送回复view
 * Created by Mr.Tui on 2016/7/7.
 */
public class SendCommentView {

    public enum ToType {info, comment, reply}

    Activity context;

    @InjectView(R.id.etComment)
    EditText etComment;

    @InjectView(R.id.btnSend)
    Button btnSend;

    private String toName;

    private Long newsId;

    private Long commentId;

    private Long replyId;

    private ToType toType;

    Dialog dialog;

    Callback callback;

    public SendCommentView(Activity context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void show(ToType toType, String toName, Long newsId, Long commentId, Long replyId) {
        if (toType == ToType.info) {
            if (!PermissionsMgr.getInstance().canInfoComment()) {
                DialogUtil.showPermissionsDialog(context, "");
                return;
            }
        } else {
            if (!PermissionsMgr.getInstance().canInfoCommentReply()) {
                DialogUtil.showPermissionsDialog(context, "");
                return;
            }
        }

        this.toType = toType;
        this.toName = toName;
        this.newsId = newsId;
        this.commentId = commentId;
        this.replyId = replyId;
        if (dialog == null) {
            dialog = new Dialog(context, R.style.ActionDialog) {
                @Override
                public boolean onTouchEvent(MotionEvent event) {
                    if (isShowing() && shouldCloseOnTouch(getContext(), event)) {
                        hideKeyBoard();
                    }
                    return super.onTouchEvent(event);
                }

                public boolean shouldCloseOnTouch(Context context, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN
                            && isOutOfBounds(context, event) && getWindow().peekDecorView() != null) {
                        return true;
                    }
                    return false;
                }

                private boolean isOutOfBounds(Context context, MotionEvent event) {
                    final int x = (int) event.getX();
                    final int y = (int) event.getY();
                    final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
                    final View decorView = getWindow().getDecorView();
                    return (x < -slop) || (y < -slop)
                            || (x > (decorView.getWidth() + slop))
                            || (y > (decorView.getHeight() + slop));
                }
            };
            dialog.setContentView(R.layout.layout_send_comment);
            ButterKnife.inject(this, dialog);
        }
        EditTextUtil.limitEditTextLengthChinese(etComment, 500, null, false);
        etComment.setText("");
        dialog.show();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = DensityUtil.getWidth();
        dialog.getWindow().setAttributes(wmlp);
        fillContent();
        SoftInputUtil.showKeyboard(context.getCurrentFocus());
    }

    public void hide() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void fillContent() {
        if (toType == ToType.info) {
            etComment.setHint("发表我的观点");
        } else if (toType == ToType.comment) {
            etComment.setHint("回复" + toName + ":");
        } else if (toType == ToType.reply) {
            etComment.setHint("回复" + toName + ":");
        }
    }

    @OnTextChanged(R.id.etComment)
    void OnCommentChanged() {
        if (etComment.getText().length() > 0) {
            btnSend.setEnabled(true);
        } else {
            btnSend.setEnabled(false);
        }
    }

    @OnClick(R.id.btnSend)
    void sendClick() {
        String content = etComment.getText().toString().trim();
        if (content.length() == 0) {
            ToastUtil.showShort("请输入要发布的内容");
            return;
        }
        hideKeyBoard();
        if (callback != null) {
            if (toType == ToType.info) {
                callback.comment(newsId, etComment.getText().toString());
            } else if (toType == ToType.comment) {
                callback.commentReply(commentId, etComment.getText().toString());
            } else if (toType == ToType.reply) {
                callback.commentReply(commentId, replyId, etComment.getText().toString());
            }
        }
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
    }

    public interface Callback {

        /**
         * 发表评论
         */
        public void comment(long newsId, String content);

        /**
         * 对评论发表回复
         */
        public void commentReply(long commentId, String content);

        /**
         * 对回复发表回复
         */
        public void commentReply(long commentId, long replyId, String content);
    }
}
