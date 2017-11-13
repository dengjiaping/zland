package com.zhisland.lib.image.viewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.lib.R;
import com.zhisland.lib.bitmap.ImageCache;
import com.zhisland.lib.component.act.BaseFragmentActivity;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.image.GalleryListener;
import com.zhisland.lib.image.NewsGallery;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 浏览图片集的大图，支持图片随手势放大缩小，也可支持删除
 * <p/>
 * 使用方法： FreeImageViewer.invoke(Context, ArrayList<ZHPicture>, curIndex,
 * maxIndex, rightButtonId, reqCode);
 * 参数含义：Context/图片集合/图片当前位置/图片总数/titlebar右侧按钮（现在支持保存、删除），可为空/请求码
 * <p/>
 * 删除图集中图片返回删除位置,RESULT_CODE = FreeImageViewer.POST_IMAGE_DELETE int deleteIndex
 * = data.getIntExtra(FreeImageViewer.TO_INDEX, -1)；
 */
public class FreeImageViewer extends BaseFragmentActivity implements GalleryListener {

    // 显示titlebar样式
    public static final int TYPE_SHOW_TITLE_BAR = 1;
    // 显示只有数字的样式
    public static final int TYPE_SHOW_NUMBER = 2;

    public static final String IMAGES = "freeimages";
    public static final String CUR_INDEX = "cur_index";
    public static final String MAX_INDEX = "max_index";
    public static final String RIGHT_INDEX = "btn_index";
    public static final String INK_TYPE = "ink_type";

    public static final String DELETE_URLS = "delete_urls";

    /**
     * 右侧按钮，支持保存，删除功能
     */
    public static final int BUTTON_SAVE = 100;
    public static final int BUTTON_DELETE = 101;

    /**
     * 用户删除RESULT_CODE
     */
    public static final int POST_IMAGE_DELETE = 1009;

    private static final String TAG_DELETE = "tag_delete";

    public static int screenWidth;
    public static int screenHeight;
    private NewsGallery gallery;
    private GalleryAdapter adapter;
    private boolean isFullScreen = false;
    private RelativeLayout titleBar;

    private TextView titleBarTitle;
    private ImageView titleBarBack;
    private TextView titleBarRightBtn;
    private TextView tvNumber;

    private int rightBtnIndex = 0;
    private int curIndex = 0;
    private int maxIndex = 0;

    private ImageDataAdapter urls;
    private ArrayList<String> deleteUrls;

    private int type;

    /**
     * 浏览指定的图片集合
     *
     * @param context
     * @param adapter
     * @param curIndex
     * @param maxIndex
     * @param rightButtonId FreeImageViewer.BUTTON_DELET
     * @param reqCode
     * @param type          FreeImageViewer.TYPE_SHOW_TITLE_BAR
     */
    public static void invoke(Activity context, ImageDataAdapter adapter,
                              int curIndex, int maxIndex, int rightButtonId, int reqCode, int type) {
        Intent intent = new Intent(context, FreeImageViewer.class);
        intent.putExtra(IMAGES, adapter);
        intent.putExtra(CUR_INDEX, curIndex);
        intent.putExtra(MAX_INDEX, maxIndex);
        intent.putExtra(RIGHT_INDEX, rightButtonId);
        intent.putExtra(INK_TYPE, type);
        context.startActivityForResult(intent, reqCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);

        urls = (ImageDataAdapter) this.getIntent()
                .getSerializableExtra(IMAGES);
        if (urls == null || urls.count() < 1) {
            this.finish();
            return;
        }
        this.setSwipeBackEnable(false);

        curIndex = getIntent().getIntExtra(CUR_INDEX, 0);
        maxIndex = getIntent().getIntExtra(MAX_INDEX, 0);
        rightBtnIndex = getIntent().getIntExtra(RIGHT_INDEX, 0);
        type = getIntent().getIntExtra(INK_TYPE, TYPE_SHOW_TITLE_BAR);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.free_image_viewer);

        gallery = (NewsGallery) findViewById(R.id.gallery);
        gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
        gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
        gallery.setGestureListener(this);

