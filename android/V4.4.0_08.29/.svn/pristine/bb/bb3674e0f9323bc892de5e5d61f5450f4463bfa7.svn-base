package com.zhisland.android.blog.im.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zhisland.android.blog.R;

public class BigEmotionParser {
	public static String[] expText;
	public static Bitmap[] expIcons;
	private final HashMap<String, Bitmap> expToRes;

	private final Context mContext;
	private static BigEmotionParser _instance;

	public static BigEmotionParser getInstance(Context context) {
		if (_instance == null) {
			_instance = new BigEmotionParser(context);
		}
		return _instance;
	}

	public void destroy(Context context) {
		expToRes.clear();
		for (Bitmap bm : expIcons) {
			if (bm != null && !bm.isRecycled()) {
				bm.recycle();
			}
		}
		_instance = null;
	}

	public int count() {
		return expText.length;
	}

	public Bitmap res(int pos) {
		return expIcons[pos];
	}

	public String text(int pos) {
		return expText[pos];
	}

	private BigEmotionParser(Context context) { 
		mContext = context;
		expText = mContext.getResources().getStringArray(
				R.array.big_emotion_img_name);
		expToRes = buildLargeToRes();
	}

	public Bitmap getBitmap(String text) {
		return expToRes.get(text);
	}

	private HashMap<String, Bitmap> buildLargeToRes() {
		String[] emotionNames = mContext.getResources().getStringArray(
				R.array.big_emotion_img_name);

		if (emotionNames.length != expText.length) {
			throw new IllegalStateException("Emotion resource ID/text mismatch");
		}

		int iconNum = emotionNames.length;
		expIcons = new Bitmap[iconNum];
		HashMap<String, Bitmap> eMotionToRes = new HashMap<String, Bitmap>(
				iconNum);
		for (int i = 0; i < iconNum; i++) {
			String iconFileName = "big_emotion/" + emotionNames[i] + ".png";
			Bitmap iconBitmap = getImageFromAssets(mContext, iconFileName);
			if (iconBitmap == null)
				continue;
			expIcons[i] = iconBitmap;
			eMotionToRes.put(expText[i], expIcons[i]);
		}

		return eMotionToRes;
	}

	static Bitmap getImageFromAssets(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	public static Bitmap decodeStream(InputStream picStream) {
		Bitmap bitmap;
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = 4;
		bitmap = BitmapFactory.decodeStream(picStream);
		return bitmap;
	}

	public static String[] getBigEmotionTexts() {
		return expText;
	}

	public static Bitmap[] getBigEmotionIcons() {
		return expIcons;
	}
}
