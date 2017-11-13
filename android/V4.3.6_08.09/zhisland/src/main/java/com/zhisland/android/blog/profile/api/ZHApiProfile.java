package com.zhisland.android.blog.profile.api;

import android.content.Context;

import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.base.ApiBase;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.profile.dto.CompanyState;
import com.zhisland.android.blog.profile.dto.CompanyType;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.android.blog.profile.dto.Honor;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.profile.dto.UserAssistant;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.profile.dto.UserContactInfo;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.ArrayList;
import java.util.List;

public class ZHApiProfile extends ApiBase {

    private static class Holder {
        private static ZHApiProfile INSTANCE = new ZHApiProfile();
    }

    public static ZHApiProfile Instance() {
        return Holder.INSTANCE;
    }

    private ZHApiProfile() {

    }

    /**
     * 获取神评论列表
     */
    public void getUserCommentList(Context context, long userId, String nextId,
                                   TaskCallback<ZHPageData<UserComment>> callback) {
        addTask(context, new TaskGetUserCommentList(context, userId, nextId,
                callback));
    }

    /**
     * 置顶神评论
     */
    public void topUserComment(Context context, long commentId, int status,
                               TaskCallback<Object> callback) {
        addTask(context, new TaskTopUserComment(context, commentId, status,
                callback));
    }

    /**
     * 删除评论
     */
    public void deleteComment(Context context, long commentId,
                              TaskCallback<Object> callback) {
        addTask(context, new TaskDeleteUserComment(context, commentId,
                callback));
    }

    /**
     * 添加神评论
     */
    public void createUserComment(Context context, long toUserId, String content,
                                  TaskCallback<Object> callback) {
        addTask(context, new TaskCreateUserComment(context, toUserId, content,
                callback));
    }

    /**
     * 是否可以评论
     */
    public void getCommentEnable(Context context, long toUid,
                                 TaskCallback<Object> callback) {
        addTask(context, new TaskCommentEnable(context, toUid, callback));
    }

    /**
     * 创建荣誉
     */
    public void createHonor(Context context, Honor honor, TaskCallback<String> callback) {
        addTask(context, new TaskCreateHonor(context, honor, callback));
    }

    /**
     * 更新荣誉
     */
    public void updateHonor(Context context, Honor honor, TaskCallback<Object> callback) {
        addTask(context, new TaskUpdateHonor(context, honor, callback));
    }

    /**
     * 删除荣誉
     */
    public void deleteHonor(Context context, String honorId, TaskCallback<Object> callback) {
        addTask(context, new TaskDeleteHonor(context, honorId, callback));
    }

    /**
     * 获取荣誉列表
     */
    public void getHonorList(Context context, TaskCallback<ArrayList<Honor>> callback) {
        addTask(context, new TaskGetHonorList(context, callback));
    }

    /**
     * 创建供给
     */
    public void createResource(Context context, Resource resource, TaskCallback<Long> callback) {
        addTask(context, new TaskCreateResource(context, resource, callback));
    }

    /**
     * 更新供给
     */
    public void updateResource(Context context, Resource resource, TaskCallback<Object> callback) {
        addTask(context, new TaskUpdateResource(context, resource, callback));
    }

    /**
     * 删除供给
     */
    public void deleteResource(Context context, Resource resource, TaskCallback<Object> callback) {
        addTask(context, new TaskDeleteResource(context, resource, callback));
    }

    /**
     * 提交我的助理
     */
    public void updateAssistant(Context context, UserAssistant userAssistant,
                                TaskCallback<Object> callback) {
        addTask(context, new TaskUpdateAssistant(context, userAssistant,
                callback));
    }

    /**
     * 更新点滴
     */
    public void updateDrip(Context context, List<CustomDict> dicts, TaskCallback<Object> callback) {
        addTask(context, new TaskUpdateDrip(context, dicts, callback));
    }

    /**
     * 获取默认点滴列表
     */
    public void getDefaultDrips(Context context, TaskCallback<List<CustomDict>> callback) {
        addTask(context, new TaskGetDefaultDripList(context, callback));
    }

    /**
     * 更新我的联系方式
     */
    public void updateContactInfo(Context context, UserContactInfo contactInfo,
                                  TaskCallback<Object> callback) {
        addTask(context, new TaskUpdateContactInfo(context, contactInfo,
                callback));
    }

    /**
     * 举报用户
     */
    public void reportUser(Context context, long userId, String reasonId,
                           TaskCallback<Object> callback) {
        addTask(context, new TaskReportUser(context, userId, reasonId, callback));
    }

    /**
     * 获取资源数据.1 资源 2 需求
     */
    public void getResourceList(Context context, Integer type,
                                TaskCallback<ArrayList<Resource>> callback) {
        addTask(context, new TaskGetResourceList(context, type, callback));
    }

    /**
     * 获取公司发展状态的字典
     *
     * @param responseCallback
     */
    public void getCompanyStateDic(Context context,
                                   TaskCallback<ArrayList<CompanyState>> responseCallback) {
        addTask(context, new TaskgetCompanyStateList(context, responseCallback));
    }

    /**
     * 获取公司类型的字典
     *
     * @param responseCallback
     */
    public void getCompanyTypeDic(Context context,
                                  TaskCallback<ArrayList<CompanyType>> responseCallback) {
        addTask(context, new TaskgetCompanyTypeList(context, responseCallback));
    }

    /**
     * 新增公司
     */
    public void creatCompany(Context context, Company company,
                             TaskCallback<Long> responseCallback) {
        addTask(context, new TaskCreatCompany(context, company,
                responseCallback));
    }

    /**
     * 更新公司
     */
    public void updateCompany(Context context, Company company,
                              TaskCallback<Object> responseCallback) {
        addTask(context, new TaskUpdateCompany(context, company,
                responseCallback));
    }

    /**
     * 删除公司
     */
    public void deleteCompany(Context context, long companyId,
                              TaskCallback<Object> responseCallback) {
        addTask(context, new TaskDeleteCompany(context, companyId,
                responseCallback));
    }

    /**
     * 获取举报原因列表
     */
    public void getReportReasonList(Context context, TaskCallback<ArrayList<Country>> responseCallback) {
        addTask(context, new TaskGetReportReason(context, responseCallback));
    }
}
