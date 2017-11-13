package com.zhisland.android.blog.info.view.impl;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.CommentDetailModel;
import com.zhisland.android.blog.info.presenter.CommentDetailPresenter;
import com.zhisland.android.blog.info.view.ICommentDetail;
import com.zhisland.android.blog.common.comment.view.ReplyAdapter;
import com.zhisland.android.blog.info.view.impl.holder.CommentDetailHolder;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更多回复页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragCommentDetail extends FragBaseMvp<CommentDetailPresenter> implements ICommentDetail {

    private static final int TAG_ID_RIGHT = 100;

    private static final String INTENT_KEY_COMMENT = "intent_key_comment";
    private static final String INTENT_KEY_INFO = "intent_key_info";

    CommentDetailHolder holder = new CommentDetailHolder();
    ReplyAdapter replyAdapter;
    SendCommentView sendCommentView;
    Dialog progressDlg;
    CommonDialog deleteDialog;

    @Override
    public String getPageName() {
        return "FragCommentDetail";
    }

    public static void invoke(Context context, ZHInfo zhInfo, Comment comment) {
        if (comment == null) {
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragCommentDetail.class;
        param.title = "全部回复";
        param.enableBack = true;
        param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
        param.runnable = titleRunnable;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_IMG);
        tb.imgResId = R.drawable.sel_btn_share_green;
        param.titleBtns.add(tb);
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_COMMENT, comment);
        intent.putExtra(INTENT_KEY_INFO, zhInfo);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            if (fragment != null && fragment instanceof FragCommentDetail) {
                ((FragCommentDetail) fragment).shareInfo();
            }
        }
    };

    @Override
    protected CommentDetailPresenter createPresenter() {
        Comment comment = (Comment) getActivity().getIntent().getSerializableExtra(INTENT_KEY_COMMENT);
        ZHInfo info = (ZHInfo) getActivity().getIntent().getSerializableExtra(INTENT_KEY_INFO);

        CommentDetailPresenter presenter = new CommentDetailPresenter();
        CommentDetailModel model = new CommentDetailModel();
        presenter.setModel(model);
        presenter.setInfo(info);
        presenter.setComment(comment);
        replyAdapter = new ReplyAdapter(getActivity());
        replyAdapter.setComment(comment);
        replyAdapter.setOnReplyListener(presenter);
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //生成listview并设置属性。
        ListView root = new ListView(getActivity());
        root.setBackgroundColor(getResources().getColor(R.color.color_bg1));
        root.setDividerHeight(0);
        root.setSelector(new ColorDrawable(Color.TRANSPARENT));
        root.setAdapter(replyAdapter);
        //listview设置header和footer
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_reply_head, null);
        root.addHeaderView(headerView);
        View footerView = new View(getActivity());
        AbsListView.LayoutParams lpFooter = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(30));
        footerView.setLayoutParams(lpFooter);
        root.addFooterView(footerView);
        //listview设置LayoutParams
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(holder, root);
        ButterKnife.inject(this, root);
        return root;
    }

    //----------------- ICommentDetail 方法 start ---------------

    @Override
    public void refreshInfoView(ZHInfo info) {
        holder.tvTitle.setText(info.title);
        holder.tvSummary.setText(info.recommendText);
    }

    @Override
    public void setLikeCount(int count) {
        holder.tvLike.setText(String.valueOf(count));
    }

    @Override
    public void setLikeEnabled(boolean enabled) {
        holder.tvLike.setEnabled(enabled);
    }

    @Override
    public void showDeleteBtn() {
        holder.tvDelete.setVisibility(View.VISIBLE);
        holder.vLineDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDeleteBtn() {
        holder.tvDelete.setVisibility(View.GONE);
        holder.vLineDelete.setVisibility(View.GONE);
    }

    @Override
    public void refreshReplyView(List<Reply> replies) {
        replyAdapter.setData(replies);
        replyAdapter.notifyDataSetChanged();
        replyAdapter.setData(replies);
    }

    @Override
    public void hideSendCommentView() {
        if (sendCommentView != null) {
            sendCommentView.hide();
        }
    }

    @Override
    public void showCommentView() {
        holder.rlCommentMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCommentView() {
        holder.rlCommentMain.setVisibility(View.GONE);
    }

    @Override
    public void showLineView() {
        holder.vCommentLine.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLineView() {
        holder.vCommentLine.setVisibility(View.GONE);
    }

    @Override
    public void refreshCommentView(Comment comment) {
        ImageWorkFactory.getCircleFetcher().loadImage(comment.publisher.userAvatar, holder.ivFirstAvatar, R.drawable.avatar_default);
        holder.tvTime.setText(comment.publishTime);
        holder.tvName.setText(comment.publisher.name);
        holder.tvPosition.setText((comment.publisher.userCompany == null ? ""
                : (comment.publisher.userCompany + " "))
                + (comment.publisher.userPosition == null ? "" : comment.publisher.userPosition));
        holder.tvContent.setText(comment.content);
    }

    @Override
    public void showCommentDlg(String text) {
        if (progressDlg == null) {
            progressDlg = makeProgressDlg();
        }
        TextView tvText = (TextView) progressDlg.findViewById(R.id.tvText);
        tvText.setText(text);
        progressDlg.show();
    }

    @Override
    public void hideCommentDlg() {
        if (progressDlg != null) {
            progressDlg.dismiss();
        }
    }

    @Override
    public void toastReplySuccess() {
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_info_dlg, null);
        toast.setView(view);
        toast.show();
    }

    @Override
    public void showShareView(ZHInfo info) {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_NEWS_SHARE);
        DialogUtil.getInstatnce().showInfoDialog(getActivity(), info, getPageName());
    }

    @Override
    public void showSendCommentView(SendCommentView.ToType toType, String toName, Long newsId, Long commentId, Long replyId) {
        if (toType == SendCommentView.ToType.subject) {
            if (!PermissionsMgr.getInstance().canInfoComment()) {
                DialogUtil.showPermissionsDialog(getActivity(), "");
                return;
            }
        } else {
            if (!PermissionsMgr.getInstance().canInfoCommentReply()) {
                DialogUtil.showPermissionsDialog(getActivity(), "");
                return;
            }
        }
        if (sendCommentView == null) {
            sendCommentView = new SendCommentView(getActivity(), callback);
        }
        sendCommentView.show(toType, toName, String.valueOf(newsId), commentId, replyId);
    }

    @Override
    public void showDeleteDialog(final Reply reply) {
        if (deleteDialog == null) {
            deleteDialog = new CommonDialog(getActivity());
        }
        if (!deleteDialog.isShowing()) {
            deleteDialog.show();
            if (reply == null) {
                deleteDialog.setTitle("确定删除该条观点？");
            } else {
                deleteDialog.setTitle("确定删除该条回复？");
            }
            deleteDialog.setLeftBtnContent("取消");
            deleteDialog.setRightBtnContent("确定删除");
            deleteDialog.setRightBtnColor(getActivity().getResources()
                    .getColor(R.color.color_ac));
            deleteDialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                    if (reply == null) {
                        presenter().deleteComment();
                    } else {
                        presenter().deleteReply(reply);
                    }
                }
            });
        }
    }

    @Override
    public void gotoProfileDetail(long uid) {
        ActProfileDetail.invoke(getActivity(), uid);
    }

    @Override
    public void gotoInfoDetail(long newsId) {
        ActInfoDetail.invoke(getActivity(), newsId);
    }

    //----------------- ICommentDetail 方法 end ---------------

    //------------------------- private method start --------------------

    private Dialog makeProgressDlg() {
        Dialog dialog = new Dialog(getActivity(), R.style.PROGRESS_DIALOG);
        dialog.setContentView(R.layout.layout_info_dlg);
        ImageView ivOk = (ImageView) dialog.findViewById(R.id.ivOk);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        ivOk.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        return dialog;
    }

    private SendCommentView.Callback callback = new SendCommentView.Callback() {

        @Override
        public void comment(String newsId, String content) {
            presenter().comment(newsId, content);
        }

        @Override
        public void commentReply(long commentId, String content) {
            presenter().commentReply(commentId, null, content);
        }

        @Override
        public void commentReply(long commentId, long replyId, String content) {
            presenter().commentReply(commentId, replyId, content);
        }
    };

    //------------------------- private method end --------------------

    //------------------------- click action start --------------------

    private void shareInfo() {
        presenter().onShareClicked();
    }

    @OnClick(R.id.tvLike)
    void likeClick() {
        presenter().commentLike();
    }

    @OnClick(R.id.tvComment)
    void commentClick() {
        presenter().onCommentClick();
    }

    @OnClick(R.id.tvDelete)
    void deleteClick() {
        presenter().onCommentDeleteClick();
    }

    @OnClick(R.id.rlInfoContent)
    void infoContentClick() {
        presenter().onInfoContentClick();
    }

    @OnClick(R.id.ivFirstAvatar)
    void avatarClick() {
        presenter().onAvatarClick();
    }

    @OnClick(R.id.tvName)
    void nameClick() {
        presenter().onNameClick();
    }

    @OnClick(R.id.tvPosition)
    void positionClick() {
        presenter().onPositionClick();
    }

    //------------------------- click action end --------------------
}