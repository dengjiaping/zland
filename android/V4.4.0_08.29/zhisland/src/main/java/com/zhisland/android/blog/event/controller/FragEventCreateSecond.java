package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.feed.bean.Attach;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 活动编辑第二页
 */
public class FragEventCreateSecond extends FragBase {

    Event event;

    @InjectView(R.id.tvWho)
    TextView tvWho;

    @InjectView(R.id.etNumber)
    EditText etNumber;

    @InjectView(R.id.etCost)
    EditText etCost;

    @InjectView(R.id.internationalPhoneView)
    InternationalPhoneView internationalPhoneView;

    @InjectView(R.id.ivPublic)
    ImageView ivPublic;

    Dialog dlgLimitLevel;
    private boolean isShare = false;

    @Override
    public String getPageName() {
        return "EventCreateConfig";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_create_event_second, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        initView();
        return root;
    }

    private void initView() {
        internationalPhoneView.setEditHint("输入手机号码（选填）");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        event = (Event) getActivity().getIntent().getSerializableExtra(
                ActEventCreate.KEY_EVENT);
        if (event == null) {
            event = new Event();
        } else {
            fillEvent();
        }
    }

    /**
     * 将event中的数据填到View上
     */
    private void fillEvent() {
        setLimitLevelView();
        if (event.totalNum != null && event.totalNum > 0) {
            etNumber.setText("" + event.totalNum);
        }

        if (event.price != null && event.price > 0) {
            etCost.setText("" + event.price.intValue());
        }

        if (!StringUtil.isNullOrEmpty(event.contactMobile)) {
            internationalPhoneView.setDictMobile(event.contactMobile);
        }

        if (event.displayLevel == null) {
            event.displayLevel = Event.DISPLAY_LEVEL_PUBLIC;
        }
        switch (event.displayLevel) {
            case Event.DISPLAY_LEVEL_PRIVATE:
                ivPublic.setBackgroundResource(R.drawable.switch_off);
                break;
            case Event.DISPLAY_LEVEL_PUBLIC:
                ivPublic.setBackgroundResource(R.drawable.switch_on);
                break;
        }
    }

    private void setLimitLevelView() {
        if (event.userLimitLevel == null) {
            event.userLimitLevel = Event.LIMIT_LEVEL_ALL;
        }
        switch (event.userLimitLevel) {
            case Event.LIMIT_LEVEL_ALL:
                tvWho.setText("全部用户");
                break;
            case Event.LIMIT_LEVEL_VIP:
                tvWho.setText("仅限岛邻");
                break;
            case Event.LIMIT_LEVEL_VIP_AND_INVITE:
                tvWho.setText("岛邻及海客");
                break;
        }
    }

    @OnClick(R.id.tvWho)
    void onLimitLevelClick() {
        showLimitLevelDialog();
    }

