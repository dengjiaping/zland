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
 * 更新公司task
 * */
public class TaskUpdateCompany extends TaskBase<Object, Object> {

	private Company company;

	public TaskUpdateCompany(Object context, Company company,
			TaskCallback<Object> responseCallback) {
		super(context, responseCallback);
		this.company = company;
		this.isPureRest = true;
	}

	@Override
	public void execute() {
		RequestParams params = null;
		params = this.put(params, "company", GsonHelper.GetCommonGson().toJson(company));
		this.put(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/company";
	}

	@Override
	protected Type getDeserializeType() {
		return new TypeToken<Object>() {
		}.getType();
	}

	@Override
	protected Object handleStringProxy(HttpResponse response) throws Exception {
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if (user != null) {
			User userSave = new User();
			userSave.uid = PrefUtil.Instance().getUserId();
			if (user.companies != null) {
				userSave.companies = new ArrayList<Company>();
				for(Company companyTmp : user.companies){
					if(companyTmp.companyId != company.companyId){
						userSave.companies.add(companyTmp);
					}else{
						userSave.companies.add(company);
						if(user.companyId != null && user.companyId == company.companyId){
							userSave.userCompany = company.name;
							userSave.userPosition = company.position;
						}
					}
				}
			}
			DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(userSave);
		}
		EventBus.getDefault().post(
				new EBCompany(EBCompany.TYPE_COMPANY_EDIT,
						company));
		EventBus.getDefault().post(
				new EBProfile(EBProfile.TYPE_CHANGE_POSITION));
		return super.handleStringProxy(response);
	}
}
