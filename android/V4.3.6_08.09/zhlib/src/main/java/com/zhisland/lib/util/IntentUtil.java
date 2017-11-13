package com.zhisland.lib.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

public class IntentUtil {

    public static Intent intentToSendMail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        return intent;
    }

    public static Intent intentToSendSMS(String str) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", str);
        return sendIntent;
    }

    public static void dialTo(Context context, String num) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + num));
        try {
            context.startActivity(i);
        } catch (Exception e) {
            ToastUtil.showShort("该设备不支持拨打电话功能");
        }
    }

    public static Intent intentToSelectVideo() {
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("video/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        return wrapperIntent;
    }

    public static Intent intentToCaptureImage(String filepath) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(filepath)));
        return cameraIntent;
    }

    public static Intent intentToMapApp(long latitude,
                                        long longitude, String label) {
        String format = "geo:0,0?q="
                + Double.toString((double) latitude / (double) 1000000) + ","
                + Double.toString((double) longitude / (double) 1000000) + "("
                + label + ")";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent cropIntent(Uri inUri, int outputX, int outputY, Uri outUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(inUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", outputX);
        cropIntent.putExtra("aspectY", outputY);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropIntent.putExtra("return-data", false);
        return cropIntent;
    }

    /**
     * 调用系统界面，给指定的号码发送短信，并附带短信内容
     *
     * @param context
     * @param number
     * @param body
     */
    public static Intent sendSmsWithBody(String number, String body) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", body);
        return sendIntent;
    }

    /**
     * 跳转系统权限设置界面
     */
    public static void intentToSystemSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }
}
