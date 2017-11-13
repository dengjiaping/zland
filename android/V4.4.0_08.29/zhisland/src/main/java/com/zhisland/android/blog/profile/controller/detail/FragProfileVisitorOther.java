package com.zhisland.android.blog.profile.controller.detail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.base.DefaultTitleBarClickListener;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.webview.ActionDialog;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 访客个人主页（别人） fragment
 */
public class FragProfileVisitorOther extends FragBase implements IProfileView {

    private static final int TAG_LEFT_BTN = 100;
    private static final int TAG_RIGHT_BTN = 111;

    @InjectView(R.id.ivAvatar)
    ImageView ivAvatar;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvComAndPos)
    TextView tvComAndPos;

    @InjectView(R.id.vLineBottom)
    View vLineBottom;

    @InjectView(R.id.tvHasAttention)
    TextView tvHasAttention;

    private TitleBarProxy titleBar;
    private Dialog actionDialog;
    private Dialog reportDlg;

    UserDetail userDetail;

    private boolean viewInitOver = false;

    @Override
    public String getPageName() {
        return "FragProfileVisitorOther";
    }

    @Override
    public void onUpdate(UserDetail userDetail) {
        this.userDetail = userDetail;
        if (viewInitOver) {
            onUpdate();
        }
    }

    @Override
    public void onLoadError(Throwable error) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_profile_visitor_other, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(lp);
        ButterKnife.inject(this, rootView);

        titleBar = new TitleBarProxy();
        titleBar.configTitle(rootView, TitleType.TITLE_LAYOUT, new DefaultTitleBarClickListener(getActivity()) {
            @Override
            public void onTitleClicked(View view, int tagId) {
                switch (tagId) {
                    case TAG_LEFT_BTN:
                        getActivity().finish();
                        break;
                    case TAG_RIGHT_BTN:
                        if (userDetail == null || userDetail.user == null) {
                            return;
                        }
                        showReportAction();
                        break;
                }
            }
        });
        ImageView leftBtn = TitleCreator.Instance().createImageButton(getActivity(), R.drawable.sel_btn_back_white);
        titleBar.addLeftTitleButton(leftBtn, TAG_LEFT_BTN);
        ImageView rightBtn = TitleCreator.Instance().createImageButton(getActivity(), R.drawable.sel_btn_feed_more);
        titleBar.addRightTitleButton(rightBtn, TAG_RIGHT_BTN);
        titleBar.hideBottomLine();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewInitOver = true;
        onUpdate();
    }

    private void onUpdate() {
        if (userDetail != null) {
            if (userDetail.user != null) {
                tvName.setText(userDetail.user.name);
                tvComAndPos.setText(userDetail.user.combineCompanyAndPosition());
                int resId = R.drawable.avatar_default_man;
                if (userDetail.user.sex != null && userDetail.user.sex == User.VALUE_SEX_WOMAN) {
                    resId = R.drawable.avatar_default_woman;
                }
                ImageWorkFactory.getCircleFetcher().loadImage(userDetail.user.userAvatar, ivAvatar, resId, ImageWorker.ImgSizeEnum.SMALL);
            }

            //TODO
            boolean isAtt = false;
            vLineBottom.setVisibility(isAtt ? View.VISIBLE : View.INVISIBLE);
            tvHasAttention.setVisibility(isAtt ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void showReportAction() {
        if (actionDialog != null && actionDialog.isShowing()) {
            return;
        }
        getReportReasonTask();
        ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
        actions.add(new ActionItem(1, R.color.color_dc, "我要举报"));
        actionDialog = DialogUtil.createActionSheet(getActivity(), "", "取消",
                actions, new ActionDialog.OnActionClick() {

                    @Override
                    public void onClick(DialogInterface df, int id,
                                        ActionItem item) {
                        if (actionDialog != null && actionDialog.isShowing()) {
                            actionDialog.dismiss();
                        }
                        switch (id) {
                            case 1:
                                showReportDialog();
                                break;
                        }
                    }
                });

        actionDialog.show();
    }

    /**
     * 举报dialog
     */
    private void showReportDialog() {
        final ArrayList<Country> datas = (ArrayList<Country>) DBMgr.getMgr().getCacheDao().get(ActProfileDetail.CACH_REPORT_REASON_LIST);
        if (datas == null) {
            showToast("举报原因拉取失败");
            return;
        }
        if (reportDlg == null) {
            reportDlg = new Dialog(getActivity(), R.style.DialogGuest);
            reportDlg.setContentView(R.layout.profile_report_user);
            reportDlg.setCancelable(true);

            ListView listView = (ListView) reportDlg
                    .findViewById(R.id.lvReportReason);
            listView.setAdapter(new ReportReasonAdapter(getActivity(),
                    datas));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    reportUserTask(datas.get(position).code);
                }
            });

            TextView tvReportTitle = (TextView) reportDlg
                    .findViewById(R.id.tvReportTitle);
            tvReportTitle.setText("我要举报 " + userDetail.user.name);

            Window dialogWindow = reportDlg.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = DensityUtil.getWidth() - DensityUtil.dip2px(70); // 宽度
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setAttributes(lp);
        }
        if (!reportDlg.isShowing()) {
            reportDlg.show();
        }
    }

    /**
     * 获取举报原因
     */
    private void getReportReasonTask() {
        ZHApis.getProfileApi().getReportReasonList(getActivity(),
                new TaskCallback<ArrayList<Country>>() {

                    @Override
                    public void onSuccess(ArrayList<Country> content) {
                        DBMgr.getMgr().getCacheDao().set(ActProfileDetail.CACH_REPORT_REASON_LIST, content);
                    }

                    @Override
                    public void onFailure(Throwable error) {

                    }
                });
    }

    /**
     * 举报用户task
     */
    private void reportUserTask(String reason) {
        showProgressDlg();
        ZHApis.getProfileApi().reportUser(getActivity(), userDetail.user.uid, reason,
                new TaskCallback<Object>() {

                    @Override
                    public void onSuccess(Object content) {
                        showToast("举报成功");
                        if (reportDlg != null && reportDlg.isShowing()) {
                            reportDlg.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        showToast("举报失败");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideProgressDlg();
                    }
                });
    }
}
