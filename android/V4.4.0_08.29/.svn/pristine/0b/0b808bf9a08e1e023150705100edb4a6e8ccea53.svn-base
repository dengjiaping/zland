package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.im.data.IMMessage;
import com.zhisland.lib.util.DensityUtil;

public class RowVideo extends BaseRowView {

	public View imageViewContent;
	public RelativeLayout progress;
	public ImageView image;
	public TextView progressValue;
	public LinearLayout progressView;
	public ProgressBar progressBar;
	public Button canceBtn;
	public RelativeLayout videoDes;
	public TextView videoSize;
	public TextView videoDuration;

	public RowVideo(Context context) {
		super(context, CONTENT_TYPE_NONE);
	}

	// ======== create view methods==========
	@Override
	public void addContentView(MaxWidthLinearLayout container, Context context) {

		imageViewContent = inflater.inflate(R.layout.chat_row_video, null);
		progress = (RelativeLayout) imageViewContent
				.findViewById(R.id.view_chat_row_progress);
		image = (ImageView) imageViewContent.findViewById(R.id.tv_chat_video);
		progressValue = (TextView) imageViewContent
				.findViewById(R.id.tv_chat_progress_value);
		videoDes = (RelativeLayout) imageViewContent
				.findViewById(R.id.view_chat_video_des);
		videoSize = (TextView) imageViewContent.findViewById(R.id.video_size);
		videoDuration = (TextView) imageViewContent
				.findViewById(R.id.video_duration);
		image.setClickable(false);
		this.pbSending.setVisibility(View.GONE);

		container.addView(imageViewContent);

		// add upload views
		progressView = (LinearLayout) inflater.inflate(
				R.layout.chat_row_video_progress, null);
		progressView.setBackgroundColor(0xfff0efed);
		progressBar = (ProgressBar) progressView
				.findViewById(R.id.pb_chat_row_sending);
		canceBtn = (Button) progressView.findViewById(R.id.btn_pb_cancel);
		canceBtn.setOnClickListener(this);
		container.setOrientation(LinearLayout.VERTICAL);
		container.setGravity(Gravity.CENTER_VERTICAL);
		container.addView(progressView);

	}

	@Override
	public void configMyView() {
		super.configMyView();
		BaseRowUtil.cleanSelfOtherBackground(container);
		BaseRowUtil.configSelfBackground(imageViewContent, contentType);
		imageViewContent.setPadding(0, 0, 0, 0);
		imageViewContent
				.setBackgroundResource(R.drawable.chat_right_transparent);
	}

	@Override
	public void configOtherView() {
		super.configOtherView();
		BaseRowUtil.cleanSelfOtherBackground(container);
		BaseRowUtil.configOtherBackground(imageViewContent, contentType);
		progressView.setVisibility(View.GONE);
		imageViewContent.setPadding(0, 0, 0, 0);
		imageViewContent
				.setBackgroundResource(R.drawable.chat_left_transparent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pb_cancel:
			if (this.msg != null) {
				// IMAttachment att = DatabaseHelper.getHelper()
				// .getAttachmentDao()
				// .getAttachmentById(this.msg.messageId);
				// ZHLoadManager.Instance().getHttpV1UploadMgr()
				// .stopByToken(att.uploadToken);
			}
			break;
		case R.id.chat_row_container: {
			// Intent intent = new Intent(context, ChatPlayActivity.class);
			// intent.putExtra(ChatPlayActivity.MES_ID, msg.messageId);
			// context.startActivity(intent);
			break;
		}
		default:
			super.onClick(v);
			break;
		}
	}

	// ======methods to row listener=======

	@Override
	public void fill(IMMessage msg) {
		super.fill(msg);
		if (msg == null)
			return;

		fillMenu(container);

		// setVideoDes(msg);
		Bitmap bm;
		Bitmap roundedBitmap = null;
		pbSending.setVisibility(View.GONE);
		ivError.setVisibility(View.GONE);

		// if (msg.getAttachMent().thumbnail != null) {
		// bm = BitmapFactory.decodeByteArray(msg.getAttachMent().thumbnail,
		// 0, msg.getAttachMent().thumbnail.length);
		//
		// if (bm != null) {
		// roundedBitmap = ImageRoundFetcher.getRoundedCornerBitmap(bm,
		// 10.0f);
		// BitmapDrawable bd = new BitmapDrawable(roundedBitmap);
		//
		// container.setBackgroundDrawable(bd);
		// } else {
		// container
		// .setBackgroundResource(R.drawable.information_placeholder);
		// }
		// } else {
		// container.setBackgroundResource(R.drawable.information_placeholder);
		// }

		resizePicLayout(roundedBitmap);

		if (msg.isSendBySelf()) {
			refresh(msg.status, msg.progress);
		} else {
			progress.setVisibility(View.GONE);
			progressValue.setVisibility(View.GONE);
			progressView.setVisibility(View.GONE);
		}
		image.setImageResource(R.drawable.video_play);

	}

