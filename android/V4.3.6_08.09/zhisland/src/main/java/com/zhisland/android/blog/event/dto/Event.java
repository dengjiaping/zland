package com.zhisland.android.blog.event.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.event.controller.EventListAdapter;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.util.StringUtil;

/**
 * 活动
 */
public class Event extends OrmDto {

    /**
     * 活动类型
     */
    public static final int CATEGORY_ALL = -1;
    public static final int CATEGORY_TYPE_UGC = 0; // UGC活动
    public static final int CATEGORY_TYPE_OFFICIAL = 1; // 官方活动
    public static final int CATEGORY_TYPE_SCENE = 2; // 现场模式活动
    public static final int CATEGORY_TYPE_TAG = 100;

    /**
     * 活动状态
     */
    public static final int STATUS_SIGNING = 1; // 报名中
    public static final int STATUS_CANCEL = 2; // 活动已取消
    public static final int STATUS_PROGRESSING = 3; // 进行中
    public static final int STATUS_END = 4; // 活动已结束
    public static final int STATUS_SIGN_OVER = 5; // 报名截止

    /**
     * 报名状态
     */
    public static final int SIGN_STATUS_YES = 1; // 已报名
    public static final int SIGN_STATUS_NO = 0; // 未报名
    public static final int SIGN_STATUS_CANCEL = 2; // 取消报名

    /*
     * 审核状态
     */
    public static final int STATUS_AUDIT_WAIT = 0; // 待审核
    public static final int STATUS_AUDIT_ACCEPT = 1; // 已审核
    public static final int STATUS_AUDIT_REFUSE = 2; // 已拒绝

    /*
     * 用户限制等级（1、全部用户 2、岛邻用户及海客 3、岛邻用户）
     */
    public static final int LIMIT_LEVEL_ALL = 1; // 全部用户
    public static final int LIMIT_LEVEL_VIP_AND_INVITE = 2; // 岛邻用户及海客
    public static final int LIMIT_LEVEL_VIP = 3; // 岛邻用户

    /*
     * 显示等级（1、分享传播 0或空、显示在列表中）
     */
    public static final int DISPLAY_LEVEL_PRIVATE = 1; // 分享传播
    public static final int DISPLAY_LEVEL_PUBLIC = 0; // 显示在列表中

    public static final int IS_WHITE_YES = 1; // 是白名单

    public static final int INVOICE_OPTION_NEED = 1; // 是白名单

    private static final long serialVersionUID = 1L;

    public static final String TB_NAME = "event_dto";

    public static final String ITF_ID = "id";
    public static final String ITF_EVENT_TITLE = "title";
    public static final String ITF_START_TIME = "startTime";
    public static final String ITF_END_TIME = "endTime";
    public static final String ITF_TIME_ZONE = "timeZone";
    public static final String ITF_PROVINCE_ID = "provinceId";
    public static final String ITF_CITY_ID = "cityId";
    public static final String ITF_PROVINCE_NAME = "provinceName";
    public static final String ITF_CITY_NAME = "cityName";
    public static final String ITF_LOCATION = "location";
    public static final String ITF_EVENT_STATUS = "eventStatus";
    public static final String ITF_SIGN_STATUS = "signStatus";
    public static final String ITF_ATTEND_FRIENDS = "attendFriends";
    public static final String ITF_IMGURL = "imgUrl";
    public static final String ITF_SIGNED_COUNT = "signedCount";
    public static final String ITF_TYPE = "type";
    public static final String ITF_AUDITED_COUNT = "auditedCount";
    public static final String ITF_AUDITING_COUNT = "auditingCount";

    public static final String ITF_IMG_URLS = "imgUrls";
    public static final String ITF_PUBLIC_USER = "publisherUser";
    public static final String ITF_CONTENT = "content";
    public static final String ITF_HONOR_GUEST = "honorGuests";
    public static final String ITF_SIGNED_USERS = "signedUsers";
    public static final String ITF_LATITUDE = "latitude";
    public static final String ITF_LONGITUDE = "longitude";
    public static final String ITF_CONTACT_MOBILE = "contactMobile";
    public static final String ITF_SHARE_URL = "shareUrl";
    public static final String ITF_PRICE = "price";
    public static final String ITF_PRICE_VIP = "vipPrice";
    public static final String ITF_PRICE_VIP_RECOMMONED = "vipRecommonedPrice";
    public static final String ITF_AUDIT_STATUS = "auditStatus";
    public static final String ITF_CATEGORY = "category";

    public static final String ITF_SIGN_LIMIT = "signLimit";
    public static final String ITF_QUELITY_PRICE = "qualityPrice";
    public static final String ITF_LIMIT_LEVEL = "userLimitLevel";
    public static final String ITF_DISPLAY_LEVEL = "displayLevel";
    public static final String ITF_TOTAL_NUM = "totalNum";
    public static final String ITF_USER_SIGN_TIME = "userSignTime";
    public static final String ITF_CURRENT_STATE = "currentState";
    public static final String ITF_PAY_DATA = "payData";
    public static final String ITF_PRICE_TEXT = "priceText";
    public static final String ITF_CURRENT_PRICE = "currentPrice";
    public static final String ITF_PERIOD = "period";
    public static final String ITF_BRAND_NAME = "brandName";
    public static final String ITF_PUBLISHER_NAME = "brandPublisherName";

