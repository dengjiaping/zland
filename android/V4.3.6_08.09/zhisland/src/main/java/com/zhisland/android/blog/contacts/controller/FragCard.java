package com.zhisland.android.blog.contacts.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.beem.project.beem.service.aidl.IXmppFacade;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.TabHome;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.contacts.api.TaskProcessRecommend;
import com.zhisland.android.blog.common.view.flingswipe.SwipeFlingAdapterView;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.TimeUtil;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.tabhome.eb.EBTabHome;
import com.zhisland.im.BeemApplication;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.MLog;

import de.greenrobot.event.EventBus;

public class FragCard extends FragBase {

	private static final int MAX_SIZE = 5;
	private static final int REQ_CODE = 12;

	private ArrayList<User> users;
	private UserCardAdapter adapter;

	@InjectView(R.id.flUserCardRoot)
	public FrameLayout flUserCardRoot;

	@InjectView(R.id.emptyView)
	public EmptyView emptyView;

	@InjectView(R.id.llCard)
	public LinearLayout llCard;

	@InjectView(R.id.frame)
	SwipeFlingAdapterView flingContainer;

	@InjectView(R.id.tvExchangeCard)
	TextView tvExchangeCard;

	private int from;

	private User userSelf;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.act_user_card, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initViews();
		return root;
	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		from = getActivity().getIntent().getExtras().getInt(ActCard.INK_FROM);
		userSelf = DBMgr.getMgr().getUserDao().getSelfUser();
		switch (from) {
		case ActCard.FROM_BOOT_CARD:
			ArrayList<User> tmpUsers = new ArrayList<User>();

			List<IMContact> imContacts = com.zhisland.im.data.DBMgr
					.getHelper(getActivity()).getContactDao()
					.getFriendRequests();
			if (imContacts != null) {
				for (IMContact ic : imContacts) {
					User user = DBMgr.getMgr().getUserDao()
							.getUserById(ic.getUserId());
					if (user != null) {
						tmpUsers.add(user);
						if (tmpUsers.size() == MAX_SIZE) {
							break;
						}
					}
				}
			}
			if (tmpUsers.size() < MAX_SIZE) {
				ArrayList<User> recommends = (ArrayList<User>) DBMgr.getMgr()
						.getCacheDao().get(TabHome.KEY_CACH_RECOMMEND_USER);
				if (recommends != null) {
					for (User user : recommends) {
						tmpUsers.add(user);
						if (tmpUsers.size() == MAX_SIZE) {
							break;
						}
					}
				}
			}

			users = tmpUsers;

			flUserCardRoot.setBackgroundResource(Color.TRANSPARENT);
			break;
		case ActCard.FROM_RECOMMEND:
			users = (ArrayList<User>) DBMgr.getMgr().getCacheDao()
					.get(TabHome.KEY_CACH_RECOMMEND_USER);
			flUserCardRoot.setBackgroundColor(getResources().getColor(
					R.color.color_bg2));
			break;
		case ActCard.FROM_ADD_FRIEND:
			flUserCardRoot.setBackgroundColor(getResources().getColor(
					R.color.color_bg2));
			// get add friends
			List<IMContact> contacts = com.zhisland.im.data.DBMgr.getHelper()
					.getContactDao().getFriendRequests();
			if (contacts != null && contacts.size() > 0) {
				users = new ArrayList<User>(contacts.size());
				for (IMContact ic : contacts) {
					User user = DBMgr.getMgr().getUserDao()
							.getUserById(ic.getUserId());
					if (user != null) {
						users.add(user);
					}
				}
			}
			break;
		}

