package com.zhisland.android.blog.feed.view.impl.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.feed.bean.AttachResource;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

/**
 * 负责资源模版的展示
 * Created by zhuchuntao on 16/9/2.
 */
public class ResourceHolder implements AttachHolder, View.OnClickListener {

    private Context context;
    private TextView contentView;
    private TagFlowLayout tagView;

    private FeedViewListener listener;
    private Feed feed;

    private View layout;

    public ResourceHolder(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.layout_resource, null);
        contentView = (TextView) layout.findViewById(R.id.resource_title);
        tagView = (TagFlowLayout) layout.findViewById(R.id.resource_tag);
    }

    @Override
    public View getView() {
        return layout;
    }

    @Override
    public void fillAttach(Feed feed, FeedViewListener feedViewListener) {
        this.listener = feedViewListener;
        this.feed = feed;

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        lp.setMargins(DensityUtil.dip2px(16), 0, DensityUtil.dip2px(16), 0);
        layout.setLayoutParams(lp);

        if (null != feed && feed.type == FeedType.RESOURCE) {
            AttachResource resource = (AttachResource) feed.attach;
            if (resource != null) {
                if (!TextUtils.isEmpty(resource.title))
                    contentView.setText(resource.title);
                List<ZHDicItem> hotTags = resource.tags;
                TagAdapter hotAdapter = new TagAdapter<ZHDicItem>(hotTags) {
                    @Override
                    public View getView(
                            com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
                            int position, ZHDicItem tag) {
                        TextView textView = new TextView(context);
                        textView.setTextColor(context.getResources().getColor(
                                R.color.color_f1));
                        DensityUtil.setTextSize(textView, R.dimen.txt_14);
                        textView.setBackgroundResource(R.drawable.rect_sf2_clarge);
                        int dpi7 = DensityUtil.dip2px(7);
                        int dpi12 = DensityUtil.dip2px(14);
                        textView.setPadding(dpi12, dpi7, dpi12, dpi7);
                        textView.setText(tag.name);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                                ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                        params.rightMargin = DensityUtil.dip2px(9);
                        params.topMargin = DensityUtil.dip2px(12);
                        textView.setLayoutParams(params);
                        return textView;
                    }
                };
                //tagView.setOnTagClickListener(hotTagClickListener);
                tagView.setAdapter(hotAdapter);
            }


        }
    }

    @Override
    public void recycleAttach() {
        this.feed = null;
        this.listener = null;

    }

    @Override
    public void onClick(View view) {
        if (null == listener) {
            return;
        }
        switch (view.getId()) {
            case R.id.resource_title:
                listener.onAttachClicked(feed, null);
                break;
        }
    }
}
