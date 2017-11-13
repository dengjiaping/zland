package com.zhisland.android.blog.aa.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.lib.OrmDto;

import java.util.ArrayList;

/**
 * Created by Mr.Tui on 2016/5/31.
 */
public class SplashData extends OrmDto {

    public static final String KEY_CACHE_SPLASH_DATA = "key_cache_splash_data";

    @SerializedName("id")
    public long id;

    /**
     * 言论作者
     */
    @SerializedName("author")
    public String author;

    /**
     * 言论内容
     */
    @SerializedName("content")
    public String content;

    /**
     * 背景图片
     */
    @SerializedName("picUrl")
    public String picUrl;

    /**
     * 过期时间（时间长度）单位毫秒
     */
    @SerializedName("expireMillisecond")
    public long expireTime;

    /**
     * 显示时间。单位毫秒
     */
    @SerializedName("playMillisecond")
    public int delay;

    /**
     * 过期时间（时间点）。数值为得到数据是的系统时间加expireTime。
     */
    public long validity;

    /**
     * 是否使用过
     */
    public boolean isUserd = false;

    /**
     * 是否有效
     */
    public boolean isValidity() {
        if (System.currentTimeMillis() > validity) {
            return false;
        }
        return true;
    }

    /**
     * 获取第一条未过期的SplashData.（调用该方法，意味着返回的SplashData已使用,isUserd已被置为true）
     */
    public static SplashData getValidity() {
        ArrayList<SplashData> datas = getCache();
        if (datas == null || datas.size() == 0) {
            return null;
        } else {
            SplashData data = datas.get(0);
            if (!data.isUserd) {
                data.isUserd = true;
                DBMgr.getMgr().getCacheDao().set(KEY_CACHE_SPLASH_DATA, datas);
            }
            return data;
        }
    }

    /**
     * 获取未过期且没用过的SplashData.（调用该方法，意味着返回的SplashData已使用，isUserd已被置为true）
     *
     * @return SplashData 返回的数据isUserd为true，null代表没有未过期且没用过的。
     */
    public static SplashData getValidityUnUsed() {
        ArrayList<SplashData> datas = getCache();
        if (datas == null || datas.size() == 0) {
            return null;
        } else {
            SplashData data = datas.get(0);
            if (!data.isUserd) {
                data.isUserd = true;
                DBMgr.getMgr().getCacheDao().set(KEY_CACHE_SPLASH_DATA, datas);
                return data;
            } else {
                return null;
            }
        }
    }

    /**
     * 保存接口得到的数据到缓存。
     */
    public static void cache(ArrayList<SplashData> datas) {
        if (datas != null) {
            ArrayList<SplashData> oldDatas = getCache();
            for (SplashData data : datas) {
                if (data.validity <= 0) {
                    data.validity = System.currentTimeMillis() + data.expireTime;
                }
                if (oldDatas != null && oldDatas.size() > 0) {
                    for (SplashData oldData : oldDatas) {
                        if (oldData.id == data.id) {
                            data.isUserd = oldData.isUserd;
                            break;
                        }
                    }
                }
            }
            DBMgr.getMgr().getCacheDao().set(KEY_CACHE_SPLASH_DATA, datas);
        }
    }

    /**
     * 获取缓存数据.(剔除已经过期的)
     */
    public static ArrayList<SplashData> getCache() {
        Object datas = DBMgr.getMgr().getCacheDao().get(KEY_CACHE_SPLASH_DATA);
        if (datas != null && datas instanceof ArrayList) {
            ArrayList<SplashData> result = (ArrayList<SplashData>) datas;
            for (int i = result.size() - 1; i >= 0; i--) {
                if (!result.get(i).isValidity()) {
                    result.remove(i);
                }
            }
            return (ArrayList<SplashData>) datas;
        }
        return null;
    }
}
