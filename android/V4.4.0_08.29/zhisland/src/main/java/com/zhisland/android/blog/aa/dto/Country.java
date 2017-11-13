package com.zhisland.android.blog.aa.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.util.StringUtil;

/**
 * 国家编码 实体bean
 */
public class Country extends OrmDto {

    // 已选择的国家码
    public static final String DICT_SELECTED = "DICT_SELECTED";
    // 默认中国地区国家码
    private static final String CHINA_NAME = "中国";
    public static final String CHINA_CODE = "0086";
    public static final String CHINA_SHOW_CODE = "86";

    private static final long serialVersionUID = 1L;

    @SerializedName("code")
    public String code;

    @SerializedName("name")
    public String name;

    @SerializedName("showCode")
    public String showCode;

    /**
     * 缓存用户登录或者注册时选择的国家码
     */
    public static void cachUserCountry(Country country) {
        PrefUtil.Instance().setCountryName(country.name);
        PrefUtil.Instance().setCountryCode(country.code);
        PrefUtil.Instance().setCountryShowCode(country.showCode);
    }

    /**
     * 获取用户登录或者注册时选择的国家码
     */
    public static Country getUserCountry() {
        String countryCode = PrefUtil.Instance().getCountryCode();
        if (StringUtil.isNullOrEmpty(countryCode)) {
            //可能用户没有登录 或者 是V4.3.3之前的版本，检测DB里有没有缓存
            Object obj = DBMgr.getMgr().getCacheDao().get(DICT_SELECTED);
            if (obj != null) {
                //老版本中有缓存，将数据插入PrefUtil中
                Country country = (Country) obj;
                PrefUtil.Instance().setCountryName(country.name);
                PrefUtil.Instance().setCountryCode(country.code);
                PrefUtil.Instance().setCountryShowCode(country.showCode);
            } else {
                //没有登录，插入默认数据，默认为中国
                PrefUtil.Instance().setCountryName(CHINA_NAME);
                PrefUtil.Instance().setCountryCode(CHINA_CODE);
                PrefUtil.Instance().setCountryShowCode(CHINA_SHOW_CODE);
            }
        }

        Country country = new Country();
        country.name = PrefUtil.Instance().getCountryName();
        country.code = PrefUtil.Instance().getCountryCode();
        country.showCode = PrefUtil.Instance().getCountryShowCode();
        return country;
    }
}
