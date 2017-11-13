package com.zhisland.android.blog.profile.dto;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.aa.dto.CustomShare;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonExclusionStrategy;
import com.zhisland.lib.async.http.task.GsonHelper;

public class UserDetail extends OrmDto {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户基本信息
	 * */
	@SerializedName("user")
	public User user;

	/**
	 * 他们这样评价我 当前任职 我的荣誉 我的供给 我的需求 我的点滴 岛丁 Assistant 助理 Assistant 联系方式 Assistant
	 */
	@SerializedName("blocks")
	public List<SimpleBlock> blocks;

	/**
	 * 微信分享链接
	 * */
	@SerializedName("share")
	public CustomShare share;

	/**
	 * 获取供给块
	 * */
	public SimpleBlock<Resource> getSupplyBlock() {
		if (blocks != null) {
			for (SimpleBlock block : blocks) {
				if (block != null && block.type == SimpleBlock.TYPE_SUPPLY) {
					return (SimpleBlock<Resource>) block;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取需求块
	 * */
	public SimpleBlock<Resource> getDemandBlock() {
		if (blocks != null) {
			for (SimpleBlock block : blocks) {
				if (block != null && block.type == SimpleBlock.TYPE_DEMAND) {
					return (SimpleBlock<Resource>) block;
				}
			}
		}
		return null;
	}
	

	/*---------------------------- UserDetail 的GsonHelper start-------------------------------*/
	private static Gson UserDetailGson;
	private static final Object LockObj = new Object();

	/**
	 * 获取User的json解析器。
	 * */
	public static Gson getUserGson() {
		if (UserDetailGson == null) {
			synchronized (LockObj) {
				if (UserDetailGson == null) {
					GsonBuilder gsonBuilder = new GsonBuilder()
							.setExclusionStrategies(new GsonExclusionStrategy())
							.serializeSpecialFloatingPointValues();
					gsonBuilder.registerTypeAdapter(SimpleBlock.class,
							new UserDeserializer());
					gsonBuilder.registerTypeAdapter(SimpleBlock.class,
							new UserSerializer());
					UserDetailGson = gsonBuilder.create();
				}
			}
		}
		return UserDetailGson;
	}

	/**
	 * user 反序列化时，处理List<SimpleBlock> blocks变量，根据SimpleBlock的type决定反序列化成什么对象。
	 * */
	public static class UserSerializer implements JsonDeserializer<SimpleBlock> {

		private static final String TAG = "DateDeserializer";

		@Override
		public SimpleBlock deserialize(JsonElement json,
				java.lang.reflect.Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			int type = json.getAsJsonObject().get("type").getAsInt();
			if (type == SimpleBlock.TYPE_HONOR) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<Honor>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_DEMAND
					|| type == SimpleBlock.TYPE_SUPPLY) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<Resource>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_ASSISTANT
					|| type == SimpleBlock.TYPE_DING) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<UserAssistant>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_CONTACT) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<UserContactInfo>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_DRIP) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<CustomDict>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_POSITION) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<Company>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_USER_COMMENT) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<UserComment>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_USER_INTRODUCTION) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<String>>() {
						}.getType());
			} else if (type == SimpleBlock.TYPE_COMMON_FRIENDS) {
				return GsonHelper.GetCommonGson().fromJson(json,
						new TypeToken<SimpleBlock<User>>() {
						}.getType());
			}
			return null;
		}
	}

	/**
	 * user 序列化时，处理List<SimpleBlock> blocks变量，如果是SimpleBlock的实例，按SimpleBlock序列化。
	 * */
	public static class UserDeserializer implements JsonSerializer<SimpleBlock> {

		@Override
		public JsonElement serialize(SimpleBlock arg0, Type arg1,
				JsonSerializationContext arg2) {
			if (arg0 instanceof SimpleBlock) {
				return GsonHelper.GetCommonGson().toJsonTree(arg0,
						SimpleBlock.class);
			} else {
				return GsonHelper.GetCommonGson().toJsonTree(arg0,
						SimpleBlock.class);
			}
		}

	}

	/*---------------------------- UserDetail 的GsonHelper end-------------------------------*/
}
