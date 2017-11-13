package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.im.data.IMMessage;

public class RowLocation extends BaseRowView {

	View imageViewContent;
	TextView locText;

	public RowLocation(Context context) {
		super(context, CONTENT_TYPE_BLOCK);
	}

	@Override
	public void addContentView(MaxWidthLinearLayout container, Context context) {
		imageViewContent = inflater.inflate(R.layout.chat_row_location, null);
		locText = (TextView) imageViewContent.findViewById(R.id.loc_text);
		this.pbSending.setVisibility(View.GONE);
		container.addView(imageViewContent);
	}

	@Override
	public void fill(IMMessage msg) {
		super.fill(msg);
		if (msg == null)
			return;

		// fillMenu(imageViewContent);

		// IMAttachment att = msg.getAttachMent();
		// if (att == null)
		// return;

		// locText.setText(att.getLocation());
		pbSending.setVisibility(View.GONE);
		ivError.setVisibility(View.GONE);
	}

	@Override
	public void recycle() {
		super.recycle();
		// cleanMenu(imageViewContent);
	}

	@Override
	public void onClick(View view) {
		if (view == container) {
//			IMAttachment att = msg.getAttachMent();
//			if (att == null)
//				return;
//
//			Intent intent = new Intent(context, BDMapActivity.class);
//			intent.putExtra(BDMapActivity.MAP_CENTER_KEY_LAT,
//					(int) (att.latitude * 1E6));
//			intent.putExtra(BDMapActivity.MAP_CENTER_KEY_LON,
//					(int) (att.logintude * 1E6));
//			context.startActivity(intent);
		} else {
			super.onClick(view);
		}
	}

	@Override
	public void refresh(int status, int progress) {
		// TODO Auto-generated method stub
		
	}
}
