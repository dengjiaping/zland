package com.zhisland.android.blog.profile.controller.resource;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 我的资源与需求
 */
public class FragUserResource extends FragBase {

    public static final String INTENT_KEY_USER_DETAIL = "intent_key_user_detail";

    public static void invoke(Context context, UserDetail userDetail) {
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragUserResource.class;
        param.title = "我的资源&需求";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_USER_DETAIL, userDetail);
        context.startActivity(intent);
    }

    private ExpandableListView expdListView;

    private SimpleBlock<Resource> demandblock;

    private SimpleBlock<Resource> supplyblock;

    ResourceAdapter resourceAdapter;

    @InjectView(R.id.ivUCHHead)
    ImageView ivUCHHead;

    @InjectView(R.id.tvUCHDesc)
    TextView tvUCHDesc;

    @InjectView(R.id.tvUCHBtnText)
    TextView tvUCHBtnText;

    @InjectView(R.id.tvUCHBtnTextRight)
    TextView tvUCHBtnTextRight;

    @InjectView(R.id.btnUCHRight)
    LinearLayout btnUCHRight;

    EmptyView emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        expdListView = new ExpandableListView(getActivity());
        expdListView.setGroupIndicator(null);
        expdListView.setDivider(null);
        expdListView.setFastScrollEnabled(false);
        expdListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        expdListView.setBackgroundColor(getResources().getColor(
                R.color.color_bg1));
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        expdListView.setLayoutParams(lp);
        expdListView.setVerticalScrollBarEnabled(false);
        expdListView.setPadding(DensityUtil.dip2px(15), 0,
                DensityUtil.dip2px(15), 0);

        View headerView = LayoutInflater.from(getActivity()).inflate(
                R.layout.user_common_head, null);
        expdListView.addHeaderView(headerView);

        setFootView();

        resourceAdapter = new ResourceAdapter();
        expdListView.setAdapter(resourceAdapter);

        ButterKnife.inject(this, expdListView);
        EventBus.getDefault().register(this);
        initViews();
        return expdListView;
    }

    /**
     * 设置footView，其中emptyView包含在footView中
     */
    private void setFootView() {
        LinearLayout footerView = new LinearLayout(getActivity());
        footerView.setGravity(Gravity.CENTER_HORIZONTAL);
        footerView.setPadding(0, 0, 0, DensityUtil.dip2px(30));
        ListView.LayoutParams lpFooter = new ListView.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(lpFooter);
        emptyView = new EmptyView(getActivity());
        emptyView.setImgRes(R.drawable.img_emptybox);
        emptyView.setPrompt("您还未添加资源&需求");
        emptyView.setBtnVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = DensityUtil.dip2px(80);
        footerView.addView(emptyView, lp);
        expdListView.addFooterView(footerView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Object userDetail = getActivity().getIntent()
                .getSerializableExtra(INTENT_KEY_USER_DETAIL);
        //如果userDetail为空,是从Find模块ActAllRes跳转过来的。
        if (userDetail != null) {
            demandblock = ((UserDetail) userDetail).getDemandBlock();
            supplyblock = ((UserDetail) userDetail).getSupplyBlock();
            resourceAdapter.notifyDataSetChanged();
        } else {
            getUserDetail();
        }
    }

    /**
     * 如果userDetail为空,是从Find模块ActAllRes跳转过来的。拉取UserDetail
     */
    private void getUserDetail() {
        Object obj = DBMgr.getMgr().getCacheDao().get(ActProfileDetail.CACH_USER_DETAIL + PrefUtil.Instance().getUserId());
        if (obj != null && obj instanceof UserDetail) {
            demandblock = ((UserDetail) obj).getDemandBlock();
            supplyblock = ((UserDetail) obj).getSupplyBlock();
            resourceAdapter.notifyDataSetChanged();
        }
        ZHApis.getUserApi().getUserDetail(getActivity(), PrefUtil.Instance().getUserId(), new TaskCallback<UserDetail>() {

            @Override
            public void onSuccess(UserDetail content) {
                demandblock = content.getDemandBlock();
                supplyblock = content.getSupplyBlock();
                resourceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable error) {
            }

        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected String getPageName() {
        return "ProfileSupplyDemandList";
    }

    public void initViews() {
        ivUCHHead.setImageResource(R.drawable.img_profile_resourse);
        SpannableString ss = new SpannableString(
                "您的资源和需求将会与\n5000+高端商务人脉进行匹配推荐");
        ss.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.color_dc)), 11, 16,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvUCHDesc.setText(ss);
        tvUCHBtnText.setText("添加资源");
        btnUCHRight.setVisibility(View.VISIBLE);
        tvUCHBtnTextRight.setText("添加需求");
        expdListView.setOnGroupClickListener(onGroupClickListener);
    }

    OnGroupClickListener onGroupClickListener = new OnGroupClickListener() {

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            return true;
        }
    };

    public void onEventMainThread(EBProfile eb) {
        switch (eb.getType()) {
            case EBProfile.TYPE_CHANGE_SUPPLY:
                getDataFromInternet(Resource.TYPE_SUPPLY);
            case EBProfile.TYPE_CHANGE_DEMAND:
                getDataFromInternet(Resource.TYPE_DEMAND);
                break;
            default:
                break;
        }
    }

    private void getDataFromInternet(final int type) {
        ZHApis.getProfileApi().getResourceList(getActivity(), type,
                new TaskCallback<ArrayList<Resource>>() {

                    @Override
                    public void onSuccess(ArrayList<Resource> content) {
                        SimpleBlock<Resource> block = type == Resource.TYPE_SUPPLY ? supplyblock
                                : demandblock;
                        if (block.data == null) {
                            block.data = new ArrayList<Resource>();
                        } else {
                            block.data.clear();
                        }
                        if (content != null) {
                            block.data.addAll(content);
                        }
                        resourceAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable error) {

                    }
                });
    }

    @OnClick(R.id.btnUCH)
    void addSupplyClick() {
        if (PermissionsMgr.getInstance().canResourceAndDemandPublish()) {
            if (supplyblock != null && supplyblock.maxCount != null
                    && supplyblock.data != null
                    && supplyblock.maxCount <= supplyblock.data.size()) {
                ToastUtil.showShort("您现在只能发布" + supplyblock.maxCount + "条资源");
                return;
            }
            FragSupplyEdit.invoke(getActivity(), null);
        } else {
            DialogUtil.showPermissionsDialog(getActivity(), getPageName());
        }
    }

    @OnClick(R.id.btnUCHRight)
    void addDemandClick() {
        if (PermissionsMgr.getInstance().canResourceAndDemandPublish()) {
            if (demandblock != null && demandblock.maxCount != null
                    && demandblock.data != null
                    && demandblock.maxCount <= demandblock.data.size()) {
                ToastUtil.showShort("您现在只能发布" + demandblock.maxCount + "条需求");
                return;
            }
            FragDemandEdit.invoke(getActivity(), null);
        } else {
            DialogUtil.showPermissionsDialog(getActivity(), getPageName());
        }
    }

    public void expandAll() {
        int groupCount = resourceAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expdListView.expandGroup(i);
        }
    }

    private class ResourceAdapter extends BaseExpandableListAdapter {

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (getGroupCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
            expandAll();
        }

        @Override
        public Resource getChild(int groupPosition, int childPosition) {
            if (groupPosition == 0) {
                if (supplyblock == null || supplyblock.data == null
                        || supplyblock.data.size() == 0) {
                    return demandblock.data.get(childPosition);
                } else {
                    return supplyblock.data.get(childPosition);
                }
            }
            return demandblock.data.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            UserResourceCell holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_resource, null);
                holder = new UserResourceCell(convertView, getActivity());
                convertView.setTag(holder);
            } else {
                holder = (UserResourceCell) convertView.getTag();
            }

            holder.fill(getChild(groupPosition, childPosition), true);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition == 0) {
                if (supplyblock == null || supplyblock.data == null
                        || supplyblock.data.size() == 0) {
                    return demandblock.data.size();
                } else {
                    return supplyblock.data.size();
                }
            }
            return demandblock.data.size();
        }

        @Override
        public SimpleBlock<Resource> getGroup(int groupPosition) {
            if (groupPosition == 0) {
                if (supplyblock == null || supplyblock.data == null
                        || supplyblock.data.size() == 0) {
                    return demandblock;
                } else {
                    return supplyblock;
                }
            }
            return demandblock;
        }

        @Override
        public int getGroupCount() {
            int count = 2;
            if (demandblock == null || demandblock.data == null
                    || demandblock.data.size() == 0) {
                count--;
            }
            if (supplyblock == null || supplyblock.data == null
                    || supplyblock.data.size() == 0) {
                count--;
            }
            return count;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(getActivity());
                ((TextView) convertView).setTextColor(getResources().getColor(
                        R.color.color_f2));
                ((TextView) convertView).setTextSize(16);
                convertView.setPadding(0, DensityUtil.dip2px(15), 0, 0);
            }
            SimpleBlock<Resource> SimpleBlock = getGroup(groupPosition);
            if (SimpleBlock.type != null) {
                if (SimpleBlock.type == SimpleBlock.TYPE_SUPPLY) {
                    ((TextView) convertView).setText("我的资源("
                            + SimpleBlock.data.size() + "/"
                            + SimpleBlock.maxCount + ")");
                } else {
                    ((TextView) convertView).setText("我的需求("
                            + SimpleBlock.data.size() + "/"
                            + SimpleBlock.maxCount + ")");
                }
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }

}