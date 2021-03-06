package com.zhisland.android.blog.profile.controller.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.feed.model.IIntrestableModel;
import com.zhisland.android.blog.feed.model.impl.IntrestableModel;
import com.zhisland.android.blog.im.controller.ActChat;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profilemvp.bean.RelationConstants;
import com.zhisland.android.blog.profilemvp.eb.EBRelation;
import com.zhisland.android.blog.profilemvp.model.remote.ProfileApi;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.rxjava.RxBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人页bottom
 */
public class FragProfileDetailBottom extends FragBase {

    private static final String PAGE_NAME = "FragProfileDetailBottom";
    private static final String TAG_CANCEL_ATTENTION = "cancelAttention";

    @InjectView(R.id.llBottom)
    LinearLayout llBottom;
    @InjectView(R.id.llAttention)
    LinearLayout llAttention;
    @InjectView(R.id.tvAttention)
    TextView tvAttention;
    @InjectView(R.id.tvIM)
    TextView tvIM;
    @InjectView(R.id.tvLike)
    TextView tvLike;

    private User userSelf;
    private UserDetail userFrom;
    // 是否执行加好友的操作
    public boolean isAddFriend;
    private IIntrestableModel intrestableModel;//this is a shit use, after this version ,these code should rewrite use mvp patter


    public FragProfileDetailBottom() {
        userSelf = DBMgr.getMgr().getUserDao().getSelfUser();
        intrestableModel = new IntrestableModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_profile_detail_bottom,
                null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataToView();
    }

