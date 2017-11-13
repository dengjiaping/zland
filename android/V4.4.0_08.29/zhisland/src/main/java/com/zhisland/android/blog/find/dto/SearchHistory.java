package com.zhisland.android.blog.find.dto;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.lib.OrmDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 搜索历史
 * Created by Mr.Tui on 2016/5/20.
 */
public class SearchHistory extends OrmDto {

    public static final String KEY_CACHE_SEARCH_HISTORY = "key_cache_search_history_";

    public static final int MAX_CACHE_COUNT = 10;

    /**
     * 搜索关键字
     * */
    public String searchWord;

    /**
     * 搜索时间
     * */
    public long searchTime;

    /**
     * 搜索类型
     * */
    public FindType type;

    /**
     * 根据FindType，获取FindType的搜索历史记录
     * */
    public static ArrayList<SearchHistory> getCacheData(FindType type) {
        Object obj = DBMgr.getMgr().getCacheDao().get(KEY_CACHE_SEARCH_HISTORY + type + PrefUtil.Instance().getUserId());
        if (obj != null) {
            return (ArrayList<SearchHistory>) obj;
        }
        return null;
    }

    /**
     * 根据FindType，添加searchWord到FindType类型的历史记录。
     * */
    public static void cacheData(FindType type, String searchWord) {
        ArrayList<SearchHistory> historyList = getCacheData(type);
        if (historyList == null) {
            historyList = new ArrayList<SearchHistory>();
        }
        SearchHistory historyNew = null;
        for (SearchHistory history : historyList) {
            if (history != null && history.searchWord != null && history.searchWord.equals(searchWord)) {
                historyNew = history;
                break;
            }
        }
        if (historyNew != null) {
            historyList.remove(historyNew);
        } else {
            historyNew = new SearchHistory();
        }
        historyNew.type = type;
        historyNew.searchWord = searchWord;
        historyNew.searchTime = System.currentTimeMillis();
        historyList.add(0, historyNew);
        for (int i = historyList.size(); i > MAX_CACHE_COUNT; i--) {
            historyList.remove(i - 1);
        }
        DBMgr.getMgr().getCacheDao().set(KEY_CACHE_SEARCH_HISTORY + type + PrefUtil.Instance().getUserId(), historyList);
    }
}
