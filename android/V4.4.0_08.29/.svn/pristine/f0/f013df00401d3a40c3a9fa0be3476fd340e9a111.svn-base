package com.zhisland.android.blog.event.controller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.uri.TextLinkMovementMethod;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.ScrollTitleBar;
import com.zhisland.android.blog.common.view.SignUpListview;
import com.zhisland.android.blog.common.view.SignUpListview.OnSignUpClickListener;
import com.zhisland.android.blog.common.view.banner.BannerView;
import com.zhisland.android.blog.common.view.flowlayout.FlowLayout;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.common.view.pullzoom.PullToZoomScrollViewEx;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExTitleListener;
import com.zhisland.android.blog.event.dto.CurrentState;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.android.blog.event.dto.PriceTag;
import com.zhisland.android.blog.event.dto.VoteTo;
import com.zhisland.android.blog.event.eb.EBEvent;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 活动详情页
 */
public class FragEventDetail extends FragBase {

    private final int LINE_MAX_COUNT = 12;

    @InjectView(R.id.rlHead)
    RelativeLayout rlHead;

    @InjectView(R.id.llOpenUp)
    LinearLayout llOpenUp;

    @InjectView(R.id.tvOpenUp)
    TextView tvOpenUp;

    @InjectView(R.id.tvEventDesc)
    TextView tvEventDesc;

    @InjectView(R.id.tvSignUpTag)
    TextView tvSignUpTag;

    @InjectView(R.id.tvSeeAll)
    TextView tvSeeAll;

    @InjectView(R.id.imgsSignUp)
    SignUpListview vSignUp;

    @InjectView(R.id.tvCategory)
    TextView tvCategory;

    @InjectView(R.id.llEventGuest)
    LinearLayout llEventGuest;

    @InjectView(R.id.tvPrice)
    TextView tvPrice;

    @InjectView(R.id.tvPriceGuests)
    TextView tvPriceGuests;

    @InjectView(R.id.tvPriceVip)
    TextView tvPriceVip;

    @InjectView(R.id.tvPriceGuestsName)
    TextView tvPriceGuestsName;

    @InjectView(R.id.tvPriceVipName)
    TextView tvPriceVipName;

    @InjectView(R.id.tvPriceUgc)
    TextView tvPriceUgc;

    @InjectView(R.id.tvPriceTag)
    TextView tvPriceTag;

    @InjectView(R.id.tvPriceGuestsTag)
    TextView tvPriceGuestsTag;

    @InjectView(R.id.tvPriceVipTag)
    TextView tvPriceVipTag;

    @InjectView(R.id.tvPriceAllTag)
    TextView tvPriceAllTag;

    @InjectView(R.id.tvPriceAllTagDesc)
    TextView tvPriceAllTagDesc;

    @InjectView(R.id.llPrice)
    LinearLayout llPrice;

    @InjectView(R.id.llPriceUgc)
    LinearLayout llPriceUgc;

    @InjectView(R.id.llPriceGuests)
    LinearLayout llPriceGuests;

    @InjectView(R.id.llPriceVip)
    LinearLayout llPriceVip;

    @InjectView(R.id.llPriceAllTag)
    LinearLayout llPriceAllTag;

    @InjectView(R.id.llSignUp)
    LinearLayout llSignUp;

    @InjectView(R.id.tvLoc)
    TextView tvLoc;

    @InjectView(R.id.tvTime)
    TextView tvTime;

    @InjectView(R.id.tvTitle)
    TextView tvTitle;

    @InjectView(R.id.rlTitle)
    ScrollTitleBar rlTitle;

    @InjectView(R.id.scrollView)
    PullToZoomScrollViewEx scrollView;

    @InjectView(R.id.rlBottom)
    RelativeLayout rlBottom;

    @InjectView(R.id.tvBottom)
    TextView tvBottom;

    @InjectView(R.id.llPrompt)
    LinearLayout llPrompt;