    /*现场模式字段 start*/
    public static final String ITF_SCENE_MIN_IMAGE = "sceneMinImage";
    public static final String ITF_SCENE_MAX_IMAGE = "sceneMaxImage";
    public static final String ITF_AUDIT_DEAD_LINE_TIME = "auditDeadlineTime";
    public static final String ITF_SIGN_IN = "signIn";
    public static final String ITF_SPOT_COVER_URL = "spotCoverUrl";
    /*现场模式字段 end*/


    /**4.3.5新加 start
     *
     */

    /**
     * 活动详情页面，喜欢活动的状态
     */
    public static final String ITF_LIKE_STATUS = "likeStatus";

    /**
     * 原价
     */
    public static final String ITF_COSTPRICE_TEXT = "costPriceText";
    /**
     * 海客价
     */
    public static final String ITF_QUALITYPRICE_TEXT = "qualityPriceText";
    /**
     * 岛亲价
     */
    public static final String ITF_VIPPRICETEXT = "vipPriceText";

    /**4.3.5新加 end
     *
     */
    @SerializedName(ITF_COSTPRICE_TEXT)
    public String costPriceText;

    @SerializedName(ITF_QUALITYPRICE_TEXT)
    public String qualityPriceText;

    @SerializedName(ITF_VIPPRICETEXT)
    public String vipPriceText;



    /**
     * 活动id
     */
    @SerializedName(ITF_ID)
    public long eventId;

    /**
     * 活动标题
     */
    @SerializedName(ITF_EVENT_TITLE)
    public String eventTitle;

    /**
     * 活动类型（标签）
     */
    @SerializedName(ITF_CATEGORY)
    public String category;

    /**
     * 活动开始时间
     */
    @SerializedName(ITF_START_TIME)
    public long startTime;

    /**
     * 活动结束时间
     */
    @SerializedName(ITF_END_TIME)
    public long endTime;

    /**
     * 活动时间显示文本
     */
    @SerializedName(ITF_PERIOD)
    public String period;

    /**
     * 活动时间的时区
     */
    @SerializedName(ITF_TIME_ZONE)
    public int timeZone;

    /**
     * 活动地点所在省ID
     */
    @SerializedName(ITF_PROVINCE_ID)
    public int provinceId;

    /**
     * 活动地点所在市ID
     */
    @SerializedName(ITF_CITY_ID)
    public int cityId;

    /**
     * 活动地点所在省
     */
    @SerializedName(ITF_PROVINCE_NAME)
    public String provinceName;

    /**
     * 活动地点所在市
     */
    @SerializedName(ITF_CITY_NAME)
    public String cityName;

    /**
     * 活动地点
     */
    @SerializedName(ITF_LOCATION)
    public String location;

    /**
     * 活动状态:1-报名中 2-取消 3-进行中 4-结束 5-报名已满
     */
    @SerializedName(ITF_EVENT_STATUS)
    public int eventStatus;

    /**
     * 报名限制状态（1、报名已满）
     */
    @SerializedName(ITF_SIGN_LIMIT)
    public int signLimit;

    /**
     * 报名状态:1-已报名 0-未报名 2-取消报名
     */
    @SerializedName(ITF_SIGN_STATUS)
    public int signStatus;

    /**
     * 报名审核状态:0-待审核 1-已审核 3-已拒绝
     */
    @SerializedName(ITF_AUDIT_STATUS)
    public int auditStatus;

    /**
     * 出席好友
     */
    @SerializedName(ITF_ATTEND_FRIENDS)
    public ArrayList<String> attendFriends;

    /**
     * 活动图片
     */
    @SerializedName(ITF_IMGURL)
    public String imgUrl;

    /**
     * 已报名人数
     */
    @SerializedName(ITF_SIGNED_COUNT)
    public int signedCount;

    /**
     * 审核通过的人数
     */
    @SerializedName(ITF_AUDITED_COUNT)
    public int auditedCount;

    /**
     * 待审核的人数
     */
    @SerializedName(ITF_AUDITING_COUNT)
    public int auditingCount;

    /**
     * 活动类型ID
     */
    @SerializedName(ITF_TYPE)
    public int type;

    /**
     * 活动图片数组
     */
    @SerializedName(ITF_IMG_URLS)
    public ArrayList<String> imgUrls;

    /**
     * 活动发布人
     */
    @SerializedName(ITF_PUBLIC_USER)
    public User publicUser;

    /**
     * 活动详情
     */
    @SerializedName(ITF_CONTENT)
    public String content;

    /**
     * 活动嘉宾
     */
    @SerializedName(ITF_HONOR_GUEST)
    public String honorGuest;

