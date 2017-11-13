/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.beem.service;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.beem.project.beem.otr.BeemOtrManager;
import com.beem.project.beem.service.aidl.IChat;
import com.beem.project.beem.service.aidl.IMessageListener;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMUser;
import com.zhisland.im.smack.MessageMetadataExtension;
import com.zhisland.im.util.Constants;
import com.zhisland.lib.load.HttpUploadInfo;
import com.zhisland.lib.load.LoadPriority;
import com.zhisland.lib.load.LoadStatus;
import com.zhisland.lib.load.ZHLoadManager;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileMgr.DirType;
import com.zhisland.lib.util.file.FileUtil;

import net.java.otr4j.OtrException;
import net.java.otr4j.session.SessionID;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateListener;
import org.jivesoftware.smackx.packet.DeliveryReceipt;
import org.jivesoftware.smackx.packet.DeliveryReceiptRequest;

import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * An adapter for smack's Chat class.
 * TODO what about OTR
 *
 * @author darisk
 */
public class ChatAdapter extends IChat.Stub {
    private static final String TAG = "ChatAdapter";
    private static final String PROTOCOL = "XMPP";

    private final Chat mAdaptee;
    private final String mParticipantJid;
    private String mState;
    private boolean mIsOpen;
    private final RemoteCallbackList<IMessageListener> mRemoteListeners = new RemoteCallbackList<IMessageListener>();
    private final MsgListener mMsgListener = new MsgListener();
    private SessionID mOtrSessionId;
    private int mUnreadMsgCount;

