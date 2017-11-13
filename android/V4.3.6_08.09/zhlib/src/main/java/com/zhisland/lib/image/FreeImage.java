package com.zhisland.lib.image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arthur on 2014/10/15.
 */
public class FreeImage implements ImgBrowsable {

    private static final long serialVersionUID = 1L;
    @SerializedName("src")
    public String src;
    @SerializedName("left")
    public int left;
    @SerializedName("top")
    public int top;
    @SerializedName("width")
    public int width;
    @SerializedName("height")
    public int height;

    @Override
    public String url() {
        return src;
    }
}