		if (users != null && users.size() > 0) {
			showContentView();
		} else {
			showEmptyView();
			User self = DBMgr.getMgr().getUserDao()
					.getUserById(PrefUtil.Instance().getUserId());
			if (from == ActCard.FROM_RECOMMEND && self.isVip()) {
				getRecommendUserTask();
			}
		}
	}

	private void getRecommendUserTask() {
		final long curTime = System.currentTimeMillis();
		long oldTime = PrefUtil.Instance().getLastRecommendDate();
		if (oldTime > 0) {
			boolean sameDay = TimeUtil.isSameDay(curTime, oldTime);
			if (sameDay) {
				return;
			}
		}
		ZHApis.getUserApi().getRecommendUser(getActivity(),
				new TaskCallback<ArrayList<User>>() {

					@Override
					public void onSuccess(ArrayList<User> content) {
						if (content != null && content.size() > 0 && getActivity() != null) {
							for (User user : content) {
								user.isRecommend = true;
							}
							FragCard.this.users = content;
							showContentView();
							DBMgr.getMgr()
									.getCacheDao()
									.set(TabHome.KEY_CACH_RECOMMEND_USER,
											content);
                            EventBus.getDefault().post(
                                    new EBTabHome(EBTabHome.TYPE_TAB_RED_DOT, TabHome.TAB_ID_Find));
							PrefUtil.Instance().setLastRecommendDate(curTime);
						}
					}

					@Override
					public void onFailure(Throwable error) {
					}
				});
	}

	private void showContentView() {
		llCard.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);

		((ActCard) getActivity()).showTitleCount(users.size());

		refreshExchageText();

		adapter = new UserCardAdapter(users, getActivity());
		flingContainer.setAdapter(adapter);
		flingContainer
				.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
					@Override
					public void removeFirstObjectInAdapter() {
						if (users.size() > 0) {
							users.remove(0);
							adapter.notifyDataSetChanged();
							if (users.size() == 0) {
								showEmptyView();
							} else {
								refreshExchageText();
							}
							if (getActivity() != null) {
								((ActCard) getActivity()).showTitleCount(users
										.size());
							}
						}
					}

					@Override
					public void onLeftCardExit(Object dataObject) {// 忽略
						final User user = (User) dataObject;
						if (user.isRecommend != null) {
							ingoreRecommendFriend(user);
						} else {
							refuseFriendRequest(user);
						}
					}

					@Override
					public void onRightCardExit(Object dataObject) {// 交换卡片
						User user = (User) dataObject;
						if (user.isRecommend != null) {
							addRecommendFriend(user);
						} else {
							agreeFriendRequest(user);
						}
					}

					@Override
					public void onAdapterAboutToEmpty(int itemsInAdapter) {

					}

					@Override
					public void onScroll(float scrollProgressPercent) {
						Log.e("zhapp", scrollProgressPercent + "");
						View view = flingContainer.getSelectedView();
						if (view != null) {
							view.findViewById(R.id.item_swipe_left_indicator)
									.setAlpha(
											scrollProgressPercent < 0 ? -scrollProgressPercent
													: 0);
							view.findViewById(R.id.item_swipe_right_indicator)
									.setAlpha(
											scrollProgressPercent > 0 ? scrollProgressPercent
													: 0);
						}
					}

					@Override
					public void onLeftExitAnim() {
						View view = flingContainer.getSelectedView();
						if (view != null) {
							view.findViewById(R.id.item_swipe_left_indicator)
									.setAlpha(1);
						}
					}

					@Override
					public void onRightExitAnim() {
						View view = flingContainer.getSelectedView();
						if (view != null) {
							view.findViewById(R.id.item_swipe_right_indicator)
									.setAlpha(1);
						}
					}
				});

		flingContainer
				.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
					@Override
					public void onItemClicked(int itemPosition,
							Object dataObject) {
						User user = (User) dataObject;
						ActProfileDetail.invoke(getActivity(), user.uid,
								REQ_CODE);
					}
				});
	}

	private void refreshExchageText() {
		if (users.size() > 0) {
			if (users.get(0).isRecommend != null) {
				tvExchangeCard.setVisibility(View.GONE);
			} else {
				tvExchangeCard.setVisibility(View.VISIBLE);
			}
		}
	}

	// 同意好友请求
	private void agreeFriendRequest(User user) {
		IMUtil.acceptFriend(user.uid);
	}

	// 拒绝好友请求
	private void refuseFriendRequest(final User user) {
		ZHApis.getUserApi().ignoreRequest(null, new TaskCallback<Object>() {

			@Override
			public void onSuccess(Object content) {
				try {
					IXmppFacade xmppFacade = BeemApplication.getInstance()
							.getXmppFacade();
					if (xmppFacade != null) {
						xmppFacade.ignoreInvite(IMContact.buildJid(user.uid));
					}
				} catch (RemoteException e) {
					MLog.e(getTag(), e.getMessage(), e);
				}
			}

			@Override
			public void onFailure(Throwable error) {
			}
		}, user.uid);

	}

	// 加推荐的好友
	private void addRecommendFriend(User user) {
		@SuppressWarnings("unchecked")
		ArrayList<User> recommends = (ArrayList<User>) DBMgr.getMgr()
				.getCacheDao().get(TabHome.KEY_CACH_RECOMMEND_USER);
		Iterator<User> iterator = recommends.iterator();
		while (iterator.hasNext()) {
			User next = iterator.next();
			if (user.uid == next.uid) {
				iterator.remove();
				break;
			}
		}

		if (PermissionsMgr.getInstance().canAddFriend(user)) {
			boolean addFriend = IMUtil.addFriend(user.uid);
			if (addFriend) {
				DBMgr.getMgr().getCacheDao()
						.set(TabHome.KEY_CACH_RECOMMEND_USER, recommends);
				ZHApis.getUserApi().processRecommend(null, user.uid,
						TaskProcessRecommend.AGREE_RECOMMEND, null);
			}
		} else {
			DialogUtil.showPermissionsDialog(getActivity(), getPageName());
		}
	}

	// 忽略推荐的好友
	private void ingoreRecommendFriend(User user) {
		@SuppressWarnings("unchecked")
		ArrayList<User> recommends = (ArrayList<User>) DBMgr.getMgr()
				.getCacheDao().get(TabHome.KEY_CACH_RECOMMEND_USER);
		Iterator<User> iterator = recommends.iterator();
		while (iterator.hasNext()) {
			User next = iterator.next();
			if (user.uid == next.uid) {
				iterator.remove();
				break;
			}
		}
		ZHApis.getUserApi().processRecommend(null, user.uid,
				TaskProcessRecommend.INGORE_RECOMMEND, null);
		DBMgr.getMgr().getCacheDao()
				.set(TabHome.KEY_CACH_RECOMMEND_USER, recommends);
	}

	private void showEmptyView() {
		switch (from) {
		case ActCard.FROM_BOOT_CARD:
			getActivity().finish();
			break;
		case ActCard.FROM_RECOMMEND:
			llCard.setVisibility(View.GONE);
			emptyView.setPrompt("暂时没有推荐人脉");
			emptyView.setImgRes(R.drawable.img_friends_empty);
			emptyView.setVisibility(View.VISIBLE);
			break;
		case ActCard.FROM_ADD_FRIEND:
			llCard.setVisibility(View.GONE);
			emptyView.setPrompt("暂未收到好友请求");
			emptyView.setImgRes(R.drawable.img_friends_empty);
			emptyView.setVisibility(View.VISIBLE);
			break;
		}
	}

	@OnClick(R.id.right)
	public void right() {
		flingContainer.getTopCardListener().selectRight();
	}

	@OnClick(R.id.left)
	public void left() {
		flingContainer.getTopCardListener().selectLeft();
	}

	private class UserCardAdapter extends BaseAdapter {

		private List<User> users;
		private Context context;

		public UserCardAdapter(List<User> users, Context context) {
			this.users = users;
			this.context = context;
		}

		@Override
		public int getCount() {
			return users.size();
		}

		@Override
		public User getItem(int position) {
			return users.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			CardViewHolder viewHolder;
			if (view == null) {
				view = LayoutInflater.from(context).inflate(
						R.layout.item_user_card, parent, false);
				viewHolder = new CardViewHolder(context, view);
				view.setTag(viewHolder);
			} else {
				viewHolder = (CardViewHolder) view.getTag();
			}
			viewHolder.fill(getItem(position));
			return view;
		}

	}

	@Override
	protected String getPageName() {
		return "DiscoveryFriendRequest";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQ_CODE) {
				right();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
