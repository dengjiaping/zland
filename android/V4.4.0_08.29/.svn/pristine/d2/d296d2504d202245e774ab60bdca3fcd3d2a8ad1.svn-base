package com.zhisland.android.blog.common.uri;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.util.SparseArray;

import com.zhisland.android.blog.common.webview.FragWebViewActivity;
import com.zhisland.android.blog.contacts.uri.AUriActiveInvite;
import com.zhisland.android.blog.contacts.uri.AUriFriendList;
import com.zhisland.android.blog.contacts.uri.AUriInviteDealed;
import com.zhisland.android.blog.event.uri.AUriEvent;
import com.zhisland.android.blog.event.uri.AUriEventCreatedList;
import com.zhisland.android.blog.event.uri.AUriEventSignConfirm;
import com.zhisland.android.blog.event.uri.AUriEventSignedList;
import com.zhisland.android.blog.find.uri.AUriBoss;
import com.zhisland.android.blog.find.uri.AUriBossHome;
import com.zhisland.android.blog.find.uri.AUriResources;
import com.zhisland.android.blog.freshtask.uri.AUriFreshTaskDetail;
import com.zhisland.android.blog.freshtask.uri.AUriTasksCard;
import com.zhisland.android.blog.freshtask.uri.AUriTasksList;
import com.zhisland.android.blog.im.uri.AUriSingleChat;
import com.zhisland.android.blog.info.uri.AUriInfo;
import com.zhisland.android.blog.profile.uri.AUriCommentList;
import com.zhisland.android.blog.profile.uri.AUriPosition;
import com.zhisland.lib.uri.IUriMgr;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;

import java.util.Locale;

/**
 * new uri manager
 */
public class AUriMgr implements IUriMgr {

    // 传递过来的 uri
    public static final String URI_BROWSE = "uri_browse";

    public static String AUTHORITY = "com.zhisland";

    private static String SEP_ID = "/#";
    private static final String TAG = "zhuri";

    private static AUriMgr instance;
    private UriMatcher matcher;
    private int codeCur = 1;
    private SparseArray<AUriBase> handlers;

    public static String SCHEMA_BLOG() {
        return "zhisland";
    }

    public static AUriMgr instance() {
        if (instance == null) {
            synchronized (AUriMgr.class) {
                if (instance == null) {
                    instance = new AUriMgr();
                }
            }
        }
        return instance;
    }

