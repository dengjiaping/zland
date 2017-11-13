package com.zhisland.android.blog.common.app;

import android.content.SharedPreferences.Editor;

import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.contacts.api.TaskGetFriendListByUid;
import com.zhisland.android.blog.contacts.push.InviteSuccessHandler;

import java.util.Map;

/**
 * a class to help manage the preference's
 */
public class PrefUtil {

    // ================当前登录user的用户信息
    private static final String PREF_UID = "pref_uid";
    private static final String PREF_UNAME = "pref_uname";
    private static final String PREF_AVATAR = "pref_avatar";
    private static final String PREF_COMPANY = "pref_company";
    private static final String PREF_POSITION = "pref_position";
    private static final String PREF_USER_TYPE = "pref_user_type";
    // 用户显示的手机号
    private static final String PREF_USER_MOBILE = "pref_user_mobile";
    private static final String PREF_TOKEN = "pref_token";
    // 当前用户的json串
    private static final String PREF_USER_JSON_STRING = "pref_user_json_string";
    // ================当前登录user的用户信息

    private static final String PREF_HAS_LOGIN = "pref_has_login";
    private static final String PREF_INPUT_HEIGHT = "pref_input_height";
    // update
    private static final String PREF_UPDATE = "pref_update"
            + AppUtil.Instance().getVersionName();

    private static final String PREF_LAST_LOC = "pref_last_loc";
    // 上一次获取推荐人的日期
    private static final String PREF_LAST_RECOMMEND_DATE = "pref_recommend_date";
    // 弹邀请拦截
    private static final String PREF_SHOULD_POP_INVITE_VIEW = "pref_is_pop_invite_view";

    private static final String PREF_TABHOME_NEED_POP_UPDATE_DLG = "pref_tabhome_need_pop_update_dlg";

    private static final String PREF_NEED_REFRESH_USER = "pref_need_refresh_user";
    // 最大评论数
    private static final String PREF_COMMENT_MAX_TOP_COUNT = "pref_comment_max_top_count";
    // 是否拉取过我的点滴接口
    private static final String PREF_IS_GET_DRIP_TASK = "pref_is_get_drip_task";
    // 国家码 name
    private static final String PREF_COUNTRY_NAME = "pref_country_name";
    // 国家码 code
    private static final String PREF_COUNTRY_CODE = "pref_country_code";
    // 国家码 show code
    private static final String PREF_COUNTRY_SHOW_CODE = "pref_country_show_code";
    // show 注册引导
    private static final String PREF_SHOW_GUIDE_REGISTER = "pref_guide_register";
    // show 基本信息引导
    private static final String PREF_SHOW_GUIDE_BASIC_INFO = "pref_guide_basic_info";
    // 注册时杀死逻辑
    private static final String PREF_REGISTER_KILL_PAGE = "pref_register_kill_page";
    // 上传vcard时间
    private static final String PREF_VCARD_UPLOAD_TIME = "pref_vcard_upload_time" + PrefUtil.Instance().getUserId();

    private static final String PREF_CONTACT_FRIEND_COUNT = "pref_contact_friend_count";
    /**
     * 新手任务形象照引导
     */
    private static final String PREF_GUIDE_FIGURE = "pref_guide_figure";
    /**
     * 神评精选弹出时间
     */
    private static final String PREF_PRICE_COMMENT_TIME = "pref_price_comment_time";
    /**
     * 新手任务，是否完成加好友操作
     */
    private static final String PREF_IS_ADD_FRIEND = "pref_is_add_friend";
    /**
     * 新手任务，是否完成召唤好友操作
     */
    private static final String PREF_IS_CALL_FRIEND = "pref_is_call_friend";
    /**
     * 新手任务，显示访问通讯录弹框
     */
    private static final String PREF_SHOW_FRESH_TASK_CONTACT = "pref_show_fresh_task_contact";
    /**
     * 是否显示 title fresh task
     */
    private static final String PREF_IS_SHOW_TITLE_FRESH_TASK = "pref_is_show_title_fresh_task";
    /**
     * 是否显示 title red dot
     */
    private static final String PREF_IS_SHOW_TITLE_FRESH_TASK_RED_DOT = "pref_is_show_title_fresh_task_red_dot";
    /**
     * 是否显示过 新手任务mask
     */
    private static final String PREF_IS_SHOW_FRESH_TASK_MASK = "pref_is_show_fresh_task_mask";

    /**
     * app从前台切到后台的时间。
     */
    private static final String PREF_TO_BACKGROUND_TIME = "pref_to_background_time";

    private static final String PREF_INFO_GUIDE_SHOW_OVER = "pref_info_guide_show_over";

