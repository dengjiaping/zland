package com.zhisland.lib.view.web;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/*
 * 正和岛网页 分享 的数据段
 */
public class WebShare implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("url")
    public String url;

    @SerializedName("title")
    public String title = "";

    @SerializedName("description")
    public String description = "";

    @SerializedName("image")
    public String imageurl = "";

    public StringBuilder toHtml(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("<section class=\"abstract\">");
        sb.append(description);
        sb.append("</section>");

        return sb;
    }

}
