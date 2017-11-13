package com.zhisland.android.blog.im.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.webview.ActionDialog;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.android.blog.im.view.AudioListener;
import com.zhisland.android.blog.im.view.AudioPlayer;
import com.zhisland.android.blog.im.view.row.BaseRowView;
import com.zhisland.android.blog.im.view.row.OnRowListener;
import com.zhisland.android.blog.im.view.row.RowAudio;
import com.zhisland.android.blog.im.view.row.RowForwardNews;
import com.zhisland.android.blog.im.view.row.RowImage;
import com.zhisland.android.blog.im.view.row.RowLocation;
import com.zhisland.android.blog.im.view.row.RowSectionTitle;
import com.zhisland.android.blog.im.view.row.RowText;
import com.zhisland.android.blog.im.view.row.RowVCard;
import com.zhisland.android.blog.im.view.row.RowVideo;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMMessage;
import com.zhisland.im.data.IMUser;
import com.zhisland.im.util.Constants;
import com.zhisland.lib.component.adapter.BaseListAutoSectionAdapter;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.text.ZHLink.OnLinkClickListener;
import com.zhisland.lib.util.text.ZHLinkBuilder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ImSessAdapter extends BaseListAutoSectionAdapter<IMMessage>
        implements OnCreateContextMenuListener, OnMenuItemClickListener,
        OnLinkClickListener {

    public static interface OnAdapterListener {

        void onUserAt(String userName);

        void onReplyClicked(IMMessage message);

        void onLongTextCollapsed(IMMessage msg);
    }

    private static final String TAG = "sessadapter";

    private static final int TIME_SECTION_INTERVAL = 5 * 60 * 1000;

    private static final int VT_TEXT = 0;
    private static final int VT_PICTURE = 1;
    private static final int VT_VIDEO = 2;
    protected static final int VT_AUDIO = 3;
    private static final int VT_LOCATION = 4;
    private static final int VT_VCARD = 5;
    private static final int VT_FORWARD_NEWS = 6;
    private static final int VT_TITLE_SECTION = 7;
    private static final int VT_MAX = 8;

    protected static final int MENU_COPY = 1;
    protected static final int MENU_DELETE = 2;
    protected static final int MENU_FORWARD = 3;
    protected static final int MENU_COLLECTION = 4;
    protected static final int MENU_TXT_STORE = 5;
    private static final String MENU_TEXT_COPY = "复制";
    private static final String MENU_TEXT_DELETE = "删除";
    private static final String MENU_TEXT_FORWARD = "转发";
    private static final String MENU_TEXT_COLLECTION = "收藏";
    private static final String MENU_TEXT_STORE = "存档";

    private OnAdapterListener adapterListener;
    protected Context context;
    protected ArrayList<OnRowListener> rowListeners = new ArrayList<OnRowListener>();
    private final AudioMgr audioMgr;
    private IMUser userSelf;
    private IMUser userOther;
    private IMMessage menuMsg;
    private OnRowListener menuListener;
    private String strLinkMenu;
    private Dialog numberDialog;
    private Dialog insertOrEditContactDialog;
    private final ReadTextDialog readDialog;

    public ImSessAdapter(Context context) {
        super(null);

        this.context = context;
        this.readDialog = new ReadTextDialog(context);
        audioMgr = new AudioMgr();
        if (userSelf == null) {
            String avatar = PrefUtil.Instance().getUserAvatar();
            String name = PrefUtil.Instance().getUserName();
            String jid = IMUser.buildJid(PrefUtil.Instance().getUserId());
            userSelf = new IMUser();
            userSelf.jid = jid;
            userSelf.name = name;
            userSelf.avatar = avatar;
        }
    }

    public void setAdapterListener(OnAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    /**
     * update message views
     *
     * @param messageThread
     * @param status
     * @param progress
     */
    public void refresh(String messageThread, int status, int progress) {
        boolean refreshed = false;
        for (OnRowListener row : rowListeners) {
            IMMessage msg = row.getMessage();
            if (msg == null)
                continue;

            if (msg.thread.equalsIgnoreCase(messageThread)) {
                msg.status = status;
                msg.progress = progress;
                row.refresh(status, progress);
                refreshed = true;
                break;
            }
        }

        if (refreshed)
            return;

        for (IMMessage msg : data) {
            if (msg.thread.equalsIgnoreCase(messageThread)) {
                msg.status = status;
                msg.progress = progress;
                break;
            }
        }
    }

    // ========auto section overrides========
    @Override
    protected boolean needStartNewSection(IMMessage pre, IMMessage next) {
        if (pre == null || next == null)
            return true;
        long interval = Math.abs(pre.time - next.time);
        MLog.d(TAG, interval + "");
        return interval > TIME_SECTION_INTERVAL;
    }

    @Override
    protected IMMessage createSectionTitle(IMMessage item) {
        IMMessage msg = new IMMessage();
        msg.time = item.time;
        msg.type = Constants.MSG_TYPE_SECTIOIN_TITLE;
        return msg;
    }

    // ==========common adapter overrides=============

    @Override
    public int getViewTypeCount() {
        return VT_MAX;
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage msg = this.getItem(position);
        int viewType;
        switch (msg.type) {
            case Constants.MSG_TYPE_SECTIOIN_TITLE:
                viewType = VT_TITLE_SECTION;
                break;
            case Constants.MSG_TYPE_IMAGE:
                viewType = VT_PICTURE;
                break;
            case Constants.MSG_TYPE_VIDEO:
                viewType = VT_VIDEO;
                break;
            case Constants.MSG_TYPE_AUDIO:
                // case IMProtocolConstant.MessageTypeDataAudio:
                viewType = VT_AUDIO;
                break;
            case Constants.MSG_TYPE_INFO: {
                viewType = VT_FORWARD_NEWS;
                break;
            }
            case Constants.MSG_TYPE_LOC: {
                viewType = VT_LOCATION;
                break;
            }
            case Constants.MSG_TYPE_VCARD: {
                viewType = VT_VCARD;
                break;
            }
            // case IMProtocolConstant.MessageTypeLocation:
            // viewType = VT_LOCATION;
            // break;
            // case IMProtocolConstant.MessageTypeVCard:
            // viewType = VT_VCARD;
            // break;
            // case IMProtocolConstant.MessageTypeSingleNewsPush:
            // case IMProtocolConstant.MessageTypeDayAskRecommend:
            // viewType = VT_SINGLE_NEWS;
            // break;
            // case IMProtocolConstant.MessageTypeMultiNewsPush:
            // viewType = VT_MULTI_NEWS;
            // break;
            // case IMProtocolConstant.MessageTypeForwardNews:
            // case IMProtocolConstant.MessageTypeAction:
            // viewType = VT_FORWARD_NEWS;
            // break;
            // case IMProtocolConstant.MessageTypeClientPromotion:
            // case IMProtocolConstant.MessageTypeClientNoContactPromotion:
            // viewType = VT_CLIENT_PROMPT;
            // break;
            // case IMProtocolConstant.MessageTypeFriendRecommend:
            // case IMProtocolConstant.MessageTypeFriendInvition:
            // viewType = VT_FS_INVITION;
            // break;
            // case IMProtocolConstant.MessageTypeGroupFeed:
            // viewType = VT_GROUP_FEED;
            // break;
            // case IMProtocolConstant.MessageTypeOperation:
            // case IMProtocolConstant.MessageTypeGroupAuth:
            // viewType = VT_MSG_PROMPT;
            // break;
            // case IMProtocolConstant.MessageTypeEmotion: {
            // viewType = VT_BIG_EMOTION;
            // break;
            // }
            default: {
                viewType = VT_TEXT;
            }
        }

        return viewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MLog.d(TAG, "get view " + position);
        if (convertView == null) {
            int itemViewType = getItemViewType(position);
            MLog.d(TAG, "convertView is null normal im mess" + itemViewType);
            OnRowListener rowListener = createRowListener(context, itemViewType);
            convertView = rowListener.getView();
            convertView.setTag(rowListener);
        }
        fillView(position, convertView);
        return convertView;
    }

    protected void fillView(int position, View convertView) {
        IMMessage msg = getItem(position);
        Object obj = convertView.getTag();
        if (obj instanceof OnRowListener) {
            OnRowListener rowListener = (OnRowListener) obj;
            rowListener.fill(msg);
            if (!rowListeners.contains(rowListener)) {
                rowListeners.add(rowListener);
            }
        }
    }

    @Override
    protected void recycleView(View view) {
        Object obj = view.getTag();
        if (obj instanceof OnRowListener) {
            OnRowListener rowListener = (OnRowListener) obj;
            rowListener.recycle();
            if (rowListeners.contains(rowListener)) {
                rowListeners.remove(rowListener);
            }
        }
    }

    protected OnRowListener createRowListener(Context context, int type) {

        OnRowListener rowListener;
        switch (type) {
            case VT_TEXT: {
                RowText rowText = new RowText(context, readDialog);
                rowListener = rowText;
                rowText.setOnLinkListener(this);
                break;
            }
            case VT_PICTURE: {
                rowListener = new RowImage(context);
                break;
            }
            case VT_VIDEO: {
                rowListener = new RowVideo(context);
                break;
            }
            case VT_AUDIO: {
                RowAudio rowAudio = new RowAudio(context);
                rowAudio.setAudioMgr(audioMgr);
                rowListener = rowAudio;
                break;
            }
            case VT_LOCATION: {
                rowListener = new RowLocation(context);
                break;
            }
            case VT_VCARD: {
                rowListener = new RowVCard(context);
                break;
            }
            case VT_FORWARD_NEWS: {
                rowListener = new RowForwardNews(context);
                break;
            }

            case VT_TITLE_SECTION: {
                rowListener = new RowSectionTitle(context);
                break;
            }

            default:
                rowListener = null;
        }
        if (rowListener != null) {
            rowListener.setUsers(userSelf, userOther);
            rowListener.setOnCreateMenuListener(this);
            rowListener.setOnAdapterListener(adapterListener);
        }
        return rowListener;
    }

    // ==========other public methods=========
    public class AudioMgr implements AudioListener {

        public IMMessage curMsg;
        protected boolean stoped = false;

        public void playAudio(IMMessage msg) {

            stoped = false;
            curMsg = null;

            if (msg.local != null) {
                curMsg = msg;
                refreshAnimations(msg);
                msg.isRead = true;
                DBMgr.getHelper().getMsgDao().readMsg(msg.id);
                AudioPlayer.instance().play(msg.local, this);
            }
        }

        protected void refreshAnimations(IMMessage msgPlaying) {
            for (OnRowListener holder : rowListeners) {
                if (holder instanceof RowAudio) {
                    ((RowAudio) holder).refreshPlayStatus(msgPlaying);
                }
            }
        }

        public void stop() {
            curMsg = null;
            stoped = true;
            refreshAnimations(null);
            AudioPlayer.instance().stop();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (!stoped) {
                playNext();
            }
            return false;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (!stoped) {
                playNext();
            }
        }

        public void playNext() {
            refreshAnimations(null);
            final IMMessage msg = nextAudioMsg(curMsg);
            if (msg != null) {
                playAudio(msg);
            } else {
                stop();
            }
        }

        public IMMessage nextAudioMsg(IMMessage mes) {
            if (data == null)
                return null;
            int curIndex = data.indexOf(mes);
            if (curIndex >= 0 && curIndex < data.size() - 1) {
                for (int i = curIndex + 1; i < data.size(); i++) {
                    IMMessage msg = data.get(i);
                    if (msg.type == Constants.MSG_TYPE_AUDIO && !msg.isRead)
                        return msg;
                }
            }
            return null;
        }
    }

    public void updateConvertStatus(long token) {
        for (OnRowListener rowListener : rowListeners) {
            if (rowListener instanceof RowForwardNews) {
                ((RowForwardNews) rowListener).refreshConvertStatus(true);
            }
        }
    }

    public void refreshStatus() {
        for (OnRowListener rowListener : rowListeners) {
            if (rowListener instanceof BaseRowView) {
                ((BaseRowView) rowListener).refreshStatus(true);
            }
        }
    }

    /**
     * set other user info
     */
    public void setUser(IMUser mUser) {
        this.userOther = mUser;
    }

    public void release() {
        if (audioMgr != null) {
            audioMgr.stop();
        }
    }

    public IMMessage getOldestMsg() {
        if (data == null) {
            return null;
        }

        for (IMMessage msg : data) {
            if (msg.type != Constants.MSG_TYPE_SECTIOIN_TITLE)
                return msg;
        }
        return null;
    }

    // ============menu methods=======
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        menu.clear();
        Object obj = v.getTag(R.id.chat_data_mes);
        Object objListener = v.getTag(R.id.chat_data_listener);

        IMMessage msg = (IMMessage) obj;
        menuListener = (OnRowListener) objListener;
        if (msg == null || menuListener == null)
            return;

        menuMsg = msg;

        switch (msg.type) {
            case Constants.MSG_TYPE_IMAGE: {
                menu.add(0, MENU_DELETE, menu.size(), MENU_TEXT_DELETE)
                        .setOnMenuItemClickListener(this);
                // menu.add(0, MENU_FORWARD, menu.size(), MENU_TEXT_FORWARD)
                // .setOnMenuItemClickListener(this);
                // if (menuMsg.getAttachMent().isDownloaded()) {
                // menu.add(0, MENU_TXT_STORE, menu.size(), MENU_TEXT_STORE)
                // .setOnMenuItemClickListener(this);
                // }
                // TODO
                break;
            }
            case Constants.MSG_TYPE_VIDEO:
            case Constants.MSG_TYPE_AUDIO:
            case Constants.MSG_TYPE_LOC:
            case Constants.MSG_TYPE_VCARD: {
                menu.add(0, MENU_DELETE, menu.size(), MENU_TEXT_DELETE)
                        .setOnMenuItemClickListener(this);
                // menu.add(0, MENU_FORWARD, menu.size(), MENU_TEXT_FORWARD)
                // .setOnMenuItemClickListener(this);
                break;
            }
            case Constants.MSG_TYPE_INFO: {
                // menu.add(0, MENU_DELETE, menu.size(), MENU_TEXT_DELETE)
                // .setOnMenuItemClickListener(this);
                // menu.add(0, MENU_FORWARD, menu.size(), MENU_TEXT_FORWARD)
                // .setOnMenuItemClickListener(this);
                // if (this.menuMsg.getAttachMent().getClassId() !=
                // IMProtocolConstant.IMFeedClassId
                // || this.menuMsg.getAttachMent().getClassId() !=
                // IMProtocolConstant.IMWebActionClassId) {
                // menu.add(0, MENU_COLLECTION, menu.size(), MENU_TEXT_COLLECTION)
                // .setOnMenuItemClickListener(this);
                // }
                break;
            }
            case Constants.MSG_TYPE_TEXT: {
                menu.add(0, MENU_COPY, menu.size(), MENU_TEXT_COPY)
                        .setOnMenuItemClickListener(this);
                menu.add(0, MENU_DELETE, menu.size(), MENU_TEXT_DELETE)
                        .setOnMenuItemClickListener(this);
                // menu.add(0, MENU_FORWARD, menu.size(), MENU_TEXT_FORWARD)
                // .setOnMenuItemClickListener(this);
                // menu.add(0, MENU_TXT_STORE, menu.size(), MENU_TEXT_STORE)
                // .setOnMenuItemClickListener(this);
                // menu.add(0, MENU_COLLECTION, menu.size(), MENU_TEXT_COLLECTION)
                // .setOnMenuItemClickListener(this);
                break;
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_COPY: {
                ClipboardManager cm = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(menuMsg.body);
                return true;
            }
            case MENU_DELETE: {
                DBMgr.getHelper().getMsgDao().deleteMessage(menuMsg);
                this.removeItem(menuMsg);
                break;
            }
            case MENU_FORWARD: {
                switch (menuMsg.type) {

                    case Constants.MSG_TYPE_AUDIO:
                    case Constants.MSG_TYPE_IMAGE:
                    case Constants.MSG_TYPE_VIDEO:
                        // IMAttachment attach = menuMsg.getAttachMent();
                        // if (attach != null && !attach.isDownloaded()) {
                        // menuListener.performOnClick();
                        // break;
                        // }
                        // TODO
                    default: {
                        // TODO
                        // Intent intent = new Intent(context,
                        // SelectForwardDestFragActivity.class);
                        // ((Activity) context).startActivityForResult(intent,
                        // ChatSessionFragment.REQ_SELECT_FORWARD_DEST);
                    }
                }

                break;
            }
            // case MENU_TXT_STORE: {
            // String excludeIds = "0";
            // if (menuMsg.messgeType == IMProtocolConstant.MessageTypeNormal) {
            // Intent intent = new Intent(context,
            // GroupForwardListActvity.class);
            // intent.putExtra(GroupForwardListActvity.EXCLUDE_IDS, excludeIds);
            // intent.putExtra(GroupForwardListActvity.FORWARD_CONTENT_ID,
            // menuMsg.messageBody);
            // context.startActivity(intent);
            // } else if (menuMsg.messgeType ==
            // IMProtocolConstant.MessageTypePicture) {
            // if (menuMsg.getAttachMent().isDownloaded()) {
            // Intent intent = new Intent(context,
            // GroupForwardListActvity.class);
            // intent.putExtra(GroupForwardListActvity.EXCLUDE_IDS,
            // excludeIds);
            // intent.putExtra(GroupForwardListActvity.FORWARD_IMAGE_ID,
            // menuMsg.getAttachMent().localFileName);
            // context.startActivity(intent);
            // } else {
            // menuListener.performOnClick();
            // }
            // }
            //
            // break;
            // }
            // case MENU_COLLECTION: {
            // if (menuMsg != null) {
            // final Activity act = (Activity) context;
            // if (menuMsg.messgeType == IMProtocolConstant.MessageTypeNormal) {
            // act.showDialog(FragBaseActivity.FRAG_DIALOG_COMMON_WAITTING);
            // ZHBlogEngineFactory.getZHIslandEngineAPI().setFavoriteList(
            // menuMsg.messageBody, null, null, null, 0, null,
            // null, new TaskCallback<Object>() {
            //
            // @Override
            // public void onSuccess(Object content) {
            // act.removeDialog(FragBaseActivity.FRAG_DIALOG_COMMON_WAITTING);
            // ToastUtil.showShort("收藏成功");
            // }
            //
            // @Override
            // public void onFailure(Throwable failture) {
            // act.removeDialog(FragBaseActivity.FRAG_DIALOG_COMMON_WAITTING);
            // }
            //
            // });
            // }
            // }
            //
            // break;
            // }
        }
        return true;
    }

    private boolean canSend() {
        return userOther != null;
    }

    @Override
    public void onLinkClicked(Context context, String regex, String data) {
        if (regex.equals(ZHLinkBuilder.REGEX_URL)) {
            AUriMgr.instance().viewRes(context, data);
        } else if (regex.equals(ZHLinkBuilder.REGEX_NUMBER)) {
            strLinkMenu = data;
            showNumberDialog(strLinkMenu);
        } else if (regex.equals(ZHLinkBuilder.REGEX_APP_SCHEMA_URL)) {
            // AUriMgr.instance().viewRes(context, data);
        }
    }

    private void showNumberDialog(String prefix) {
        String title = prefix + "可能是一个电话号码，你可以";
        List<ActionItem> menus = new ArrayList<ActionItem>();
        menus.add(new ActionItem(1, "使用"));
        menus.add(new ActionItem(2, "创建新联系人"));
        menus.add(new ActionItem(3, "复制"));
        if (numberDialog == null) {
            numberDialog = DialogUtil.createActionSheet(context, title, "取消",
                    menus, new OnActionClick() {

                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            ActionItem item) {
                            switch (which) {
                                case ActionDialog.ID_CANCEL: {
                                    numberDialog.dismiss();
                                    break;
                                }
                                case 1: {
                                    IntentUtil.dialTo(context, strLinkMenu);
                                    numberDialog.dismiss();
                                    break;
                                }
                                case 2: {
                                    String title = strLinkMenu + "可能是一个电话号码，你可以";
                                    numberDialog.dismiss();
                                    showInsertOrEditContactDialog(title);
                                    break;
                                }
                                case 3: {
                                    ClipboardManager cm = (ClipboardManager) context
                                            .getSystemService(Context.CLIPBOARD_SERVICE);
                                    cm.setText(strLinkMenu);
                                    numberDialog.dismiss();
                                    break;
                                }
                            }
                        }
                    });
        }

        ((ActionDialog) numberDialog).setTitle(title);
        numberDialog.show();
    }

    private void showInsertOrEditContactDialog(String title) {
        if (insertOrEditContactDialog == null) {
            List<ActionItem> menus = new ArrayList<ActionItem>();
            menus.add(new ActionItem(1, "创建新联系人"));
            menus.add(new ActionItem(2, "添加到现有联系人"));
            insertOrEditContactDialog = DialogUtil.createActionSheet(context,
                    title, "取消", menus, new OnActionClick() {

                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            ActionItem item) {
                            switch (which) {
                                case 1: {
                                    Intent intent = new Intent(Intent.ACTION_INSERT);
                                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                                    intent.putExtra(Contacts.Intents.Insert.PHONE,
                                            strLinkMenu);
                                    context.startActivity(intent);
                                    insertOrEditContactDialog.dismiss();
                                    break;
                                }
                                case 2: {
                                    Intent intent = new Intent(
                                            Intent.ACTION_INSERT_OR_EDIT);
                                    intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);

                                    intent.putExtra(
                                            android.provider.ContactsContract.Intents.Insert.PHONE,
                                            strLinkMenu);
                                    context.startActivity(intent);
                                    insertOrEditContactDialog.dismiss();
                                    break;
                                }
                            }
                        }
                    });
        }
        ((ActionDialog) insertOrEditContactDialog).setTitle(title);
        insertOrEditContactDialog.show();
    }

}
