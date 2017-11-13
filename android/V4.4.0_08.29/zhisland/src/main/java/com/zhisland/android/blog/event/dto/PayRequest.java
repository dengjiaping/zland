package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 活动当前状态类
 */
public class PayRequest extends OrmDto {

    private static final long serialVersionUID = 1L;

    @SerializedName("appid")
    public String appId;

    @SerializedName("partnerid")
    public String partnerId;

    @SerializedName("prepayid")
    public String prepayId;

    @SerializedName("noncestr")
    public String nonceStr;

    @SerializedName("timestamp")
    public String timeStamp;

    @SerializedName("sign")
    public String sign;

    @SerializedName("package")
    public String packageValue;

}
