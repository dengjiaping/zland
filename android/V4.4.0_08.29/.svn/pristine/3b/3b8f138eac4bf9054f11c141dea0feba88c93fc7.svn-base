package com.zhisland.android.blog.feed.presenter;

import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.lib.image.viewer.ImageDataAdapter;

import java.util.List;

/**
 * Created by arthur on 2016/9/8.
 */
public class FeedImageAdapter implements ImageDataAdapter {

    private List<FeedPicture> pics;

    public FeedImageAdapter() {

    }

    public FeedImageAdapter(List<FeedPicture> pics) {
        this.pics = pics;
    }

    @Override
    public int count() {
        return pics == null ? 0 : pics.size();
    }

    @Override
    public String getUrl(int postion) {
        if (pics == null || pics.size() <= postion)
            return null;

        return pics.get(postion).url;
    }

    @Override
    public void remove(int position) {
        if (pics == null || pics.size() <= position)
            return;

        pics.remove(position);
    }

    @Override
    public String getDesc(int position) {
        return null;
    }
}
