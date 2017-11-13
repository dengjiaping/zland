package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.im.data.IMMessage;

public class RowVCard extends BaseRowView {

	public ImageView ivVcardAvatar;
	public TextView tvVcardName;
	public TextView tvVcardDesc;
	public View vcardContent;
	public View divider;

	public RowVCard(Context context) {
		super(context, CONTENT_TYPE_BLOCK);
	}

	@Override
	public void addContentView(MaxWidthLinearLayout container, Context context) {
		vcardContent = inflater.inflate(R.layout.chat_vcard_row, null);
		ivVcardAvatar = (ImageView) vcardContent
				.findViewById(R.id.iv_chat_vcard_row_avatar);
		tvVcardName = (TextView) vcardContent
				.findViewById(R.id.iv_chat_vcard_row_name);
		tvVcardDesc = (TextView) vcardContent
				.findViewById(R.id.iv_chat_vcard_row_desc);
		divider = vcardContent.findViewById(R.id.iv_chat_vcard_row_divider);

		container.addView(vcardContent);
	}

	@Override
	public void configMyView() {
		super.configMyView();
		divider.setBackgroundResource(R.color.chat_row_right_divider);
	}

	@Override
	public void configOtherView() {
		super.configOtherView();
		divider.setBackgroundResource(R.color.chat_row_right_divider);
	}

	@Override
	public void fill(IMMessage msg) {
		super.fill(msg);
		if (msg == null)
			return;

		// fillMenu(vcardContent);

		// show content view
		// IMUser vcardUser = DatabaseHelper.getHelper().getUserDao()
		// .getUserById(msg.vcardId);
		// ImageWorkFactory.getCircleFetcher().loadAvatar(vcardUser.avatarUrl,
		// ivVcardAvatar, R.drawable.defaultavatar100);
		// tvVcardName.setText(vcardUser.nickname);
		// tvVcardDesc.setText(vcardUser.title);

	}

	@Override
	public void recycle() {
		super.recycle();
		this.ivVcardAvatar.setImageBitmap(null);
		// cleanMenu(vcardContent);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.chat_row_container: {
			// IMUIUtils.launchUserDetail(context, msg.getVcardUser().userId);
			break;
		}
		default: {
			super.onClick(view);
			break;
		}
		}

	}

	@Override
	public void refresh(int status, int progress) {
		// TODO Auto-generated method stub
		
	}

}