    public ArrayList<User> honorGuestList;

    /**
     * 报名用户列表
     */
    @SerializedName(ITF_SIGNED_USERS)
    public ArrayList<User> signedUsers;

    /**
     * 活动地址纬度
     */
    @SerializedName(ITF_LATITUDE)
    public long latitude;

    /**
     * 活动地址经度
     */
    @SerializedName(ITF_LONGITUDE)
    public long longitude;

    /**
     * 活动联系电话
     */
    @SerializedName(ITF_CONTACT_MOBILE)
    public String contactMobile;

    /**
     * 活动分享url
     */
    @SerializedName(ITF_SHARE_URL)
    public String shareUrl;

    /**
     * 活动原价
     */
    @SerializedName(ITF_PRICE)
    public Float price;

    /**
     * 海客价
     */
    @SerializedName(ITF_QUELITY_PRICE)
    public Float qualityPrice;

    /**
     * 活动岛亲价
     */
    @SerializedName(ITF_PRICE_VIP)
    public Float vipPrice;

    /**
     * 活动会员邀请价
     */
    @SerializedName(ITF_PRICE_VIP_RECOMMONED)
    public Float vipRecommonedPrice;

    /**
     * 用户限制等级（1、全部用户 2、岛邻用户及海客 3、岛邻用户）
     */
    @SerializedName(ITF_LIMIT_LEVEL)
    public Integer userLimitLevel;

    /**
     * 显示等级（1、分享传播 0或空、显示在列表中）
     */
    @SerializedName(ITF_DISPLAY_LEVEL)
    public Integer displayLevel;

    /**
     * 用户报名时间
     */
    @SerializedName(ITF_USER_SIGN_TIME)
    public Long userSignTime;

    /**
     * 活动规模（人数限制）
     */
    @SerializedName(ITF_TOTAL_NUM)
    public Integer totalNum;

    /**
     * 当前状态
     */
    @SerializedName(ITF_CURRENT_STATE)
    public CurrentState currentState;

    /**
     * 喜欢状态
     */
    @SerializedName(ITF_LIKE_STATUS)
    public LikeStatus likeStatus;

    /**
     * 支付相关数据
     */
    @SerializedName(ITF_PAY_DATA)
    public PayData payData;

    /**
     * 用户在该活动应该支付的金额和身份显示,如 “岛亲价 ￥1800”。
     */
    @SerializedName(ITF_PRICE_TEXT)
    public String priceText;

    /**
     * 用户在该活动应该支付的金额
     */
    @SerializedName(ITF_CURRENT_PRICE)
    public Double currentPrice;

    //活动品牌认证字段，“官方活动”、“个人活动”、“约局”
    @SerializedName(ITF_BRAND_NAME)
    public String brandName;

    //活动发布方
    @SerializedName(ITF_PUBLISHER_NAME)
    public String brandPublisherName;

    // ------------------现场模式字段 start-------------------
    /**
     * 小图
     */
    @SerializedName(ITF_SCENE_MIN_IMAGE)
    public String sceneMinImage;

    /**
     * 大图
     */
    @SerializedName(ITF_SCENE_MAX_IMAGE)
    public String sceneMaxImage;

    /**
     * 审核截止时间
     */
    @SerializedName(ITF_AUDIT_DEAD_LINE_TIME)
    public Long auditDeadlineTime;

    /**
     * 是否已经签到 0：未签到 1：已签到
     */
    @SerializedName(ITF_SIGN_IN)
    public int signIn;

    // 活动状态
    @SerializedName("statusName")
    public String statusName;

    // 出席好友
    @SerializedName("friend")
    public String friend;

    // 是否享受优惠（折扣）0 不享受 1享受
    @SerializedName("discount")
    public Integer discount;

    /**
     * h5
     */
    @SerializedName(ITF_SPOT_COVER_URL)
    public String spotCoverUrl;


    // 本地的type,用于显示列表不同的样式
    public int localType = EventListAdapter.TYPE_EVENT;


    // ----------------现场模式字段 end---------------------

    // 服务器返回的数据 有的imgUrl有字段，有的在imgUrls
    public String getEventImgUrl() {
        String url = imgUrl;
        if (StringUtil.isNullOrEmpty(url) && imgUrls != null
                && imgUrls.size() > 0) {
            url = imgUrls.get(0);
        }
        return url;
    }

    /**
     * 是否可审核
     */
    public boolean canAudit() {
        if (eventStatus == STATUS_SIGNING && auditDeadlineTime != null
                && auditDeadlineTime * 1000l > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 距离上次报名时间是否超过24小时。
     */
    public boolean canSignUp() {
        if (userSignTime == null) {
            return true;
        }
        if (System.currentTimeMillis() > ((userSignTime + 24 * 60 * 60) * 1000l)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是优惠活动
     */
    public boolean isDiscountEvent() {
        if (discount != null && discount == 1) {
            return true;
        } else {
            return false;
        }
    }
}
