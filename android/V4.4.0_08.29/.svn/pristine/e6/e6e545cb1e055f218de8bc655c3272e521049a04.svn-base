package com.zhisland.lib.async.http.task;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializer implements JsonSerializer<Date> {

	@Override
	public JsonElement serialize(Date arg0, Type arg1,
			JsonSerializationContext arg2) {
		String formatedDateString = GsonHelper.DATE_FORMAT.format(arg0);
		return new JsonPrimitive(formatedDateString);
	}

}
