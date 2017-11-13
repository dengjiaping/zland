package com.zhisland.lib.authority;

import android.content.Intent;

import com.zhisland.lib.component.act.BaseFragmentActivity;
import com.zhisland.lib.util.MLog;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限管理
 * Created by arthur on 2016/9/12.
 */
public class AuthorityMgr {

    private static final String TAG = "zhauth";
    private Map<String, IAuthorityHandler> handlerMap;

    /**
     * 注册权限监听
     *
     * @param authorityName
     * @param handler
     */
    public void register(String authorityName, IAuthorityHandler handler) {
        if (authorityName == null || handler == null)
            return;

        handlerMap.put(authorityName, handler);
    }

    /**
     * 对权限做拦截
     *
     * @param intent
     * @return 如果不需要拦截则返回true，需要拦截则返回false
     */
    public boolean handleAuthority(BaseFragmentActivity context, Intent intent) {
        if (intent.getComponent() == null)
            return true;

        String componentName = parseIntentName(intent);
        IAuthorityHandler handler = handlerMap.get(componentName);
        if (handler != null) {
            try {
                return handler.handle(context, intent);
            } catch (Exception ex) {
                MLog.e(TAG, ex.getMessage(), ex);
                return false;
            }
        }
        return true;
    }

    private String parseIntentName(Intent intent) {
        return intent.getComponent().getClassName();
    }


    //region Singleton methods

    public static AuthorityMgr Instance() {
        return InstanceHolder.Instance;
    }

    private static class InstanceHolder {
        private static final AuthorityMgr Instance = new AuthorityMgr();
    }

    private AuthorityMgr() {
        handlerMap = new HashMap<>();
    }

    //endregion
}
