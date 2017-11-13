package com.zhisland.android.blog.feed.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.common.comment.view.CommentHolder;
import com.zhisland.android.blog.common.comment.view.ReplyAdapter;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.model.impl.FeedCommentDetailModel;
import com.zhisland.android.blog.feed.presenter.FeedCommentDetailPresenter;
import com.zhisland.android.blog.feed.view.IFeedCommentDetail;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.FragPullListMvp;

import java.util.List;

/**
 * 评论详情，更多回复页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragCommentDetail extends FragPullListMvp<Reply, FeedCommentDetailPresenter> implements IFeedCommentDetail {

    private static final String INTENT_KEY_COMMENT = "intent_key_comment";
    private static final String INTENT_KEY_FEED = "intent_key_feed";

    ReplyAdapter replyAdapter;

    CommentHolder commentHolder;

    CommonDialog deleteDialog;

    SendCommentView sendCommentView;

    @Override
    public String getPageName() {
        return "FragCommentDetail";
    }

    public static void invoke(Context context, Feed feed, Comment comment) {
        if (comment == null || feed == null) {
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragCommentDetail.class;
        param.title = "全部回复";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_COMMENT, comment);
        intent.putExtra(INTENT_KEY_FEED, feed);
        context.startActivity(intent);
    }

    @Override
    protected FeedCommentDetailPresenter createPresenter() {
        Comment comment = (Comment) getActivity().getIntent().getSerializableExtra(INTENT_KEY_COMMENT);
        Feed feed = (Feed) getActivity().getIntent().getSerializableExtra(INTENT_KEY_FEED);

        FeedCommentDetailPresenter presenter = new FeedCommentDetailPresenter();
        FeedCommentDetailModel model = new FeedCommentDetailModel();
        presenter.initData(feed, comment);
        presenter.setModel(model);

        replyAdapter.setComment(comment);
        replyAdapter.setOnReplyListener(presenter);
        return presenter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().needRefreshOnResume = false;
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        replyAdapter = new ReplyAdapter(getActivity());
        getPullProxy().setAdapter(replyAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_comment, null);
        getPullProxy().getPullView().getRefreshableView().addHeaderView(headerView);
        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
        getPullProxy().getPullView().setBackgroundColor(getResources().getColor(R.color.color_bg2));
        commentHolder = new CommentHolder(getActivity(), headerView, presenter());
        commentHolder.setForCommentDetail(true);
        return root;
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        presenter().getReplyData(nextId);
    }

    //----------------- IFeedCommentDetail 方法 start ---------------

    @Override
    public void fillComment(Comment comment) {
        if (comment.replyList != null) {
            comment.replyList.clear();
        }
        commentHolder.fill(comment, true);
    }

    @Override
    public void onLoadSucessfully(ZHPageData<Reply> data) {
        super.onLoadSucessfully(data);
        if (data.pageIsLast) {
            pullView.setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            pullView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        }
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
    public void hideSendCommentView() {
        if (sendCommentView != null) {
            sendCommentView.hide();
        }
    }

    @Override
    public void showSendCommentView(SendCommentView.ToType toType, String toName, String subjectId, Long commentId, Long replyId) {
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
        sendCommentView.show(toType, toName, subjectId, commentId, replyId);
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
    public void refreshReplyView(List<Reply> replies) {
        replyAdapter.setData(replies);
        replyAdapter.notifyDataSetChanged();
    }

    //----------------- IFeedCommentDetail 方法 end ---------------

    //------------------------- private method start --------------------


    private SendCommentView.Callback callback = new SendCommentView.Callback() {

        @Override
        public void comment(String subJectId, String content) {
            //该页面不会触发该回调
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


    //------------------------- click action end --------------------
}