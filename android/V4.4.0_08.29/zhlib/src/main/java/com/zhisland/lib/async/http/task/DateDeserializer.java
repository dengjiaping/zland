package com.zhisland.lib.async.http.task;

import java.text.ParseException;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;

public class DateDeserializer implements JsonDeserializer<Date> {

	private static final String TAG = "DateDeserializer";

	@Override
	public Date deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		String s = json.getAsJsonPrimitive().getAsString();

		if (StringUtil.isNullOrEmpty(s)) {
			MLog.i(TAG, "date string is null ");
			return null;
		}
		Date date = null;
		try {
			MLog.i(TAG, "date: " + s);
			date = GsonHelper.DATE_FORMAT.parse(s);

		} catch (ParseException e) {
			MLog.e(TAG, e.getLocalizedMessage(), e);
		}
		return date;
	}
}