        adapter = new GalleryAdapter(this, urls);
        gallery.setAdapter(adapter);
        gallery.setSelection(curIndex, true);

        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                setTitleText(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        tvNumber = (TextView) findViewById(R.id.tvNumber);
        titleBar = (RelativeLayout) findViewById(R.id.navigation);
        titleBarTitle = (TextView) findViewById(R.id.titledes);
        titleBarBack = (ImageView) findViewById(R.id.gallery_back);
        titleBarBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeViewer();
            }
        });
        titleBarRightBtn = (TextView) findViewById(R.id.gallery_action);
        titleBarRightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (rightBtnIndex) {
                    case BUTTON_SAVE: {
                        String item = adapter.getItem(gallery
                                .getSelectedItemPosition());
                        if (ImageCache.getInstance().containBitmap(
                                StringUtil.validUrl(item))) {
                            saveImage();
                        }
                    }
                    break;
                    case BUTTON_DELETE:
                        showConfirmDlg(TAG_DELETE, "确认删除图片吗？", "确定", "取消", null);
                        break;
                }

            }
        });
        screenWidth = DensityUtil.getWidth();
        screenHeight = DensityUtil.getHeight();

        switch (type) {
            case TYPE_SHOW_TITLE_BAR:
                titleBar.setVisibility(View.VISIBLE);
                setRightBtnText();
                break;
            case TYPE_SHOW_NUMBER:
                tvNumber.setVisibility(View.VISIBLE);
                break;
        }
        setTitleText(curIndex);
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_NONE;
    }

    public void setRightBtnText() {
        switch (rightBtnIndex) {
            case BUTTON_SAVE:
                if (titleBarRightBtn != null) {
                    titleBarRightBtn.setText("保存");
                }
                break;
            case BUTTON_DELETE:
                if (titleBarRightBtn != null) {
                    titleBarRightBtn.setText("删除");
                }
                break;
            default:
                titleBarRightBtn.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 设置标题
     */
    public void setTitleText(int cur) {
        switch (type) {
            case TYPE_SHOW_TITLE_BAR:
                if (cur >= 0 && maxIndex > 0 && cur < maxIndex) {
                    titleBarTitle.setText(cur + 1 + "/" + maxIndex);
                    curIndex = cur;
                }
                break;
            case TYPE_SHOW_NUMBER:
                if (cur >= 0 && maxIndex > 0 && cur < maxIndex) {
                    tvNumber.setText(cur + 1 + "/" + maxIndex);
                    curIndex = cur;
                }
                break;
        }
    }

    /**
     * 保存图片
     */
    private void saveImage() {
        if (FileUtil.isExternalMediaMounted()) {

            if (adapter == null)
                return;

            String url = adapter.getItem(gallery.getSelectedItemPosition());
            String originalFilePath = ImageCache.getInstance()
                    .getFileFromDiskCache(url);
            if (StringUtil.isNullOrEmpty(originalFilePath))
                return;

            String imageName = FileUtil.convertFileNameFromUrl(url);

            File sdcardFileDir = FileMgr.Instance().getDir(
                    FileMgr.DirType.IMAGE);

            File sdcardFile = new File(sdcardFileDir, imageName + ".jpg");
            FileUtil.copyFile(new File(originalFilePath), sdcardFile);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(sdcardFile)));

            ToastUtil.showLong("已成功保存至  " + sdcardFile.getAbsolutePath());

        } else {
            ToastUtil.showLong("请插入SD卡");
        }
    }

    @Override
    public void onSingleTabUp() {
        switch (type) {
            case TYPE_SHOW_TITLE_BAR:
                if (isFullScreen) {
                    titleBar.setVisibility(View.INVISIBLE);
                    adapter.setDescVisible(0);
                } else {
                    titleBar.setVisibility(View.VISIBLE);
                    adapter.setDescVisible(1);
                }
                isFullScreen = !isFullScreen;
                break;
            case TYPE_SHOW_NUMBER:
                FreeImageViewer.this.finish();
                break;
        }
    }


    @Override
    public void onOkClicked(String tag, Object arg) {
        hideConfirmDlg(TAG_DELETE);
        deleteImage();
    }

    @Override
    public void onNoClicked(String tag, Object arg) {
        hideConfirmDlg(TAG_DELETE);
    }

    /**
     * 删除图片
     */
    private void deleteImage() {
        int position = gallery.getSelectedItemPosition();
        String deleteUrl = urls.getUrl(position);
        if (deleteUrls == null) {
            deleteUrls = new ArrayList<>();
        }
        deleteUrls.add(deleteUrl);

        if (adapter.getCount() == 1) {
            urls.remove(position);
            closeViewer();
        } else {
            if (curIndex == (maxIndex - 1)) {
                curIndex--;
            }
            maxIndex--;
            setTitleText(curIndex);

            urls.remove(position);
            adapter = new GalleryAdapter(this, urls);
            gallery.setAdapter(adapter);
            gallery.setSelection(curIndex, false);
        }
    }

    /**
     * 关闭浏览
     */
    private void closeViewer() {
        Intent intent = new Intent();
        intent.putExtra(DELETE_URLS, deleteUrls);
        setResult(POST_IMAGE_DELETE, intent);
        FreeImageViewer.this.finish();
    }


    @Override
    public String getPageName() {
        return null;
    }
}