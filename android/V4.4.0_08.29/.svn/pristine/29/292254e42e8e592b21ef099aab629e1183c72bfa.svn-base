package com.zhisland.android.blog.im.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.lib.util.text.SpanCreator;
import com.zhisland.lib.util.text.ZHImageSpan;

public class ImageSpanCreator implements SpanCreator {

    @Override
    public Object createSpan(Context context, String regex, String text,
                             Object obj) {
        Drawable drawable = ExpressParser
                .getInstance().expIconGetter(context)
                .getDrawable(text);
        return new ZHImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
    }

    public Bitmap getBitmapWithSize(Bitmap bitmap, int width, int hight) {
        if (bitmap == null) {
            return null;
        }

        Bitmap desBitmap = Bitmap.createBitmap(width, hight, Config.ARGB_8888);

        Canvas canvas = new Canvas();
        canvas.setBitmap(desBitmap);

        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect destRect = new Rect(0, 0, width, hight);

        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setStyle(Style.FILL);

        canvas.save();
        canvas.drawBitmap(bitmap, srcRect, destRect, paint);

        return desBitmap;
    }
}
