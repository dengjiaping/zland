//package com.zhisland.android.blog.common.util;
//
//import java.util.Hashtable;
//
//import android.graphics.Bitmap;
//import android.view.View;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//
//public final class QRCodeUtil {
//	private static final int BLACK = 0xff000000;
//
//	public static Bitmap createQRCode(String str, int widthAndHeight)
//			throws WriterException {
//		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//		BitMatrix matrix = new MultiFormatWriter().encode(str,
//				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
//		int width = matrix.getWidth();
//		int height = matrix.getHeight();
//		int[] pixels = new int[width * height];
//
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				if (matrix.get(x, y)) {
//					pixels[y * width + x] = BLACK;
//				}
//			}
//		}
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}
//
//	public static Bitmap getScreenShotByView(View view) {
//		view.setDrawingCacheEnabled(true);
//		view.buildDrawingCache();
//		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//		view.setDrawingCacheEnabled(false);
//		return bitmap;
//	}
//}