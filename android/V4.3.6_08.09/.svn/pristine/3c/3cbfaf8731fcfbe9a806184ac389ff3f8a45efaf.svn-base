package com.zhisland.android.blog.find.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.contacts.dto.Loc;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.setting.eb.EBSetting;
import com.zhisland.android.blog.im.controller.ActChat;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.setting.controller.FragSetVisiable;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import de.greenrobot.event.EventBus;

/**
 * 附近的人页面
 * */
public class FragContactNear extends FragPullList<User> {

	private static final String PAGE_NAME = "DiscoveryNearbyPeople";

	private int invisible = User.VALUE_INVISIBLE;

	private CommonDialog commonDialog;

	private boolean isDestroy = true;

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}

	public static void invoke(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragContactNear.class;
		param.enableBack = true;
		param.title = "附近的人";
		Intent intent = CommonFragActivity.createIntent(context, param);
		context.startActivity(intent);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		EventBus.getDefault().register(this);
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if (user != null && user.invisible != null) {
			invisible = user.invisible;
		}
		getPullProxy().setAdapter(new UserAdapter());
		if (invisible == User.VALUE_VISIBLE) {
			getPullProxy().setRefreshKey(PAGE_NAME);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getPullProxy().getPullView().setBackgroundColor(
				getResources().getColor(R.color.color_bg2));

		internalView.setFastScrollEnabled(false);
		internalView.setDividerHeight(0);
		internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		internalView.setHeaderDividersEnabled(false);
		internalView.setFooterDividersEnabled(false);
		internalView.setBackgroundColor(getResources().getColor(
				R.color.color_bg2));

		EmptyView ev = new EmptyView(getActivity());
		ev.setImgRes(R.drawable.img_friends_empty);
		ev.setPrompt("暂时没有附近的人");
		ev.setBtnVisibility(View.INVISIBLE);
		getPullProxy().getPullView().setEmptyView(ev);
	}

	@Override
	public void loadNormal() {
		if (invisible == User.VALUE_INVISIBLE) {
			if (commonDialog == null) {
				commonDialog = new CommonDialog(getActivity());
				commonDialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						if (invisible != User.VALUE_VISIBLE) {
							getActivity().finish();
						}
					}
				});
			}
			commonDialog.show();
			commonDialog.setContent("打开隐私设置，去看看附近有哪些重要的人脉");
			commonDialog.setLeftBtnContent("取消");
			commonDialog.setRightBtnContent("设置");
			commonDialog.setRightBtnColor(getResources().getColor(
					R.color.color_dc));
			commonDialog.tvDlgConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FragSetVisiable.invoke(getActivity());
				}
			});
			commonDialog.tvDlgCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			});
			getPullProxy().onRefreshFinished();
		} else {
			getLocation();
			// getDataFromInternet(null);
		}
	}

	private LocationManagerProxy mLocationManagerProxy;

	private void getLocation() {
		if (mLocationManagerProxy == null) {
			mLocationManagerProxy = LocationManagerProxy
					.getInstance(getActivity());
		}

		if (mLocationManagerProxy == null) {
			ToastUtil.showShort("定位失败。");
			getPullProxy().onRefreshFinished();
			return;
		}

		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 5,
				new AMapLocationListener() {

					@Override
					public void onStatusChanged(String arg0, int arg1,
							Bundle arg2) {

					}

					@Override
					public void onProviderEnabled(String arg0) {

					}

					@Override
					public void onProviderDisabled(String arg0) {

					}

					@Override
					public void onLocationChanged(Location arg0) {

					}

					@Override
					public void onLocationChanged(AMapLocation amapLocation) {
						if (amapLocation != null
								&& amapLocation.getAMapException()
										.getErrorCode() == 0) {
							// 获取位置信息
							Double geoLat = amapLocation.getLatitude();
							Double geoLng = amapLocation.getLongitude();
							MLog.d(PAGE_NAME, geoLat + " " + geoLng + " "
									+ amapLocation.getAccuracy());

							final Loc loc = new Loc();
							loc.alt = amapLocation.getAltitude();
							loc.lat = amapLocation.getLatitude();
							loc.lon = amapLocation.getLongitude();
							loc.horAccuracy = amapLocation.getAccuracy();
							loc.verAccuracy = amapLocation.getAccuracy();
							ZhislandApplication.LATEST_LOC = loc;
							PrefUtil.Instance().setLastLoc(
									GsonHelper.GetCommonGson().toJson(loc));
							getDataFromInternet(null);
						} else {
							ToastUtil.showShort("定位失败。");
							getPullProxy().onRefreshFinished();
						}
					}
				});

		mLocationManagerProxy.setGpsEnable(true);
	}

	@Override
	public void loadMore(String nextId) {
		super.loadMore(nextId);
		getDataFromInternet(nextId);
	}

	private void getDataFromInternet(String maxId) {
		ZHApis.getUserApi().getUsersNearBy(getActivity(), PrefUtil.Instance().getLastLoc(),
				maxId, new TaskCallback<ZHPageData<User>>() {

					@Override
					public void onSuccess(ZHPageData<User> content) {
						getPullProxy().onLoadSucessfully(content);
					}

					@Override
					public void onFailure(Throwable error) {
						getPullProxy().onLoadFailed(error);
					}
				});
	}

	@Override
	public void onDestroy() {
		isDestroy = true;
		super.onDestroy();
	}

	@Override
	public void onResume() {
		isDestroy = false;
		super.onResume();
	}

	public void onEventMainThread(EBSetting eb) {
		if (getActivity() == null || isDestroy) {
			return;
		}
		switch (eb.getType()) {
		case EBSetting.TYPE_VISIBLE_SETTING:
			User user = DBMgr.getMgr().getUserDao().getSelfUser();
			if (user != null) {
				invisible = user.invisible;
			}
			if (invisible == User.VALUE_VISIBLE) {
				getPullProxy().setRefreshKey(PAGE_NAME);
				getPullProxy().pullDownToRefresh(true);
			}
			if (commonDialog != null) {
				commonDialog.dismiss();
			}
			break;
		}
	}

	class UserAdapter extends BaseListAdapter<User> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			UserHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.item_near_user, null);
				holder = new UserHolder(convertView, getActivity());
				convertView.setTag(holder);
			} else {
				holder = (UserHolder) convertView.getTag();
			}
			holder.fill(getItem(position), position);
			return convertView;
		}

		@Override
		protected void recycleView(View view) {

		}
	}

	class UserHolder {

		User user;

		Context context;

		int position;

		@InjectView(R.id.ivAvatar)
		AvatarView avatarView;

		@InjectView(R.id.tvName)
		TextView tvName;

		@InjectView(R.id.tvComAndPos)
		TextView tvComAndPos;

		@InjectView(R.id.ivRight)
		ImageView ivRight;

		@InjectView(R.id.tvDistance)
		TextView tvDistance;

		@InjectView(R.id.vlineBottom)
		View vlineBottom;

		View item;

		public UserHolder(View v, final Context context) {
			this.context = context;
			item = v;
			ButterKnife.inject(this, v);
		}

		public void fill(User user, int position) {
			this.user = user;
			this.position = position;
			avatarView.fill(user, true);
			tvName.setText(user.name);
			tvComAndPos.setText((user.userCompany == null ? ""
					: user.userCompany)
					+ " "
					+ (user.userPosition == null ? "" : user.userPosition));

			tvDistance.setText(getDistance(user.distance)
					+ StringUtil.convertFrom(System.currentTimeMillis()
							- user.timeDiff * 1000 * 60));

			if (user.isFriend == IMContact.FRIEND_YES) {
				ivRight.setVisibility(View.VISIBLE);
				if (user.relationLevel == IMContact.RELATION_TRUST) {
					ivRight.setImageResource(R.drawable.btn_chat_gold_selector);
				} else {
					ivRight.setImageResource(R.drawable.btn_chat_selector);
				}
			} else {
				ivRight.setVisibility(View.GONE);
			}

			if (position == getPullProxy().getAdapter().getCount() - 1) {
				vlineBottom.setVisibility(View.GONE);
			} else {
				vlineBottom.setVisibility(View.VISIBLE);
			}

			item.setOnClickListener(itemClickListener);
		}

		OnClickListener itemClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActProfileDetail.invoke(getActivity(), user.uid);
			}
		};

		@OnClick(R.id.ivRight)
		void chatClick() {
			ActChat.invoke(context, user.uid);
		}
	}

	private String getDistance(Integer distance) {
		if (distance == null) {
			return "";
		} else {
			if (distance < 0) {
				distance = 0;
			}
			if (distance < 1000) {
				return ((distance + 100) / 100 * 100) + "m以内 | ";
			} else {
				return ((distance + 100) / 100) / 10f + "km以内 | ";
			}
		}
	}
}
