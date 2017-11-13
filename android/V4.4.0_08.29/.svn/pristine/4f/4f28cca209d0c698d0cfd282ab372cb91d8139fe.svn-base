package com.zhisland.android.blog.profile.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.contacts.dto.Organization;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.util.StringUtil;

/**
 * 版本4.2.3之前的user，已废弃
 * */
@DatabaseTable(tableName = UserDeprecated.TB_NAME, daoClass = UserDeprecatedDao.class)
@Deprecated
public class UserDeprecated extends OrmDto {

	private static final long serialVersionUID = 1L;

	public static final int VALUE_RELATION_LEVEL_TRUST = IMContact.RELATION_TRUST;
	public static final int VALUE_RELATION_LEVEL_NORMAL = IMContact.RELATION_NORMAL;

	public static final int VALUE_VISIBLE = 0;
	public static final int VALUE_INVISIBLE = 1;

	public static final int VALUE_SEX_MAN = 0;
	public static final int VALUE_SEX_WOMAN = 1;

	public static final int VALUE_TYPE_VIP = 400; // 岛亲
	public static final int VALUE_TYPE_DING = 100; // 岛丁
	public static final int VALUE_TYPE_INVITE = 300; // 优质用户
	public static final int VALUE_TYPE_YUZHUCE = 50; // 预注册

	public static final int VALUE_TYPE_VIP_BLUE = 1; // 蓝岛
	public static final int VALUE_TYPE_VIP_GREEN = 2; // 绿岛
	public static final int VALUE_TYPE_VIP_LIFE = 1; // 终身

	public static final int VALUE_ACTIVITY_NORMAL = 1; // 正常用户
	public static final int VALUE_ACTIVITY_DELETE = -1; // 删除用户
	public static final int VALUE_ACTIVITY_BAD = -2; // 黑名单用户
	public static final int VALUE_ACTIVITY_FREEZE = -3; // 冻结用户

	public static final int VALUE_WHITE_LIST_YES = 1; // 是白名单

	public static final String TB_NAME = "user_dto";

	public static final String COL_ID = "userId";
	public static final String COL_NAME = "userName";
	public static final String COL_AVATAR = "userAvatar";
	public static final String COL_COMPANY = "userCompany";
	public static final String COL_POSITION = "userPosition";
	public static final String COL_TYPE = "userType";
	public static final String COL_VIP_TYPE = "zhislandType";// 蓝绿标识
	public static final String COL_LIFE_VIP = "isLifelong";// 是否是终身岛邻
	public static final String COL_MOBILE = "userMobile";

	public static final String COL_PROFILE = "profile";

	public static final String COL_INTRODUCE = "introduce";
	public static final String COL_SEX = "sex";
	public static final String COL_INDUSTRY = "industry";
	public static final String COL_PROVINCE_ID = "provinceId";
	public static final String COL_CITY_ID = "cityId";

	public static final String COL_COMPANIES = "companies";
	public static final String COL_INTERESTS = "interests";
	public static final String COL_SPECIALTIES = "specialties";
	public static final String COL_WANT_FRIENDS = "wantFriends";
	public static final String COL_LAST_EVENT = "lastEvent";
	public static final String COL_EMAIL = "email";
	public static final String COL_IS_TRUST = "relationLevel";
	public static final String COL_FIGURE = "figure";
	public static final String COL_COMMON_FRIENDS = "commonFriends";
	public static final String COL_INVISIBLE = "invisible";

	public static final String COL_DISTANCE = "distance";
	public static final String COL_TIME_DIFF = "timeDiff";
	public static final String COL_VIP_AVATAR_SERVICE = "vipAvatarService";
	public static final String COL_ASSISTANTMOBILE = "assistantMobile";
	public static final String COL_COMPANY_ID = "companyId";

	public static final String COL_ORGS = "col_orgs";

	public static final String COL_BEGIN_TIME = "beginTime";
	public static final String COL_END_TIME = "endTime";
	public static final String COL_ACTIVITY = "activity";
	public static final String COL_WHITE_LIST = "whiteList";
	public static final String COL_NEXT_APPROVE_TIME = "nextApproveTime";
	public static final String COL_EXPIRE_END_TIME = "expireEndTime";
	public static final String COL_EXPIRE_NEXT_APPROVE_TIME = "expireNextApproveTime";

	public static final String COL_ACCOUNT = "account";
	public static final String COL_USER_ALIAS = "userAlias";

	public static final String COL_INVITATION_CODE_REMAINING_COUNT = "invitationCodeRemainingCount";

	@SerializedName(COL_ID)
	@DatabaseField(columnName = COL_ID, id = true)
	public long uid;

	@SerializedName(COL_NAME)
	@DatabaseField(columnName = COL_NAME)
	public String name;

