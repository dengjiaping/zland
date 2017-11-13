package com.zhisland.android.blog.find.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.HorizontalListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 企业家首页
 * Created by Mr.Tui on 2016/5/17.
 */
public class FragBossHome extends FragBase {

    public static final String PAGE_NAME = "DiscoveryEnterprise";

    public static final String KEY_FAMOUS_DAOLIN = "key_famous_daolin";
    public static final String KEY_NEW_DAOLIN = "key_new_daolin";

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragBossHome.class;
        param.swipeBackEnable = false;
        param.enableBack = true;
        param.title = "企业家";
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @InjectView(R.id.llFamous)
    LinearLayout llFamous;
    @InjectView(R.id.llNew)
    LinearLayout llNew;
    @InjectView(R.id.hlvFamous)
    HorizontalListView hlvFamous;
    @InjectView(R.id.hlvNew)
    HorizontalListView hlvNew;
    @InjectView(R.id.lineBelowFamous)
    View lineBelowFamous;

    private UserAdapter adapterFamous;
    private UserAdapter adapterNew;

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_boss_home, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        initView();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initView() {
        adapterFamous = new UserAdapter(getActivity());
        hlvFamous.setAdapter(adapterFamous);
        hlvFamous.setOnItemClickListener(famousItemClickListener);

        adapterNew = new UserAdapter(getActivity());
        hlvNew.setAdapter(adapterNew);
        hlvNew.setOnItemClickListener(newItemClickListener);

        Object famousDaolin = DBMgr.getMgr().getCacheDao().get(KEY_FAMOUS_DAOLIN);
        if (famousDaolin != null) {
            fillFamousDaolinView((ArrayList<User>) famousDaolin);
        }

        Object newDaolin = DBMgr.getMgr().getCacheDao().get(KEY_NEW_DAOLIN);
        if (newDaolin != null) {
            fillNewDaolinView((ArrayList<User>) newDaolin);
        }
    }

    private void initData() {
        getFamousDaolinData();
        getNewDaolinData();
    }

    private void getFamousDaolinData() {
        ZHApis.getFindApi().getFamousDaolin(getActivity(), new TaskCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> content) {
                DBMgr.getMgr().getCacheDao().set(KEY_FAMOUS_DAOLIN, content);
                fillFamousDaolinView(content);
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    private void getNewDaolinData() {
        ZHApis.getFindApi().getGetNewDaolin(getActivity(), new TaskCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> content) {
                DBMgr.getMgr().getCacheDao().set(KEY_NEW_DAOLIN, content);
                fillNewDaolinView(content);
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    private void fillFamousDaolinView(ArrayList<User> content) {
        adapterFamous.clear();
        if (content != null && content.size() > 0) {
            adapterFamous.addAll(content);
            llFamous.setVisibility(View.VISIBLE);
            lineBelowFamous.setVisibility(View.VISIBLE);
        } else {
            llFamous.setVisibility(View.GONE);
            lineBelowFamous.setVisibility(View.GONE);
        }
        adapterFamous.notifyDataSetChanged();
    }

    private void fillNewDaolinView(ArrayList<User> content) {
        adapterNew.clear();
        if (content != null && content.size() > 0) {
            adapterNew.addAll(content);
            llNew.setVisibility(View.VISIBLE);
        } else {
            llNew.setVisibility(View.GONE);
        }
        adapterNew.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener famousItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            if (adapterFamous.getCount() > position) {
                ActProfileDetail.invoke(getActivity(), adapterFamous.getItem(position).uid);
            }
        }
    };

    AdapterView.OnItemClickListener newItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            if (adapterNew.getCount() > position) {
                ActProfileDetail.invoke(getActivity(), adapterNew.getItem(position).uid);
            }
        }
    };

    @OnClick(R.id.llSearch)
    void onSearchClick() {
        ActSearch.invoke(getActivity(), ActSearch.TYPE_BOSS);
    }

    @OnClick(R.id.llAllBoss)
    void onAllBossClick() {
        ActAllBoss.invoke(getActivity());
    }

    @OnClick(R.id.llNear)
    void onNearClick() {
        FragContactNear.invoke(getActivity());
    }

    private class UserAdapter extends ArrayAdapter<User> {

        private LayoutInflater inflater;

        public UserAdapter(Context context) {
            super(context, R.layout.item_guest);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_guest, parent,
                        false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            User user = getItem(position);
            holder.fill(user);
            return convertView;
        }

    }

    static class Holder {

        @InjectView(R.id.ivGuestAvatar)
        public AvatarView guestavatarView;
        @InjectView(R.id.tvGuestName)
        public TextView tvGuestName;
        @InjectView(R.id.tvGuestComp)
        public TextView tvGuestComp;

        public Holder(View view) {
            ButterKnife.inject(this, view);
        }

        public void fill(User user) {
            if (user != null) {
                tvGuestName.setText(StringUtil.isNullOrEmpty(user.name) ? ""
                        : user.name);
                tvGuestComp
                        .setText((StringUtil.isNullOrEmpty(user.userCompany) ? ""
                                : user.userCompany)
                                + (StringUtil.isNullOrEmpty(user.userPosition) ? ""
                                : user.userPosition));
                guestavatarView.fill(user, true);
            }
        }
    }
}
