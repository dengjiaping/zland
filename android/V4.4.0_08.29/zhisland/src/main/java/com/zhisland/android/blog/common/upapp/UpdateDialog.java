package com.zhisland.android.blog.common.upapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.zhisland.android.blog.R;

public class UpdateDialog extends AlertDialog {

	public UpdateDialog(Context context, int theme) {
		super(context, theme);
	}

	public UpdateDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_update_dialog);
	}
}