    private static final String PREF_HAS_VISITED_FragHaikeConfirm = "pref_has_visited_FragHaikeConfirm";

    /**
     * 活动详情第一次显示喜欢guid。
     */
    private static final String PREF_LIKEDIALOG_FIRSTSHOW = "pref_likedialog_firstshow";

    // 新增粉丝数
    private static final String PREF_FANS_COUNT = "pref_fans_count";
    // 互动消息
    private static final String PREF_INTERACTIVE_COUNT = "pref_interactive_count";
    // 系统消息
    private static final String PREF_SYSTEM_COUNT = "pref_system_count";


    /**
     * set key and valueint
     */
    public <T> void setKeyValue(String key, T i) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        if (i instanceof Integer) {
            editor.putInt(key, (Integer) i);
        } else if (i instanceof String) {
            editor.putString(key, (String) i);
        } else if (i instanceof Long) {
            editor.putLong(key, (Long) i);
        } else if (i instanceof Boolean) {
            editor.putBoolean(key, (Boolean) i);
        }
        editor.commit();
    }

    /**
     * 获取value
     *
     * @param key
     * @param defaultValue 当不存在时返回默认值
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getByKey(String key, T defaultValue) {
        Map<String, ?> maps = ZhislandApplication.APP_PREFERENCE.getAll();
        if (!maps.containsKey(key))
            return defaultValue;

        Object value = maps.get(key);
        return (T) value;
    }

    public boolean needRefreshSelf() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_NEED_REFRESH_USER, false);
    }

    public void setNeedRefrshSelf(boolean need) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_NEED_REFRESH_USER, need);
        editor.commit();

    }

    public long getUserId() {
        return ZhislandApplication.APP_PREFERENCE.getLong(PREF_UID, 0);
    }

    public void setUserID(long i) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_UID, i);
        editor.commit();
    }

    public String getUserName() {
        return ZhislandApplication.APP_PREFERENCE.getString(PREF_UNAME, "");
    }

    public void setUserName(String userName) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_UNAME, userName);
        editor.commit();
    }

    public String getUserJsonString() {
        return ZhislandApplication.APP_PREFERENCE.getString(
                PREF_USER_JSON_STRING, "");
    }

    public void setUserJsonString(String jsonString) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_USER_JSON_STRING, jsonString);
        editor.commit();
    }

    public Integer getUserType() {
        return ZhislandApplication.APP_PREFERENCE.getInt(PREF_USER_TYPE, -1);
    }

    public void setUserType(Integer userType) {
        if (userType == null) {
            userType = -1;
        }
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putInt(PREF_USER_TYPE, userType);
        editor.commit();
    }

    public int getCommentMaxTop() {
        return ZhislandApplication.APP_PREFERENCE.getInt(
                PREF_COMMENT_MAX_TOP_COUNT, 2);
    }

    public void setCommentMaxTop(Integer maxCount) {
        if (maxCount == null) {
            return;
        }
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putInt(PREF_COMMENT_MAX_TOP_COUNT, maxCount);
        editor.commit();
    }

    public boolean isGetDripTask() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_IS_GET_DRIP_TASK, false);
    }

    public void setIsGetDripTask(boolean isGetTask) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_IS_GET_DRIP_TASK, isGetTask);
        editor.commit();
    }

    public String getUserAvatar() {
        return ZhislandApplication.APP_PREFERENCE.getString(PREF_AVATAR, "");
    }

    public void setUserAvatar(String userAvatar) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_AVATAR, userAvatar);
        editor.commit();
    }

    public String getUserCompany() {
        return ZhislandApplication.APP_PREFERENCE.getString(PREF_COMPANY, "");
    }

    public void setUserCompany(String userCompany) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_COMPANY, userCompany);
        editor.commit();
    }

    public String getUserMobile() {
        return ZhislandApplication.APP_PREFERENCE.getString(PREF_USER_MOBILE, "");
    }

    public void setUserMobile(String userMobile) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_USER_MOBILE, userMobile);
        editor.commit();
    }

    public String getUserPosition() {
        return ZhislandApplication.APP_PREFERENCE.getString(PREF_POSITION, "");
    }

    public void setUserPosition(String userPosition) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_POSITION, userPosition);
        editor.commit();
    }

    public boolean hasLogin() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(PREF_HAS_LOGIN,
                false);
    }

    public void setIsCanLogin(boolean isLogin) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_HAS_LOGIN, isLogin);
        editor.commit();

    }

    public void setToken(String token) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return ZhislandApplication.APP_PREFERENCE.getString(PREF_TOKEN, null);
    }

    public void setLastRecommendDate(long time) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_LAST_RECOMMEND_DATE, time);
        editor.commit();
    }

    public long getLastRecommendDate() {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_LAST_RECOMMEND_DATE, 0);
    }

    public void setLatestVersion(boolean isLast) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_UPDATE, isLast);
        editor.commit();
    }

    public boolean isLatestVersion() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(PREF_UPDATE, true);
    }

    public void setInputHeight(int diff) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putInt(PREF_INPUT_HEIGHT, diff);
        editor.commit();
    }

    public int getInputHeight() {
        return ZhislandApplication.APP_PREFERENCE.getInt(PREF_INPUT_HEIGHT, 0);
    }

    public void setLastLoc(String loc) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_LAST_LOC, loc);
        editor.commit();
    }

    public String getLastLoc() {
        return ZhislandApplication.APP_PREFERENCE.getString(PREF_LAST_LOC, "");
    }

    public void setShouldPopInviteView() {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_SHOULD_POP_INVITE_VIEW, false);
        editor.commit();
    }

    public boolean getShouldPopInviteView() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_SHOULD_POP_INVITE_VIEW, true);
    }

    public void setCountryName(String countryName) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_COUNTRY_NAME, countryName);
        editor.commit();
    }

    public String getCountryName() {
        return ZhislandApplication.APP_PREFERENCE.getString(
                PREF_COUNTRY_NAME, "");
    }

    public void setVisitedFragHaikeConfirm(boolean visited) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_HAS_VISITED_FragHaikeConfirm + getUserId(), visited);
        editor.commit();
    }

    public boolean hasVisitedFragHaikeConfirm() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_HAS_VISITED_FragHaikeConfirm + getUserId(), false);
    }

    public void setInfoGuideShowOver(boolean showOver) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_INFO_GUIDE_SHOW_OVER + getUserId(), showOver);
        editor.commit();
    }

    public boolean getInfoGuideShowOver() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_INFO_GUIDE_SHOW_OVER + getUserId(), false);
    }

    public void setCountryCode(String countryCode) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_COUNTRY_CODE, countryCode);
        editor.commit();
    }

    public String getCountryCode() {
        return ZhislandApplication.APP_PREFERENCE.getString(
                PREF_COUNTRY_CODE, "");
    }

    public void setCountryShowCode(String countryShowCode) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putString(PREF_COUNTRY_SHOW_CODE, countryShowCode);
        editor.commit();
    }

    public String getCountryShowCode() {
        return ZhislandApplication.APP_PREFERENCE.getString(
                PREF_COUNTRY_SHOW_CODE, "");
    }

    public void setShowGuideRegister() {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_SHOW_GUIDE_REGISTER, true);
        editor.commit();
    }

    public boolean getShowGuideRegister() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_SHOW_GUIDE_REGISTER, false);
    }

    public void setShowGuideBasicInfo() {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_SHOW_GUIDE_BASIC_INFO, true);
        editor.commit();
    }

    public boolean getShowGuideBasicInfo() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_SHOW_GUIDE_BASIC_INFO, false);
    }

    public void setRegisterKillPage(int pageType) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putInt(PREF_REGISTER_KILL_PAGE, pageType);
        editor.commit();
    }

    public int getRegisterKillPage() {
        return ZhislandApplication.APP_PREFERENCE.getInt(
                PREF_REGISTER_KILL_PAGE, -1);
    }

    public void setVcardUploadTime(long uploadTime) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_VCARD_UPLOAD_TIME, uploadTime);
        editor.commit();
    }

    public long getVCardUploadTime() {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_VCARD_UPLOAD_TIME, -1);
    }

    public void setContactFriendCount(long uid, long count) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_CONTACT_FRIEND_COUNT + uid, count);
        editor.commit();
    }

    public long getContactFriendCount(long uid) {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_CONTACT_FRIEND_COUNT + uid, 0);
    }

    public void setGuideFigure(long uid) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_GUIDE_FIGURE + uid, true);
        editor.commit();
    }

    public boolean getGuideFigure(long uid) {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_GUIDE_FIGURE + uid, false);
    }

    public void setPriceCommentTime(long time) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_PRICE_COMMENT_TIME, time);
        editor.commit();
    }

    public long getPriceCommentTime() {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_PRICE_COMMENT_TIME, -1);
    }

    public void setIsAddFriend(long userId) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_IS_ADD_FRIEND + userId, true);
        editor.commit();
    }

    public boolean getIsAddFriend(long userId) {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_IS_ADD_FRIEND + userId, false);
    }

    public void setIsCallFriend(long userId) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_IS_CALL_FRIEND + userId, true);
        editor.commit();
    }

    public boolean getIsCallFriend(long userId) {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_IS_CALL_FRIEND + userId, false);
    }

    public void setShowTaskContact() {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_SHOW_FRESH_TASK_CONTACT, true);
        editor.commit();
    }

    public boolean getShowTaskContact() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_SHOW_FRESH_TASK_CONTACT, false);
    }

    public void setIsShowTitleFreshTask(boolean showFreshTask) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_IS_SHOW_TITLE_FRESH_TASK, showFreshTask);
        editor.commit();
    }

    public boolean getIsShowTitleFreshTask() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_IS_SHOW_TITLE_FRESH_TASK, false);
    }

    public void setIsShowTitleFreshTaskRedDot(boolean showRedDot) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_IS_SHOW_TITLE_FRESH_TASK_RED_DOT, showRedDot);
        editor.commit();
    }

    public boolean getIsShowTitleFreshTaskRedDot() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_IS_SHOW_TITLE_FRESH_TASK_RED_DOT, false);
    }

    public void setIsShowFreshTaskMask() {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_IS_SHOW_FRESH_TASK_MASK, true);
        editor.commit();
    }

    public boolean getIsShowFreshTaskMask() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_IS_SHOW_FRESH_TASK_MASK, false);
    }

    public void setAppToBackgroundTime(long time) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_TO_BACKGROUND_TIME, time);
        editor.commit();
    }

    public long getAppToBackgroundTime() {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_TO_BACKGROUND_TIME, 0);
    }

    public void setNewlyAddedFansCount(long count) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_FANS_COUNT, count);
        editor.commit();
    }

    public long getNewlyAddedFansCount() {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_FANS_COUNT, 0);
    }

    public void setInteractiveCount(long count) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_INTERACTIVE_COUNT, count);
        editor.commit();
    }

    public long getInteractiveCount() {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_INTERACTIVE_COUNT, 0);
    }

    public void setSystemMsgCount(long count) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putLong(PREF_SYSTEM_COUNT, count);
        editor.commit();
    }

    public long getSystemMsgCount() {
        return ZhislandApplication.APP_PREFERENCE.getLong(
                PREF_SYSTEM_COUNT, 0);
    }

    public void clearAll() {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.remove(PREF_UID);
        editor.remove(PREF_UNAME);
        editor.remove(PREF_AVATAR);
        editor.remove(PREF_USER_TYPE);
        editor.remove(PREF_COMPANY);
        editor.remove(PREF_POSITION);
        editor.remove(PREF_USER_MOBILE);
        editor.remove(PREF_USER_JSON_STRING);
        editor.remove(PREF_TOKEN);
        editor.remove(PREF_HAS_LOGIN);
        editor.remove(PREF_LAST_RECOMMEND_DATE);
        editor.remove(PREF_SHOULD_POP_INVITE_VIEW);
        editor.remove(PREF_TABHOME_NEED_POP_UPDATE_DLG);
        editor.remove(PREF_IS_GET_DRIP_TASK);
        editor.remove(PREF_COUNTRY_NAME);
        editor.remove(PREF_COUNTRY_CODE);
        editor.remove(PREF_COUNTRY_SHOW_CODE);
        editor.remove(PREF_PRICE_COMMENT_TIME);
        editor.remove(PREF_IS_SHOW_TITLE_FRESH_TASK);
        editor.remove(PREF_IS_SHOW_TITLE_FRESH_TASK_RED_DOT);
        editor.remove(PREF_LIKEDIALOG_FIRSTSHOW);
        editor.remove(PREF_FANS_COUNT);
        editor.remove(PREF_INTERACTIVE_COUNT);
        editor.remove(PREF_SYSTEM_COUNT);
        editor.commit();
        setKeyValue(TaskGetFriendListByUid.VERSION_KEY, "0");
        setKeyValue(InviteSuccessHandler.INVITE_USER_ID, "");
        setKeyValue(PermissionsMgr.PREF_KEY_PERMISSIONS, null);
    }


    public void setLikeDialogShouldShow(Boolean isShouldShow) {
        Editor editor = ZhislandApplication.APP_PREFERENCE.edit();
        editor.putBoolean(PREF_LIKEDIALOG_FIRSTSHOW, false);
        editor.commit();
    }

    public Boolean getLikeDialogShouldShow() {
        return ZhislandApplication.APP_PREFERENCE.getBoolean(
                PREF_LIKEDIALOG_FIRSTSHOW, true);
    }

    private static PrefUtil instance;

    public static final PrefUtil Instance() {
        if (instance == null) {
            synchronized (PrefUtil.class) {
                if (instance == null)
                    instance = new PrefUtil();
            }
        }
        return instance;
    }

    private PrefUtil() {
    }
}