    @InjectView(R.id.llBody)
    LinearLayout llBody;

    @InjectView(R.id.llScrollBody)
    LinearLayout llScrollBody;

    @InjectView(R.id.hlvHonorGuest)
    HorizontalListView hlvGuest;

    @InjectView(R.id.tflCategory)
    TagFlowLayout tflCategory;

    @InjectView(R.id.llAuthorInfo)
    LinearLayout llAuthorInfo;

    @InjectView(R.id.tvAuthorInfo)
    TextView tvAuthorInfo;

    @InjectView(R.id.tvAuthorPhone)
    TextView tvAuthorPhone;

    @InjectView(R.id.tvNumOfPeople)
    TextView tvNumOfPeople;

    @InjectView(R.id.vLineGuestPosition)
    View vLineGuestPosition;

    @InjectView(R.id.tvEnjoyEvent)
    TextView tvEnjoyEvent;

    @InjectView(R.id.tvPriceName)
    TextView tvPriceName;

    private Event event;

    private long eventId;

    private int headImgHeight;

    private GuestAdapter adapter;

    boolean signUping = false;

    private List<String> allTags = new ArrayList<String>();
    // banner 轮播器
    private BannerView bannerView;

    @Override
    public String getPageName() {
        return "EventDetail";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_event_detail, null);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        EventBus.getDefault().register(this);
        initView();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ZhislandApplication.trackerClickEvent(null, TrackerAlias.PAGE_EVENT_DETAIL);
        eventId = getActivity().getIntent().getLongExtra(
                ActEventDetail.KEY_INTENT_EVENTID, 0);
        if (eventId > 0) {
            event = getEventFromDB(eventId);
            if (event != null) {
                setDataToView();
            } else {
                llBody.setVisibility(View.GONE);
            }
            getDataFromInternet(eventId);
        } else {
            getActivity().finish();
        }
    }

    private void initView() {
        headImgHeight = DensityUtil.getWidth() * 3 / 4;
        llPrompt.getLayoutParams().height = DensityUtil.getHeight()
                - headImgHeight - DensityUtil.dip2px(85);
        initTitleAndZoom();
        tflCategory.setAdapter(allAdapter);
        adapter = new GuestAdapter(getActivity());
        hlvGuest.setAdapter(adapter);
        hlvGuest.setOnItemClickListener(GuestItemClickListener);
        vSignUp.setOnSignUpClickListener(OnSignUpClickListener);
        tvEventDesc.setMovementMethod(new TextLinkMovementMethod(getActivity()));
        tvEventDesc.setLinkTextColor(getResources().getColor(R.color.color_dc));
        tvEventDesc.setMaxLines(Integer.MAX_VALUE);
        tvEventDesc.addOnLayoutChangeListener(new OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                if (oldTop == 0 && oldBottom == 0 && bottom != 0) {
                    int lineCount = tvEventDesc.getLineCount();
                    if (lineCount > LINE_MAX_COUNT) {
                        llOpenUp.setVisibility(View.VISIBLE);
                    } else {
                        llOpenUp.setVisibility(View.GONE);
                    }
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            tvEventDesc.setMaxLines(LINE_MAX_COUNT);
                        }
                    });
                }
            }
        });
    }

    /**
     * 初始化title和head的动画
     */
    private void initTitleAndZoom() {
        int titleHeight = getResources().getDimensionPixelOffset(
                R.dimen.title_height);

        // 设置pullzoom的属性
        scrollView.reset();
        scrollView.setZoomView(rlHead);
        scrollView.setScrollContentView(llScrollBody);
        scrollView.setHeaderViewSize(LayoutParams.MATCH_PARENT, headImgHeight);

        // 设置标题栏渐变效果
        ScrollViewExTitleListener titleAlphaListener = new ScrollViewExTitleListener();
        titleAlphaListener.setTitledOffset(headImgHeight - titleHeight);
        titleAlphaListener.setTitleView(rlTitle);
        rlTitle.setLeftRes(R.drawable.sel_btn_back_white,
                R.drawable.sel_btn_back);
        rlTitle.setRightRes(R.drawable.sel_btn_share,
                R.drawable.sel_btn_share_green);
        scrollView.addOnScrollChangedListener(titleAlphaListener);

        // 设置标题栏点击监听事件
        rlTitle.setRightClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (event == null) {
                    return;
                }
                DialogUtil.getInstatnce().showEventDialog(getActivity(), event,
                        DialogUtil.FROM_EVENT_DETAIL, getPageName());
            }
        });
        rlTitle.setLeftClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 将数据添加到View上显示。
     */
    private void setDataToView() {
        llBody.setVisibility(View.VISIBLE);
        setCommonView();
        setDifference();
        boolean showShare = getActivity().getIntent().getBooleanExtra(
                ActEventDetail.KEY_INTENT_SHOW_SHARE, false);
        if (showShare) {
            DialogUtil.getInstatnce().showCreateEventSuccessDialog(
                    getActivity(), event);
            getActivity().getIntent().removeExtra(
                    ActEventDetail.KEY_INTENT_SHOW_SHARE);
        }
    }

    /**
     * 设置官方活动和个人活动都存在的View内容
     */
    private void setCommonView() {
        if (event != null) {
            tvTitle.setText(event.eventTitle);
            if (event.type == Event.CATEGORY_TYPE_UGC) {
                tvEventDesc.setText(event.content);
            } else {
                tvEventDesc.setText(Html.fromHtml(event.content));
            }
            if (event.totalNum == null || event.totalNum <= 0) {
                tvNumOfPeople.setText("不限");
            } else {
                tvNumOfPeople.setText(event.totalNum + "人");
            }
            setEventTimeView();
            setLocView();
            setCategoryView();
            setVp();
            setSignUpView();
            setBottomBtn();
            setLikeStateView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerView != null) {
            bannerView.startTurning();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bannerView != null) {
            bannerView.stopTurning();
        }
    }

    /**
     * 设置活动地点View内容
     */
    private void setLocView() {
        String loc = event.provinceName == null ? "" : event.provinceName;
        if (event.cityName != null && !event.cityName.equals(loc)) {
            loc += event.cityName == null ? "" : event.cityName;
        }
        loc += event.location == null ? "" : event.location;
        tvLoc.setText(loc);
    }

    /**
     * 设置活动标签View内容
     */
    private void setCategoryView() {
        allTags.clear();
        if (!StringUtil.isNullOrEmpty(event.category)) {
            String[] categories = event.category.split(",");
            for (int i = 0; i < categories.length; i++) {
                allTags.add(categories[i]);
            }
        }
        allAdapter.notifyDataChanged();
    }

    /**
     * 设置活动时间View显示
     */
    private void setEventTimeView() {
        tvTime.setText(event.period);
    }

    /**
     * 设置活动头图内容
     */
    private void setVp() {
        if (event == null) {
            return;
        }
        if (event.imgUrls == null || event.imgUrls.size() == 0) {
            event.imgUrls = new ArrayList<>();
            if (StringUtil.isNullOrEmpty(event.imgUrl)) {
                event.imgUrls.add("");
            } else {
                event.imgUrls.add(event.imgUrl);
            }
        }

        if (bannerView == null) {
            bannerView = new BannerView(getActivity());
            bannerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            bannerView.setPageIndicatorPadding(DensityUtil.dip2px(5));
            bannerView.setPageIndicator(new int[]{R.drawable.bg_circle_white_60, R.drawable.bg_circle_white});
        }
        bannerView.setPages(new BannerView.BannerHolder<String>() {
            private ImageView imageView;

            @Override
            public View createView(Context context) {
                imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

            @Override
            public void UpdateUI(Context context, int position, String data) {
                ImageWorkFactory.getFetcher().loadImage(data, imageView, R.drawable.img_info_default_pic);
            }
        }, event.imgUrls);
        if (bannerView != null) {
            rlHead.removeView(bannerView);
        }
        rlHead.addView(bannerView, 0);
    }

    /**
     * 设置已报名View的内容显示
     */
    private void setSignUpView() {
        if (event != null && event.signedUsers != null
                && event.signedUsers.size() > 0) {
            tvSignUpTag.setText("已报名(" + event.signedCount + ")");
            List<String> imgs = new ArrayList<String>();
            for (User user : event.signedUsers) {
                imgs.add(user.userAvatar);
            }
            vSignUp.setSignUpUsers(event.signedUsers, event.signedCount);
            llSignUp.setVisibility(View.VISIBLE);
            if (event.signedCount <= 4) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vSignUp.getLayoutParams();
                params.bottomMargin = DensityUtil.dip2px(0);
                vSignUp.setLayoutParams(params);

            }
            if (event.signedCount > SignUpListview.mDefaultCount) {
                tvSeeAll.setVisibility(View.VISIBLE);
            } else {
                tvSeeAll.setVisibility(View.GONE);
            }
        } else {
            llSignUp.setVisibility(View.GONE);
        }
    }

    /**
     * 设置官方活动和个人活动有差异的View
     */
    private void setDifference() {
        if (event == null) {
            return;
        }

        if (event.type == Event.CATEGORY_TYPE_UGC) {
            tvCategory.setText(event.brandName == null ? "" : event.brandName);
            tvCategory.setBackgroundResource(R.drawable.rect_bsc_cmiddle);
            if (TextUtils.isEmpty(event.brandName)) {
                tvCategory.setVisibility(View.GONE);
            }
            llEventGuest.setVisibility(View.GONE);
            vLineGuestPosition.setVisibility(View.VISIBLE);
            if (event.publicUser != null && event.publicUser.name != null) {
                llAuthorInfo.setVisibility(View.VISIBLE);
                setPublicUserView(event.publicUser.name);
            } else {
                llAuthorInfo.setVisibility(View.GONE);
            }

        } else if (event.type == Event.CATEGORY_TYPE_OFFICIAL) {
            tvCategory.setText(event.brandName == null ? "官方" : event.brandName);
            tvCategory.setBackgroundResource(R.drawable.rect_bdc_cmiddle);
            llEventGuest.setVisibility(View.VISIBLE);
            llAuthorInfo.setVisibility(View.VISIBLE);
            setGuestView();
            setPublicUserView(event.brandPublisherName == null ? "正和岛官方" : event.brandPublisherName);
        }
        setPriceView();
    }

    /**
     * 设置价格View的显示
     */
    private void setPriceView() {
        if (event.type == Event.CATEGORY_TYPE_UGC) {
            llPrice.setVisibility(View.GONE);
            llPriceVip.setVisibility(View.GONE);
            llPriceGuests.setVisibility(View.GONE);
            llPriceUgc.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(event.priceText)) {
                tvPriceUgc.setVisibility(View.GONE);
            } else {
                if (event.priceText.contains("¥")) {
                    tvPriceUgc.setText(event.priceText.split("¥")[1]);
                } else {
                    tvPriceUgc.setText(event.priceText);
                }
            }
        } else if (event.type == Event.CATEGORY_TYPE_OFFICIAL) {

            llPriceUgc.setVisibility(View.GONE);

            //原价qualityPriceText
            if (!TextUtils.isEmpty(event.costPriceText)) {
                llPrice.setVisibility(View.VISIBLE);
                fillPrice(event.costPriceText, tvPrice);
            } else {
                llPrice.setVisibility(View.GONE);
            }

            //海客价qualityPriceText
            if (!TextUtils.isEmpty(event.qualityPriceText)) {
                llPriceGuests.setVisibility(View.VISIBLE);
                fillPrice(event.qualityPriceText, tvPriceGuests);
            } else {
                llPriceGuests.setVisibility(View.GONE);
            }

            //岛邻价vipPrice
            if (!TextUtils.isEmpty(event.vipPriceText)) {
                llPriceVip.setVisibility(View.VISIBLE);
                fillPrice(event.vipPriceText, tvPriceVip);
            } else {
                llPriceVip.setVisibility(View.GONE);
            }

            /**
             *  价格合并
             *  如果三个价格一样的时候，后台只返回一个价格，其他两个价格为空
             *  原价costPriceText
             *  岛邻价vipPrice
             *  海客价qualityPriceText
             */
            if (!TextUtils.isEmpty(event.costPriceText) && TextUtils.isEmpty(event.qualityPriceText) && TextUtils.isEmpty(event.vipPriceText)) {
                llPrice.setVisibility(View.GONE);
                llPriceVip.setVisibility(View.GONE);
                llPriceGuests.setVisibility(View.GONE);
                llPriceUgc.setVisibility(View.VISIBLE);
                if (event.costPriceText.contains("¥")) {
                    tvPriceUgc.setText(event.costPriceText.split("¥")[1]);
                } else {
                    tvPriceUgc.setText(event.costPriceText);
                }
            }
            setPriceTag();
            setOfficialPriceColor();
        }

    }

    private void setPriceTag() {
        tvPriceTag.setVisibility(View.GONE);
        tvPriceGuestsTag.setVisibility(View.GONE);
        tvPriceVipTag.setVisibility(View.GONE);
        llPriceAllTag.setVisibility(View.GONE);
        if (event.type != Event.CATEGORY_TYPE_UGC && event.priceTags != null) {
            for (PriceTag priceTag : event.priceTags) {
                switch (priceTag.type) {
                    case PriceTag.TYPE_ALL:
                        llPriceAllTag.setVisibility(View.VISIBLE);
                        tvPriceAllTag.setText(priceTag.tag);
                        tvPriceAllTagDesc.setText(priceTag.content);
                        break;
                    case PriceTag.TYPE_NORMAL:
                        tvPriceTag.setVisibility(View.VISIBLE);
                        tvPriceTag.setText(priceTag.tag);
                        break;
                    case PriceTag.TYPE_HAIKE:
                        tvPriceGuestsTag.setVisibility(View.VISIBLE);
                        tvPriceGuestsTag.setText(priceTag.tag);
                        break;
                    case PriceTag.TYPE_VIP:
                        tvPriceVipTag.setVisibility(View.VISIBLE);
                        tvPriceVipTag.setText(priceTag.tag);
                        break;
                }
            }
        }
    }

    /**
     * 根据用户类型，设置官方活动价格显示时，哪个价格为高亮
     */
    private void setOfficialPriceColor() {
        User user = DBMgr.getMgr().getUserDao().getSelfUser();
        int colorF1 = getActivity().getResources().getColor(R.color.color_f1);
        if (user.isVip() && !TextUtils.isEmpty(event.vipPriceText)) {
            // 身份是岛邻，且有岛邻价，则岛邻价高亮
            tvPriceVip.setTextColor(colorF1);
            tvPriceVipName.setTextColor(colorF1);
        } else if (user.isHaiKe() && !TextUtils.isEmpty(event.qualityPriceText)) {
            // 身份是海客，且有海客价，则海客价高亮
            tvPriceGuestsName.setTextColor(colorF1);
            tvPriceGuests.setTextColor(colorF1);
        } else if (user.isYuZhuCe() && !TextUtils.isEmpty(event.costPriceText)) {
            tvPriceName.setTextColor(colorF1);
            tvPrice.setTextColor(colorF1);
        }
    }

    /**
     * 设置发布人View的内容显示
     */
    private void setPublicUserView(String name) {
        String content;
        content = "该活动由 " + name + " 发布";
        tvAuthorInfo.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(content);
        ss.setSpan(spanName, 5, 5 + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(event.contactMobile)) {
            tvAuthorPhone.setVisibility(View.VISIBLE);
        } else {
            tvAuthorPhone.setVisibility(View.GONE);
        }
        tvAuthorInfo.setText(ss);
    }

    @OnClick(R.id.tvAuthorPhone)
    void onPhoneClick() {
        ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_DETAIL_PHONE);
        IntentUtil.dialTo(getActivity(), event.contactMobile);
    }

    /**
     * 用户名的span，点击跳个人页
     */
    ClickableSpan spanName = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            if (event.type == Event.CATEGORY_TYPE_UGC && event.publicUser != null) {
                ActProfileDetail.invoke(getActivity(), event.publicUser.uid);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.color_f1));
            ds.setUnderlineText(false);
        }

    };

    private void fillPrice(String price, TextView tvPrice) {
        tvPrice.setText(price);
    }

    /**
     * 设置嘉宾View
     */
    private void setGuestView() {
        adapter.clear();
        if (event != null && event.honorGuestList != null
                && event.honorGuestList.size() > 0) {
            adapter.addAll(event.honorGuestList);
            llEventGuest.setVisibility(View.VISIBLE);
            vLineGuestPosition.setVisibility(View.GONE);
        } else {
            llEventGuest.setVisibility(View.GONE);
            vLineGuestPosition.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void getDataFromInternet(long id) {
        if (event == null) {
            showLoadProgress();
        }
        ZHApis.getEventApi().getEventDetail(getActivity(), id, new TaskCallback<Event>() {

            @Override
            public void onSuccess(Event content) {
                if (isAdded() && !isDetached()) {
                    hidePrompt();
                    event = content;
                    setDataToView();
                }
            }

            @Override
            public void onFailure(Throwable error) {
                if (event == null) {
                    showEmptyPrompt();
                }
            }

            @Override
            public void onFinish() {
                hideProgressDlg();
                super.onFinish();
            }

        });
    }

    /**
     * 显示数据加载失败的View
     */
    private void showEmptyPrompt() {
        llPrompt.setVisibility(View.VISIBLE);
        // rlBottom.setVisibility(View.INVISIBLE);
        rlBottom.setEnabled(false);
    }

    /**
     * 隐藏数据加载失败的View
     */
    private void hidePrompt() {
        llPrompt.setVisibility(View.GONE);
    }

    private void showLoadProgress() {
        // rlBottom.setVisibility(View.INVISIBLE);
        rlBottom.setEnabled(false);
        showProgressDlg("加载中...");
    }

    private Event getEventFromDB(long id) {
        return DBMgr.getMgr().getEventCacheDao().getEventById(id);
    }

    public void onEventMainThread(EBEvent eb) {
        switch (eb.getType()) {
            case EBEvent.TYPE_SIGN_CONFIRM:
                signUp();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }


    /**
     * 活动嘉宾 Adapter
     */
    private class GuestAdapter extends ArrayAdapter<User> {

        private LayoutInflater inflater;

        public GuestAdapter(Context context) {
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

        /** View holder for the views we need access to */

    }

    /**
     * 活动嘉宾Holder
     */
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

    @OnClick(R.id.btnReload)
    void reloadClick() {
        getDataFromInternet(eventId);
    }

    @OnClick(R.id.llOpenUp)
    void openUpClick() {
        openOrClose();
    }

    @OnClick(R.id.tvEventDesc)
    void contentClick() {
        openOrClose();
    }

    @OnClick(R.id.tvEnjoyEvent)
    void enjoyEventShow() {
        if (null == event || null == event.likeStatus) {
            return;
        }
        if (event.likeStatus.isOperable == 1) {
            updateLikeStates();
        }
    }


    /**
     * 改变喜欢状态
     */
    private void updateLikeStates() {
        showProgressDlg("正在记录您的喜好...");
        ZHApis.getEventApi().postLikeStatus(getActivity(), eventId, new TaskCallback<Object>() {
            @Override
            public void onSuccess(Object content) {

                Boolean isShow = PrefUtil.Instance().getLikeDialogShouldShow();
                if (isShow) {
                    DialogUtil.createEventEnjoyDialog(getActivity());
                } else {
                    ToastUtil.showLong("已记录您的喜好");
                }

                tvEnjoyEvent.setEnabled(false);
                event.likeStatus.isOperable = 0;
                PrefUtil.Instance().setLikeDialogShouldShow(false);
                hideProgressDlg();
            }

            @Override
            public void onFailure(Throwable error) {
                super.onFinish();
                hideProgressDlg();
            }
        });
    }


    /**
     * 设置喜欢此活动
     */
    private void setLikeStateView() {

        if (event == null) {
            tvEnjoyEvent.setEnabled(false);
        } else {
            if (null != event.likeStatus) {

                if (event.likeStatus.isOperable == 1) {
                    tvEnjoyEvent.setEnabled(true);
                } else {
                    tvEnjoyEvent.setEnabled(false);
                }

            }

        }

    }

    private void openOrClose() {
        tvEventDesc.setMaxLines(Integer.MAX_VALUE);
        llOpenUp.setVisibility(View.GONE);
    }

    @OnClick(R.id.llLoc)
    void locClick() {
        /*
         * 先去掉，不跳地图 if (checkAppInstall("com.baidu.BaiduMap")) { Intent intent =
		 * null; try { intent = Intent
		 * .getIntent("intent://map/place/search?queryAllContacts=" + event.location +
		 * "&region=" + event.cityName +
		 * "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"
		 * ); } catch (URISyntaxException e) { e.printStackTrace(); }
		 * startActivity(intent); } else if
		 * (checkAppInstall("com.autonavi.minimap")) { try { Intent intent =
		 * Intent
		 * .getIntent("androidamap://viewGeo?sourceApplication=zhisland&addr=" +
		 * event.cityName + event.location); startActivity(intent); } catch
		 * (URISyntaxException e) { e.printStackTrace(); } } else {
		 * ToastUtil.showShort("您没有安装百度和高德地图，请安装百度或高德地图后使用"); }
		 */
    }

    protected boolean checkAppInstall(String packageName) {
        if (packageName == null) {
            return false;
        }
        boolean isInstallGMap = false;
        List<PackageInfo> packs = getActivity().getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (p.versionName == null) {
                continue;
            }
            if (packageName.equals(p.packageName)) {
                isInstallGMap = true;
                break;
            }
        }
        return isInstallGMap;
    }

    @OnClick(R.id.rlBottom)
    void bottomBtnClick() {
        switch (event.currentState.state) {
            case CurrentState.STATE_SIGN_UP:
                AUriMgr.instance().viewRes(getActivity(), event.currentState.uri);
                break;
            case CurrentState.STATE_WAIT_PAY:
                if (event.payData == null || event.payData.isOnLine == null) {
                    return;
                }
                if (event.payData.isOnLine == PayData.TYPE_IS_ON_LINE) {
                    FragEventOnlinePayment.invoke(getActivity(), event, this.getClass().getName());
                } else {
                    FragEventOfflinePayment.invoke(getActivity(), event, this.getClass().getName());
                }
                break;
            case CurrentState.STATE_MANAGER:
                FragInitiatedEvents.invoke(getActivity());
                break;
        }
    }

    @OnClick(R.id.tvSeeAll)
    void onSeeAllClick() {
        FragSignedList.invoke(getActivity(), event);
    }

    /**
     * 请求报名
     */
    private void signUp() {
        showProgressDlg("请求中...");
        if (signUping) {
            return;
        }
        signUping = true;
        ZHApis.getEventApi().getEventVote(getActivity(), eventId,
                new TaskCallback<ArrayList<VoteTo>>() {

                    @Override
                    public void onSuccess(ArrayList<VoteTo> content) {
                        if (isAdded() || !isDetached()) {
                            FragSignConfirm.invoke(getActivity(), event,
                                    content);
                        }
                    }

                    @Override
                    public void onFailure(Throwable error) {
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        signUping = false;
                        if (isAdded() || !isDetached()) {
                            hideProgressDlg();
                        }
                    }
                });
    }

    /**
     * 设置底部报名按钮状态
     * <p/>
     * 1.活动状态最优先 2.报名状态
     */
    private void setBottomBtn() {
        if (event == null) {
            return;
        }
        if (event.currentState != null) {
            rlBottom.setEnabled(event.currentState.isOperable == CurrentState.IS_OPERABLE_YES);
            tvBottom.setText(event.currentState.stateName);
        } else {
            setBottomBtnByEventState();
        }
    }

    /**
     * 如果CurrentState为空，根据event的活动状态设置底部按钮状态。
     */
    private void setBottomBtnByEventState() {
        if (event.eventStatus == Event.STATUS_SIGNING) {
            if (event.publicUser != null
                    && event.publicUser.uid == PrefUtil.Instance().getUserId()) {
                tvBottom.setText("管理");
                rlBottom.setEnabled(true);
                return;
            } else {
                if (event.signStatus == Event.SIGN_STATUS_YES) {
                    if (event.auditStatus == Event.STATUS_AUDIT_ACCEPT) {
                        rlBottom.setEnabled(false);
                        tvBottom.setText("报名成功");
                    } else {
                        tvBottom.setText("报名申请中");
                        rlBottom.setEnabled(false);
                    }
                } else {
                    rlBottom.setEnabled(true);
                    tvBottom.setText("立即报名");
                }
            }
        } else if (event.eventStatus == Event.STATUS_CANCEL) {
            tvBottom.setText("活动已取消");
            rlBottom.setEnabled(false);
        } else if (event.eventStatus == Event.STATUS_PROGRESSING) {
            tvBottom.setText("活动进行中");
            rlBottom.setEnabled(false);
        } else if (event.eventStatus == Event.STATUS_END) {
            tvBottom.setText("活动结束");
            rlBottom.setEnabled(false);
        } else if (event.eventStatus == Event.STATUS_SIGN_OVER) {
            rlBottom.setEnabled(false);
            if (event.signStatus == Event.SIGN_STATUS_YES) {
                if (event.auditStatus == Event.STATUS_AUDIT_ACCEPT) {
                    tvBottom.setText("报名成功");
                } else {
                    tvBottom.setText("报名申请中");
                }
            } else {
                tvBottom.setText("立即报名");
            }
        }
    }

    OnItemClickListener GuestItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            HonorGuestDialogProxy.HonorGuestDialog(getActivity(),
                    event.honorGuestList.get(arg2));
        }
    };

    OnSignUpClickListener OnSignUpClickListener = new OnSignUpClickListener() {

        @Override
        public void onItemClick(int index, View v) {
            if (event.signedUsers.get(index) == null) {
                return;
            }
            ActProfileDetail.invoke(getActivity(), event.signedUsers.get(index).uid);
        }

        @Override
        public void moreClick(View v) {
            FragSignedList.invoke(getActivity(), event);
        }
    };

    TagAdapter allAdapter = new TagAdapter<String>(allTags) {
        @Override
        public View getView(
                FlowLayout parent,
                int position, String tag) {
            TextView textView = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.tag_text, null);
            int tagHeight = DensityUtil.dip2px(24);
            textView.setBackgroundResource(R.drawable.rect_sdc_clarge);
            textView.setPadding(DensityUtil.dip2px(8), 0, DensityUtil.dip2px(8), 0);
            MarginLayoutParams params = new MarginLayoutParams(
                    MarginLayoutParams.WRAP_CONTENT, tagHeight);
            params.rightMargin = DensityUtil.dip2px(10);
            params.topMargin = DensityUtil.dip2px(10);
            textView.setTextColor(getResources().getColorStateList(
                    R.color.sel_font_color_green));
            textView.setLayoutParams(params);
            textView.setText(tag);
            return textView;
        }
    };
}
