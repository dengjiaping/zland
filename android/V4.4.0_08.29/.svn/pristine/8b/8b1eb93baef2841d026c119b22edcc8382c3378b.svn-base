package com.zhisland.android.blog.im.view.row;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMMessage;
import com.zhisland.im.util.Constants;
import com.zhisland.lib.bitmap.ImageResizer;
import com.zhisland.lib.image.viewer.DefaultStringAdapter;
import com.zhisland.lib.image.viewer.FreeImageViewer;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 聊天的图片行
 * 
 * @author arthur
 *
 */
public class RowImage extends BaseRowView {

	private static final int MAX_LENGTH = DensityUtil.getWidth() / 3;
	RelativeLayout rlProgress;
	ImageView image;
	TextView progressValue;
	View imageViewContent;

	public RowImage(Context context) {
		super(context, CONTENT_TYPE_NONE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.chat_row_container: {
			List<IMMessage> imageMsgs = DBMgr.getHelper().getMsgDao()
					.getPicMessages(msg.chat);
			if (imageMsgs == null || imageMsgs.size() < 1)
				return;

			DefaultStringAdapter adapter = new DefaultStringAdapter();

			int i = 0, fromIndex = -1;
			for (IMMessage item : imageMsgs) {
				if (StringUtil.isNullOrEmpty(item.url)) {
					adapter.add(item.local);
				} else {
					adapter.add(item.url);
				}
				if (fromIndex < 0) {
					if (msg.id == item.id) {
						fromIndex = i;
					}
					i++;
				}
			}

			FreeImageViewer.invoke((Activity) context, adapter, fromIndex,
					adapter.count(), -1, -1, FreeImageViewer.TYPE_SHOW_NUMBER);

			break;
		}
		default:
			super.onClick(view);
		}
	}

	// ======== create view methods==========
	@Override
	public void addContentView(MaxWidthLinearLayout container, Context context) {

		imageViewContent = inflater.inflate(R.layout.chat_row_image, null);

		rlProgress = (RelativeLayout) imageViewContent
				.findViewById(R.id.view_chat_row_progress);
		image = (ImageView) imageViewContent.findViewById(R.id.tv_chat_image);
		progressValue = (TextView) imageViewContent
				.findViewById(R.id.tv_chat_progress_value);
		this.pbSending.setVisibility(View.GONE);

		container.addView(imageViewContent);
	}

	@Override
	public void configMyView() {
		super.configMyView();
		container.setPadding(0, 0, 0, 0);
		image.setBackgroundResource(R.drawable.chat_right_transparent);
	}

	@Override
	public void configOtherView() {
		super.configOtherView();
		container.setPadding(0, 0, 0, 0);
		image.setBackgroundResource(R.drawable.chat_left_transparent);
	}

	// ======methods to row listener=======

	@Override
	public void fill(IMMessage msg) {
		super.fill(msg);

		fillMenu(container);

		if (msg != null && msg.body != null) {
			Bitmap bm = ImageResizer.base64ToBitmap(msg.body);
			if (bm != null) {
				resizePicLayout(bm);
				BitmapDrawable bd = new BitmapDrawable(bm);
				container.setBackgroundDrawable(bd);
			} else {
				container
						.setBackgroundResource(R.drawable.information_placeholder);
			}

			if (msg.isSendBySelf()) {
				if (msg.status == Constants.MSG_STATUS_SENDING) {
					IMMessage dbMsg = DBMgr.getHelper().getMsgDao()
							.getMessageByThread(msg.thread);
					msg.status = dbMsg.status;
					msg.progress = dbMsg.progress;
				}
				refresh(msg.status, msg.progress);
			} else {
				rlProgress.setVisibility(View.GONE);
				progressValue.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void recycle() {
		super.recycle();
		this.image.setImageBitmap(null);
		this.progressValue.setText(" 0%");
		cleanMenu(container);
	}

	private void resizePicLayout(Bitmap bm) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.image
				.getLayoutParams();
		int max = bm.getWidth() > bm.getHeight() ? bm.getWidth() : bm.getHeight();
		int height, width;
		//delete max limit for small image, if add these codes, translate small image
		//will show a smaller one.
		//if (max > MAX_LENGTH) {
		width = (int) ((double) MAX_LENGTH / (double) max * (double) bm.getWidth());
		height = (int) ((double) MAX_LENGTH / (double) max * (double) bm.getHeight());
		//} else {
		//	width = bm.getWidth();
		//	height = bm.getHeight();
		//}
		layoutParams.width = width;
		layoutParams.height = height;
		this.image.setLayoutParams(layoutParams);
		layoutParams = (RelativeLayout.LayoutParams) this.rlProgress
				.getLayoutParams();

		layoutParams.width = width;
		layoutParams.height = height;
		this.rlProgress.setLayoutParams(layoutParams);
		this.rlProgress.setGravity(Gravity.CENTER);
		layoutParams = (RelativeLayout.LayoutParams)this.container.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		container.setLayoutParams(layoutParams);
	}

	@Override
	public void refresh(int status, int pro) {
		switch (status) {
		case Constants.MSG_STATUS_ERROR:
		case Constants.MSG_STATUS_CONTINUE: {
			ivError.setVisibility(View.VISIBLE);
			rlProgress.setVisibility(View.GONE);
			break;
		}
		case Constants.MSG_STATUS_SENDING: {
			progressValue.setText(" " + pro + "%");
			rlProgress.setVisibility(View.VISIBLE);
			ivError.setVisibility(View.GONE);
			break;
		}
		case Constants.MSG_STATUS_NORMAL:
		default: {
			progressValue.setText(" " + 100 + "%");
			ivError.setVisibility(View.GONE);
			rlProgress.setVisibility(View.GONE);
			break;
		}
		}

	}

}