    private AUriMgr() {
        handlers = new SparseArray<>();
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        try {
            registerHandlers();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * uri 分发
     */
    @Override
    public void viewRes(Context context, String uriString) {
        if (StringUtil.isNullOrEmpty(uriString))
            return;
        uriString = uriString.trim();

        MLog.d(TAG, uriString);
        Uri uri = Uri.parse(uriString);

        int code = matcher.match(uri);
        AUriBase handler = handlers.get(code);
        if (handler != null) {
            handler.viewRes(context, uri, null, null);
        } else {
            FragWebViewActivity.launch(context, uriString, null);
        }
    }

    /**
     * 根据type和id获取URI string
     */
    public String getUriString(long id, String path) {
        if (StringUtil.isNullOrEmpty(path))
            return null;

        return String.format(Locale.getDefault(), SCHEMA_BLOG()
                + "://%s/%s/%d", AUTHORITY, path, id);
    }

    /**
     * 是否为支持的URI
     */
    public boolean isValid(Uri uri) {
        int code = matcher.match(uri);
        AUriBase handler = handlers.get(code);
        return handler != null;
    }

    /**
     * 获取uri代表的URI path
     */
    public boolean isPath(String url, String path) {
        if (StringUtil.isNullOrEmpty(url) || StringUtil.isNullOrEmpty(path)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        int code = matcher.match(uri);
        AUriBase handler = handlers.get(code);
        if (handler != null)
            return handler.getUriPath().equals(path);

        return false;
    }

    /**
     * 注册新的handler
     */
    public void registerHandler(String path, Class<? extends AUriBase> cls) throws IllegalAccessException, InstantiationException {
        AUriBase handler = cls.newInstance();

        matcher.addURI(AUTHORITY, path, codeCur);
        handler.setPath(path);
        handlers.put(codeCur, handler);
        MLog.d(TAG, path + " is registered with  code " + codeCur);
        codeCur++;
    }

    /*======================= 与服务器定义的path =======================================*/
    // 主页
    public static final String PATH_HOME = "home";
    // 资讯
    public static final String PATH_NEWS = "news";
    // 活动
    public static final String PATH_EVENTS = "event";
    // 全部企业家
    public static final String PATH_USERS = "users";
    // 个人详情
    public static final String PATH_USER_DETAIL = "user";
    // 新手任务列表
    public static final String PATH_TASKS_LIST = "tasks/list";
    // 新手任务卡片
    public static final String PATH_TASKS_CARD = "tasks/card";
    // 全部资源列表
    public static final String PATH_RESOURCES = "resources";
    // IM 单聊
    public static final String PATH_CHAT_SINGLE = "chat/single";
    //
    public static final String PATH_INVITATION_PASSED = "invitationPassed";
    // 用户权益页
    public static final String PATH_PERMISSIONS = "permissions";
    // 报名确认页
    public static final String PATH_EVENT_SIGN_CONFIRM = "event/sign_confirm";
    // 新手任务 任务详情
    public static final String PATH_FRESH_TASK_DETAIL = "task";
    // 主动邀请
    public static final String PATH_ACTIVE_INVITE = "invite/invitation";
    // 主动邀请
    public static final String PATH_ACTIVE_INVITE_DEALED = "invite/dealed";
    /*======================= 与服务器定义的path =======================================*/

    /*======================= 自定义path =======================================*/
    // 好友列表
    public static final String PATH_FRIEND_LIST = "custom/friend/list";
    // 我发起的活动列表
    public static final String PATH_EVENT_CREATED_LIST = "custom/event/created/list";
    // 我报名的活动列表
    public static final String PATH_EVENT_SIGNED_LIST = "custom/event/signed/list";
    // 评论列表
    public static final String PATH_COMMENT_LIST = "custom/comment/list";
    // 加好友请求列表
    public static final String PATH_ADD_FRIEND_REQUEST = "custom/add/friend/request";
    /*======================= 自定义path =======================================*/

    // 资讯推荐文章
    public static final String PATH_INFO_ADD = "news/add";
    // 资讯观点发布
    public static final String PATH_INFO_COMMENT = "news/comment/add";
    // 资讯观点回复发布
    public static final String PATH_INFO_COMMENT_REPLY = "news/comment/reply/add";
    // 活动发布
    public static final String PATH_EVENT_ADD = "event/add";
    // 资源需求发布
    public static final String PATH_RESOURCE_ADD = "resource/add";
    // 邀请好友
    public static final String PATH_USER_INVITE = "relation/user/invite";
    // feed发布
    public static final String PATH_FEED_ADD = "feed/add";

    //企业家首页
    public static final String PATH_ENTERPRISER = "enterpriser";

    //搜索岛邻
    public static final String PATH_SEARCH_USERS = "search/users";

    //搜索资源
    public static final String PATH_SEARCH_RESOURCE = "search/resource";

    //当前任职
    public static final String PATH_POSITION = "position";

    /**
     * 注册默认支持的URI跳转
     */
    private void registerHandlers() throws InstantiationException, IllegalAccessException {
        // 注册公用 uri handler
        this.registerHandler(PATH_HOME, AUriHome.class);
        this.registerHandler(PATH_HOME + SEP_ID, AUriHome.class);
        this.registerHandler(PATH_NEWS, AUriInfo.class);
        this.registerHandler(PATH_NEWS + SEP_ID, AUriInfo.class);
        this.registerHandler(PATH_EVENTS, AUriEvent.class);
        this.registerHandler(PATH_EVENTS + SEP_ID, AUriEvent.class);
        this.registerHandler(PATH_USERS, AUriBoss.class);
        this.registerHandler(PATH_USER_DETAIL + SEP_ID, AUriUser.class);
        this.registerHandler(PATH_TASKS_LIST, AUriTasksList.class);
        this.registerHandler(PATH_TASKS_CARD + SEP_ID, AUriTasksCard.class);
        this.registerHandler(PATH_RESOURCES, AUriResources.class);
        this.registerHandler(PATH_CHAT_SINGLE + SEP_ID, AUriSingleChat.class);
        this.registerHandler(PATH_INVITATION_PASSED, AUriHome.class);
        this.registerHandler(PATH_PERMISSIONS, AUriPermissions.class);
        this.registerHandler(PATH_EVENT_SIGN_CONFIRM, AUriEventSignConfirm.class);
        this.registerHandler(PATH_FRESH_TASK_DETAIL + SEP_ID, AUriFreshTaskDetail.class);
        this.registerHandler(PATH_ACTIVE_INVITE, AUriActiveInvite.class);
        this.registerHandler(PATH_ACTIVE_INVITE_DEALED, AUriInviteDealed.class);
        this.registerHandler(PATH_ENTERPRISER, AUriBossHome.class);
        this.registerHandler(PATH_SEARCH_USERS, AUriBoss.class);
        this.registerHandler(PATH_SEARCH_RESOURCE, AUriResources.class);
        this.registerHandler(PATH_POSITION, AUriPosition.class);

        // 注册自定义 uri handler
        this.registerHandler(PATH_FRIEND_LIST, AUriFriendList.class);
        this.registerHandler(PATH_EVENT_CREATED_LIST, AUriEventCreatedList.class);
        this.registerHandler(PATH_EVENT_SIGNED_LIST, AUriEventSignedList.class);
        this.registerHandler(PATH_COMMENT_LIST, AUriCommentList.class);
//        this.registerHandler(PATH_ADD_FRIEND_REQUEST, AUriCardAddFriend.class);//好友关系去掉了
    }
}
