package com.zhisland.android.blog.common.util;

import java.lang.reflect.Field;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zhisland.lib.util.StringUtil;

public class DBUtil {
	
	public static void addColumnNotNull(UpdateBuilder ub, Object obj) {
		Class<?> classObj = obj.getClass();
		Field[] fields = classObj.getDeclaredFields();
		for (Field field : fields) {
			try {
				DatabaseField databaseField = field
						.getAnnotation(DatabaseField.class);
				if (databaseField != null) {
					String columnName = databaseField.columnName();
					field.setAccessible(true);
					Object val = field.get(obj);
					if (!StringUtil.isNullOrEmpty(columnName) && val != null) {
						ub.updateColumnValue(columnName, val);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void updateFieldNotNull(Object oldObject, Object newObject) {
		Class<?> classObj = oldObject.getClass();
		Field[] fields = classObj.getDeclaredFields();
		for (Field field : fields) {
			try {
				SerializedName serializedName = field
						.getAnnotation(SerializedName.class);
				if (serializedName != null) {
					field.setAccessible(true);
					Object val = field.get(newObject);
					if (val != null) {
						field.set(oldObject, val);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
