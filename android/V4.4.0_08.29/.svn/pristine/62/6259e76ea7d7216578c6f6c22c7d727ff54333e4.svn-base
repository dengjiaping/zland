package com.zhisland.android.blog.aa.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.view.CustomListView;
import com.zhisland.lib.component.application.EnvType;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 修改环境
 */
public class FragModifyEnv extends FragBase {

    /**
     * 自定义环境host
     */
    public static final String CUSTOM_HOST = "custom_host";

    @InjectView(R.id.lvEnv)
    CustomListView lvEnv;
    @InjectView(R.id.etHost)
    EditText etHost;

    /**
     * 环境 datas
     */
    private List<String> envs;

    /**
     * 选择的环境
     */
    private int selEnv = Config.ENV_TYPE - 1;
    private EnvAdapter adapter;

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragModifyEnv.class;
        param.enableBack = true;
        param.title = "修改环境";
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_modify_env, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        etHost.setText(PrefUtil.Instance().getByKey(CUSTOM_HOST, ""));
        adapter = new EnvAdapter();
        lvEnv.setAdapter(adapter);
        lvEnv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selEnv = position;
                adapter.notifyDataSetChanged();
                if (isCustomEnv()) {
                    etHost.setVisibility(View.VISIBLE);
                } else {
                    etHost.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        envs = new ArrayList<>();
        envs.add("开发环境");
        envs.add("测试环境");
        envs.add("预发布环境");
        envs.add("线上环境");
        envs.add("自定义环境");
    }

    @OnClick(R.id.btnModifyEnv)
    public void onClickBtnModifyEnv() {
        if (isCustomEnv()) {
            String host = etHost.getText().toString().trim();
            if (StringUtil.isNullOrEmpty(host)) {
                ToastUtil.showShort("host不能为空。。。");
                return;
            }
            // 修改host
            PrefUtil.Instance().setKeyValue(CUSTOM_HOST, host);
        }
        Config.ENV_TYPE = selEnv + 1;
        ZhislandApplication.APP_CONFIG.setEnvType(Config.ENV_TYPE);
        ZhislandApplication.configXMPP();
        ToastUtil.showShort("修改环境为" + envs.get(selEnv));
        getActivity().finish();
    }

    /**
     * 是否是自定义环境
     */
    private boolean isCustomEnv() {
        return (selEnv + 1) == EnvType.ENV_CUSTOM;
    }

    @Override
    public String getPageName() {
        return "FragModifyEnv";
    }

    class EnvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return envs.size();
        }

        @Override
        public String getItem(int position) {
            return envs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_env, parent, false);
            TextView tvEnv = (TextView) view.findViewById(R.id.tvEnv);
            ImageView ivEnv = (ImageView) view.findViewById(R.id.ivEnv);
            tvEnv.setText(getItem(position));
            if (position == selEnv) {
                ivEnv.setVisibility(View.VISIBLE);
            } else {
                ivEnv.setVisibility(View.INVISIBLE);
            }
            if (isCustomEnv()){
                etHost.setVisibility(View.VISIBLE);
            }
            return view;
        }

    }
}
