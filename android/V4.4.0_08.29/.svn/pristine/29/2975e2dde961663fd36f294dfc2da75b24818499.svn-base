package com.zhisland.android.blog.profile.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 公司类型
 * 
 * @author zhangxiang
 * 
 */
public class CompanyType extends OrmDto {
	/**
	 * [{tagId=org_type_1, tagName=商业机构, custom=null,
	 * parentId=organization_type}, {tagId=org_type_2, tagName=非商业机构,
	 * custom=null, parentId=organization_type}, {tagId=org_type_3,
	 * tagName=社会组织, custom=null, parentId=organization_type}]
	 */

	public static final String KEY_COMMERCE = "org_type_1";
	public static final String KEY_UN_COMMERCE = "org_type_2";
	public static final String KEY_SOCIETY = "org_type_3";
	public static final String KEY_ZHISLAND_ORG = "zhisland_org";
	public static ArrayList<CompanyType> types;
	
	@SerializedName("tagId")
	public String tagId;

	@SerializedName("tagName")
	public String tagName;

	@SerializedName("parentId")
	public String parentId;

	/**
	 * 排序标识，0,1,2,...
	 */
	@SerializedName("custom")
	public String custom;

	public CompanyType() {

	}

	public CompanyType(CompanyType type) {
		this.tagId = type.tagId;
		this.tagName = type.tagName;
		this.parentId = type.parentId;
		this.custom = type.custom;
	}

}
