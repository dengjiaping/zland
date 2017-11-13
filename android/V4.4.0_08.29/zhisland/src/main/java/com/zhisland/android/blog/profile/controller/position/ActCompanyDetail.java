package com.zhisland.android.blog.profile.controller.position;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.lib.component.act.TitleType;

/**
 * 公司信息详情
 * 
 * @author zhangxiang
 *
 */
public class ActCompanyDetail extends FragBaseActivity {

	/**
	 * 公司详情界面通过Company Id来跳转
	 */
	public static final String INTENT_KEY_COMPANY_ID = "intent_key_company_id";
	
	/**
	 * 当前显示的公司所对应的用户id 要转递，用来判断是否可以编辑
	 */	
	public static final String INTENT_KEY_USER_ID = "intent_key_user_id";
	
	public static final String INTENT_KEY_IS_SELF = "intent_key_is_self";
	
	private FragCompanyDetail fragCompanyDetail;

	/**
	 * 通过 CompanyID 来跳转到公司详情
	 * @param context
	 * @param companyId
	 */
	public static void launch(Context context, long companyId, boolean isSelf){
		Intent intent = new Intent(context, ActCompanyDetail.class);
		intent.putExtra(INTENT_KEY_COMPANY_ID, companyId);
		intent.putExtra(INTENT_KEY_IS_SELF, isSelf);
		context.startActivity(intent);
	}
	
	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(false);
		fragCompanyDetail = new FragCompanyDetail();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, fragCompanyDetail);
		ft.commit();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_NONE;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		fragCompanyDetail.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
