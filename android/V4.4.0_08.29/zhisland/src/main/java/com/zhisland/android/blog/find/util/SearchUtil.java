package com.zhisland.android.blog.find.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.StringUtil;

/**
 * Created by Mr.Tui on 2016/6/2.
 */
public class SearchUtil {

    /**
     * 将搜索结果文字中的keyword高亮为color。
     *
     * @param content  结果文字
     * @param keywords 搜索关键字数组
     * @param color    高亮的颜色
     * @return SpannableString 含有高亮关键字的SpannableString
     */
    public static final SpannableString makeHighLight(String content, String[] keywords, int color) {
        if (StringUtil.isNullOrEmpty(content)) {
            return new SpannableString("");
        }
        SpannableString result = new SpannableString(content);
        if (keywords != null) {
            for (String keyword : keywords) {
                if (!StringUtil.isNullOrEmpty(keyword)) {
                    int index = 0;
                    do {
                        index = content.indexOf(keyword, index);
                        if (index < 0) {
                            break;
                        }
                        result.setSpan(new ForegroundColorSpan(color),
                                index, index + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        index = index + keyword.length();
                    } while (index < content.length());
                }
            }
        }
        return result;
    }
}
