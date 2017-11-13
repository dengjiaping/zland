package com.zhisland.lib.load;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.load.UploadFileRes;

/**
 * Created by Mr.Tui on 2016/6/15.
 */
public class UpLoadResult {

    @SerializedName("errorCode")
    public int code;

    @SerializedName("msg")
    public String msg;

    @SerializedName("result")
    public boolean result;

    @SerializedName("data")
    public UploadFileRes data;
}
