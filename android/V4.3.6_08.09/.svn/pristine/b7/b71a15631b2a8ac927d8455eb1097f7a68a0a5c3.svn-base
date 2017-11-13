package com.zhisland.android.blog.common.view.express;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.PageControl;
import com.zhisland.lib.view.SwipeView;
import com.zhisland.lib.view.SwipeView.InterceptListener;

public class SimpleAttachController implements OnClickListener {

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

	private final int totalWidth;

	private int totalHeight;

	public SimpleAttachController(Activity activity,
			SimpleController simpleController, View container, EditText et) {
		this.activity = activity;
		this.container = container;
		this.et = et;

		this.parser = ExpressParser.getInstance(activity);

		totalWidth = DensityUtil.getWidth();

		initViews();
		fillExpress();
		sv.setPageControl(pc);
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
					if (offset > con.length()) {
						offset = con.length();
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

		// calculate gridview's height
		totalHeight = PrefUtil.Instance().getInputHeight();
		if (totalHeight <= 0) {
			totalHeight = getAttachDefaultHeight();
		}
		refreshAttachHeight(totalHeight);
	}

	/**
	 * 根据输入法的高度调整输入框的高度
	 */
	public void refreshAttachHeight(int height) {
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) container
				.getLayoutParams();
		params.height = height;
		container.setLayoutParams(params);
	}

	public int getAttachDefaultHeight() {
		return ExpressAdapter.EXP_PANNEL_HEIGH
				+ ExpressAdapter.PAGE_INDICATOR_HEIGHT + DensityUtil.dip2px(25);
	}

}