	@SerializedName(COL_AVATAR)
	@DatabaseField(columnName = COL_AVATAR)
	public String userAvatar;

	@SerializedName(COL_COMPANY)
	@DatabaseField(columnName = COL_COMPANY)
	public String userCompany;

	@SerializedName(COL_POSITION)
	@DatabaseField(columnName = COL_POSITION)
	public String userPosition;

	/**
	 * 用户类型（预注册用户：50；岛丁用户: 100；标准用户: 200；优质用户: 300；岛邻用户: 400；）
	 */
	@SerializedName(COL_TYPE)
	@DatabaseField(columnName = COL_TYPE)
	public Integer userType;

	/**
	 * 蓝绿标识
	 * */
	@SerializedName(COL_VIP_TYPE)
	@DatabaseField(columnName = COL_VIP_TYPE)
	public Integer zhislandType;

	@SerializedName(COL_LIFE_VIP)
	@DatabaseField(columnName = COL_LIFE_VIP)
	public Integer isLifelong;

	@SerializedName(COL_MOBILE)
	@DatabaseField(columnName = COL_MOBILE)
	public String userMobile;

	/**
	 * 用户介绍。用于活动嘉宾的介绍。
	 * */
	@SerializedName(COL_PROFILE)
	@DatabaseField(columnName = COL_PROFILE)
	public String profile;

	/**
	 * 用户描述
	 * */
	@SerializedName(COL_INTRODUCE)
	@DatabaseField(columnName = COL_INTRODUCE)
	public String introduce;

	@SerializedName(COL_COMPANIES)
	@DatabaseField(columnName = COL_COMPANIES, dataType = DataType.SERIALIZABLE)
	public ArrayList<Company> companies;

	@SerializedName(COL_INTERESTS)
	@DatabaseField(columnName = COL_INTERESTS)
	public String interests;

	@SerializedName(COL_SPECIALTIES)
	@DatabaseField(columnName = COL_SPECIALTIES)
	public String specialties;

	@SerializedName(COL_WANT_FRIENDS)
	@DatabaseField(columnName = COL_WANT_FRIENDS)
	public String wantFriends;

	@SerializedName(COL_LAST_EVENT)
	@DatabaseField(columnName = COL_LAST_EVENT, dataType = DataType.SERIALIZABLE)
	public Event lastEvent;

	@SerializedName(COL_EMAIL)
	@DatabaseField(columnName = COL_EMAIL)
	public String email;

	@SerializedName(COL_SEX)
	@DatabaseField(columnName = COL_SEX)
	public Integer sex;

	@SerializedName(COL_INDUSTRY)
	@DatabaseField(columnName = COL_INDUSTRY, dataType = DataType.SERIALIZABLE)
	public ZHDicItem industry;

	@SerializedName(COL_PROVINCE_ID)
	@DatabaseField(columnName = COL_PROVINCE_ID)
	public Integer provinceId;

	@SerializedName(COL_CITY_ID)
	@DatabaseField(columnName = COL_CITY_ID)
	public Integer cityId;

	/**
	 * 信任好友(0-不是 1-是)
	 */
	@SerializedName(COL_IS_TRUST)
	@DatabaseField(columnName = COL_IS_TRUST)
	public Integer relationLevel;

	@SerializedName(COL_FIGURE)
	@DatabaseField(columnName = COL_FIGURE)
	public String figure;

	@SerializedName(COL_COMMON_FRIENDS)
	@DatabaseField(columnName = COL_COMMON_FRIENDS, dataType = DataType.SERIALIZABLE)
	public ArrayList<UserDeprecated> commFriends;

	@SerializedName(COL_INVISIBLE)
	@DatabaseField(columnName = COL_INVISIBLE)
	public Integer invisible;

	/**
	 * 岛亲头像服务(0-不需要 1-需要)
	 */
	@SerializedName(COL_VIP_AVATAR_SERVICE)
	@DatabaseField(columnName = COL_VIP_AVATAR_SERVICE)
	public Integer vipAvatarService;

	/**
	 * 助理电话
	 */
	@SerializedName(COL_ASSISTANTMOBILE)
	@DatabaseField(columnName = COL_ASSISTANTMOBILE)
	public String assistantMobile;

	/**
	 * 公司id
	 */
	@SerializedName(COL_COMPANY_ID)
	@DatabaseField(columnName = COL_COMPANY_ID)
	public Long companyId;

	@SerializedName(COL_DISTANCE)
	public Integer distance;

	@SerializedName(COL_TIME_DIFF)
	public Long timeDiff;

	/**
	 * 是否是好友
	 */
	@SerializedName("isFriend")
	public Integer isFriend;

	// 是否向你加过好友
	public Boolean isAddFriend;

	// 现场模式是否已约过
	public Boolean isAlreadYue;

