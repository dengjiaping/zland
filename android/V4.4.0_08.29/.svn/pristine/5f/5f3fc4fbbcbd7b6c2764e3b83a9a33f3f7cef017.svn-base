package com.zhisland.android.blog.message.view.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.android.blog.message.model.ISystemMessageModel;
import com.zhisland.android.blog.message.model.impl.SystemMessageModel;
import com.zhisland.android.blog.message.presenter.FansMessagePresenter;
import com.zhisland.android.blog.message.presenter.SystemMessagePresenter;
import com.zhisland.android.blog.message.view.ISystemMessageView;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;
import com.zhisland.lib.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by arthur on 2016/9/13.
 */
public class FragSystemMessageList extends FragPullListMvps<Message> implements ISystemMessageView {

    private SystemMessagePresenter systemMessagePresenter;

    /**
     * 调起新增粉丝列表页
     *
     * @param context
     */
    public static void Invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragSystemMessageList.class;
        param.enableBack = true;
        param.title = "系统消息";
        CommonFragActivity.invoke(context, param);
    }

    //region 生命周期
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().setAdapter(new MessageAdapter());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().setBackgroundResource(R.color.color_bg2);
    }

    //endregion

    //region 重载的方法
    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenterMap = new HashMap<>(1);

        systemMessagePresenter = new SystemMessagePresenter();
        ISystemMessageModel model = new SystemMessageModel();
        systemMessagePresenter.setModel(model);

        presenterMap.put(FansMessagePresenter.class.getName(), systemMessagePresenter);

        return presenterMap;
    }

    @Override
    public void loadNormal() {
        systemMessagePresenter.onLoadNormal();
    }

    @Override
    public void loadMore(String nextId) {
        systemMessagePresenter.onLoadMore(nextId);
    }
    //endregion

    class MessageAdapter extends BaseListAdapter<Message> {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_system_message, null);

                MsgHolder msgHolder = new MsgHolder(convertView);
                convertView.setTag(msgHolder);
            }
            MsgHolder msgHolder = (MsgHolder) convertView.getTag();
            msgHolder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }


    }

    class MsgHolder {

        @InjectView(R.id.ivSysMsgIcon)
        ImageView ivSysMsgIcon;
        @InjectView(R.id.tvSysMsgTime)
        TextView tvSysMsgTime;
        @InjectView(R.id.tvSysMsgContent)
        TextView tvSysMsgContent;

        private Message curItem;

        MsgHolder(View view) {
            ButterKnife.inject(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    systemMessagePresenter.onItemClicked(curItem);
                }
            });
        }

        public void fill(Message item) {
            this.curItem = item;
            tvSysMsgContent.setText(item.content);
            tvSysMsgTime.setText(StringUtil.convertFrom(item.time * 1000));
        }
    }
}