	private void setVideoDes(IMMessage msg) {

		// IMAttachment att = msg.getAttachMent();
		// if (att != null) {
		// videoSize.setText(att.size / 1024 + "K");
		// videoDuration.setText(IMProtocolUtils.getVideoDuration(att.time));
		// }
	}

	@Override
	public void recycle() {
		super.recycle();
		this.image.setImageBitmap(null);
		this.container.setBackgroundDrawable(null);
		cleanMenu(container);
		this.progressValue.setText(" 0%");
		if (this.progressBar != null) {
			this.progressBar.setProgress(0);
		}
	}

	//
	// @Override
	// public void refreshHttpUpStatus() {
	//
	// if (msg == null)
	// return;

	// long msgId = msg.messageId;

	// AttachmentDao attDao = DatabaseHelper.getHelper().getAttachmentDao();
	// IMAttachment att = attDao.getAttachmentById(msgId);
	// long token = 0;
	// if (att != null) {
	// token = att.uploadToken;
	// }
	// HttpUploadInfo upInfo = ZHLoadManager.Instance().getHttpV1UploadMgr()
	// .getLoadInfo(token);
	// MessageDao msgDao = DatabaseHelper.getHelper().getMsgDao();
	// IMMessage msg = msgDao.getMessageById(msgId);
	//
	// if (upInfo != null) {
	// switch (upInfo.status) {
	// case UploadStatus.ERROR:
	// case IMStatusCode.EIMStatusCodeNetworkError: {
	// handleMessageFailed(msg,
	// IMStatusCode.EIMStatusCodeNetworkError, null);
	// ivError.setVisibility(View.VISIBLE);
	// progress.setVisibility(View.GONE);
	// }
	// break;
	// case IMStatusCode.EIMStatusCodeRequestIsBlocked:
	// case IMStatusCode.EIMStatusCodeMessageUserNotContact: {
	// handleMessageFailed(msg, upInfo.status, null);
	// ivError.setVisibility(View.GONE);
	// progress.setVisibility(View.GONE);
	// }
	// break;
	// case UploadStatus.FINISH:
	// att.uploadToken = -1;
	// msgDao.updateMessageState(msgId, IMMessage.MSG_STATE_FINISHED);
	// att.update(att);
	// attDao.updateAttachment(att);
	// ivError.setVisibility(View.GONE);
	// progressValue.setText(" " + 100 + "%");
	// if (progressBar != null) {
	// progressBar.setProgress(100);
	// }
	//
	// progress.setVisibility(View.GONE);
	// if (progressView != null) {
	// progressView.setVisibility(View.GONE);
	// }
	//
	// break;
	// case UploadStatus.SENDING:
	// case UploadStatus.WAIT:
	// int curBlock = upInfo.curBlock < 0 ? 0 : upInfo.curBlock + 1;
	// int percent = (int) (curBlock * 100 / upInfo.totalBlock);
	// progressValue.setText(" " + percent + "%");
	// if (progressBar != null) {
	// progressBar.setProgress(percent);
	// }
	// progress.setVisibility(View.VISIBLE);
	// ivError.setVisibility(View.GONE);
	// break;
	// }
	// } else {
	// progress.setVisibility(View.GONE);
	// if (progressView != null) {
	// progressView.setVisibility(View.GONE);
	// }
	// }
	// }

	private void resizePicLayout(Bitmap bm) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.image
				.getLayoutParams();

		layoutParams.width = DensityUtil.dip2px(bm.getWidth());
		layoutParams.height = DensityUtil.dip2px(bm.getHeight());
		this.image.setLayoutParams(layoutParams);
		layoutParams = (RelativeLayout.LayoutParams) this.progress
				.getLayoutParams();

		layoutParams.width = DensityUtil.dip2px(bm.getWidth());
		layoutParams.height = DensityUtil.dip2px(bm.getHeight());
		this.progress.setLayoutParams(layoutParams);

		layoutParams = (RelativeLayout.LayoutParams) this.videoDes
				.getLayoutParams();

		layoutParams.width = DensityUtil.dip2px(bm.getWidth());
		this.videoDes.setLayoutParams(layoutParams);
	}

	private void handleMessageFailed(IMMessage msg, int status, String des) {
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
