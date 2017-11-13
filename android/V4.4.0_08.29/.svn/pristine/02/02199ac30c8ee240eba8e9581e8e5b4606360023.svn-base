package com.zhisland.android.blog.profilemvp.view.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.common.view.ScaleImageView;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.profilemvp.bean.UserPhoto;
import com.zhisland.android.blog.profilemvp.model.ModelFactory;
import com.zhisland.android.blog.profilemvp.model.remote.ProfileApi;
import com.zhisland.android.blog.profilemvp.presenter.MyAttentionPresenter;
import com.zhisland.android.blog.profilemvp.presenter.MyPhotoPresenter;
import com.zhisland.android.blog.profilemvp.view.IMyPhotoView;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.image.viewer.FreeImageViewer;
import com.zhisland.lib.image.viewer.ImageDataAdapter;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人主页  相册页面
 * Created by Mr.Tui on 2016/9/6.
 */
public class FragMyPhoto extends FragPullListMvps<UserPhoto> implements IMyPhotoView {

    public static final String INTENT_KEY_USER_ID = "intent_key_user_id";
    private static final int TAG_ID_RIGHT = 101;

    public static void invoke(Context context, long userId) {
        if (userId <= 0) {
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragMyPhoto.class;
        param.enableBack = true;
        param.title = "相册";
        if (userId == PrefUtil.Instance().getUserId()) {
            param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
            CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_IMG);
            tb.isLeft = false;
            tb.imgResId = R.drawable.bg_nav_add;
            param.titleBtns.add(tb);
            param.runnable = titleRunnable;
        }
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_USER_ID, userId);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            if (tagId == TAG_ID_RIGHT) {
                ActAddPhoto.invoke(context);
            }
        }
    };

    MyPhotoPresenter myPhotoPresenter;

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        long userId = getActivity().getIntent().getLongExtra(INTENT_KEY_USER_ID, 0);
        Map<String, BasePresenter> presenterMap = new HashMap<>(1);
        myPhotoPresenter = new MyPhotoPresenter();
        myPhotoPresenter.setUserId(userId);
        myPhotoPresenter.setModel(ModelFactory.CreateMyPhotoModel());
        presenterMap.put(MyAttentionPresenter.class.getSimpleName(), myPhotoPresenter);
        return presenterMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    ClickableSpan clickableSpan = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            myPhotoPresenter.onAddPhotoClick();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.color_dc));
            ds.setUnderlineText(false);
        }

    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        internalView.setFastScrollEnabled(false);
        internalView.addHeaderView(getHeadView());
        pullView.setBackgroundColor(getResources().getColor(R.color.color_bg1));
        pullView.getRefreshableView().setDividerHeight(0);
        pullView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private View getHeadView() {
        View v = new View(getActivity());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(16));
        v.setLayoutParams(lp);
        return v;
    }

    @Override
    public void loadNormal() {
        super.loadNormal();
        myPhotoPresenter.getPhoto(null);
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        myPhotoPresenter.getPhoto(nextId);
    }

    @Override
    public void onLoadSucessfully(ZHPageData<UserPhoto> dataList) {
        super.onLoadSucessfully(dataList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        PhotoAdapter userAdapter = new PhotoAdapter();
        getPullProxy().setAdapter(userAdapter);
    }

    public void onEventMainThread(EBProfile eb) {
        if (eb.getType() == EBProfile.TYPE_CHANGE_PHOTO && !isDetached()) {
            getPullProxy().pullDownToRefresh(true);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void goToAddPhoto() {
        ActAddPhoto.invoke(getActivity());
    }

    class PhotoAdapter extends BaseListAdapter<UserPhoto> {

        @Override
        public int getCount() {
            return (super.getCount() + 2) / 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhotoHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_gallery, null);
                holder = new PhotoHolder(convertView, getActivity());
                convertView.setTag(holder);
            } else {
                holder = (PhotoHolder) convertView.getTag();
            }
            UserPhoto one = (position * 3) < super.getCount() ? getItem(position * 3) : null;
            UserPhoto two = (position * 3 + 1) < super.getCount() ? getItem(position * 3 + 1) : null;
            UserPhoto three = (position * 3 + 2) < super.getCount() ? getItem(position * 3 + 2) : null;
            holder.fill(one, two, three, position);
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

    class PhotoHolder {

        Context context;

        @InjectView(R.id.sivOne)
        ScaleImageView sivOne;

        @InjectView(R.id.sivTwo)
        ScaleImageView sivTwo;

        @InjectView(R.id.sivThree)
        ScaleImageView sivThree;

        UserPhoto photoOne;
        UserPhoto photoTwo;
        UserPhoto photoThree;
        int position;

        public PhotoHolder(View v, final Context context) {
            this.context = context;
            ButterKnife.inject(this, v);
        }

        public void fill(UserPhoto one, UserPhoto two, UserPhoto three, int position) {
            photoOne = one;
            photoTwo = two;
            photoThree = three;
            this.position = position;
            fillImage(sivOne, one);
            fillImage(sivTwo, two);
            fillImage(sivThree, three);
        }

        private void fillImage(ImageView iv, UserPhoto photo) {
            if (photo != null) {
                ImageWorkFactory.getFetcher().loadImage(photo.url, iv, ImageWorker.ImgSizeEnum.SMALL);
                iv.setVisibility(View.VISIBLE);
            } else {
                iv.setVisibility(View.INVISIBLE);
            }
        }

        @OnClick(R.id.sivOne)
        void onOneClick() {
            gotoPhotoBrowse(position * 3);
        }

        @OnClick(R.id.sivTwo)
        void onTwoClick() {
            gotoPhotoBrowse(position * 3 + 1);
        }

        @OnClick(R.id.sivThree)
        void onThreeClick() {
            gotoPhotoBrowse(position * 3 + 2);
        }

        private void gotoPhotoBrowse(int fromIndex) {
            List<UserPhoto> data = getPullProxy().getAdapter().getData();
            if (data != null && data instanceof ArrayList) {
                PhotoBrowseAdapter photoBrowseAdapter = new PhotoBrowseAdapter((ArrayList<UserPhoto>) data);
                FreeImageViewer.invoke(getActivity(), photoBrowseAdapter, fromIndex, photoBrowseAdapter.count(),
                        FreeImageViewer.BUTTON_DELETE, 0, FreeImageViewer.TYPE_SHOW_TITLE_BAR);
            }
        }

    }

    static class PhotoBrowseAdapter implements ImageDataAdapter {

        private ArrayList<UserPhoto> data;

        public PhotoBrowseAdapter(ArrayList<UserPhoto> data) {
            this.data = data;
        }

        @Override
        public void remove(final int location) {
            UserPhoto userPhoto = null;
            if (data != null && data.size() > location) {
                userPhoto = data.remove(location);
            }
            if (userPhoto != null) {
                final long photoId = userPhoto.photoId;
                Observable.create(new AppCall<Void>() {
                    @Override
                    protected Response<Void> doRemoteCall() throws Exception {
                        Call<Void> call = RetrofitFactory.getInstance().getHttpsApi(ProfileApi.class).deletePhoto(photoId);
                        return call.execute();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showShort("删除图片失败");
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                EBProfile eBProfile = new EBProfile(EBProfile.TYPE_CHANGE_PHOTO, null);
                                EventBus.getDefault().post(eBProfile);
                            }
                        });
            }
        }

        @Override
        public String getDesc(int position) {
            return data.get(position).text;
        }


        @Override
        public int count() {
            return data == null ? 0 : data.size();
        }

        @Override
        public String getUrl(int position) {
            return data.get(position).url;
        }

    }

}