    private void showLimitLevelDialog() {
        if (dlgLimitLevel == null) {
            dlgLimitLevel = new Dialog(getActivity(), R.style.ActionDialog);
            View contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.dlg_limit_level, null);
            contentView.findViewById(R.id.tvAll).setOnClickListener(
                    dlgClickListener);
            contentView.findViewById(R.id.tvVipAndInvite).setOnClickListener(
                    dlgClickListener);
            contentView.findViewById(R.id.tvVip).setOnClickListener(
                    dlgClickListener);
            dlgLimitLevel.setContentView(contentView);
            WindowManager.LayoutParams wmlp = dlgLimitLevel.getWindow()
                    .getAttributes();
            wmlp.gravity = Gravity.BOTTOM;
            wmlp.width = DensityUtil.getWidth();
        }
        dlgLimitLevel.show();
    }

    OnClickListener dlgClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAll:
                    event.userLimitLevel = Event.LIMIT_LEVEL_ALL;
                    break;
                case R.id.tvVipAndInvite:
                    event.userLimitLevel = Event.LIMIT_LEVEL_VIP_AND_INVITE;
                    break;
                case R.id.tvVip:
                    event.userLimitLevel = Event.LIMIT_LEVEL_VIP;
                    break;
            }
            setLimitLevelView();
            dlgLimitLevel.dismiss();
        }
    };

    @OnClick(R.id.llPublic)
    void onPublicClick() {
        switch (event.displayLevel) {
            case Event.DISPLAY_LEVEL_PRIVATE:
                event.displayLevel = Event.DISPLAY_LEVEL_PUBLIC;
                ivPublic.setBackgroundResource(R.drawable.switch_on);
                break;
            case Event.DISPLAY_LEVEL_PUBLIC:
                event.displayLevel = Event.DISPLAY_LEVEL_PRIVATE;
                ivPublic.setBackgroundResource(R.drawable.switch_off);
                break;
        }
    }

    /**
     * 发布或修改活动
     */
    public void createOrUpdateEvent(boolean isCreat) {
        showProgressDlg("请求中...");
        getViewInfoToEvent();
        Integer displayLevel = event.displayLevel;
        if (isCreat) {
            if (isShare && displayLevel != null
                    && displayLevel == Event.DISPLAY_LEVEL_PUBLIC) {
                createShareEvent();
            } else {
                createEvent();
            }
        } else {
            updateEvent();
        }
    }

    /**
     * 发布活动
     */
    private void createShareEvent() {

        final Integer displayLevel = event.displayLevel;
        ZHApis.getEventApi().createShareEvent(getActivity(), event, new TaskCallback<Feed>() {

            @Override
            public void onSuccess(Feed content) {
                if (content == null)
                    return;

                content.action = EbAction.ADD;
                RxBus.getDefault().post(content);

                if (getActivity() == null) {
                    return;
                }
                getActivity().finish();
                Attach attach = (Attach) content.attach;
                try {
                    Uri uri = Uri.parse(attach.uri);
                    long eventId = Long.parseLong(uri.getLastPathSegment());
                    if (displayLevel != null
                            && displayLevel == Event.DISPLAY_LEVEL_PRIVATE) {
                        ActEventDetail.invoke(getActivity(), eventId, true);
                        getActivity().finish();
                    } else {
                        FragEventSpread.invoke(getActivity(), eventId, FragEventSpread.FROM_SHARE);
                        getActivity().finish();
                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void onFailure(Throwable error) {
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isAdded() || !isDetached()) {
                    hideProgressDlg();
                }
            }
        });
    }

    /**
     * 发布活动
     */
    private void createEvent() {

        final Integer displayLevel = event.displayLevel;
        ZHApis.getEventApi().createEvent(getActivity(), event, new TaskCallback<Event>() {

            @Override
            public void onSuccess(Event content) {
                if (getActivity() == null) {
                    return;
                }
                if (displayLevel != null
                        && displayLevel == Event.DISPLAY_LEVEL_PRIVATE) {
                    ActEventDetail.invoke(getActivity(), content.eventId, true);
                    getActivity().finish();
                } else {
                    FragEventSpread.invoke(getActivity(), content.eventId, FragEventSpread.FROM_NORMAL);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Throwable error) {
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isAdded() || !isDetached()) {
                    hideProgressDlg();
                }
            }
        });
    }

    /**
     * 修改活动
     */
    private void updateEvent() {
        ZHApis.getEventApi().updateEvent(getActivity(), event, new TaskCallback<Object>() {

            @Override
            public void onSuccess(Object content) {
                showToast("修改活动成功");
                if (getActivity() != null) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Throwable error) {
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isAdded() || !isDetached()) {
                    hideProgressDlg();
                }
            }
        });
    }

    /**
     * 将页面上填写的信息保存到event
     */
    private void getViewInfoToEvent() {
        String totalNum = etNumber.getText().toString();
        String cost = etCost.getText().toString();
        if (!StringUtil.isNullOrEmpty(internationalPhoneView.getMobileNum())) {
            event.contactMobile = internationalPhoneView.getDictMobile();
        } else {
            event.contactMobile = "";
        }
        try {
            if (!StringUtil.isNullOrEmpty(totalNum)) {
                event.totalNum = Integer.parseInt(totalNum);
            } else {
                event.totalNum = null;
            }
        } catch (Exception e) {

        }
        try {
            if (!StringUtil.isNullOrEmpty(cost)) {
                event.price = Float.parseFloat(cost);
            } else {
                event.price = null;
            }
        } catch (Exception e) {

        }

    }

    /**
     * 将event保存到intent
     */
    public void setEventToIntent() {
        getViewInfoToEvent();
        getActivity().getIntent().putExtra(ActEventCreate.KEY_EVENT, event);
    }

    /**
     * 检查输入的内容是否合法
     */
    public boolean checkInputContent() {
        String phone = internationalPhoneView.getMobileNum();
        if (StringUtil.isNullOrEmpty(phone)) {
            return true;
        }
        if (!internationalPhoneView.checkInput()) {
            showToast("您输入的手机号不正确");
            return false;
        }
        return true;
    }

    //是否是从新鲜事发布跳转过来的
    public void setShare(boolean isShare) {
        this.isShare = isShare;
    }
}
