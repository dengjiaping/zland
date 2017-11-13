package com.zhisland.android.blog.common.dto;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.util.LoadDataFromAssert;
import com.zhisland.android.blog.profile.dto.CompanyState;
import com.zhisland.android.blog.profile.dto.CompanyType;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典数据块
 */
@DatabaseTable(tableName = Dict.TB_NAME, daoClass = DictDao.class)
public class Dict extends OrmDto {

    public static final String TB_NAME = "dict";

    // 国家码
    private static final int TYPE_COUNTRY_CODE = 1;
    // 活动标签
    public static final int TYPE_EVENT_TAG = 2;
    // 用户行业
    private static final int TYPE_USER_INDUSTRY = 3;
    // 资源类别
    private static final int TYPE_SUPPLY_CATEGORY = 4;
    // 需求类别
    private static final int TYPE_DEMAND_CATEGORY = 5;
    // 公司行业
    private static final int TYPE_COMPANY_INDUSTRY = 6;
    // 公司机构
    private static final int TYPE_COMPANY_ORG = 7;
    // 公司发展阶段
    private static final int TYPE_COMPANY_STATE_TYPE = 8;
    // 地区城市
    private static final int TYPE_CITY = 9;
    // 点滴
    private static final int TYPE_DRIP = 10;
    // 活动排序列表
    private static final int TYPE_EVENT_ORDER = 11;

    private static final String COL_TYPE = "type";
    private static final String COL_VERSION = "version";
    private static final String COL_DATA = "data";

    // 类型
    @SerializedName(COL_TYPE)
    @DatabaseField(columnName = COL_TYPE, id = true)
    Integer type;

    // 版本
    @SerializedName(COL_VERSION)
    @DatabaseField(columnName = COL_VERSION)
    Long version;

    // 数据
    @SerializedName(COL_DATA)
    @DatabaseField(columnName = COL_DATA)
    String data;

    private Dict() {
    }

    public static Dict getInstance() {
        return DictHolder.INSTANCE;
    }

    /**
     * 添加字典到数据库
     */
    public void addDict(int type, String data) {
        Dict dict = new Dict();
        dict.type = type;
        dict.data = data;
        dict.version = Long.valueOf(0);
        DBMgr.getMgr().getDictDao().addDict(dict);
    }

    /**
     * 获取国家码
     */
    public List<Country> getCountryCode() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_COUNTRY_CODE);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<List<Country>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().loadCountries();
        }
    }

    /**
     * 获取活动标签
     */
    public List<String> getEventTags() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_EVENT_TAG);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<List<String>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getEventTags();
        }
    }

    /**
     * 获取 用户行业
     */
    public ArrayList<ZHDicItem> getProfileIndustry() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_USER_INDUSTRY);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<List<ZHDicItem>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getIndustriesPersonal();
        }
    }

    /**
     * 获取 公司行业
     */
    public ArrayList<ZHDicItem> getCompanyIndustry() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_COMPANY_INDUSTRY);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<List<ZHDicItem>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getIndustriesCompany();
        }
    }

    /**
     * 获取 资源类别
     */
    public ArrayList<ZHDicItem> getSupplyCategory() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_SUPPLY_CATEGORY);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<List<ZHDicItem>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getSupplyCategory();
        }
    }

    /**
     * 获取 需求类别
     */
    public ArrayList<ZHDicItem> getDemandCategory() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_DEMAND_CATEGORY);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<List<ZHDicItem>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getDemandCategory();
        }
    }

    /**
     * 获取城市列表
     */
    public ArrayList<City> getCities() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_CITY);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<List<City>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getCities();
        }
    }

    /**
     * 获取公司发展状态的字典
     */
    public ArrayList<CompanyState> getCompanyState() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_COMPANY_STATE_TYPE);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<ArrayList<CompanyState>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getCompanyState();
        }
    }

    /**
     * 获取公司类型的字典
     */
    public ArrayList<CompanyType> getCompanyType() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_COMPANY_ORG);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<ArrayList<CompanyType>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getCompanyType();
        }
    }

 /**
     * 获取活动排序列表
     */
    public ArrayList<ZHDicItem> getEventOrder() {
        String data = DBMgr.getMgr().getDictDao().getDictData(TYPE_EVENT_ORDER);
        if (!StringUtil.isNullOrEmpty(data)) {
            return GsonHelper.GetCommonGson().fromJson(data, new TypeToken<ArrayList<ZHDicItem>>() {
            }.getType());
        } else {
            return LoadDataFromAssert.getInstance().getEventOrder();
        }
    }

    /**
     * 获取字典版本号
     */
    public String getDictVersion() {
        DictVersion dictVersion = new DictVersion();
        dictVersion.countryCode = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_COUNTRY_CODE);
        dictVersion.eventLabel = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_EVENT_TAG);
        dictVersion.industry = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_USER_INDUSTRY);
        dictVersion.supplyType = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_SUPPLY_CATEGORY);
        dictVersion.demandType = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_DEMAND_CATEGORY);
        dictVersion.companyIndustry = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_COMPANY_INDUSTRY);
        dictVersion.companyType = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_COMPANY_ORG);
        dictVersion.companyStageType = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_COMPANY_STATE_TYPE);
        dictVersion.district = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_CITY);
        dictVersion.tips = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_DRIP);
        dictVersion.eventOrder = DBMgr.getMgr().getDictDao().getDictVersion(TYPE_EVENT_ORDER);
        return GsonHelper.GetCommonGson().toJson(dictVersion);
    }

    /**
     * 字典版本号内部类
     */
    class DictVersion extends OrmDto {
        // 国家码
        public Long countryCode;

        // 活动标签
        public Long eventLabel;

        // 用户行业列表
        public Long industry;

        // 资源类别列表
        public Long supplyType;

        // 需求类别列表
        public Long demandType;

        // 公司行业列表
        public Long companyIndustry;

        // 公司机构类型
        public Long companyType;

        // 公司发展阶段
        public Long companyStageType;

        // 城市列表
        public Long district;

        // 点滴
        public Long tips;

        // 活动排序列表
        private Long eventOrder;
    }

    private static class DictHolder {
        private static Dict INSTANCE = new Dict();
    }

}
