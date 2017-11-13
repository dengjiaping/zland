package com.zhisland.android.blog.find.controller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.DefaultTitleBarClickListener;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.view.RedDotView;
import com.zhisland.android.blog.common.webview.FragWebViewActivity;
import com.zhisland.android.blog.contacts.controller.ActCard;
import com.zhisland.android.blog.contacts.eb.EBHasNewInvite;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventTitleClick;
import com.zhisland.android.blog.tabhome.View.TitleFreshTaskView;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.im.data.IMContact;
import com.zhisland.im.event.EventRelation;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 发现tab
 */
public class FragFindTab extends FragBase {

    private static final int TAG_LEFT_BTN = 111;
    @InjectView(R.id.tvRequestNum)
    RedDotView tvRequestNum;

    @InjectView(R.id.tvContactFCount)
    TextView tvContactFCount;

    @InjectView(R.id.llThreeItem)
    LinearLayout llThreeItem;

    ItemHolder holdBoss;
    ItemHolder holdResource;
    ItemHolder holdOrg;

    private TitleBarProxy titleBar;
    private TitleFreshTaskView titleFreshTaskView;

    private int lineWidth = DensityUtil.dip2px(0.7f);

    /**
     * 企业家、资源需求、企业家组织，三个item的高度。
     */
    private int itemHeight = (DensityUtil.getWidth() - lineWidth * 2) / 3;
    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemHeight, itemHeight);

    public static final int TYPE_BOSS = 0;
    public static final int TYPE_RESOURCE = 1;
    public static final int TYPE_ORG = 2;

    @Override
    protected String getPageName() {
        return "DiscoveryHome";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout root = (LinearLayout) inflater.inflate(
                R.layout.frag_tab_item, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        View content = inflater.inflate(R.layout.frag_find_tab, null);
        root.addView(content, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        titleBar = new TitleBarProxy();
        titleBar.configTitle(root, TitleType.TITLE_LAYOUT, new DefaultTitleBarClickListener(getActivity()) {
            @Override
            public void onTitleClicked(View view, int tagId) {
                switch (tagId) {
                    case TAG_LEFT_BTN:
                        //跳转任务列表
                        BusFreshTask.Bus().post(new EventTitleClick());
                        break;
                }
            }
        });
        titleBar.setTitle("发现");
        titleFreshTaskView = new TitleFreshTaskView(getActivity());
        titleFreshTaskView.addLeftTitle(titleBar, TAG_LEFT_BTN);

        ButterKnife.inject(this, root);
        EventBus.getDefault().register(this);
        initView();
        return root;
    }

    private void initView() {
        initThreeItem();
    }

    /**
     * 初始化企业家，资源需求，企业家组织三个View
     */
    private void initThreeItem() {
        llThreeItem.removeAllViews();
        holdBoss = new ItemHolder(TYPE_BOSS);
        holdResource = new ItemHolder(TYPE_RESOURCE);
        holdOrg = new ItemHolder(TYPE_ORG);

        holdBoss.fillView(R.drawable.sel_btn_boss, "企业家");
        holdResource.fillView(R.drawable.sel_btn_resource, "资源及需求");
        holdOrg.fillView(R.drawable.sel_btn_org, "企业家组织");

        holdBoss.addToParent(llThreeItem);
        addLine();
        holdResource.addToParent(llThreeItem);
        addLine();
        holdOrg.addToParent(llThreeItem);
    }

    /**
     * 添加三个item之间的竖线
     */
    private void addLine() {
        View vline = new View(getActivity());
        vline.setBackgroundColor(getResources().getColor(R.color.color_div));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(lineWidth, itemHeight);
        llThreeItem.addView(vline, lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        handler.removeCallbacks(refeshRequestCount);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataToView();
    }

    private void setDataToView() {
        setRequestCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        setContactFCount();
    }

    /**
     * 设置可能认识的人数量显示
     */
    private void setContactFCount() {
        long count = PrefUtil.Instance().getContactFriendCount(PrefUtil.Instance().getUserId());
        if (count > 0) {
            tvContactFCount.setText("" + count);
        } else {
            tvContactFCount.setText("");
        }
    }

    public void onEventMainThread(EventRelation eventRelation) {
        switch (eventRelation.action) {
            case EventRelation.REQUEST_ACCEPTED:
            case EventRelation.REQUEST_IGNORE:
            case EventRelation.RECEIVE_ACCEPTED:
                handler.removeCallbacks(refeshRequestCount);
                handler.postDelayed(refeshRequestCount, 200);
                break;
        }
    }

    public void onEventMainThread(EBHasNewInvite eb) {
        handler.removeCallbacks(refeshRequestCount);
        handler.postDelayed(refeshRequestCount, 200);
    }

    /**
     * 刷新好友请求数量显示Runnable
     */
    Runnable refeshRequestCount = new Runnable() {

        @Override
        public void run() {
            setRequestCount();
        }
    };

    /**
     * 设置好友请求数量显示
     */
    private void setRequestCount() {
        List<IMContact> contactList = com.zhisland.im.data.DBMgr.getHelper()
                .getContactDao().getRequestContact();
        int count = 0;

        if (contactList != null && contactList.size() > 0) {
            for (IMContact contact : contactList) {
                long uid = contact.getUserId();
                if (uid > 0) {
                    User user = DBMgr.getMgr().getUserDao().getUserById(uid);
                    if (user != null) {
                        count++;
                    }
                }
            }
        }
        if (count > 0) {
            tvRequestNum.setText(String.valueOf(count));
            tvRequestNum.setVisibility(View.VISIBLE);
        } else {
            tvRequestNum.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.llRequest)
    void requestClick() {
        ActCard.invoke(getActivity(), ActCard.FROM_ADD_FRIEND);
    }

    @OnClick(R.id.llContactF)
    void ContactFClick() {
        ActContactFriend.invoke(getActivity());
    }

    @OnClick(R.id.llSearch)
    void searchClick() {
        ActSearch.invoke(getActivity(), ActSearch.TYPE_ALL);
    }

    /**
     * 友盟上报 PageStart
     */
    public void pageStart() {
        ZhislandApplication.trackerPageStart(getPageName());
    }

    /**
     * 友盟上报 PageEnd
     */
    public void pageEnd() {
        ZhislandApplication.trackerPageEnd(getPageName());
    }

    /**
     * 刷新新手任务title
     */
    public void refreshTitleFreshTask(boolean showFreshTask, boolean showRedDot) {
        if (titleFreshTaskView != null) {
            titleFreshTaskView.refreshTitleFreshTask(showFreshTask, showRedDot);
        }
    }

    /**
     * 企业家、资源需求、企业家组织三个Item的holder
     */
    class ItemHolder {

        LinearLayout llItem;

        ImageView ivIcon;

        TextView tvName;

        int type;

        public ItemHolder(int type) {
            this.type = type;

            llItem = new LinearLayout(getActivity());
            llItem.setOrientation(LinearLayout.VERTICAL);
            llItem.setLayoutParams(lp);
            llItem.setGravity(Gravity.CENTER);

            ivIcon = new ImageView(getActivity());
            llItem.addView(ivIcon);

            tvName = new TextView(getActivity());
            tvName.setTextColor(getResources().getColor(R.color.color_f2));
            tvName.setGravity(Gravity.CENTER);
            tvName.setTextSize(12);
            tvName.setPadding(0, DensityUtil.dip2px(10), 0, 0);
            llItem.addView(tvName);
            llItem.setOnClickListener(itemClick);
        }

        View.OnClickListener itemClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case TYPE_BOSS:
                        FragBossHome.invoke(getActivity());
                        break;
                    case TYPE_RESOURCE:
                        if (PermissionsMgr.getInstance().canJumpAllResources()) {
                            ActAllRes.invoke(getActivity());
                        } else {
                            DialogUtil.showPermissionsDialog(getActivity(), getPageName());
                        }
                        break;
                    case TYPE_ORG:
                        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_ORGANIZATION);
                        FragWebViewActivity.launch(getActivity(), Config.getOrgUrl(), "岛邻组织",
                                Config.getOrgDomain(),
                                "uid=" + PrefUtil.Instance().getUserId(), true, false);
                        break;
                }
            }
        };

        /**
         * 将该item的View添加到父控件中
         */
        public void addToParent(LinearLayout llParent) {
            if (llParent != null && llItem.getParent() == null) {
                llParent.addView(llItem);
            }
        }

        public void fillView(int resId, String name) {
            ivIcon.setImageResource(resId);
            tvName.setText(name);
        }

    }

    @Override
    protected boolean reportUMOnCreateAndOnDestory() {
        return false;
    }
}
