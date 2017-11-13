package com.zhisland.android.blog.info.view.impl;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.webview.WVWrapper;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.InfoDetailModel;
import com.zhisland.android.blog.info.presenter.InfoReadPresenter;
import com.zhisland.android.blog.info.presenter.InfoSocialPresenter;
import com.zhisland.android.blog.info.view.IInfoReadView;
import com.zhisland.android.blog.info.view.IInfoSocialView;
import com.zhisland.android.blog.common.comment.view.CommentAdapter;
import com.zhisland.android.blog.info.view.impl.holder.InfoReaderHolder;
import com.zhisland.android.blog.info.view.impl.holder.InfoSocialHolder;
import com.zhisland.android.blog.common.comment.view.SendCommentView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.file.FileMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资讯详情页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragInfoDetail extends FragPullListMvps<Comment> implements IInfoSocialView, IInfoReadView {

    /**
     * 资讯缓存的资源文件和数据库的路径
     */
    public final static String CACHE_PATH = FileMgr.Instance().getDir(FileMgr.DirType.HTML).getAbsolutePath();

    public final static String INTENT_KEY_INFO = "intent_key_info";

    private final String KEY_READ = InfoReadPresenter.class.getName();
    private final String KEY_SOCIAL = InfoSocialPresenter.class.getName();

    //阅读模式view
    private SwitchBtnCallBack switchBtnCallBack;
    private InfoReaderHolder readHolder = new InfoReaderHolder();
    private CommonDialog modeSwitchDlg;
    private WVWrapper wvWrapper;

    //社会化view
    private InfoSocialHolder socialHolder = new InfoSocialHolder();
    private SendCommentView sendCommentView;
    private Dialog dialog;
    private CommonDialog deleteDialog;
    private CommentAdapter commentAdapter;
    private View emptyView;

    private boolean upDownAnimShowing = false;
    private ValueAnimator bottomCommentAnim;

    //=================override abstarct methods================

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        ZHInfo inkInfo = (ZHInfo) getActivity().getIntent().getSerializableExtra(INTENT_KEY_INFO);
        Map<String, BasePresenter> map = new HashMap<>();
        InfoDetailModel model = new InfoDetailModel();
        InfoReadPresenter infoReadPresenter = new InfoReadPresenter();
        infoReadPresenter.setModel(model);
        infoReadPresenter.initData(inkInfo);
        map.put(KEY_READ, infoReadPresenter);

        InfoSocialPresenter infoSocialPresenter = new InfoSocialPresenter();
        infoSocialPresenter.setModel(model);
        infoSocialPresenter.setNewsId(inkInfo.newsId);
        map.put(KEY_SOCIAL, infoSocialPresenter);
        commentAdapter.setOnCommentListener(infoSocialPresenter);
        return map;
    }

    private InfoReadPresenter getReadPresenter() {
        return (InfoReadPresenter) presenter(KEY_READ);
    }

    private InfoSocialPresenter getSocialPresenter() {
        return (InfoSocialPresenter) presenter(KEY_SOCIAL);
    }

    @Override
    public void loadMore(String nextId) {
        getSocialPresenter().getCommentListFromInternet(nextId);
    }

    @Override
    public String getPageName() {
        return "InfomationDetail";
    }

    //==============life cycle methods============
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        commentAdapter = new CommentAdapter(getActivity());
        getPullProxy().setAdapter(commentAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_info_head, null);
        pullView.getRefreshableView().addHeaderView(headerView);

        ButterKnife.inject(this, root);
        ButterKnife.inject(readHolder, root);
        ButterKnife.inject(socialHolder, root);

        initView();
        initWebView();
        internalView.setOnScrollListener(scrollListener);
        return root;
    }

    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            getSocialPresenter().onScrolledComment(firstVisibleItem + visibleItemCount > 2);
        }
    };

    @Override
    public void showSendBottomView() {
        if (bottomCommentAnim != null) {
            bottomCommentAnim.cancel();
        }
        int curTranslationY = (int) socialHolder.rlSendComment.getTranslationY();
        bottomCommentAnim = ObjectAnimator.ofInt(curTranslationY, 0);
        bottomCommentAnim.setDuration((long) (250f * curTranslationY / DensityUtil.dip2px(50)));
        bottomCommentAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentDiff = (Integer) animation.getAnimatedValue();
                socialHolder.rlSendComment.setTranslationY(currentDiff);
                socialHolder.llSendComment.setVisibility(View.VISIBLE);
            }
        });
        bottomCommentAnim.start();
    }

    @Override
    public void hideSendBottomView() {
        if (bottomCommentAnim != null) {
            bottomCommentAnim.cancel();
        }
        int curTranslationY = (int) socialHolder.rlSendComment.getTranslationY();
        bottomCommentAnim = ObjectAnimator.ofInt(curTranslationY, DensityUtil.dip2px(50));
        bottomCommentAnim.setDuration((long) (250f * (DensityUtil.dip2px(50) - curTranslationY) / DensityUtil.dip2px(50)));
        bottomCommentAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentDiff = (Integer) animation.getAnimatedValue();
                socialHolder.rlSendComment.setTranslationY(currentDiff);
                socialHolder.llSendComment.setVisibility(View.GONE);
            }
        });
        bottomCommentAnim.start();
    }

    @Override
    protected View createDefaultFragView() {
        return getActivity().getLayoutInflater().inflate(
                R.layout.frag_info_detail, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.PAGE_NEWS_DETAIL);
    }

    private void initView() {
        sendCommentView = new SendCommentView(getActivity(), callback);
        pullView.setMode(PullToRefreshBase.Mode.DISABLED);
        pullView.getRefreshableView().setDividerHeight(0);
        pullView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    SendCommentView.Callback callback = new SendCommentView.Callback() {

        @Override
        public void comment(String newsId, String content) {
            getSocialPresenter().comment(newsId, content);
        }

        @Override
        public void commentReply(long commentId, String content) {
            getSocialPresenter().commentReply(commentId, null, content);
        }

        @Override
        public void commentReply(long commentId, long replyId, String content) {
            getSocialPresenter().commentReply(commentId, replyId, content);
        }
    };

    private void initWebView() {
        wvWrapper = new WVWrapper(readHolder.webView, false, true);
        //开启WebView的本地缓存功能，存入APPCACHE
        readHolder.webView.getSettings().setAppCacheEnabled(true);
        //设置缓存路径，5.0以后会自动管理
        readHolder.webView.getSettings().setAppCachePath(CACHE_PATH);

        readHolder.webView.setFocusable(false);
        wvWrapper.setWebView(readHolder.webView);
        wvWrapper.setWebListener(new WVWrapper.WebListener() {

            @Override
            public void showShare() {

            }

            @Override
            public void onPageStart() {

            }

            @Override
            public void onPageFinish() {
                if (getReadPresenter() != null) {
                    pullView.getRefreshableView().setSelectionFromTop(0, 0);
                    getReadPresenter().onContentLoadFinish();
                }
            }

            @Override
            public void onReceivedTitle(String title) {

            }

            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

            }

            @Override
            public void onHideCustomView() {

            }

            @Override
            public void loadError() {

            }
        });
    }

    //===============social view methods=======================

    @Override
    public void refreshCommentList() {
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCommentDlg(String text) {
        if (dialog == null) {
            dialog = makeProgressDlg();
        }
        TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
        tvText.setText(text);
        dialog.show();
    }

    @Override
    public void hideSendCommentView() {
        sendCommentView.hide();
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
        sendCommentView.show(toType, toName, String.valueOf(newsId), commentId, replyId);
    }

    @Override
    public void hideCommentDlg() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onloadSuccess(ZHPageData<Comment> data) {
        getPullProxy().onLoadSucessfully(data);
        if (data.pageIsLast) {
            pullView.setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            pullView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        }
    }

    @Override
    public void onLoadFailed(Throwable error) {
        getPullProxy().onLoadFailed(error);
        pullView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    @Override
    public void showUpDownView(int upCount, int downCount) {
        socialHolder.llBottom.setVisibility(View.VISIBLE);
        socialHolder.vLineBottom.setVisibility(View.VISIBLE);
        socialHolder.rlDown.setVisibility(View.VISIBLE);
        socialHolder.rlUp.setVisibility(View.VISIBLE);
        socialHolder.vLineUpDown.setVisibility(View.VISIBLE);
        socialHolder.tvUp.setText(String.valueOf(upCount));
        socialHolder.tvDown.setText(String.valueOf(downCount));
    }

    @Override
    public void setUpDownSelect(boolean upSelect, boolean downSelect) {
        if (upSelect) {
            socialHolder.rlUp.setBackgroundResource(R.drawable.bg_info_detail_up_down_selected);
            socialHolder.rlDown.setBackgroundResource(R.drawable.bg_info_detail_up_down);
            socialHolder.ivUp.setBackgroundResource(R.drawable.img_info_detail_up);
            socialHolder.ivDown.setBackgroundResource(R.drawable.img_info_detail_down_empty);
        } else if (downSelect) {
            socialHolder.rlUp.setBackgroundResource(R.drawable.bg_info_detail_up_down);
            socialHolder.rlDown.setBackgroundResource(R.drawable.bg_info_detail_up_down_selected);
            socialHolder.ivUp.setBackgroundResource(R.drawable.img_info_detail_up_empty);
            socialHolder.ivDown.setBackgroundResource(R.drawable.img_info_detail_down);
        } else {
            socialHolder.rlUp.setBackgroundResource(R.drawable.bg_info_detail_up_down);
            socialHolder.rlDown.setBackgroundResource(R.drawable.bg_info_detail_up_down);
            socialHolder.ivUp.setBackgroundResource(R.drawable.img_info_detail_up_empty);
            socialHolder.ivDown.setBackgroundResource(R.drawable.img_info_detail_down_empty);
        }
    }

    @Override
    public void setCommentCount(int count) {
        if (count > 0) {
            socialHolder.tvCommentCount.setText(String.valueOf(count));
            socialHolder.tvCommentCount.setVisibility(View.VISIBLE);
        } else {
            socialHolder.tvCommentCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void toastCommentSuccess() {
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_info_dlg, null);
        toast.setView(view);
        toast.show();
    }

    @Override
    public void showComment() {
        setCommentEmptyView();
        readHolder.llReport.setVisibility(View.VISIBLE);
        socialHolder.llComment.setVisibility(View.VISIBLE);
        pullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideModeDlg() {
        if (modeSwitchDlg != null) {
            modeSwitchDlg.dismiss();
        }
    }

    @Override
    public Comment getCommentItem(long commentId) {
        List<Comment> data = commentAdapter.getData();
        if (data != null) {
            for (Comment comment : data) {
                if (comment.commentId == commentId) {
                    return comment;
                }
            }
        }
        return null;
    }

    @Override
    public void addCommentItemAtFirst(Comment comment) {
        commentAdapter.insert(comment);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeCommentItem(Comment comment) {
        commentAdapter.removeItem(comment);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDeleteDialog(final Comment comment, final Reply reply) {
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
            deleteDialog.setRightBtnColor(getResources()
                    .getColor(R.color.color_ac));
            deleteDialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                    if (reply == null) {
                        getSocialPresenter().deleteComment(comment);
                    } else {
                        getSocialPresenter().deleteReply(comment, reply);
                    }
                }
            });
        }
    }

    @Override
    public void showUpDownAnim(final int upCount, final int downCount, final boolean hide) {
        upDownAnimShowing = true;
        ValueAnimator va = null;
        if (hide) {
            va = ObjectAnimator.ofFloat(1f, 0f);
            va.setStartDelay(1000);
            va.setDuration(500);
        } else {
            va = ObjectAnimator.ofFloat(0f, 1f);
            va.setDuration(500);
        }
        final float upScale = 1f + (upCount + 0.005f) / (upCount + downCount + 0.01f) * 3;
        final float downScale = 1f + (downCount + 0.005f) / (upCount + downCount + 0.01f) * 3;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                socialHolder.rlUp.getLayoutParams().height = (int) (DensityUtil.dip2px(36) * (1 + cVal * upScale));
                socialHolder.rlDown.getLayoutParams().height = (int) (DensityUtil.dip2px(36) * (1 + cVal * downScale));
                socialHolder.rlUp.requestLayout();
                if (cVal * upScale > 1f) {
                    socialHolder.tvUp.setVisibility(View.VISIBLE);
                    socialHolder.tvUpText.setVisibility(View.VISIBLE);
                } else {
                    socialHolder.tvUp.setVisibility(View.GONE);
                    socialHolder.tvUpText.setVisibility(View.GONE);
                }
                if (cVal * downScale > 1f) {
                    socialHolder.tvDown.setVisibility(View.VISIBLE);
                    socialHolder.tvDownText.setVisibility(View.VISIBLE);
                } else {
                    socialHolder.tvDown.setVisibility(View.GONE);
                    socialHolder.tvDownText.setVisibility(View.GONE);
                }
                if (hide) {
                    if (cVal <= 0.01f) {
                        upDownAnimShowing = false;
                    }
                } else {
                    if (cVal >= 0.99f) {
                        showUpDownAnim(upCount, downCount, true);
                    }
                }
            }
        });
        va.start();
    }

    // =============read view implements============
    @Override
    public void loadContentRead(String content) {
        wvWrapper.loadData(content);
    }

    @Override
    public void loadContentUrl(String url) {
        wvWrapper.loadUrl(url);
    }

    @Override
    public void setSwitchShowStatue(boolean show) {
        if (switchBtnCallBack != null) {
            switchBtnCallBack.setVisibility(show);
        }
    }

    @Override
    public void setSwitchRead() {
        if (switchBtnCallBack != null) {
            switchBtnCallBack.setSwitchRead();
        }
    }

    @Override
    public void setSwitchUrl() {
        if (switchBtnCallBack != null) {
            switchBtnCallBack.setSwitchUrl();
        }
    }

    @Override
    public void setFromName(String name) {
        readHolder.tvFrom.setText(name);
    }

    @Override
    public void showToReadDlg() {
        if (modeSwitchDlg == null) {
            modeSwitchDlg = new CommonDialog(getActivity());
            modeSwitchDlg.setCanceledOnTouchOutside(false);
        }
        modeSwitchDlg.show();
        modeSwitchDlg.setTitle("原网页加载较慢，\n试试阅读模式");
        modeSwitchDlg.setRightBtnContent("阅读模式");
        modeSwitchDlg.setLeftBtnGone();
        modeSwitchDlg.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReadPresenter().onSwitchDlgBtnClicked();
                modeSwitchDlg.dismiss();
            }
        });
    }

    @Override
    public void gotoProfileAct(long uid) {
        ActProfileDetail.invoke(getActivity(), uid);
    }

    @Override
    public void shareData(ZHInfo info) {
        getSocialPresenter().setInfo(info);
        loadInfoCover(info);
    }

    @Override
    public void onReadConentloaded() {
        getSocialPresenter().onReadConentloaded();
    }

    @Override
    public void hideListView() {
        pullView.setVisibility(View.GONE);
    }

    @Override
    public void gotoCommentDetail(Comment comment, ZHInfo info) {
        FragCommentDetail.invoke(getActivity(), info, comment);
    }

    @Override
    public void showShareView(ZHInfo info) {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_NEWS_SHARE);
        DialogUtil.getInstatnce().showInfoDialog(getActivity(), info, getPageName());
    }

    /**
     * 生成一个进度对话框
     */
    private Dialog makeProgressDlg() {
        Dialog dialog = new Dialog(getActivity(), R.style.PROGRESS_DIALOG);
        dialog.setContentView(R.layout.layout_info_dlg);
        ImageView ivOk = (ImageView) dialog.findViewById(R.id.ivOk);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        ivOk.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        return dialog;
    }

    /**
     * 资讯分享
     */
    public void onShareClicked() {
        getSocialPresenter().onShareClicked();
    }

    /**
     * 预加载资讯图，用于分享用
     */
    private void loadInfoCover(ZHInfo info) {
        if (info != null) {
            ImageView imageView = new ImageView(getActivity());
            ImageWorkFactory.getFetcher().loadImage(info.coverSmall, imageView);
        }
    }

    private void setCommentEmptyView(){
        if (emptyView == null) {
            emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.item_comment_foot, null);
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSocialPresenter().wantSendComment(SendCommentView.ToType.subject, null, null, null);
                }
            });
        }
        commentAdapter.setEmptyView(emptyView);
    }

    @OnClick(R.id.rlUp)
    void upClick() {
        if (!upDownAnimShowing) {
            getSocialPresenter().infoUp();
        }
    }

    @OnClick(R.id.rlDown)
    void downClick() {
        if (!upDownAnimShowing) {
            getSocialPresenter().infoDown();
        }
    }

    @OnClick(R.id.llCommentCount)
    void commentCountClick() {
        pullView.getRefreshableView().setSelectionFromTop(2, DensityUtil.dip2px(60));
    }

    @OnClick(R.id.rlSendComment)
    void sendCommentClick() {
        getSocialPresenter().wantSendComment(SendCommentView.ToType.subject, null, null, null);
    }

    @OnClick(R.id.tvReport)
    void reportClick() {
        getReadPresenter().onReportClicked();
    }

    @OnClick(R.id.tvFrom)
    void onFromClick() {
//        getReadPresenter().onFromClick();
    }

    @Override
    public void gotoReport(ZHInfo info) {
        ActReportType.invoke(getActivity(), info);
    }

    public void onReadModeClick() {
        getReadPresenter().onReadModeClick();
    }

    public void onUrlModeClick() {
        getReadPresenter().onUrlModeClick();
    }

    public void setSwitchBtnCallBack(SwitchBtnCallBack switchBtnCallBack) {
        this.switchBtnCallBack = switchBtnCallBack;
    }

    public interface SwitchBtnCallBack {

        public void setVisibility(boolean visibility);

        public void setSwitchRead();

        public void setSwitchUrl();
    }

}