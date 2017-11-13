package com.zhisland.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;

public class ScreenTool {

    public static Bitmap checkRotateImage(String path, Bitmap bitmap) {

        // if (Build.VERSION.SDK_INT <= 7) {
        // return null;
        // }

        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(path);
            int tag = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            int degree = 0;
            if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
                degree = 90;
            } else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
                degree = 180;
            } else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
                degree = 270;
            }
            if (degree != 0 && bitmap != null) {

                Bitmap bitMap = null;

                Matrix m = new Matrix();
                m.setRotate(degree, (float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);

                bitMap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), m, true);
                return bitMap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static final void HideInput(Activity activity) {
        View curFoc = activity.getCurrentFocus();
        if (curFoc != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(curFoc.getWindowToken(), 0);
        }
    }

    /**
     * 强制隐藏输入法软键盘
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }

}
