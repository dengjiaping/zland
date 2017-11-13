package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beem.project.beem.service.Message;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.im.data.IMMessage;

public class RowForwardNews extends BaseRowView {

	public ImageView ivNewsPic;
	public TextView tvNewsTitle;
	public TextView tvNewsDesc;
	public View newsContent;
	public TextView tvFrom;

	public RowForwardNews(Context context) {
		super(context, CONTENT_TYPE_BLOCK);
	}

	@Override
	public void addContentView(MaxWidthLinearLayout container, Context context) {

		container.setOrientation(LinearLayout.VERTICAL);

		newsContent = inflater.inflate(R.layout.chat_forwardnews_row, null);
		ivNewsPic = (ImageView) newsContent
				.findViewById(R.id.iv_chat_forward_pic);
		tvNewsTitle = (TextView) newsContent
				.findViewById(R.id.iv_chat_forward_title);
		tvNewsDesc = (TextView) newsContent
				.findViewById(R.id.iv_chat_forward_desc);
		container.addView(newsContent);

		// tvFrom = (TextView) inflater.inflate(R.layout.msg_from_tv, null);
		// tvFrom.setOnClickListener(this);
		// container.addView(tvFrom);

	}

	@Override
	public void fill(IMMessage msg) {
		fill(msg, true);
	}

	private void fill(IMMessage msg, boolean refreshConvert) {
		super.fill(msg);
		tvNewsTitle.setText(msg.body);
		// IMAttachment attachment = msg.getLatestAttachMent();
		//
		// ImageWorkFactory.getFetcher().loadAvatar(attachment.getNewsPicUrl(),
		// ivNewsPic, R.drawable.news_pic_default);
		//
		// String desc = attachment.getNewsDesc();
		// if (!StringUtil.isNullOrEmpty(desc)) {
		// CharSequence msgBody = ChatViewUtil.instance(getContext())
		// .formatText(desc, null, tvNewsDesc.getLineHeight());
		// tvNewsDesc.setText(msgBody);
		// } else {
		// tvNewsDesc.setText(null);
		// }
		//
		// if (attachment != null && attachment.hasPublisher()) {
		// tvFrom.setVisibility(View.VISIBLE);
		// tvFrom.setText(attachment.getFrom());
		// } else {
		// tvFrom.setVisibility(View.GONE);
		// }
		// if (refreshConvert) {
		// refreshConvertStatus(false);
		// }
	}

	@Override
	public void recycle() {
		super.recycle();
		ivNewsPic.setImageBitmap(null);
	}

	@Override
	public void onClick(View v) {
		// if (v == container) {
		// switch (msg.messgeType) {
		// case IMProtocolConstant.IMMessageTypeAction: {
		// if (msg.getAttachMent() != null) {
		// AUriMgr.instance().viewRes(context,
		// msg.getAttachMent().hashcode);
		// }
		// break;
		// }
		// default: {
		// NavUtil.showNewsDetail(context, userOther, msg);
		// break;
		// }
		// }
		//
		// } else if (v.equals(tvFrom)) {
		// IMAttachment attachment = msg.getAttachMent();
		// if (attachment != null) {
		// long fromId = attachment.getFromId();
		// if (fromId > 0) {
		// IMUser fromUser = DatabaseHelper.getHelper().getUserDao()
		// .createUserIfNotExist(fromId, attachment.getFrom());
		// if (fromUser != null) {
		// IMUIUtils.launchUserDetail(getContext(), fromUser);
		// }
		// }
		// }
		//
		// } else {
		// super.onClick(v);
		// }
	}

	@Override
	protected void resendMsg(IMMessage msg) {
		// if (msg.messgeType == IMProtocolConstant.IMMessageTypeForwardNews) {
		// IMAttachment attach = msg.getLatestAttachMent();
		// LinkToTemplateInfo info = ZHLoadManager.Instance().getLinkMgr()
		// .getLoadInfo(attach.downloadToken);
		// if (info != null) {
		// ZHLoadManager.Instance().getLinkMgr()
		// .startByToken(context, info.token);
		// } else {
		// sendMsg(msg);
		// }
		// ivError.setVisibility(View.GONE);
		// pbSending.setVisibility(View.VISIBLE);
		// } else {
		// super.resendMsg(msg);
		// }
	}