    /**
     * 底部按钮view
     */
    private void setDataToView() {
        if (userFrom == null) {
            return;
        }
        if (userFrom.user.uid == userSelf.uid) {
            llBottom.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);
        }
        setAttentionView();
        setImView();
        setLikeView();

    }

    /**
     * profile框架 传过来的user
     */
    public void updateUser(UserDetail user) {
        this.userFrom = user;
        if (this.isAdded()) {
            setDataToView();
        }
    }

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    private void setAttentionView() {
        if (userFrom == null || userFrom.relationBtnGroup == null || userFrom.relationBtnGroup.followBtn == null) {
            return;
        }
        CustomState state = userFrom.relationBtnGroup.followBtn;
        switch (state.getState()) {
            case RelationConstants.UNFOLLOW_TA:
            case RelationConstants.BE_FOLLOWED: {
                tvAttention.setText("关注");
                llAttention.setTag(1);
                break;
            }
            case RelationConstants.FOLLOWED_TA: {
                tvAttention.setText("已关注");
                llAttention.setTag(0);
                break;
            }
            case RelationConstants.BOTH_FLLOWED: {
                tvAttention.setText("互相关注");
                llAttention.setTag(0);
                break;
            }
        }
    }

    private void setImView() {
        if (userFrom == null || userFrom.relationBtnGroup == null || userFrom.relationBtnGroup.chatBtn == null) {
            return;
        }
        CustomState state = userFrom.relationBtnGroup.chatBtn;
        tvIM.setText(state.getStateName());
        tvIM.setEnabled(true);
    }

    private void setLikeView() {
        if (userFrom == null || userFrom.relationBtnGroup == null || userFrom.relationBtnGroup.likeBtn == null) {
            return;
        }
        CustomState state = userFrom.relationBtnGroup.likeBtn;
        if (state.getState() <= 0) {
            tvLike.setText("赞");
        } else {
            tvLike.setText("" + state.getState());
        }
        if (state.isOperable()) {
            //可赞的意思，意思是还未赞过
            Drawable drawable = getResources().getDrawable(R.drawable.feed_btn_praise);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setTextColor(getResources().getColor(R.color.color_btn));
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.feed_btn_praise_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setTextColor(getResources().getColor(R.color.color_dc));
        }
        tvLike.setEnabled(true);
    }

    @OnClick(R.id.llAttention)
    void onAttentionClick(View view) {
        Object obj = view.getTag();
        if (!(obj instanceof Integer))
            return;

        if (((int) obj) > 0) {
            doAttention();
        } else {
            showCancelAttentionDlg();
        }
    }

    private void showCancelAttentionDlg() {
        showConfirmDlg(TAG_CANCEL_ATTENTION, "你已经关注了对方，\n确定要取消关注吗？", "确定", "取消", null);
    }

    private void doAttention() {
        showProgressDlg();
        final int state;
        final int ebAction;
        if (userFrom.relationBtnGroup.followBtn.getState() == RelationConstants.BE_FOLLOWED) {
            state = RelationConstants.BOTH_FLLOWED;
        } else {
            state = RelationConstants.FOLLOWED_TA;
        }
        ebAction = EBRelation.TYPE_FOLLOW_TO;
        intrestableModel.follow(userFrom.user, getPageName()).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        hideProgressDlg();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("关注失败");
                        hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        showToast("关注成功");
                        userFrom.relationBtnGroup.followBtn.setState(state);
                        setAttentionView();
                        EBRelation ebRelation = new EBRelation(ebAction, null);
                        RxBus.getDefault().post(ebRelation);
                    }
                });
    }

    private void cancelAttention() {
        showProgressDlg();
        final int state;
        final int ebAction;
        if (userFrom.relationBtnGroup.followBtn.getState() == RelationConstants.BOTH_FLLOWED) {
            state = RelationConstants.BE_FOLLOWED;
        } else {
            state = RelationConstants.UNFOLLOW_TA;
        }
        ebAction = EBRelation.TYPE_FOLLOW_CANCEL;
        intrestableModel.unfollow(userFrom.user).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        hideProgressDlg();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("取消关注失败");
                        hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        showToast("取消关注成功");
                        userFrom.relationBtnGroup.followBtn.setState(state);
                        setAttentionView();
                        EBRelation ebRelation = new EBRelation(ebAction, null);
                        RxBus.getDefault().post(ebRelation);
                    }
                });
    }

    @OnClick(R.id.llIM)
    void onIMClick() {
        if (userFrom == null || userFrom.user == null || userFrom.relationBtnGroup == null || userFrom.relationBtnGroup.chatBtn == null) {
            return;
        }
        //TODO
        ActChat.invoke(getActivity(), userFrom.user.uid);
    }

    @OnClick(R.id.llLike)
    void onLikeClick() {
        if (userFrom == null || userFrom.user == null || userFrom.relationBtnGroup == null || userFrom.relationBtnGroup.likeBtn == null) {
            return;
        }

        if (userFrom.relationBtnGroup.likeBtn.isOperable()) {
            Observable.create(new AppCall<Void>() {
                @Override
                protected Response<Void> doRemoteCall() throws Exception {
                    Call<Void> call = RetrofitFactory.getInstance().getApi(ProfileApi.class).likeUser(userFrom.user.uid);
                    return call.execute();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Void aVoid) {
                            userFrom.relationBtnGroup.likeBtn.setIsOperable(CustomState.CAN_NOT_OPERABLE);
                            int likeCount = userFrom.relationBtnGroup.likeBtn.getState();
                            likeCount++;
                            userFrom.relationBtnGroup.likeBtn.setState(likeCount);
                            setLikeView();
                        }
                    });
        } else {
            Observable.create(new AppCall<Void>() {
                @Override
                protected Response<Void> doRemoteCall() throws Exception {
                    Call<Void> call = RetrofitFactory.getInstance().getApi(ProfileApi.class).cancelLikeUser(userFrom.user.uid);
                    return call.execute();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Void aVoid) {
                            userFrom.relationBtnGroup.likeBtn.setIsOperable(CustomState.CAN_OPERABLE);
                            int likeCount = userFrom.relationBtnGroup.likeBtn.getState();
                            likeCount--;
                            userFrom.relationBtnGroup.likeBtn.setState(likeCount);
                            setLikeView();
                        }
                    });
        }
    }

    @Override
    public void onOkClicked(String tag, Object arg) {
        hideConfirmDlg(TAG_CANCEL_ATTENTION);
        cancelAttention();
    }

    @Override
    public void onNoClicked(String tag, Object arg) {
        hideConfirmDlg(TAG_CANCEL_ATTENTION);
    }
}
