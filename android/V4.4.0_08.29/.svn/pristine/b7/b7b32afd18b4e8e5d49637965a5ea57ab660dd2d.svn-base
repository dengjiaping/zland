package com.zhisland.android.blog.common.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.util.BitmapUtil;
import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.lib.bitmap.ImageResizer;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.image.viewer.DefaultStringAdapter;
import com.zhisland.lib.image.viewer.FreeImageViewer;
import com.zhisland.lib.load.ZHPicture;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ScreenTool;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.Tools;
import com.zhisland.lib.util.file.FileUtil;
import com.zhisland.lib.view.AutoWrapViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.UUID;

/**
 * 选择照片空间
 */
public class EditPhoto extends LinearLayout implements
        View.OnClickListener {

    private static final int PADDING_VER = DensityUtil.dip2px(10);

    private static final int REQ_MIN = 1007;
    public static final int REQ_CAPTURE_PHOTO = 1008;
    public static final int REQ_SELECT_IMAGE = 1009;
    private static final int REQ_BROWSE_IMAGE = 1010;
    private static final int REQ_MAX = 1011;

    private static final int DEFAULT_ROW_CONTENT_COUNT = 4;
    private static final int DEFAULT_ROW_COUNT = 2;
    public static final int IMG_SIZE = DensityUtil.getWidth() * 8 / 38;

    private static final Stack<WeakReference<ImageView>> recycled = new Stack<WeakReference<ImageView>>();

    private final Activity context;
    private int rowContentCount;
    private int rowCount;
    private final int imgSize;

    private final AutoWrapViewGroup container;
    private final ImageView ivAdd;
    private int resAdd = R.drawable.sel_post_add;
    private String captureImagePath;
    private OnCreateContextMenuListener menuListener;

    // ------public static methos-------

    public static boolean isPhotoRequest(int reqCode) {
        return reqCode > REQ_MIN && reqCode < REQ_MAX;
    }

    /**
     * 获取当前imageview展示图片的URL
     */
    public static String getUrlFromView(View view) {
        Object obj = view.getTag();
        return obj != null ? obj.toString() : "";
    }

    // ------public static methos-------

    /**
     * 每行4个元素，元素大小：元素间隔为4：1, 四周padding：元素间隔 ＝ 1.2：1
     */
    public EditPhoto(Activity context) {
        super(context);
        this.context = context;
        this.imgSize = IMG_SIZE;
        this.rowContentCount = DEFAULT_ROW_CONTENT_COUNT;
        this.rowCount = DEFAULT_ROW_COUNT;

        this.setOrientation(VERTICAL);

        container = new AutoWrapViewGroup(context);
        this.addView(container);
        int horInterval = imgSize / 8;
        int verInterval = imgSize / 8;
        this.config(rowContentCount, imgSize, horInterval, verInterval);

        ivAdd = new ImageView(context);
        ivAdd.setImageResource(resAdd);
        ivAdd.setOnClickListener(this);
        container.addView(ivAdd);
    }

    /**
     * 设置图片大小和四周pad
     *
     * @param imgSize 图片大小
     */
    public void config(int rowContentCount, int imgSize, int horInterval,
                       int verInterval) {
        int padding = (DensityUtil.getWidth() - imgSize * rowContentCount - horInterval
                * (rowContentCount - 1)) / 2;
        this.setPadding(padding, padding, padding, padding);
        container.setHorizontalInterval(horInterval);
        container.setVerticalInterval(verInterval);
        container.setChildSize(imgSize);
        container.setChildRowCount(rowContentCount);
    }

    public void configRow(int rowContentCount, int rowCount) {
        this.rowContentCount = rowContentCount;
        this.rowCount = rowCount;
    }

    /**
     * 设置添加按钮的显示资源ID
     */
    public void setAddResource(int resource) {
        this.resAdd = resource;
        if (ivAdd != null) {
            ivAdd.setImageResource(resource);
        }
    }

    /**
     * 设置按钮的长按菜单
     */
    public void setMenuListener(OnCreateContextMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    /**
     * 去掉出添加按钮外的所有控件
     */
    public void clear() {
        container.removeAllViews();
        container.addView(ivAdd);
    }

    /**
     * 将指定URL对应的view移动到指定index
     */
    public void bringToFirst(String url) {
        final int index = getIndexFromTag(url);
        if (index >= 0) {

            for (int i = index - 1; i >= 0; i--) {
                View tt = container.getChildAt(i + 1);
                int toX = tt.getLeft();
                int toy = tt.getTop();
                View cur = container.getChildAt(i);
                int fromX = cur.getLeft();
                int fromY = cur.getTop();
                cur.animate()
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .translationXBy(toX - fromX)
                        .translationYBy(toy - fromY).setDuration(500).start();
            }
            final View from = container.getChildAt(index);
            View to = container.getChildAt(0);
            int x = to.getLeft() - from.getLeft();
            int y = to.getTop() - from.getTop();
            from.animate().setInterpolator(new OvershootInterpolator())
                    .translationXBy(x).translationYBy(y).setDuration(510)
                    .setListener(new AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            for (int i = 0; i <= index; i++) {
                                View view = container.getChildAt(i);
                                view.setTranslationX(0);
                                view.setTranslationY(0);
                            }
                            container.removeView(from);
                            container.addView(from, 0);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }
                    }).start();

        }
    }

    /**
     * 移出指定的view
     */
    public void removeView(String url) {
        int index = getIndexFromTag(url);
        if (index >= 0) {
            View from = container.getChildAt(index);
            container.removeView(from);
        }

        ivAdd.setVisibility(View.VISIBLE);
    }

    /**
     * 获取选择或者拍摄的相片列表
     */
    public ArrayList<FeedPicture> getSelectedFiles() {
        int size = container.getChildCount();
        if (size <= 0)
            return null;
        ArrayList<FeedPicture> filepaths = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageView iv = (ImageView) container.getChildAt(i);
            if (iv.equals(ivAdd))
                continue;

            String filepath = (String) iv.getTag();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filepath, options);


            FeedPicture fp = new FeedPicture();
            fp.localPath = filepath;
            fp.width = options.outWidth;
            fp.height = options.outHeight;
            filepaths.add(fp);
        }
        return filepaths;
    }

    /**
     * 获取{@link ZHPicture}数组
     */
    public DefaultStringAdapter getPicArray() {

        int size = container.getChildCount();
        if (size <= 0)
            return null;

        DefaultStringAdapter adapter = new DefaultStringAdapter();

        for (int i = 0; i < size; i++) {
            ImageView iv = (ImageView) container.getChildAt(i);
            if (iv.equals(ivAdd))
                continue;
            String url = (String) iv.getTag();
            adapter.add(url);
        }
        return adapter;
    }

    /**
     * recycle all bitmaps, delete all selected or captured tmp files
     */
    public void recycle() {
        ArrayList<ImageView> toRemoved = new ArrayList<ImageView>();
        for (int i = 0, size = getChildCount(); i < size; i++) {
            ImageView iv = (ImageView) getChildAt(i);
            if (iv.equals(ivAdd))
                continue;

            toRemoved.add(iv);
            recycle(iv);
        }
    }

    /**
     * 图库选择
     */
    public void selectImage() {
        int maxCount = rowContentCount * rowCount - container.getChildCount()
                + 1;
        MultiImgPickerActivity.invoke(context, maxCount, REQ_SELECT_IMAGE);
    }

    public void addImage(String pathOrUrl, ZHPicture arg) {
        File file = new File(pathOrUrl);
        if (file.exists()) {
            addImageWithPath(pathOrUrl, arg);
        } else {
            addImageWithUrl(pathOrUrl, arg);
        }
    }

    private void addImageWithUrl(String url, ZHPicture arg) {
        ImageView image = getReusedImageView();
        image.setTag(url);
        if (arg == null) {
            arg = new ZHPicture(null, url);
        }
        image.setTag(R.id.arg1, arg);
        image.setOnCreateContextMenuListener(menuListener);
        ImageWorkFactory.getFetcher().loadImage(url, image);
        addImageView(image);
    }

    /**
     * add imageview by local file path or remote url
     */
    private void addImageWithPath(String path, ZHPicture arg) {
        long filesize;
        String fileName;
        try {

            fileName = getDisposeImagePath(path);

            if (fileName != null && fileName.length() > 0) {
                File file = new File(fileName);
                filesize = file.length();
                if (filesize <= 0) {
                    return;
                } else if (filesize > 2097152) {
                    ToastUtil.showShort("图片过大");
                    return;
                }
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(fileName,
                imgSize, imgSize);

        ImageView image = getReusedImageView();
        image.setImageBitmap(bitmap);
        image.setTag(fileName);
        if (arg == null) {
            arg = new ZHPicture(null, path);
        }
        image.setTag(R.id.arg1, arg);
        image.setOnCreateContextMenuListener(menuListener);
        addImageView(image);
    }

    private void addImageView(ImageView image) {
        container.addView(image, container.getChildCount() - 1);
        if (container.getChildCount() > rowContentCount * rowCount) {
            ivAdd.setVisibility(View.GONE);
        }
    }

    /**
     * recycle imageview
     */
    private void recycle(ImageView iv) {
        String filePath = (String) iv.getTag();
        FileUtil.deleteFile(filePath);
        iv.setImageBitmap(null);
        iv.setTag(null);
        iv.setTag(R.id.arg1, null);
    }

    /**
     * handle photo request
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQ_CAPTURE_PHOTO: {
                if (resultCode == Activity.RESULT_OK) {
                    addImageWithPath(captureImagePath, null);
                    captureImagePath = null;
                }
                break;
            }
            case REQ_SELECT_IMAGE: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        ArrayList<String> pathes = data
                                .getStringArrayListExtra(MultiImgPickerActivity.RLT_PATHES);
                        for (String path : pathes) {
                            addImage(path, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case REQ_BROWSE_IMAGE: {
                if (resultCode == FreeImageViewer.POST_IMAGE_DELETE) {
                    // 删除图片
                    ArrayList<String> deleteUrls = data.getStringArrayListExtra(FreeImageViewer.DELETE_URLS);
                    if (deleteUrls != null) {
                        Iterator<String> iterator = deleteUrls.iterator();
                        while (iterator.hasNext()) {
                            String deleteUrl = iterator.next();
                            removeSingleImage(deleteUrl);
                        }
                    }
                }
            }
        }

    }

    private void removeSingleImage(String deleteUrl) {
        if (TextUtils.isEmpty(deleteUrl)) {
            return;
        }
        removeView(deleteUrl);
    }

    private String getDisposeImagePath(String filePath) {

        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        Bitmap bitMap = BitmapFactory.decodeFile(filePath, localOptions);

        int i = localOptions.outWidth;
        int j = localOptions.outHeight;
        int k = i > j ? i : j;

        File file = new File(filePath);
        long length = file.length();

        int sample = 1;
        while (length / (1024 * 1024) > (4 * sample * sample)) {
            sample *= 2;
        }

        localOptions.inSampleSize = sample;
        localOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        localOptions.inJustDecodeBounds = false;
        bitMap = BitmapFactory.decodeFile(filePath, localOptions);

        Bitmap tmp = ScreenTool.checkRotateImage(filePath, bitMap);
        if (tmp != null) {
            if (!bitMap.isRecycled()) {
                bitMap.recycle();
                System.gc();
            }
            bitMap = tmp;
        }

        ByteArrayOutputStream outPut = null;
        String currentDir = "";
        String outFile;
        try {
            outPut = new ByteArrayOutputStream();
            String currentPath = context.getFilesDir().toString()
                    + "/bigimage/";
            bitMap.compress(CompressFormat.JPEG, 60, outPut);
            outFile = UUID.randomUUID().toString() + ".jpg";
            boolean flag = Tools.saveDataToFile(currentPath, outFile,
                    outPut.toByteArray());
            if (flag) {
                currentDir = currentPath + outFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (outPut != null) {
                try {
                    outPut.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (!bitMap.isRecycled()) {
            bitMap.recycle();
            System.gc();
        }
        return currentDir;
    }

    @Override
    public void onClick(View v) {
        if (v == ivAdd) {
            selectImage();
        } else {
            String url = getUrlFromView(v);
            FreeImageViewer.invoke(context, getPicArray(),
                    getIndexFromTag(url), getSelectedFiles().size(),
                    FreeImageViewer.BUTTON_DELETE, REQ_BROWSE_IMAGE, FreeImageViewer.TYPE_SHOW_TITLE_BAR);
        }
    }

    public int getIndexFromTag(String tagPath) {
        ArrayList<FeedPicture> selectedFiles = getSelectedFiles();
        for (int i = 0; i < selectedFiles.size(); i++) {
            if (tagPath.equals(selectedFiles.get(i).localPath)) {
                return i;
            }
        }
        return 0;
    }

    private ImageView getReusedImageView() {

        WeakReference<ImageView> weakIv;
        if (!recycled.isEmpty()) {
            weakIv = recycled.pop();
            while (weakIv.get() == null && !recycled.isEmpty()) {
                weakIv = recycled.pop();
            }
            if (weakIv.get() != null) {
                return weakIv.get();
            }
        }

        ImageView iv = new ImageView(context);
        iv.setScaleType(ScaleType.CENTER_CROP);
        iv.setOnClickListener(this);
        return iv;
    }
}
