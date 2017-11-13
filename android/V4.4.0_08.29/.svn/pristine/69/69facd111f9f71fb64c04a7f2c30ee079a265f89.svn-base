package com.zhisland.lib.image.viewer;

import java.io.Serializable;

/**
 * 图片浏览器的数据适配器
 *
 * @author 向飞
 */
public interface ImageDataAdapter extends Serializable {

    /**
     * 有多少条浏览
     *
     * @return
     */
    public int count();

    /**
     * 获取制定位置的URL
     *
     * @param postion
     * @return
     */
    public String getUrl(int postion);

    /**
     * 删除某张照片
     *
     * @param position
     */
    void remove(int position);

    /**
     * 获取某张照片的描述
     *
     * @param position
     * @return
     */
    String getDesc(int position);


}
