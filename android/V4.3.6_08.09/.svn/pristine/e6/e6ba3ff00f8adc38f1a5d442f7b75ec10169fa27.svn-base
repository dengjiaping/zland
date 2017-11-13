package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.impl.AddResourceModel;
import com.zhisland.android.blog.freshtask.view.IAddResourceView;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.retrofit.ApiError;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * Created by Mr.Tui on 2016/5/28.
 */
public class AddResourcePresenter extends BasePresenter<AddResourceModel, IAddResourceView> {

    Resource resource;

    @Override
    protected void updateView() {
        super.updateView();
        view().initViews();
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setResourceCategory(ZHDicItem tag) {
        if (tag == null || StringUtil.isNullOrEmpty(tag.key)) {
            return;
        }
        resource.categoryTags = tag.key;
        view().setCategoryTag(tag.key, tag.name);
    }

    public void setResourceIndustry(ZHDicItem tag) {
        if (tag == null || StringUtil.isNullOrEmpty(tag.key)) {
            return;
        }
        resource.industryTags = tag.key;
        view().setIndustryTag(tag.key, tag.name);
    }

    public void onCategoryClick() {
        view().goToSelectCategory(resource.getCategoryObj());
    }

    public void onIndustryClick() {
        view().goToSelectIndustry(resource.getIndustryObj());
    }

    public void saveSupply() {
        String content = view().getContent();
        if (StringUtil.isNullOrEmpty(content)) {
            ToastUtil.showShort("请输入资源介绍");
            return;
        }
        if (resource.industryTags == null) {
            ToastUtil.showShort("请选择行业标签");
            return;
        }
        if (resource.categoryTags == null) {
            ToastUtil.showShort("请选择类别标签");
            return;
        }
        resource.content = content;
        resource.type = Resource.TYPE_SUPPLY;
        view().showProgressDialog("请求中...");

        model().addResource(view().getViewContext(), resource, new TaskCallback<Object>() {
            @Override
            public void onSuccess(Object content) {
                view().hideProgressDialog();
                ToastUtil.showShort("添加资源成功");
                sendOkStatusEventBus();
                view().finishSelf();
            }

            @Override
            public void onFailure(Throwable e) {
                view().hideProgressDialog();
                if (e instanceof ApiError) {
                    ApiError error = (ApiError) e;
                    if (error.code == CodeUtil.CODE_RESOURCE_LIMIT) {
                        ToastUtil.showShort("发布的资源已经到达上限");
                        sendOkStatusEventBus();
                    }
                }
            }
        });
    }

    private void sendOkStatusEventBus() {
        EventFreshTask EventFreshTask = new EventFreshTask(TaskType.RESOURCE, TaskStatus.FINISHED);
        BusFreshTask.Bus().post(EventFreshTask);
    }
}
