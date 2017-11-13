package com.zhisland.android.blog.common.view.express;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.PageControl;
import com.zhisland.lib.view.SwipeView;
import com.zhisland.lib.view.SwipeView.InterceptListener;

public class expressionController implements OnClickListener {

	public static final int ICON_WIDTH = DensityUtil.dip2px(80);
	public static final int VERTICAL_SPACING = DensityUtil.dip2px(10);

	private final Activity activity;
	private final View container;

	private SwipeView sv;
	private SwipeView svBigEmotion;
	private PageControl pc;
	private GridView gvAttach;
	private Button btnDel;
	private ImageView ivExpressTabbar;
	private ImageView ivBigEmotionTabbar;

	private final ExpressParser parser;
	private final EditText et;
	private int maxLength = -1;

	private final int totalWidth;

	public expressionController(Activity activity, View container, EditText et) {
		this.activity = activity;
		this.container = container;
		this.et = et;
		et.addTextChangedListener(textWatcher);

		this.parser = ExpressParser.getInstance(activity);

		totalWidth = DensityUtil.getWidth();

		initViews();
		fillExpress();
		sv.setPageControl(pc);
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (maxLength > 0) {
				parser.fixContentLength(et, maxLength);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	public int getContentLength() {
		return parser.getContentLength(et);
	}

	private void fillExpress() {

		int columnNum = (totalWidth / ExpressAdapter.CELL_SIZE);
		int pageCount = columnNum * 3 - 1;
		int pageNum = (parser.count() + pageCount - 1) / pageCount;

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = v.getTag().toString();
				if (StringUtil.isEquals(text, ExpressAdapter.TAG_DELETE)) {
					parser.removeExp(et);
				} else {
					String content = et.getText().toString();
					int offset = et.getSelectionStart();
					if (offset > 0) {
						content = content.substring(0, offset) + text
								+ content.substring(offset);
					} else if (offset == 0) {
						content = text + content;
					} else {
						content += text;
					}
					CharSequence con = ChatViewUtil.instance(activity)
							.formatText(content, null, et.getLineHeight());
					et.setText(con);
					offset += text.length();
					if (offset > et.getText().length()) {
						offset = et.getText().length();
					}
					et.setSelection(offset);
				}
			}
		};

		for (int i = 0; i < pageNum; i++) {
			GridView gv = new GridView(activity);
			ExpressAdapter adapter = new ExpressAdapter(activity, gv, i + 1,
					columnNum, clickListener);
			gv.setGravity(Gravity.CENTER);
			gv.setNumColumns(columnNum);
			gv.setAdapter(adapter);
			gv.setVerticalSpacing(DensityUtil.dip2px(15));
			gv.setPadding(DensityUtil.dip2px(15), DensityUtil.dip2px(25),
					DensityUtil.dip2px(15), DensityUtil.dip2px(25));
			sv.addView(gv);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_express_tabbar: {
			sv.setVisibility(View.VISIBLE);
			svBigEmotion.setVisibility(View.GONE);
			btnDel.setVisibility(View.VISIBLE);
			ivExpressTabbar.setSelected(true);
			ivBigEmotionTabbar.setSelected(false);
			sv.setPageControl(pc);
			return;
		}
		case R.id.iv_large_express_tabbar: {
			sv.setVisibility(View.GONE);
			svBigEmotion.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.INVISIBLE);
			ivExpressTabbar.setSelected(false);
			ivBigEmotionTabbar.setSelected(true);
			svBigEmotion.setPageControl(pc);
			return;
		}
		case R.id.btn_chat_del: {
			parser.removeExp(et);
			return;
		}
		}
	}

	/**
	 * 设置swipe的intercept监听器
	 */
	public void setExpressInterceptListener(InterceptListener interceptListener) {
		sv.interceptListener = interceptListener;
		svBigEmotion.interceptListener = interceptListener;
	}

	private void initViews() {
		RelativeLayout rlExp = (RelativeLayout) container
				.findViewById(R.id.rl_express);
		sv = (SwipeView) container.findViewById(R.id.sv_chat);
		svBigEmotion = (SwipeView) container
				.findViewById(R.id.sv_large_express);
		pc = (PageControl) container.findViewById(R.id.pc_chat);
		gvAttach = (GridView) container.findViewById(R.id.gv_chat_attach);
		ivExpressTabbar = (ImageView) container
				.findViewById(R.id.iv_express_tabbar);
		ivBigEmotionTabbar = (ImageView) container
				.findViewById(R.id.iv_large_express_tabbar);
		btnDel = (Button) container.findViewById(R.id.btn_chat_del);

		btnDel.setOnClickListener(this);
		ivExpressTabbar.setOnClickListener(this);
		ivBigEmotionTabbar.setOnClickListener(this);

		sv.setVisibility(View.VISIBLE);
		svBigEmotion.setVisibility(View.GONE);
		ivExpressTabbar.setSelected(true);
		ivBigEmotionTabbar.setSelected(false);

		gvAttach.setPadding(20, 20, 20, 0);

		// set pagecontrol height
		RelativeLayout.LayoutParams pageParam = (LayoutParams) pc
				.getLayoutParams();
		pageParam.height = ExpressAdapter.PAGE_INDICATOR_HEIGHT;
		pc.setLayoutParams(pageParam);

	}

}
