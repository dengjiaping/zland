package com.zhisland.android.blog.find.view;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.find.util.SearchUtil;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 搜索资源需求结果Item的Viewholder。
 * Created by Mr.Tui on 2016/5/21.
 */
public class ResourceHolder {

    @InjectView(R.id.ivAvatar)
    AvatarView avatarView;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvComAndPos)
    TextView tvComAndPos;

    @InjectView(R.id.ivRight)
    ImageView ivRight;

    @InjectView(R.id.tvContent)
    TextView tvContent;

    @InjectView(R.id.tflTag)
    TagFlowLayout tflTag;

    @InjectView(R.id.tvTime)
    TextView tvTime;

    View item;

    private Context context;
    private Resource resource;
    private String keyword;
    private List<String> tags = new ArrayList<String>();

    public ResourceHolder(View v, Context context) {
        this.context = context;
        item = v;
        ButterKnife.inject(this, v);
        tflTag.setAdapter(tagAdapter);
        item.setOnClickListener(itemClickListener);
    }

    public void fill(Resource resource, String keyword) {
        this.keyword = keyword;
        this.resource = resource;
        if (resource.type == Resource.TYPE_SUPPLY) {
            ivRight.setImageResource(R.drawable.img_find_res);
        } else {
            ivRight.setImageResource(R.drawable.img_find_demand);
        }
        fillUserView();
        fillResourceView();
    }

    private void fillResourceView() {
        String content = resource.content == null ? "" : resource.content;
        String key = keyword == null ? "" : keyword;
        SpannableString contentSS = SearchUtil.makeHighLight(content, key.split(" "), context.getResources().getColor(R.color.color_dc));
        tvContent.setText(contentSS);

        if (resource.publicTime != null) {
            tvTime.setText(resource.publicTime);
        } else {
            tvTime.setText("");
        }

        tags.clear();
        //在搜索接口industryTags、categoryTags分别返回的时候行业标签和类别标签的名字，而不是id。和其他接口不一样，暂时特殊处理。
        setTagStringToList(resource.industryTags, tags);
        setTagStringToList(resource.categoryTags, tags);
        tagAdapter.notifyDataChanged();
    }

    private void fillUserView() {
        avatarView.fill(resource.user, true);

        String name = resource.user.name == null ? "" : resource.user.name;
        String desc = (resource.user.userCompany == null ? ""
                : (resource.user.userCompany + " "))
                + (resource.user.userPosition == null ? "" : resource.user.userPosition);
        String key = keyword == null ? "" : keyword;

        SpannableString nameSS = SearchUtil.makeHighLight(name, key.split(" "), context.getResources().getColor(R.color.color_dc));
        SpannableString descSS = SearchUtil.makeHighLight(desc, key.split(" "), context.getResources().getColor(R.color.color_dc));

        tvName.setText(nameSS);
        tvComAndPos.setText(descSS);
    }

    private void setTagStringToList(String tag, List<String> tags) {
        if (!StringUtil.isNullOrEmpty(tag) && tags != null) {
            tags.add(tag.trim());
        }
    }

    View.OnClickListener itemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (resource != null && resource.user != null) {
                ActProfileDetail.invoke(context, resource.user.uid);
            }
        }
    };

    TagAdapter tagAdapter = new TagAdapter<String>(tags) {
        @Override
        public View getView(
                com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
                int position, String tag) {
            TextView textView = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.tag_text, null);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            textView.setTextColor(context.getResources().getColor(
                    R.color.color_f2));
            DensityUtil.setTextSize(textView, R.dimen.txt_12);
            textView.setBackgroundResource(R.drawable.rect_sf2_clarge);
            int dpi10 = DensityUtil.dip2px(8);
            int dpi5 = DensityUtil.dip2px(2);
            params.rightMargin = dpi10;
            textView.setPadding(dpi10, dpi5, dpi10, dpi5);
            textView.setLayoutParams(params);
            textView.setText(tag);
            return textView;
        }
    };
}

