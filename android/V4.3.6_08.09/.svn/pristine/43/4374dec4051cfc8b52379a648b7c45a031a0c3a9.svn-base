package com.zhisland.android.blog.profile.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.eb.EBCompany;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;

import de.greenrobot.event.EventBus;

/**
 * 新增公司task
 * */
public class TaskCreatCompany extends TaskBase<Long, Object> {

	private Company company;

	public TaskCreatCompany(Object context, Company company,
			TaskCallback<Long> responseCallback) {
		super(context, responseCallback);
		this.company = company;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "company",
				GsonHelper.GetCommonGson().toJson(company));
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/company";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Long>() {
		}.getType();
	}

	@Override
	protected Long handleStringProxy(HttpResponse response) throws Exception {
		Long companyId = super.handleStringProxy(response);
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if (companyId != null && user != null) {
			User userSave = new User();
			userSave.uid = PrefUtil.Instance().getUserId();
			if (user.companies != null) {
				userSave.companies = user.companies;
			} else {
				userSave.companies = new ArrayList<Company>();
			}
			company.companyId = companyId;
			userSave.companies.add(company);
			DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(userSave);
		}
		company.companyId = companyId;
		DBMgr.getMgr().getCacheDao().set(company.companyId + Company.POSTFIX, company);
		EventBus.getDefault().post(
				new EBCompany(EBCompany.TYPE_COMPANY_ADD, company));
		EventBus.getDefault().post(
				new EBProfile(EBProfile.TYPE_CHANGE_POSITION));
		return companyId;
	}
}
