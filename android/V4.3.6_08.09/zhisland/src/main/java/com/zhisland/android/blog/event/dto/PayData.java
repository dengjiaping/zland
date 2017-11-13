package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 活动当前状态类
 */
public class PayData extends OrmDto {

    private static final long serialVersionUID = 1L;

    public static final int PAY_STATUS_UNDONE = 1; //未支付
    public static final int PAY_STATUS_OK = 3; //支付成功
    public static final int PAY_STATUS_WAIT_REFUND = 6; //待退款
    public static final int PAY_STATUS_REFUNDING = 7; //退款中
    public static final int PAY_STATUS_REFUND_COMPLETE = 8; //退款完成

    public static final int TYPE_IS_ON_LINE = 1;

    /**
     * 支付金额
     */
    @SerializedName("amounts")
    public Double amounts;
    /**
     * 支付状态
     */
    @SerializedName("payStatus")
    public Integer payStatus;

    /**
     * 线上线下支付
     */
    @SerializedName("isOnLine")
    public Integer isOnLine;

    // 实付金额
    public Double payAmounts;

    // 退款金额
    public Double refundAmounts;

    public String getAmountsFormat() {
        return String.format("%.2f", amounts);
    }

    public String getPayAmountsFormat() {
        return String.format("%.2f", payAmounts);
    }

    public String getRefundAmountsFormat() {
        return String.format("%.2f", refundAmounts);
    }

}