    /**
     * Constructor.
     *
     * @param chat The chat to adapt
     */
    public ChatAdapter(final Chat chat) {
        mAdaptee = chat;
        mParticipantJid = IMUser.ParseJid(chat.getParticipant());
        mUnreadMsgCount = DBMgr.getHelper().getChatDao()
                .getUnreadCount(mParticipantJid);
        mAdaptee.addMessageListener(mMsgListener);
        EventBus.getDefault().register(this);
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParticipantJid() throws RemoteException {
        return mParticipantJid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(com.beem.project.beem.service.Message message)
            throws RemoteException {
        switch (message.getSubType()) {
            case Constants.MSG_TYPE_IMAGE:
            case Constants.MSG_TYPE_VIDEO: {
                sendMultimedia(message);
                break;
            }
            default: {
                sendSmackMessage(message);
            }
        }
    }

    private void sendSmackMessage(com.beem.project.beem.service.Message message) {
        com.beem.project.beem.service.Message encrypted = otrEncryptMessage(message);
        if (encrypted != null) {
            transferMessage(encrypted);
            addMessage(encrypted);
        } else {
            transferMessage(message);
            addMessage(message);
        }

    }

    private void sendMultimedia(com.beem.project.beem.service.Message message) {
        com.beem.project.beem.service.Message msg = DBMgr.getHelper()
                .getMsgDao().fetchMessageByThread(message.getMessageThread());
        if (msg == null) {
            // insert message
            long token = ZHLoadManager.uploadFile(message.getLocal(), 0,
                    message.getMessageThread(), HttpUploadInfo.TYPE_PIC, 0,
                    LoadPriority.HIGH);
            message.setToken(token);
            logMessage(message);
        } else if (msg.getStatus() < Constants.MSG_STATUS_CONTINUE) {
            // restart upload
            if (msg.getToken() <= 0) {
                long token = ZHLoadManager.uploadFile(message.getLocal(), 0,
                        message.getMessageThread(), HttpUploadInfo.TYPE_PIC, 0,
                        LoadPriority.HIGH);
                message.setToken(token);
                DBMgr.getHelper().getMsgDao().updateMessage(message);
            } else {
                ZHLoadManager.Instance().getHttpUploadMgr()
                        .startByToken(null, msg.getToken());
            }
        } else {
            // send smack
            sendSmackMessage(message);
        }
    }

    //后台上传图片的通知
    public void onEvent(HttpUploadInfo info) {

        if (info == null)
            return;

        Message msg = DBMgr.getHelper().getMsgDao()
                .fetchMessageByToken(info.token);

        // happened before token was not update to db
        if (msg == null || !msg.getTo().equals(mParticipantJid))
            return;

        MLog.d(TAG,
                "upload update: " + info.hashcode + "  " + info.getPercent()
                        + "   " + info.status);
        int status;
        switch (info.status) {
            case LoadStatus.FINISH: {
                status = Constants.MSG_STATUS_CONTINUE;
                String url = info.picUrl;
                if (url != null && !url.toLowerCase().endsWith("_l.jpg")) {
                    url = url.replace(".jpg", "_l.jpg");
                }
                msg.setUrl(url);
                break;
            }
            case LoadStatus.WAIT:
            case LoadStatus.LOADING: {
                status = Constants.MSG_STATUS_SENDING;
                break;
            }
            case LoadStatus.ERROR:
            case LoadStatus.ERROR_FILE_MISS:
            case LoadStatus.ERROR_NETWORK:
            default:
                status = Constants.MSG_STATUS_FILE_ERROR;
                break;
        }

        int progress = info.getPercent();
        notifyMessageStatus(msg, status, progress);

        if (status == Constants.MSG_STATUS_CONTINUE) {
            sendSmackMessage(msg);
        }
    }

    private void notifyMessageStatus(
            com.beem.project.beem.service.Message message, int status,
            int progress) {
        final int n = mRemoteListeners.beginBroadcast();
        for (int i = 0; i < n; i++) {
            IMessageListener listener = mRemoteListeners.getBroadcastItem(i);
            try {
                listener.onStatusChanged(message.getMessageThread(), status,
                        progress);
            } catch (RemoteException e) {
                Log.w(TAG, e.getMessage());
            }
        }
        mRemoteListeners.finishBroadcast();

        message.setStatus(status);
        message.setProgress(progress);
        DBMgr.getHelper().getMsgDao().updateMessage(message);
    }

    /**
     * private method for sending message.
     *
     * @param message the message to send
     */
    private void transferMessage(com.beem.project.beem.service.Message message) {
        org.jivesoftware.smack.packet.Message send = new org.jivesoftware.smack.packet.Message();
        send.setPacketID(message.getMessageThread());
        String msgBody = message.getBody();
        send.setTo(message.getTo());
        Log.w(TAG, "message to " + message.getTo());
        send.setBody(msgBody);

        send.setThread(message.getThread());
        send.setSubject(message.getSubject());
        send.setType(org.jivesoftware.smack.packet.Message.Type.chat);

        MessageMetadataExtension metadata = new MessageMetadataExtension();
        MessageMetadataExtension.Info info;
        info = new MessageMetadataExtension.Info();
        info.setType(message.getSubType());
        info.setUrl(message.getUrl());
        info.setToken(message.getToken());
        info.setSize(message.getSize());
        info.setDuration(message.getDuration());
        info.setTitle(message.getTitle());
        info.setDescription(message.getDescription());
        info.setMessageThread(message.getMessageThread());
        metadata.setInfo(info);
        send.addExtension(metadata);
        DeliveryReceiptRequest request = new DeliveryReceiptRequest();
        send.addExtension(request);

        // TODO gerer les messages contenant des XMPPError
        // send.set
        int status = Constants.MSG_STATUS_NORMAL;
        try {
            mAdaptee.sendMessage(send);
        } catch (XMPPException e) {
            status = Constants.MSG_STATUS_ERROR;
            e.printStackTrace();
        }
        message.setStatus(status);
        notifyMessageStatus(message, status, 100);
    }

    /**
     * send message.
     *
     * @param msg to send.
     */
    public void injectMessage(String msg) {
        Message msgToSend = new Message(mParticipantJid,
                Message.MSG_TYPE_CHAT);
        msgToSend.setBody(msg);
        transferMessage(msgToSend);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessageListener(IMessageListener listen) {
        if (listen != null)
            mRemoteListeners.register(listen);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener(IMessageListener listen) {
        if (listen != null) {
            mRemoteListeners.unregister(listen);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() throws RemoteException {
        return mState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(String state) throws RemoteException {
        mState = state;
    }

    /**
     * Get the adaptee for the Chat.
     *
     * @return The real chat object
     */
    public Chat getAdaptee() {
        return mAdaptee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOpen(boolean isOpen) {
        this.mIsOpen = isOpen;
        if (isOpen) {
            mUnreadMsgCount = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return mIsOpen;
    }

    /**
     * Add a message in the chat history.
     *
     * @param msg the message to add
     */
    private void addMessage(Message msg) {
        if (!isOpen())
            mUnreadMsgCount++;
        if (!"".equals(msg.getBody()) && msg.getBody() != null) {
            logMessage(msg);
        }
    }

    /**
     * log a message.
     *
     * @param message message to log
     */
    private void logMessage(com.beem.project.beem.service.Message message) {
        DBMgr.getHelper().getMsgDao()
                .logMessage(mParticipantJid, message, mUnreadMsgCount);
    }

    /**
     * encrypt a message with an otr session.
     *
     * @param unencrypted message with cleartext body
     * @return message with encrypted body
     */
    private com.beem.project.beem.service.Message otrEncryptMessage(
            com.beem.project.beem.service.Message unencrypted) {

        if (mOtrSessionId != null && unencrypted != null
                && unencrypted.getBody() != null) {
            try {
                String body = BeemOtrManager.getInstance().getOtrManager()
                        .transformSending(mOtrSessionId, unencrypted.getBody());
                Message result = new Message(unencrypted.getTo(),
                        unencrypted.getType());
                result.setBody(body);
                result.setDescription(unencrypted.getDescription());
                result.setDuration(unencrypted.getDuration());
                result.setMessageThread(unencrypted.getMessageThread());
                result.setSize(unencrypted.getSize());
                result.setSubject(unencrypted.getSubject());
                result.setSubType(unencrypted.getSubType());
                result.setTitle(unencrypted.getTitle());
                result.setType(unencrypted.getType());
                result.setUrl(unencrypted.getUrl());
                result.setLocal(unencrypted.getLocal());
                return result;
            } catch (OtrException e) {
                Log.e(TAG, "OTR: Unable to encrypt message", e);
            }
        }
        return null;
    }

    /**
     * This method is executed when the otr session status change.
     *
     * @param otrState the new state of otr session.
     */
    public void otrStateChanged(final String otrState) {
        Message m = new Message(null, Message.MSG_TYPE_INFO);
        m.setBody(otrState);
        addMessage(m);
        final int n = mRemoteListeners.beginBroadcast();

        for (int i = 0; i < n; i++) {
            IMessageListener listener = mRemoteListeners.getBroadcastItem(i);
            try {
                listener.otrStateChanged(otrState);
            } catch (RemoteException e) {
                Log.w(TAG, e.getMessage());
            }
        }
        mRemoteListeners.finishBroadcast();
    }

    @Override
    public void startOtrSession() throws RemoteException {
        if (mOtrSessionId == null) {
            mOtrSessionId = new SessionID(mParticipantJid,
                    mParticipantJid, PROTOCOL);
            BeemOtrManager.getInstance().addChat(mOtrSessionId, this);
        }

        try {
            BeemOtrManager.getInstance().getOtrManager()
                    .startSession(mOtrSessionId);
        } catch (OtrException e) {
            mOtrSessionId = null;
            e.printStackTrace();
            throw new RemoteException();
        }
    }

    @Override
    public void endOtrSession() throws RemoteException {
        try {
            localEndOtrSession();
        } catch (OtrException e) {
            e.printStackTrace();
            throw new RemoteException();
        }
    }

    /**
     * end an Otr session.
     *
     * @return false if something bad happened.
     * @throws OtrException an exception from otr
     */
    public boolean localEndOtrSession() throws OtrException {
        if (mOtrSessionId == null)
            return true;

        BeemOtrManager.getInstance().getOtrManager().endSession(mOtrSessionId);
        BeemOtrManager.getInstance().removeChat(mOtrSessionId);
        mOtrSessionId = null;
        listenOtrSession();
        return true;
    }

    /**
     * Start listenning to an OTR session.
     */
    public void listenOtrSession() {
        if (mOtrSessionId != null)
            return;

        mOtrSessionId = new SessionID(mParticipantJid,
                mParticipantJid, PROTOCOL);
        BeemOtrManager.getInstance().addChat(mOtrSessionId, this);
        // OtrEngineImpl will make a call to "this.getSession(sessionID)" which
        // will instantiate our session.
        BeemOtrManager.getInstance().getOtrManager()
                .getSessionStatus(mOtrSessionId);
    }

    @Override
    public String getLocalOtrFingerprint() throws RemoteException {
        if (mOtrSessionId == null)
            return null;

        return BeemOtrManager.getInstance().getLocalFingerprint(mOtrSessionId);
    }

    @Override
    public String getRemoteOtrFingerprint() throws RemoteException {
        if (mOtrSessionId == null)
            return null;

        return BeemOtrManager.getInstance().getRemoteFingerprint(mOtrSessionId);
    }

    @Override
    public void verifyRemoteFingerprint(boolean ok) {
        if (mOtrSessionId != null) {
            if (ok)
                BeemOtrManager.getInstance().verifyRemoteFingerprint(
                        mOtrSessionId);
            else
                BeemOtrManager.getInstance().unverifyRemoteFingerprint(
                        mOtrSessionId);
        }
    }

    @Override
    public String getOtrStatus() throws RemoteException {
        if (mOtrSessionId == null)
            return null;
        return BeemOtrManager.getInstance().getOtrManager()
                .getSessionStatus(mOtrSessionId).toString();
    }

    @Override
    public int getUnreadMessageCount() throws RemoteException {
        return mUnreadMsgCount;
    }

    /**
     * Listener.
     */
    private class MsgListener implements ChatStateListener {
        /**
         * Constructor.
         */
        public MsgListener() {
        }

        @Override
        public void processMessage(Chat chat,
                                   org.jivesoftware.smack.packet.Message message) {
            Message msg = new Message(message);
            Log.d(TAG, "new msg " + msg.getBody());

            PacketExtension extension = message.getExtension("request",
                    DeliveryReceipt.NAMESPACE);
            if (extension != null) {
                org.jivesoftware.smack.packet.Message response = new org.jivesoftware.smack.packet.Message(
                        message.getFrom());
                DeliveryReceipt receipt = new DeliveryReceipt(
                        message.getPacketID());
                response.addExtension(receipt);

                try {
                    mAdaptee.sendMessage(response);
                } catch (XMPPException e) {
                    Log.e(TAG, "send received message:", e);
                }
            }

            DeliveryReceipt receipt = (DeliveryReceipt) message.getExtension(
                    "received", DeliveryReceipt.NAMESPACE);
            if (receipt != null) {
                return;
            }

            String body;

            if (mOtrSessionId != null) {
                try {
                    body = BeemOtrManager.getInstance().getOtrManager()
                            .transformReceiving(mOtrSessionId, msg.getBody());
                    msg.setBody(body);
                } catch (OtrException e) {
                    Log.w(TAG, "Unable to decrypt OTR message", e);
                }
            }
            int subType = msg.getSubType();

            if (!isZHMessage(subType)) {
                // 不是正和岛的消息类型，忽略不处理
                return;
            }

            switch (subType) {
                case Constants.MSG_TYPE_AUDIO: {
                    String path = FileMgr.Instance().getFilepath(DirType.CHAT,
                            UUID.randomUUID().toString() + ".amr");
                    if (FileUtil.decoderBase64File(msg.getBody(), path)) {
                        msg.setLocal(path);
                    }
                    break;
                }
            }
            msg.setStatus(Constants.MSG_STATUS_NORMAL);
            ChatAdapter.this.addMessage(msg);
            final int n = mRemoteListeners.beginBroadcast();
            for (int i = 0; i < n; i++) {
                IMessageListener listener = mRemoteListeners
                        .getBroadcastItem(i);
                try {
                    if (listener != null)
                        listener.processMessage(ChatAdapter.this, msg);
                } catch (RemoteException e) {
                    Log.w(TAG, "Error while diffusing message to listener", e);
                }
            }
            mRemoteListeners.finishBroadcast();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void stateChanged(Chat chat, ChatState state) {
            mState = state.name();
            final int n = mRemoteListeners.beginBroadcast();

            for (int i = 0; i < n; i++) {
                IMessageListener listener = mRemoteListeners
                        .getBroadcastItem(i);
                try {
                    listener.stateChanged(ChatAdapter.this);
                } catch (RemoteException e) {
                    Log.w(TAG, e.getMessage());
                }
            }
            mRemoteListeners.finishBroadcast();
        }

    }

    /**
     * 判断是不是正和岛自定义的消息类型
     *
     * @param type
     * @return
     */
    private boolean isZHMessage(int type) {
        return type == Constants.MSG_TYPE_AUDIO
                || type == Constants.MSG_TYPE_IMAGE
                || type == Constants.MSG_TYPE_TEXT;
    }

}