	// 签到（0 未签到 1 已经签到）
	@SerializedName("signIn")
	public Integer signIn;

	// 现场模式签到时间
	@SerializedName("signInTime")
	public Long signTime;

	/**
	 * 同步好友时用到的版本号
	 */
	@SerializedName("version")
	public String version;

	/**
	 * 是否是被推荐User
	 */
	@SerializedName("isRecommend")
	public Boolean isRecommend;

	/**
	 * 组织机构列表
	 */
	@DatabaseField(columnName = COL_ORGS, dataType = DataType.SERIALIZABLE)
	@SerializedName("badges")
	public ArrayList<Organization> orgs;

	/**
	 * 成为会员时间
	 */
	@SerializedName(COL_BEGIN_TIME)
	@DatabaseField(columnName = COL_BEGIN_TIME)
	public Long beginTime;

	/**
	 * 会员到期时间（终身会员 2999-01-01）
	 */
	@SerializedName(COL_END_TIME)
	@DatabaseField(columnName = COL_END_TIME)
	public Long endTime;

	/**
	 * 用户状态( 1:正常用户; -1:删除用户; -2:黑名单用户;-3：冻结)
	 */
	@SerializedName(COL_ACTIVITY)
	@DatabaseField(columnName = COL_ACTIVITY)
	public Integer activity;

	/**
	 * 白名单(1：是；其它非)
	 */
	@SerializedName(COL_WHITE_LIST)
	@DatabaseField(columnName = COL_WHITE_LIST)
	public Integer whiteList;

	/**
	 * 下次审核时间
	 */
	@SerializedName(COL_NEXT_APPROVE_TIME)
	@DatabaseField(columnName = COL_NEXT_APPROVE_TIME)
	public Long nextApproveTime;

	/**
	 * 会员结束时间（加上系统设置间隔时间）
	 */
	@SerializedName(COL_EXPIRE_END_TIME)
	@DatabaseField(columnName = COL_EXPIRE_END_TIME)
	public Long expireEndTime;

	/**
	 * 下次审核时间（加上系统设置间隔时间）
	 */
	@SerializedName(COL_EXPIRE_NEXT_APPROVE_TIME)
	@DatabaseField(columnName = COL_EXPIRE_NEXT_APPROVE_TIME)
	public Long expireNextApproveTime;

	/**
	 * 账户（注册手机号）
	 */
	@SerializedName(COL_ACCOUNT)
	@DatabaseField(columnName = COL_ACCOUNT)
	public String account;

	/**
	 * 用户别名
	 */
	@SerializedName(COL_USER_ALIAS)
	@DatabaseField(columnName = COL_USER_ALIAS)
	public String userAlias;

	/**
	 * 邀请可用数量
	 */
	@SerializedName(COL_INVITATION_CODE_REMAINING_COUNT)
	@DatabaseField(columnName = COL_INVITATION_CODE_REMAINING_COUNT)
	public Integer invitationCodeRemainingCount;

	/**
	 * 是否有可用的邀请
	 * */
	public boolean hasAvailableInvitationCode() {
		if (invitationCodeRemainingCount != null
				&& invitationCodeRemainingCount > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取兴趣爱好标签
	 */
	public List<String> getInterestTags() {
		List<String> tags = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(interests)) {
			String[] split = interests.split(",");
			Collections.addAll(tags, split);
		}
		return tags;
	}

	/**
	 * 获取擅长领域标签
	 */
	public List<String> getSpecialtiesTags() {
		List<String> tags = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(specialties)) {
			String[] split = specialties.split(",");
			Collections.addAll(tags, split);
		}
		return tags;
	}

