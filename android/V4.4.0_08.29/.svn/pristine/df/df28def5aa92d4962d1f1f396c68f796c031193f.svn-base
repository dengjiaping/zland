package com.zhisland.android.blog.contacts.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.invitation.view.impl.FragSearchInvite;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 可批准的好友界面
 */
public class FragApproveableGuest extends FragBase {

    @InjectView(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    TextView tvApprovedReqNum;
    TextView tvViewAll;
    ViewPager viewPager;
    TextView tvApproveable;
    LinearLayout laySearch;

    private RefreshListAdapter refreshListAdapter;
    private CardPagerAdapter cardPagerAdapter;

    private View mListHeaderView;

    public FragApproveableGuest() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_approveable_guest, container, false);
        mListHeaderView = inflater.inflate(R.layout.frag_approved_req_guest, null);
        ButterKnife.inject(this, view);
        //initHeaderView();
        updateView();
        return view;
    }

    private void initHeaderView() {
        TextView tvApprovedReqNum = (TextView) mListHeaderView.findViewById(R.id.tvApprovedReqNum);
        TextView tvViewAll = (TextView) mListHeaderView.findViewById(R.id.tvViewAll);
        ViewPager viewPager = (ViewPager) mListHeaderView.findViewById(R.id.viewPager);
        TextView tvApproveable = (TextView) mListHeaderView.findViewById(R.id.tvApproveable);
        LinearLayout laySearch = (LinearLayout) mListHeaderView.findViewById(R.id.laySearch);
    }


    public void updateView() {
        refreshListAdapter = new RefreshListAdapter(getActivity());
        pullToRefreshListView.setAdapter(refreshListAdapter);
        //setupHeaderView();

    }

    private void setupHeaderView() {
        pullToRefreshListView.getRefreshableView().addHeaderView(mListHeaderView);
        setupViewPager();
    }

    private void setupViewPager() {
        cardPagerAdapter = new CardPagerAdapter(getActivity());
        viewPager.setAdapter(cardPagerAdapter);
    }

    public void searchApproveableFriends() {
        FragSearchInvite.invoke(getActivity());
    }


    @Override
    public String getPageName() {
        return "FragApproveableGuest";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}


class CardPagerAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    Context mContext;
    View cardView;

    public CardPagerAdapter(Context context) {
        mContext = context;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cardView = layoutInflater.inflate(R.layout.item_approved_req, null);
    }

    @Override
    public int getCount() {

        return 5;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(cardView);
        return cardView;
    }


}

class RefreshListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context mContext;

    public RefreshListAdapter(Context context) {
        mContext = context;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_approveable, null);
        return convertView;
    }

}


