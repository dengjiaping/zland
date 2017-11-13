package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.im.data.IMMessage;
import com.zhisland.lib.util.StringUtil;

public class RowSectionTitle extends BaseRowListenerImpl {

	View rootView;
	TextView tv;

	public RowSectionTitle(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		rootView = inflater.inflate(R.layout.chat_row_title, null);
		rootView.setTag(this);
		tv = (TextView) rootView.findViewById(R.id.textView);
	}

	@Override
	public void fill(IMMessage msg) {
		super.fill(msg);
		if (msg != null) {
			if (StringUtil.isNullOrEmpty(msg.body)) {
				tv.setText(StringUtil.getTimeString(msg.time));
			} else {
				tv.setText(msg.body);
			}
		}
	}

	@Override
	public View getView() {
		return rootView;
	}

	@Override
	public void refresh(int status, int progress) {
		// TODO Auto-generated method stub

	}

}