	/**
	 * 获取愿意结识标签
	 */
	public List<String> getWantFriendsTags() {
		List<String> tags = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(wantFriends)) {
			String[] split = wantFriends.split(",");
			Collections.addAll(tags, split);
		}
		return tags;
	}

	/**
	 * 是否是信任好友
	 */
	public boolean isTrustFriend() {
		if (relationLevel != null
				&& relationLevel == VALUE_RELATION_LEVEL_TRUST) {
			return true;
		}
		return false;
	}

	/**
	 * 获取vip icon ID
	 */
	public int getVipIconId() {
		if (userType != null && userType == VALUE_TYPE_VIP) {
			if (isLifelong != null && isLifelong == VALUE_TYPE_VIP_LIFE) {
				return R.drawable.icon_goldlion;
			} else {
				if (zhislandType != null
						&& zhislandType == VALUE_TYPE_VIP_GREEN) {
					return R.drawable.icon_greenlion;
				} else if (zhislandType != null
						&& zhislandType == VALUE_TYPE_VIP_BLUE) {
					return R.drawable.icon_eagle;
				}
			}
		} else if (userType != null && userType == VALUE_TYPE_DING) {
			return R.drawable.icon_bee;
		} else if (userType != null && userType == VALUE_TYPE_INVITE) {
			return R.drawable.icon_dolphin;
		}
		return -1;
	}

	/**
	 * 获取用户级别描述
	 * */
	public String getUserTypeDesc() {
		if (userType != null && userType == VALUE_TYPE_VIP) {
			if (isLifelong != null && isLifelong == VALUE_TYPE_VIP_LIFE) {
				return isExpired() ? "服务已到期" : "终身岛邻";
			} else {
				if (zhislandType != null
						&& zhislandType == VALUE_TYPE_VIP_GREEN) {
					return isExpired() ? "服务已到期" : "绿色岛邻";
				} else if (zhislandType != null
						&& zhislandType == VALUE_TYPE_VIP_BLUE) {
					return "蓝色岛邻";
				}
			}
		} else if (userType != null && userType == VALUE_TYPE_DING) {
			return "岛丁";
		} else if (userType != null && userType == VALUE_TYPE_INVITE) {
			return "海客";
		}
		return "";
	}

	/**
	 * 是否为岛亲
	 * */
	public boolean isVip() {
		if (userType != null && userType == VALUE_TYPE_VIP) {
			// 蓝岛不存在过期状态，永远是vip
			if (zhislandType != null && zhislandType == VALUE_TYPE_VIP_BLUE) {
				return true;
			}

			if (!isExpired()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 服务是否到期
	 * */
	public boolean isExpired() {
		long timeNow = System.currentTimeMillis() / 1000l;
		if (expireNextApproveTime != null && timeNow < expireNextApproveTime) {
			return false;
		}
		return true;
	}

	/**
	 * 成为过岛亲（包括现在是岛亲）
	 * */
	public boolean isVipBefore() {
		if (userType != null && userType == VALUE_TYPE_VIP) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为海客。只有优质用户和过期岛亲为海客。
	 * */
	public boolean isHaiKe() {
		if (userType != null) {
			if (userType == VALUE_TYPE_INVITE) {
				return true;
			} else if (userType == VALUE_TYPE_VIP && !isVip()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 用户是否拥有邀请权利
	 * */
	public boolean canInvite() {
		if (userType != null) {
			if (isVip()) {
				return true;
			} else if (userType == VALUE_TYPE_DING) {
				if (whiteList != null && whiteList == VALUE_WHITE_LIST_YES) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 用户是否拥有发活动的权限
	 * */
	public boolean canInitiatedEvent() {
		if (userType != null) {
			if (isVip() || userType == VALUE_TYPE_DING) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否有加对方好友的权限
	 * */
	public boolean canSwitchCard(UserDeprecated user) {
		if (isVip()) {
			return true;
		} else {
			if (!user.isVip()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否可以报名活动
	 * 
	 * @param event
	 *            要报名的活动
	 * */
	public boolean canSignUpEvent(Event event) {
		if (event == null) {
			return false;
		}
		if (isVip() || userType == VALUE_TYPE_DING) {// 身份为岛亲和岛丁，对任何活动无限制
			return true;
		} else if (event.userLimitLevel != null
				&& event.userLimitLevel == Event.LIMIT_LEVEL_ALL) {// 身份非岛亲和岛丁，活动限制为所有人
			return true;
		} else if (isHaiKe() && event.userLimitLevel != null
				&& event.userLimitLevel == Event.LIMIT_LEVEL_VIP_AND_INVITE) {//身份为海客，活动限制为海客及岛亲
			return true;
		}
		return false;
	}

	/**
	 * 保存当前User关键字段到PrefUtil
	 */
	public static void saveSelfUserToPrefUtil(UserDeprecated user) {
		PrefUtil.Instance().setUserID(user.uid);
		PrefUtil.Instance().setUserName(user.name);
		PrefUtil.Instance().setUserAvatar(user.userAvatar);
		PrefUtil.Instance().setUserType(user.userType);
		PrefUtil.Instance().setUserCompany(user.userCompany);
		PrefUtil.Instance().setUserPosition(user.userPosition);
		PrefUtil.Instance().setUserMobile(user.userMobile);
	}

	/**
	 * 将PrefUtil 中存储的user字段拼接成User对象
	 */
	public static UserDeprecated combinationUser() {
		UserDeprecated user = new UserDeprecated();
		user.uid = PrefUtil.Instance().getUserId();
		user.name = PrefUtil.Instance().getUserName();
		user.userAvatar = PrefUtil.Instance().getUserAvatar();
		int userType = PrefUtil.Instance().getUserType();
		if (userType < 0) {
			user.userType = userType;
		}
		user.userCompany = PrefUtil.Instance().getUserCompany();
		user.userPosition = PrefUtil.Instance().getUserPosition();
		user.userPosition = PrefUtil.Instance().getUserMobile();
		return user;
	}
}
