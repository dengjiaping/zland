package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.android.blog.im.view.adapter.ImSessAdapter.OnAdapterListener;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.im.data.IMMessage;
import com.zhisland.im.data.IMUser;
import com.zhisland.im.util.Constants;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

/**
 * the base view of im sess row, and implements onRowListener to interact with
 * data.
 * <p/>
 * self layout: error->sending->container->avator
 * <p/>
 * other layout: avator->container->sending->error
 */
public abstract class BaseRowView extends RelativeLayout implements
        android.view.View.OnClickListener, OnRowListener, OnTouchListener {

    static final int AVATOR_LENGTH = DensityUtil.dip2px(40);
    static final int MAX_LENGTH = DensityUtil.getWidth() - AVATOR_LENGTH * 5
            / 2;
    static final int PAD_REPLY_HOR = DensityUtil.dip2px(8);
    static final int PAD_REPLY_VER = DensityUtil.dip2px(5);

    /**
     * 普通的文本消息，饮品消息等
     */
    public static final int CONTENT_TYPE_NORMAL = 0;
    /**
     * 适合内容是块状的布局
     */
    public static final int CONTENT_TYPE_BLOCK = 1;
    /**
     * 对内容不做人和预设
     */
    public static final int CONTENT_TYPE_NONE = 2;

    protected Context context;
    protected LayoutInflater inflater;
    protected ImageView ivAvator;
    protected TextView tvName;
    protected ProgressBar pbSending;
    protected ImageView ivError;
    protected MaxWidthLinearLayout container;
    protected TextView replyView;

    private final LayoutParams paramAvator = new LayoutParams(AVATOR_LENGTH,
            AVATOR_LENGTH);
    private final LayoutParams paramName = new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private final LayoutParams paramContainer = new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private final LayoutParams paramSending = new LayoutParams(
            DensityUtil.dip2px(15), DensityUtil.dip2px(15));
    private final LayoutParams paramError = new LayoutParams(
            DensityUtil.dip2px(15), DensityUtil.dip2px(15));
    private final LayoutParams paramReply = new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    protected IMMessage msg;
    protected IMUser userSelf;
    protected IMUser userOther;
    protected boolean showReply = false;
    protected int contentType = CONTENT_TYPE_NORMAL;

    private OnCreateContextMenuListener menuListener;
    /**
     * callback to session list adapter, such at, reply
     */
    protected OnAdapterListener adapterListener;
    private final OnLongClickListener longClickListener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            if (adapterListener != null) {
                adapterListener.onUserAt(userOther.name);
            }
            return true;
        }
    };

    public BaseRowView(Context context, int contentType) {
        super(context);
        this.contentType = contentType;
        initViews(context);
    }

    @Override
    public void setUsers(IMUser userSelf, IMUser userOther) {
        this.userSelf = userSelf;
        this.userOther = userOther;
    }

    private void initViews(Context context) {
        this.context = context;
        int padding = DensityUtil.dip2px(10);
        this.setPadding(padding, padding, padding, padding);
        this.setTag(this);

        inflater = LayoutInflater.from(context);

        ivAvator = new ImageView(context);
        ivAvator.setId(R.id.chat_row_avator);
        tvName = new TextView(context);
        tvName.setTextColor(Color.DKGRAY);
        tvName.setId(R.id.chat_row_name);
        pbSending = new ProgressBar(context);
        pbSending.setId(R.id.chat_row_pro_sending);
        pbSending.setProgressDrawable(getResources().getDrawable(
                R.drawable.img_loading_grey));
        ivError = new ImageView(context);
        ivError.setImageResource(R.drawable.sel_chat_resend);
        ivError.setId(R.id.chat_row_error);
        container = new MaxWidthLinearLayout(context);
        container.setId(R.id.chat_row_container);
        container.setMaxWidth(MAX_LENGTH);
        replyView = new TextView(context);
        replyView.setId(R.id.chat_reply);
        replyView.setBackgroundResource(R.drawable.sel_bg_btn_bgreen);
        replyView.setTextColor(Color.WHITE);
        DensityUtil.setTextSize(replyView, R.dimen.txt_16);
        replyView.setVisibility(View.GONE);
        replyView.setPadding(PAD_REPLY_HOR, PAD_REPLY_VER, PAD_REPLY_HOR,
                PAD_REPLY_VER);

        paramError.addRule(RelativeLayout.CENTER_VERTICAL);
        paramSending.addRule(RelativeLayout.CENTER_VERTICAL);

        this.addView(ivError, paramError);
        this.addView(pbSending, paramSending);
        this.addView(container, paramContainer);
        this.addView(ivAvator, paramAvator);
        this.addView(tvName, paramName);
        this.addView(replyView, paramReply);

        addContentView(container, context);

        // disable its click event, and children has no focus on
        this.setOnTouchListener(this);
        this.setOnClickListener(null);

        ivAvator.setOnClickListener(this);
        pbSending.setOnClickListener(this);
        ivError.setOnClickListener(this);
        container.setOnClickListener(this);
        replyView.setOnClickListener(this);
    }

    /**
     * change order of views to display "my" views
     */
    public void configMyView() {

        resetParams();

        BaseRowUtil.configSelfBackground(container, contentType);
        tvName.setVisibility(View.GONE);
        replyView.setVisibility(View.GONE);
        ivAvator.setVisibility(View.VISIBLE);

        paramAvator.addRule(ALIGN_PARENT_RIGHT);
        paramAvator.addRule(ALIGN_PARENT_TOP);

        paramContainer.addRule(LEFT_OF, R.id.chat_row_avator);
        paramSending.addRule(LEFT_OF, R.id.chat_row_container);
        paramError.addRule(LEFT_OF, R.id.chat_row_pro_sending);

        int margin = DensityUtil.dip2px(5);
        paramContainer.rightMargin = margin;
        paramSending.rightMargin = margin;
        paramError.rightMargin = margin;
    }

    /**
     * change order of views to display "other" views
     */
    public void configOtherView() {

        resetParams();

        BaseRowUtil.configOtherBackground(container, contentType);
        tvName.setVisibility(View.GONE);
        ivAvator.setVisibility(View.VISIBLE);

        paramAvator.addRule(ALIGN_PARENT_LEFT);
        paramAvator.addRule(ALIGN_PARENT_TOP);
        paramContainer.addRule(RIGHT_OF, R.id.chat_row_avator);
        paramSending.addRule(RIGHT_OF, R.id.chat_row_container);
        paramSending.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        paramSending.bottomMargin = DensityUtil.dip2px(7);
        paramError.addRule(RIGHT_OF, R.id.chat_row_pro_sending);

        int margin = DensityUtil.dip2px(5);
        paramContainer.leftMargin = margin;
        paramSending.leftMargin = margin;
        paramError.leftMargin = margin;
    }

    private void resetParams() {
        paramContainer.addRule(BELOW, 0);
        paramName.addRule(ALIGN_PARENT_TOP, 0);
        paramName.addRule(RIGHT_OF, 0);

        paramAvator.addRule(ALIGN_PARENT_LEFT, 0);
        paramAvator.addRule(ALIGN_PARENT_TOP, 0);
        paramContainer.addRule(RIGHT_OF, 0);
        paramSending.addRule(RIGHT_OF, 0);
        paramError.addRule(RIGHT_OF, 0);

        paramAvator.addRule(ALIGN_PARENT_RIGHT, 0);
        paramAvator.addRule(ALIGN_PARENT_TOP, 0);
        paramContainer.addRule(LEFT_OF, 0);
        paramContainer.addRule(ALIGN_PARENT_RIGHT, 0);
        paramSending.addRule(LEFT_OF, 0);
        paramSending.addRule(ALIGN_PARENT_BOTTOM, 0);
        paramSending.bottomMargin = 0;
        paramError.addRule(LEFT_OF, 0);

        paramReply.addRule(BELOW, 0);
        paramReply.addRule(ALIGN_LEFT, 0);
    }

    /**
     * add your own content view to container. and will be called only once
     */
    public abstract void addContentView(MaxWidthLinearLayout container,
                                        Context context);

    @Override
    public void onClick(View view) {
        if (view == this)
            return;
        int id = view.getId();
        switch (id) {
            case R.id.chat_row_avator:
                if (msg.isSendBySelf()) {
                    ActProfileDetail.invoke(context, PrefUtil.Instance().getUserId());
                } else {
                    ActProfileDetail.invoke(context, IMUser.parseUid(msg.contact));
                }
                break;
            case R.id.chat_row_error:
                resendMsg(msg);
                break;
            case R.id.chat_reply: {
                adapterListener.onReplyClicked(msg);
                break;
            }
        }
    }

    // ========= override of onRowListener========
    @Override
    public void fill(IMMessage msg) {
        this.msg = msg;
        this.pbSending.setVisibility(View.GONE);
        this.ivError.setVisibility(View.GONE);

        fillMenu(container);

        if (msg.direction == Constants.MSG_OUTGOING) {

            configMyView();
            if (userSelf != null) {
                ImageWorkFactory.getCircleFetcher().loadImage(
                        userSelf.avatar, ivAvator,
                        R.drawable.avatar_default);
            } else {
                // TODO load from preference
                ImageWorkFactory.getCircleFetcher().setImageResource(ivAvator,
                        R.drawable.avatar_default);
            }

            switch (msg.type) {
                case Constants.MSG_TYPE_TEXT: {
                    refreshStatus(false);
                }
                // case IMProtocolConstant.IMMessageTypeForwardNews:
                // case IMProtocolConstant.IMMessageTypeVCard:
                // case IMProtocolConstant.IMMessageTypeNormal:
                // case IMProtocolConstant.IMMessageTypeLocation:
                // case IMProtocolConstant.IMMessageTypeFriendInvition:
                // case IMProtocolConstant.IMMessageTypeEmotion:
                // refreshStatus(false);
            }
        } else {
            configOtherView();
            // if (shouldShowReply()) {
            // String timeString = StringUtil.getTimeString(msg.getMillions());
            // String text = String.format("%s %s",
            // msg.getProperty().nickname, timeString);
            // replyView.setText(text);
            // }

            /**
             * if don't use long click, and menu display nothing, onclick event
             * will be invoked.
             */
            ivAvator.setOnLongClickListener(longClickListener);
            if (userOther != null) {
                ImageWorkFactory.getCircleFetcher().loadImage(
                        userOther.avatar, ivAvator,
                        R.drawable.avatar_default);
                // if (msg.groupMessage) {
                // tvName.setText(userOther.nickname);
                // }

                // TODO group message
            } else {
                ImageWorkFactory.getCircleFetcher().setImageResource(ivAvator,
                        R.drawable.avatar_default);
            }
        }

    }

    @Override
    public void recycle() {
        this.msg = null;
        ivAvator.setOnLongClickListener(null);
        cleanMenu(container);
        replyView.setVisibility(View.GONE);
    }

    @Override
    public final IMMessage getMessage() {
        return msg;
    }

    public void refreshStatus(boolean refreshFieldFromDb) {
        if (refreshFieldFromDb) {
            // msg.refreshFromDb();
            // TODO refresh from db
        }
        // TODO refresh status
        // switch (msg.state) {
        // case IMMessage.MSG_STATE_RECEIVING:
        // case IMMessage.MSG_STATE_SENDING: {
        // ivError.setVisibility(View.GONE);
        // pbSending.setVisibility(View.VISIBLE);
        // break;
        // }
        // case IMMessage.MSG_STATE_ERROR: {
        // ivError.setVisibility(View.VISIBLE);
        // pbSending.setVisibility(View.GONE);
        // break;
        // }
        // case IMMessage.MSG_STATE_FINISHED: {
        // ivError.setVisibility(View.GONE);
        // pbSending.setVisibility(View.GONE);
        // break;
        // }
        // }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void performOnClick() {
        this.container.performClick();
    }

    @Override
    public void setOnCreateMenuListener(
            OnCreateContextMenuListener createMenuListener) {
        container.setOnCreateContextMenuListener(createMenuListener);
        this.menuListener = createMenuListener;
    }

    @Override
    public void setOnAdapterListener(OnAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    /**
     * set onCreateMenulistener and message tag
     */
    protected void fillMenu(View view) {
        BaseRowUtil.fillMenu(view, menuListener, msg, this);
    }

    /**
     * clean onCreateMenulistener and message tag
     */
    protected void cleanMenu(View view) {
        BaseRowUtil.cleanMenu(view);
    }

    /**
     * resend messages
     */
    protected void resendMsg(IMMessage msg) {
        if (msg == null)
            return;

        // TODO 重新发送
        // switch (msg.messgeType) {
        // case IMProtocolConstant.IMMessageTypePicture:
        // case IMProtocolConstant.IMMessageTypeVideo: {
        // SendUtil.resendMediaMessage(context, msg);
        // break;
        // }
        // case IMProtocolConstant.IMMessageTypeAudio: {
        // SendUtil.resendGroupAudio(context, msg);
        // break;
        // }
        // case IMProtocolConstant.IMMessageTypeDataAudio: {
        // SendUtil.resendAudio(context, msg);
        // break;
        // }
        // case IMProtocolConstant.IMMessageTypeLocation: {
        // SendUtil.resendLocation(context, msg);
        // break;
        // }
        // case IMProtocolConstant.IMMessageTypeVCard: {
        // SendUtil.resendVcard(context, msg);
        // break;
        // }
        // case IMProtocolConstant.IMMessageTypeNormal: {
        // SendUtil.resendTextMessage(context, msg);
        // break;
        // }
        // case IMProtocolConstant.IMMessageTypeEmotion: {
        // SendUtil.resendEmotionMessage(context, msg);
        // break;
        // }
        // default:
        // break;
        // }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // if (event.getAction() == MotionEvent.ACTION_DOWN) {
        // if (context instanceof ChatSessionFragActivity) {
        // ChatSessionFragActivity activity = (ChatSessionFragActivity) context;
        // activity.fragChat.collapsePanel();
        // }
        // }

        // TODO collapse the input
        return false;
    }

}
