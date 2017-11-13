package com.zhisland.android.blog.common.view.express;

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

public class ExpressParser {
	public static String[] expText;
	public static Bitmap[] expIcons;
	private final HashMap<String, Bitmap> expToRes;

	private final Pattern pattern;
	private final Context mContext;
	private static ExpressParser _instance;
	private final ExpIconGetter expIconGetter;
	private static final int IMAGE_SIZE = DensityUtil.dip2px(20);

	public static ExpressParser getInstance(Context context) {
		if (_instance == null) {
			_instance = new ExpressParser(context);
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

	private ExpressParser(Context context) {
		mContext = context;
		expText = mContext.getResources().getStringArray(R.array.express_name);
		expToRes = buildSmileyToRes();
		pattern = buildPattern();
		expIconGetter = new ExpIconGetter();
	}

	public CharSequence addSmileySpans(CharSequence text) {

		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			Bitmap icon = expToRes.get(matcher.group());
			ImageSpan span = new ImageSpan(mContext, icon);
			builder.setSpan(span, matcher.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}

	public Bitmap getBitmap(String text) {
		return expToRes.get(text);
	}

	public void removeExp(EditText et) {

		Editable text = et.getText();
		int index = et.getSelectionStart();
		if (index == (text.length() - 1)
				&& text.charAt(text.length() - 1) == ']') {
			index = text.length();
		}
		boolean isMatch = false;
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			int end = matcher.end();
			if (end == index) {
				text = text.replace(matcher.start(), matcher.end(), "");
				et.setSelection(matcher.start());
				isMatch = true;
				break;
			}
		}

		index = et.getSelectionStart();

		if (!isMatch && index > 0) {
			text = text.replace(index - 1, index, "");
			et.setSelection(index - 1);
		}
	}

	private HashMap<String, Bitmap> buildSmileyToRes() {
		String[] smileyIconNames = mContext.getResources().getStringArray(
				R.array.express_drawable_name);

		if (smileyIconNames.length != expText.length) {
			throw new IllegalStateException("Smiley resource ID/text mismatch");
		}

		int iconNum = smileyIconNames.length;
		expIcons = new Bitmap[iconNum];
		HashMap<String, Bitmap> smileyToRes = new HashMap<String, Bitmap>(
				iconNum);
		for (int i = 0; i < iconNum; i++) {
			String iconFileName = "smiley/" + smileyIconNames[i] + ".png";
			Bitmap iconBitmap = getImageFromAssets(mContext, iconFileName);
			if (iconBitmap == null)
				continue;
			expIcons[i] = iconBitmap;
			smileyToRes.put(expText[i], expIcons[i]);
		}

		return smileyToRes;
	}

	public int getContentLength(EditText et) {
		String text = et.getText().toString();
		int count = 0;
		Matcher matcher = pattern.matcher(text);
		int end = 0;
		while (matcher.find()) {
			count += matcher.start() - end;
			end = matcher.end();
			count++;
		}
		return count + text.length() - end;
	}

	public void fixContentLength(EditText et, int maxLength) {
		Editable text = et.getText();
		int count = 0;
		Matcher matcher = pattern.matcher(text);
		int end = 0;
		while (matcher.find()) {
			int countBefore = count;
			count += matcher.start() - end;
			count++;
			if (count == maxLength) {
				replace(text, matcher.end(), text.length());
			} else if (count > maxLength) {
				replace(text, matcher.start() + maxLength - countBefore,
						text.length());
			} else {
			}
			end = matcher.end();
		}
		replace(text, end + maxLength - count, text.length());
	}

	private void replace(Editable text, int start, int end) {
		text.replace(start < end ? start : end, end, "");
	}

	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(expText.length * 3);

		// Build a regex that looks like (:-)|:-(|...), but escaping the smilies
		// properly so they will be interpreted literally by the regex matcher.
		patternString.append('(');
		for (String s : expText) {
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		// Replace the extra '|' with a ')'
		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");

		return Pattern.compile(patternString.toString());
	}

	public String getRegex() {
		StringBuilder patternString = new StringBuilder(expText.length * 3);

		// Build a regex that looks like (:-)|:-(|...), but escaping the smilies
		// properly so they will be interpreted literally by the regex matcher.
		patternString.append('(');
		for (String s : expText) {
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		// Replace the extra '|' with a ')'
		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");
		return patternString.toString();
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

	static public Bitmap ZoomToFixShape(Bitmap pic1, int w, int h) {
		Bitmap tempBitmap;
		int bitH = pic1.getHeight();
		int bitW = pic1.getWidth();
		Matrix mMatrix = new Matrix();

		float scoleW = (float) w / (float) bitW;
		float scoleH = (float) h / (float) bitH;

		mMatrix.reset();
		mMatrix.postScale(scoleW, scoleH);
		tempBitmap = Bitmap.createBitmap(pic1, 0, 0, bitW, bitH, mMatrix, true);
		// pic1.recycle();
		return tempBitmap;

	}

	public static String[] getSmileTexts() {
		return expText;
	}

	public static Bitmap[] getSmileIcons() {
		return expIcons;
	}

	public String toHtml(String text, String tag) {
		if (text == null)
			return text;

		text = text.replaceAll("(\r\n|\n)", "<br />");

		StringBuilder sb = new StringBuilder();
		Matcher matcher = pattern.matcher(text);
		int offset = 0;
		while (matcher.find()) {
			sb.append(text.subSequence(offset, matcher.start()));
			offset = matcher.end();

			sb.append("<" + tag + " src=\"" + matcher.group() + "\"" + "/>");
		}

		if (offset < text.length()) {
			sb.append(text.subSequence(offset, text.length()));
		}

		return sb.toString();
	}

	public ImageGetter expIconGetter() {
		return expIconGetter;
	}

	private final class ExpIconGetter implements ImageGetter {

		@Override
		public Drawable getDrawable(String source) {
			Bitmap icon = expToRes.get(source);
			Drawable drawable = new ImageSpan(mContext, icon,
					ImageSpan.ALIGN_BASELINE).getDrawable();
			drawable.setBounds(0, 0, IMAGE_SIZE, IMAGE_SIZE);

			return drawable;
		}
	}

}
