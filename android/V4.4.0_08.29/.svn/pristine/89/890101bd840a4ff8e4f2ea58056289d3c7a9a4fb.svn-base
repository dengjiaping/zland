package com.zhisland.android.blog.profilemvp.view.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profilemvp.bean.UserPhoto;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class ProfilePhotoView extends ViewGroup {

    int smallSize;

    int margin = DensityUtil.dip2px(5);

    List<ImageView> imgViews = new ArrayList<ImageView>();

    Context context;

    int imgPadding = DensityUtil.dip2px(1f);

    private List<UserPhoto> photoList;

    private static final String TAG_ADD = "add";
    private static final String TAG_IMG = "img";
    private static final String TAG_NULL = "null";

    public ProfilePhotoView(Context context) {
        this(context, null);
    }

    public ProfilePhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && (r - l) > 0) {
            layoutChildren();
        }
    }

    private void layoutChildren() {
        int paddingTop = getPaddingTop();
        for (int i = 0; i < imgViews.size(); i++) {
            if (i == 0) {
                imgViews.get(0).layout(0, 0 + paddingTop, smallSize * 2, smallSize * 2 + margin + paddingTop);
            }
            if (i == 1) {
                imgViews.get(1).layout(smallSize * 2 + margin, 0 + paddingTop, smallSize * 3 + margin, smallSize + paddingTop);
            }
            if (i == 2) {
                imgViews.get(2).layout(smallSize * 2 + margin, smallSize + margin + paddingTop, smallSize * 3 + margin, smallSize * 2 + margin + paddingTop);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth();
        int rowW = viewWidth - getPaddingLeft() - getPaddingRight();
        margin = DensityUtil.dip2px(5);
        smallSize = (rowW - margin) / 3;
        int height = smallSize * 2 + margin + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                getChildAt(i).measure(smallSize * 2, smallSize * 2 + margin);
            } else {
                getChildAt(i).measure(smallSize, smallSize);
            }
        }
    }

    private void init(AttributeSet attrs) {
        makeAndAddView();
    }

    private void makeAndAddView() {
        imgViews.clear();
        removeAllViews();
        for (int i = 0; i < 3; i++) {
            ImageView iv = new ImageView(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                iv.setCropToPadding(true);
            }
            iv.setPadding(imgPadding, imgPadding, imgPadding, imgPadding);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#dddddd"));
            iv.setOnClickListener(ItemClick);
            imgViews.add(iv);
            addView(iv);
        }
        setDataToView();
    }

    public void setPhoto(List<UserPhoto> photoList) {
        this.photoList = photoList;
        setDataToView();
    }

    private void setDataToView() {
        if (imgViews == null || imgViews.size() < 3) {
            return;
        }
        if (photoList == null || photoList.size() == 0) {
            imgViews.get(0).setImageResource(R.drawable.img_prefile_photo_add);
            imgViews.get(0).setTag(TAG_ADD);
            imgViews.get(0).setVisibility(VISIBLE);
            imgViews.get(1).setTag(TAG_NULL);
            imgViews.get(1).setVisibility(GONE);
            imgViews.get(2).setTag(TAG_NULL);
            imgViews.get(2).setVisibility(GONE);
        } else if (photoList.size() == 1) {
            ImageWorkFactory.getFetcher().loadImage(photoList.get(0).url, imgViews.get(0), ImageWorker.ImgSizeEnum.MIDDLE);
            imgViews.get(0).setTag(TAG_IMG);
            imgViews.get(0).setVisibility(VISIBLE);
            imgViews.get(1).setImageResource(R.drawable.img_prefile_photo_add);
            imgViews.get(1).setTag(TAG_ADD);
            imgViews.get(1).setVisibility(VISIBLE);
            imgViews.get(2).setTag(TAG_NULL);
            imgViews.get(2).setVisibility(GONE);
        } else {
            ImageWorkFactory.getFetcher().loadImage(photoList.get(0).url, imgViews.get(0), ImageWorker.ImgSizeEnum.MIDDLE);
            imgViews.get(0).setTag(TAG_IMG);
            imgViews.get(0).setVisibility(VISIBLE);
            ImageWorkFactory.getFetcher().loadImage(photoList.get(1).url, imgViews.get(1), ImageWorker.ImgSizeEnum.SMALL);
            imgViews.get(1).setTag(TAG_IMG);
            imgViews.get(1).setVisibility(VISIBLE);
            imgViews.get(2).setImageResource(R.drawable.img_prefile_photo_add);
            imgViews.get(2).setTag(TAG_ADD);
            imgViews.get(2).setVisibility(VISIBLE);
        }
    }

    OnClickListener ItemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (callBack == null) {
                return;
            }
            Object obj = v.getTag();
            if (obj != null && obj instanceof String) {
                if (((String) obj).equals(TAG_ADD)) {
                    callBack.onAddClick();
                } else if (((String) obj).equals(TAG_IMG)) {
                    callBack.onImageClick();
                }
            }
        }
    };

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {

        void onImageClick();

        void onAddClick();

    }

}
