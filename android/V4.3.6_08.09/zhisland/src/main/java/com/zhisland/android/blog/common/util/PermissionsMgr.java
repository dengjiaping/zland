package com.zhisland.android.blog.common.util;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.lib.async.http.task.GsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * APP权限管理
 */
public class PermissionsMgr {

    /**
     * 权限 json
     */
    public static final String PREF_KEY_PERMISSIONS = "pref_key_permissions";

    private static final String STR_SEP = "/";

    public static PermissionsMgr getInstance() {
        return PermissionsHolder.INSTANCE;
    }

    private static class PermissionsHolder {
        private static PermissionsMgr INSTANCE = new PermissionsMgr();
    }

    /**
     * 资讯推荐文章
     */
    public boolean canInfoRecommend() {
        List<String> permissions = getPermissions();
        String path = STR_SEP + AUriMgr.PATH_INFO_ADD;
        if (permissions == null) {
            // 没拉取到数据的默认值
            if (isHaiKe() || isYuZhuCe()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (permissions.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 资讯观点发布
     */
    public boolean canInfoComment() {
        List<String> permissions = getPermissions();
        String path = STR_SEP + AUriMgr.PATH_INFO_COMMENT;
        if (permissions == null) {
            // 没拉取到数据的默认值
            if (isYuZhuCe()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (permissions.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 资讯观点回复发布
     */
    public boolean canInfoCommentReply() {
        List<String> permissions = getPermissions();
        String path = STR_SEP + AUriMgr.PATH_INFO_COMMENT_REPLY;
        if (permissions == null) {
            // 没拉取到数据的默认值
            if (isYuZhuCe()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (permissions.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 活动发布
     */
    public boolean canEventPublish() {
        List<String> permissions = getPermissions();
        String path = STR_SEP + AUriMgr.PATH_EVENT_ADD;
        if (permissions == null) {
            // 没拉取到数据的默认值
            if (isHaiKe() || isYuZhuCe()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (permissions.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 全部资源 & 需求页
     */
    public boolean canJumpAllResources() {
        List<String> permissions = getPermissions();
        String path = STR_SEP + AUriMgr.PATH_RESOURCES;
        if (permissions == null) {
            // 没拉取到数据的默认值
            if (isYuZhuCe()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (permissions.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 资源&需求发布
     */
    public boolean canResourceAndDemandPublish() {
        List<String> permissions = getPermissions();
        String path = STR_SEP + AUriMgr.PATH_RESOURCE_ADD;
        if (permissions == null) {
            // 没拉取到数据的默认值
            if (isYuZhuCe()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (permissions.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 邀请好友
     */
    public boolean canInviteFriend() {
        List<String> permissions = getPermissions();
        String path = STR_SEP + AUriMgr.PATH_USER_INVITE;
        if (permissions == null) {
            // 没拉取到数据的默认值
            if (isVip() || isDaoDing()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (permissions.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 是否有加对方好友的权限
     * <p/>
     * 现有规则：
     * 1. 岛亲可以加所有人
     * 2. 海客和岛丁可以加除岛亲的所有人
     * 3. 预注册只能加预注册
     */
    public boolean canAddFriend(User toUser) {
        boolean result = false;
        if (isVip()) {
            // 岛亲可以加所有人
            result = true;
        } else if (isHaiKe() || isDaoDing()) {
            // 海客和岛丁可以加除岛亲的所有人
            if (!toUser.isVip()) {
                result = true;
            }
        } else if (isYuZhuCe()) {
            // 预注册只能加预注册
            if (toUser.isYuZhuCe()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取权限
     */
    private List<String> getPermissions() {
        List<String> permissions = null;
        String json = PrefUtil.Instance().getByKey(PREF_KEY_PERMISSIONS, null);
        if (json != null) {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            permissions = GsonHelper.GetCommonGson().fromJson(json, type);
            if (permissions == null) {
                permissions = new ArrayList<>();
            }
        }
        return permissions;
    }

    /**
     * 当前用户是否是海客
     */
    private boolean isHaiKe() {
        User selfUser = DBMgr.getMgr().getUserDao().getSelfUser();
        return selfUser.isHaiKe();
    }

    /**
     * 当前用户是否是预注册用户
     */
    private boolean isYuZhuCe() {
        User selfUser = DBMgr.getMgr().getUserDao().getSelfUser();
        return selfUser.isYuZhuCe();
    }

    /**
     * 当前用户是否是岛亲
     */
    private boolean isVip() {
        User selfUser = DBMgr.getMgr().getUserDao().getSelfUser();
        return selfUser.isVip();
    }

    /**
     * 当前用户是否是岛丁
     */
    private boolean isDaoDing() {
        User selfUser = DBMgr.getMgr().getUserDao().getSelfUser();
        return selfUser.isDaoDing();
    }
}
