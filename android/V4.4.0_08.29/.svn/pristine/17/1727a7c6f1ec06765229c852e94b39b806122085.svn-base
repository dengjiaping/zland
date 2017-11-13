package com.zhisland.android.blog.aa.controller;

import android.app.Activity;

import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;

/**
 * 注册过程中 杀死逻辑管理
 */
public class KillSelfMgr {

    /**
     * @see FragCreateBasicInfo
     * 创建基本信息,跳转 FragCreateBasicInfo
     */
    public static final int FRAG_CREATE_BASIC_INFO = 100;
    /**
     * @see FragVisitContact
     * 授权通讯录，跳转FragRequestInvite
     */
    public static final int FRAG_VISIT_CONTACT = 200;
    /**
     * @see FragVisitContactsRefuse
     * 授权通讯录 拒绝通讯录访问权限页面，跳转FragVisitContact
     */
    public static final int FRAG_VISIT_CONTACT_REFUSE = 300;
    /**
     * @see FragRegisterSuccess
     * 有邀请人且通过职位白名单，注册成功页， 跳转主页
     */
    public static final int FRAG_REGISTER_SUCCESS = 400;


    private static KillSelfMgr mgr;
    private static final Object sysObj = new Object();

    private KillSelfMgr() {

    }

    public static KillSelfMgr getInstance() {
        if (mgr == null) {
            synchronized (sysObj) {
                if (mgr == null) {
                    mgr = new KillSelfMgr();
                }
            }
        }
        return mgr;
    }

    /**
     * 设置当前注册杀死页
     */
    public void setCurrentPage(int pageType) {
        PrefUtil.Instance().setRegisterKillPage(pageType);
    }

    /**
     * 根据上一次杀死页面，分发跳转
     */
    public void dispatch(Activity activity) {
        int pageType = PrefUtil.Instance().getRegisterKillPage();
        switch (pageType) {
            case FRAG_CREATE_BASIC_INFO:
                FragCreateBasicInfo.invoke(activity, true);
                break;
            case FRAG_VISIT_CONTACT:
            case FRAG_VISIT_CONTACT_REFUSE:
                FragVisitContact.invoke(activity, true);
                break;
            case FRAG_REGISTER_SUCCESS:
                HomeUtil.NavToHome(activity);
                break;
            default:
                ActGuide.invoke(activity);
                break;
        }
    }
}