	private void sendMsg(IMMessage msg) {
		// String title = msg.messageBody;
		// IMAttachment attach = msg.getLatestAttachMent();
		// attach.downloadToken = -1;
		// AttachmentDao attDao = DatabaseHelper.getHelper().getAttachmentDao();
		// try {
		// attDao.update(attach);
		// } catch (java.sql.SQLException e) {
		// e.printStackTrace();
		// }
		// String picUrl = attach.getNewsPicUrl();
		// String imgUrl = picUrl == null ? "" : picUrl;
		// ZHMessageForwardNews news = new ZHMessageForwardNews.Builder()
		// .newsUrl(attach.getNewsUrl()).title(title).picurl(imgUrl)
		// .classId(0).desc(attach.getNewsDesc()).build();
		// IMMsgUserModule msgModule;
		// if (!msg.groupMessage) {
		// msgModule = IMService.IM_SERVICE.getMessageModule();
		// } else {
		// msgModule = IMService.IM_SERVICE.getGroupModule();
		// }
		// msgModule.sendForwardNews(msg, news, context, null);
	}

	public void refreshConvertStatus(boolean shouldSend) {

		// if (msg == null)
		// return;
		//
		// long msgId = msg.messageId;
		// MessageDao msgDao = DatabaseHelper.getHelper().getMsgDao();
		// AttachmentDao attDao = DatabaseHelper.getHelper().getAttachmentDao();
		// IMAttachment att = attDao.getAttachmentById(msgId);
		// long token = 0;
		// if (att != null) {
		// token = att.downloadToken;
		// }
		// LinkToTemplateInfo info = ZHLoadManager.Instance().getLinkMgr()
		// .getLoadInfo(token);
		//
		// // IMMessage msg = msgDao.getMessageById(msgId);
		//
		// if (info != null && msg != null) {
		// switch (info.status) {
		//
		// case IMStatusCode.EIMStatusCodeNetworkError: {
		// handleMessageFailed(msg,
		// IMStatusCode.EIMStatusCodeNetworkError, null);
		// ivError.setVisibility(View.VISIBLE);
		// }
		// break;
		// case IMStatusCode.EIMStatusCodeRequestIsBlocked:
		// case IMStatusCode.EIMStatusCodeMessageUserNotContact: {
		// handleMessageFailed(msg, info.status, null);
		// ivError.setVisibility(View.GONE);
		// }
		// break;
		// case LoadStatus.ERROR_NETWORK: {
		// DatabaseHelper
		// .getHelper()
		// .getMsgDao()
		// .updateMessageState(msg.messageId,
		// IMMessage.MSG_STATE_ERROR, false);
		// ivError.setVisibility(View.VISIBLE);
		// pbSending.setVisibility(View.GONE);
		// break;
		// }
		// case LoadStatus.ERROR: {
		// msg.messageBody = "链接分享";
		// msgDao.updateMessage(msg);
		// ivError.setVisibility(View.GONE);
		// pbSending.setVisibility(View.VISIBLE);
		//
		// fill(msg, false);
		// if (shouldSend) {
		// sendMsg(msg);
		// }
		// break;
		// }
		// case LoadStatus.FINISH:
		// att.downloadToken = -1;
		// msg.messageBody = StringUtil.isNullOrEmpty(info.linkTitle) ? "链接分享"
		// : info.linkTitle;
		// att.setNewsPicUrl(info.linkFirstImg);
		// if (!StringUtil.isNullOrEmpty(info.linkSummary)) {
		// att.setNewsDesc(info.linkSummary);
		// }
		// attDao.updateAttachment(att);
		// msgDao.updateMessage(msg);
		// ivError.setVisibility(View.GONE);
		// pbSending.setVisibility(View.GONE);
		// fill(msg, false);
		// if (shouldSend) {
		// sendMsg(msg);
		// }
		// break;
		// case LoadStatus.LOADING: {
		// pbSending.setVisibility(View.VISIBLE);
		// fill(msg, false);
		// break;
		// }
		// case LoadStatus.WAIT: {
		// ivError.setVisibility(View.GONE);
		// break;
		// }
		// }
		// }
	}

	private void handleMessageFailed(Message msg, int status, String des) {
		// if (msg.groupMessage) {
		// ReqHandleSendGroupMessage.handleGroupSendMsgFailed(msg.messageId,
		// status, des, msg.friendId);
		// } else {
		// ReqHandleSendMessage.handleMessageSendMsgFailed(msg.messageId,
		// status, des, msg.friendId);
		// }
	}

	@Override
	public void refresh(int status, int progress) {
		// TODO Auto-generated method stub

	}
}
