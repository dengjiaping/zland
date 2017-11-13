package com.zhisland.android.blog.feed.view.impl.holder;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.feed.bean.AttachImg;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

/**
 * 负责图片模版的展示
 * Created by zhuchuntao on 16/9/2.
 */
public class ImageHolder implements AttachHolder, View.OnClickListener {

    private Context context;

    private FeedViewListener listener;
    private Feed feed;


    private ImageView oneImage;

    private RelativeLayout twoLayout;
    private ImageView twoFirstImage;
    private ImageView twoSecondImage;

    private HorizontalScrollView manyLayout;

    private LinearLayout manyContainer;

    private View layout;


    public ImageHolder(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.layout_img, null);
        oneImage = (ImageView) layout.findViewById(R.id.image_one_layout);


        twoLayout = (RelativeLayout) layout.findViewById(R.id.image_two_layout);

        twoFirstImage = (ImageView) layout.findViewById(R.id.image_two_layout_one);
        twoSecondImage = (ImageView) layout.findViewById(R.id.image_two_layout_two);

        manyLayout = (HorizontalScrollView) layout.findViewById(R.id.image_many_layout);
        manyContainer = (LinearLayout) layout.findViewById(R.id.image_many_layout_container);

    }

    @Override
    public View getView() {
        return layout;
    }

    @Override
    public void fillAttach(Feed feed, FeedViewListener feedViewListener) {
        this.listener = feedViewListener;
        this.feed = feed;


        if (null != feed && feed.type == FeedType.IMG) {
            AttachImg resource = (AttachImg) feed.attach;
            if (resource != null) {
                List<FeedPicture> pictures = resource.pictures;
                if (null != pictures && pictures.size() > 0) {
                    setDisplayLayout(pictures.size() >= 3 ? 3 : pictures.size(), pictures);
                } else {
                    setDisplayLayout(0, null);
                }
            }
        } else {
            setDisplayLayout(0, null);
        }
    }

    @Override
    public void recycleAttach() {
        this.feed = null;
        this.listener = null;
        oneImage.setImageBitmap(null);
        twoFirstImage.setImageBitmap(null);
        twoSecondImage.setImageBitmap(null);
        manyContainer.removeAllViews();
    }

    @Override
    public void onClick(View view) {
        if (null == listener) {
            return;
        }
        switch (view.getId()){
            case R.id.image_one_layout:
                listener.onAttachClicked(feed, 0);
                break;
            case R.id.image_two_layout_one:
                listener.onAttachClicked(feed, 0);
                break;
            case R.id.image_two_layout_two:
                listener.onAttachClicked(feed, 1);
                break;
            default:
                listener.onAttachClicked(feed, null);
        }

    }

    /*
    针对图片数量的不同,做不同的展示
     */
    private void setDisplayLayout(int pictureNumber, List<FeedPicture> dataList) {
        switch (pictureNumber) {
            case 1:
                oneImage.setVisibility(View.VISIBLE);
                twoLayout.setVisibility(View.GONE);
                manyLayout.setVisibility(View.GONE);
                displayOneImage(oneImage, DensityUtil.getWidth(), dataList);
                oneImage.setOnClickListener(this);

                break;
            case 2:
                oneImage.setVisibility(View.GONE);
                twoLayout.setVisibility(View.VISIBLE);
                manyLayout.setVisibility(View.GONE);
                displayTwoImage(twoFirstImage, twoSecondImage, DensityUtil.getWidth(), dataList);
                twoFirstImage.setOnClickListener(this);
                twoSecondImage.setOnClickListener(this);
                break;
            case 3:
                oneImage.setVisibility(View.GONE);
                twoLayout.setVisibility(View.GONE);
                manyLayout.setVisibility(View.VISIBLE);
                displayManyImage(DensityUtil.getWidth(), dataList);
                break;
            default:
                oneImage.setVisibility(View.GONE);
                twoLayout.setVisibility(View.GONE);
                manyLayout.setVisibility(View.GONE);
                break;
        }
    }

    /*
    显示一张图片
     */
    private void displayOneImage(ImageView image, int screenWidth, List<FeedPicture> dataList) {
        FeedPicture picture = dataList.get(0);

        if (null != picture && picture.width != 0 && picture.height != 0) {
            int height = picture.height;
            int width = picture.width;

            float ratio = (float) width / height;

            RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) image.getLayoutParams();

            if (ratio <= 0.5) {
                //竖图
                if (height < screenWidth) {
                    //图片高度 < 屏幕宽度，原比例
                    para.height = height;
                    para.width = width;
                    para = setImageRule(para, para.width, screenWidth);
                } else {
                    //图片高度 >= 屏幕宽度，等比压缩到屏幕宽度,要达到的目的就是:图片最高就是屏幕的宽度
                    para.height = screenWidth;
                    para.width = screenWidth * width / height;
                    para = setImageRule(para, para.width, screenWidth);
                }

            } else if (ratio > 0.5 && ratio <= 1) {
                //竖图
                if (height < screenWidth) {
                    //图片高度 < 屏幕宽度，原比例
                    para.height = height;
                    para.width = width;
                    para = setImageRule(para, para.width, screenWidth);
                } else {
                    //图片高度 >= 屏幕高度，定宽screenWidth/2,要达到的目的是:图片聚中
                    para.width = screenWidth / 2;
                    para.height = screenWidth * height / width / 2;
                    para = setImageRule(para, para.width, screenWidth);
                }
            } else if (ratio > 1 && ratio <= 2) {
                //横图
                if (width < screenWidth) {
                    //图片宽度 < 屏幕宽度，原始比例
                    para.height = height;
                    para.width = width;
                    para = setImageRule(para, para.width, screenWidth);
                } else {
                    //图片宽度 >= 屏幕宽度，等比压缩，取屏幕宽度,要达到的目的是,图片最宽,就是屏幕的宽度
                    para.width = screenWidth;
                    para.height = screenWidth * height / width;
                    para = setImageRule(para, para.width, screenWidth);

                }

            } else {
                //横图
                if (width < screenWidth) {
                    //图片宽度 < 屏幕宽度，原始比例
                    para.height = height;
                    para.width = width;
                    para = setImageRule(para, para.width, screenWidth);
                } else {
                    //图片宽度 >= 屏幕宽度，等比压缩，取屏幕宽度,要达到的目的是,图片最宽,就是屏幕的宽度
                    para.width = screenWidth;
                    para.height = screenWidth * height / width;
                    para = setImageRule(para, para.width, screenWidth);
                }
            }
            image.setLayoutParams(para);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                image.setCropToPadding(true);
            }
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);


            ImageWorkFactory.getFetcher().loadImage(picture.url, image);

        }

    }

    private RelativeLayout.LayoutParams setImageRule(RelativeLayout.LayoutParams para, int imageWidth, int screenWidth) {
        if (para.width < screenWidth - DensityUtil.dip2px(16) * 2) {
            para.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            para.leftMargin = DensityUtil.dip2px(16);
        } else {
            para.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            para.leftMargin = 0;
        }
        return para;
    }

    /*
    显示两张图片
     */
    private void displayTwoImage(ImageView twoFirstImage, ImageView twoSecondImage, int screenWidth, List<FeedPicture> dataList) {
        int distance = DensityUtil.dip2px(4);
        //计算得到view的宽度,高度跟宽度一致
        int width = (screenWidth - distance) / 2;


        setImageSize(twoFirstImage, width, width);
        setImageSize(twoSecondImage, width, width);

        FeedPicture picture = dataList.get(0);
        setImageSource(twoFirstImage, width, picture);

        FeedPicture picture1 = dataList.get(1);
        setImageSource(twoSecondImage, width, picture1);
    }

    /*
    显示三张甚至更多的图片
     */
    private void displayManyImage(int screenWidth, List<FeedPicture> dataList) {
        int distance = DensityUtil.dip2px(4);
        //计算得到图片的view的宽度
        int width = (int) ((screenWidth - distance) * 46.4 / 100);

        for (int i = 0; i < dataList.size(); i++) {
            ImageView view = new ImageView(context);
            view.setBackgroundResource(R.drawable.rect_bbg2_sdiv_image);
            manyContainer.addView(view);
            view.setOnClickListener(this);

            LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) view.getLayoutParams();
            para.height = width;
            para.width = width;
            if (i != 0)
                para.leftMargin = distance;
            view.setLayoutParams(para);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == listener) {
                        return;
                    }
                    listener.onAttachClicked(feed, finalI);
                }
            });

            view.setPadding(DensityUtil.dip2px(1), DensityUtil.dip2px(1), DensityUtil.dip2px(1), DensityUtil.dip2px(1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setCropToPadding(true);
            }
            FeedPicture picture = dataList.get(i);
            setImageSource(view, width, picture);
        }
    }

    /**
     * 设置view的大小
     *
     * @param view   要设置的view
     * @param width  view的宽度
     * @param height view的高度
     */
    private void setImageSize(ImageView view, int width, int height) {
        ViewGroup.LayoutParams para = view.getLayoutParams();
        para.height = width;
        para.width = width;
        view.setLayoutParams(para);
    }

    /**
     * 设置图片的样式
     *
     * @param view    要显示的view
     * @param size    图片的宽度或者高度(宽度==高度)
     * @param picture 要显示的数据
     */
    private void setImageSource(ImageView view, int size, FeedPicture picture) {

        if (null != picture && picture.width != 0 && picture.height != 0) {
            if (size < picture.height || size < picture.width) {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            ImageWorkFactory.getFetcher().loadImage(picture.url, view);
        }
    }

}
