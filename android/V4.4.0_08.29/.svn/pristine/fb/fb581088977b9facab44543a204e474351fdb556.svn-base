package com.zhisland.android.blog.im.view;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.im.view.AttachAdapter.Attach;
import com.zhisland.android.blog.im.view.AttachAdapter.AttachTile;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileMgr.DirType;
import com.zhisland.lib.view.PageControl;
import com.zhisland.lib.view.SwipeView;
import com.zhisland.lib.view.SwipeView.InterceptListener;

/**
 * 用来控制底部的表情、附件栏
 *
 * @author arthur
 */
public class AttachController implements OnClickListener {

    public static final int ICON_WIDTH = DensityUtil.dip2px(80);
    public static final int VERTICAL_SPACING = DensityUtil.dip2px(10);

    private final Activity activity;
    private final View container;
    private final SessBottomController controller;
    private boolean isFromGroup;
    private long groupId;

    private RelativeLayout rlExp;
    private SwipeView sv;
    private SwipeView svBigEmotion;
    private PageControl pc;
    private GridView gvAttach;
    private Button btnDel;
    private ImageView ivExpressTabbar;
    private ImageView ivBigEmotionTabbar;

    private final AlphaAnimation aniShow, aniHide;

    private final ExpressParser parser;
    private final BigEmotionParser emotionParser;
    private final EditText et;

    private final int totalWidth;

    protected boolean isCollapsed = false;

    private ISessController sessControllerListener;
    private int totalHeight;

    public String tmpPath;

    public AttachController(Activity activity,
                            SessBottomController sessViewController, View container,
                            EditText et, ISessController listener, boolean isFromGroup) {
        this.activity = activity;
        this.container = container;
        this.et = et;
        this.controller = sessViewController;
        this.sessControllerListener = listener;
        this.isFromGroup = isFromGroup;

        this.parser = ExpressParser.getInstance();
        emotionParser = BigEmotionParser.getInstance(activity);

        totalWidth = DensityUtil.getWidth();

        aniShow = new AlphaAnimation(0.0f, 1.0f);
        aniShow.setDuration(50);

        aniHide = new AlphaAnimation(1.0f, 0.0f);
        aniHide.setDuration(50);

        init();
        fillAttach();
        fillExpress();
        fillCustomExpress();
        sv.setPageControl(pc);
    }

    private void fillAttach() {
        int count = totalWidth / ICON_WIDTH;
        gvAttach.setNumColumns(count - 1);
        AttachAdapter adapter = new AttachAdapter(activity, this, isFromGroup);
        gvAttach.setAdapter(adapter);
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
                    CharSequence con = ChatViewUtil.instance()
                            .formatText(activity, content, null, et.getLineHeight());
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

    private void fillCustomExpress() {
        int columnNum = totalWidth / BigEmotionAdapter.EMOTION_WIDTH;
        int pageCount = columnNum * BigEmotionAdapter.LINE_TOTAL_NUM;
        int pageNum = (emotionParser.count() + pageCount - 1) / pageCount;

        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sessControllerListener != null) {
                    sessControllerListener.OnBigEmotionClick(v);
                }
            }
        };

        for (int i = 0; i < pageNum; i++) {
            GridView gv = new GridView(activity);
            BigEmotionAdapter adapter = new BigEmotionAdapter(activity, gv,
                    i + 1, columnNum, clickListener);
            adapter.init(svBigEmotion);
        }
    }

    public void setbigEmotionClickListener(ISessController listener) {
        sessControllerListener = listener;
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

        AttachTile tile = (AttachTile) v;
        Attach attach = (Attach) v.getTag();

        tile.hideNew();

        switch (attach.id) {
            case AttachAdapter.VIDEO: {
                tmpPath = FileMgr.Instance().getFilepath(DirType.TMP,
                        UUID.randomUUID().toString() + ".jpg");
                Intent intent = IntentUtil.intentToCaptureImage(tmpPath);
                activity.startActivityForResult(intent,
                        FragChatViewController.REQ_VIDEO_CAPTURE);
                break;
            }
            case AttachAdapter.PIC: {
                MultiImgPickerActivity.invoke(activity, 8,
                        FragChatViewController.REQ_IMG_SELECT);
                break;
            }
            case AttachAdapter.EXPRESS: {
                showExpress();
                break;
            }
            case AttachAdapter.VCARD: {
                // Intent intent = new Intent(activity, UserListFragActivity.class);
                // intent.putExtra(UserListFragment.USER_LIST_TYPE,
                // UserListFragment.TYPE_VCARDS);
                //
                // activity.startActivityForResult(intent,
                // ChatSessionFragment.REQ_SELECT_VCARD);
                // TODO 选择联系人
                break;
            }
            case AttachAdapter.LOCATION: {
                // Intent intent = new Intent(activity, GetLocActivity.class);
                // activity.startActivityForResult(intent,
                // ChatSessionFragment.REQ_GET_LOC);
                // TODO select location
                break;
            }
            case AttachAdapter.INVEITE: {
                sessControllerListener.onInviteMemeberClicked();
                break;
            }
            case AttachAdapter.FEED: {
                // Intent intent = new Intent(activity, PostActivity.class);
                // intent.putExtra(PostActivity.GROUP_ID, this.groupId);
                // activity.startActivity(intent);
                break;
            }
        }
    }

    /**
     * 展示attach
     */
    public void showAttach() {
        rlExp.setVisibility(View.GONE);
        gvAttach.setVisibility(View.VISIBLE);
    }

    /**
     * 展示表情
     */
    public void showExpress() {
        rlExp.setVisibility(View.VISIBLE);
        gvAttach.setVisibility(View.GONE);
    }

    /**
     * 现在是否是attach可见
     *
     * @return
     */
    public boolean isAttachShowing() {
        return gvAttach.getVisibility() == View.VISIBLE;
    }

    public void show() {
        if (isCollapsed) {
            isCollapsed = false;
            container.setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        if (!isCollapsed) {
            isCollapsed = true;
            // container.setVisibility(View.GONE);
            // gvAttach.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置swipe的intercept监听器
     */
    public void setExpressInterceptListener(InterceptListener interceptListener) {
        sv.interceptListener = interceptListener;
        svBigEmotion.interceptListener = interceptListener;
    }

    private void init() {
        rlExp = (RelativeLayout) container.findViewById(R.id.rl_express);
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
        Object lay = container.getLayoutParams();
        if (lay instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) lay;
            params.height = height;
            container.setLayoutParams(params);
        }
    }

    public static int getAttachDefaultHeight() {
        return ExpressAdapter.EXP_PANNEL_HEIGH
                + ExpressAdapter.PAGE_INDICATOR_HEIGHT + DensityUtil.dip2px(25);
    }

}
