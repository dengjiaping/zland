package com.zhisland.android.blog.profile.controller.position;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.City;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.util.TextViewAutoLinkUtil;
import com.zhisland.android.blog.common.view.ScrollTitleBar;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.common.view.pullzoom.PullToZoomScrollViewEx;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExTitleListener;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.profile.dto.CompanyState;
import com.zhisland.android.blog.profile.eb.EBCompany;
import com.zhisland.lib.bitmap.ImageCache;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * 公司信息详情的fragment
 */
public class FragCompanyDetail extends FragBase {

    private static final String HEAD_PIC = "fragCompanyDetail_head_pic";

    @InjectView(R.id.rlHead)
    RelativeLayout rlHead;

    @InjectView(R.id.ivHeadBg)
    ImageView ivHeadBg;

    @InjectView(R.id.llBody)
    LinearLayout llBody;

    @InjectView(R.id.llScrollBody)
    LinearLayout llScrollBody;

    @InjectView(R.id.llStock)
    LinearLayout llStock;

    @InjectView(R.id.scrollView)
    PullToZoomScrollViewEx pullToZoomScrollView;

    @InjectView(R.id.rlTitle)
    ScrollTitleBar rlTitle;

    @InjectView(R.id.llPrompt)
    View llPrompt;

    @InjectView(R.id.llTag)
    TagFlowLayout llTag;

    @InjectView(R.id.ivLogo)
    ImageView tvLogo;

    @InjectView(R.id.tvCompanyType)
    TextView tvCompanyType;

    @InjectView(R.id.tvStockCode)
    TextView tvStockCode;

    @InjectView(R.id.tvCompanyName)
    TextView tvCompanyName;

    @InjectView(R.id.tvCompanyDesc)
    TextView tvCompanyDesc;

    @InjectView(R.id.tvCompanyUrl)
    TextView tvCompanyUrl;

    @InjectView(R.id.tvCompanyDescTitle)
    TextView tvCompanyDescTitle;

    @InjectView(R.id.tvCompanyUrlTitle)
    TextView tvCompanyUrlTitle;

    @InjectView(R.id.vDiv2)
    View vDiv2;

    /**
     * 当前公司详情界面的company对象
     */
    private Company company;

    private boolean isSelf;

    /**
     * 空构造函数要保留
     */
    public FragCompanyDetail() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_company_detail, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long companyId = getActivity().getIntent().getLongExtra(
                ActCompanyDetail.INTENT_KEY_COMPANY_ID, -1);
        company = (Company) DBMgr.getMgr().getCacheDao()
                .get(companyId + Company.POSTFIX);
        isSelf = getActivity().getIntent().getBooleanExtra(
                ActCompanyDetail.INTENT_KEY_IS_SELF, false);
        if (company == null) {
            showToast("公司信息不存在");
            if (getActivity() != null)
                getActivity().finish();
            return;
        }
        updateView();

    }

    /**
     * 初始化界面
     */
    private void updateView() {
        updateHead();
        updateBody();
    }

    /**
     * 初始化数据
     */
    private void updateBody() {
        updateCompanyType();
        updateTag();
        updateCompanyBaseInfo();
    }

    private void updateHead() {
        int headImgHeight = (int) (DensityUtil.getWidth() * 0.75f);
        ivHeadBg.getLayoutParams().height = headImgHeight;
        ivHeadBg.getLayoutParams().height = headImgHeight
                - DensityUtil.dip2px(40);
        llPrompt.getLayoutParams().height = DensityUtil.getHeight()
                - headImgHeight - DensityUtil.dip2px(44);
        initTitleAndZoom(ivHeadBg.getLayoutParams().height);
        // 将头图用ImageCache来管理，以防OOM
        Bitmap btContactImage = ImageCache.getInstance().getBitmapFromMemCache(
                HEAD_PIC);
        if (btContactImage == null) {
            ImageCache.getInstance().addBitmapToCache(
                    HEAD_PIC,
                    btContactImage = ImageWorkFactory.getFetcher()
                            .getBitmapFromRes(
                                    R.drawable.img_company_detail_head));
        }
        ivHeadBg.setImageBitmap(btContactImage);
    }

    /**
     * 对表示公司的一些基本信息控件赋值(名字，描述，官网，Logo)
     */
    private void updateCompanyBaseInfo() {
        if (company.name != null) {
            tvCompanyName.setText(company.name);
        }
        if (!StringUtil.isNullOrEmpty(company.desc)) {
            tvCompanyDesc.setVisibility(View.VISIBLE);
            tvCompanyDescTitle.setVisibility(View.VISIBLE);
            tvCompanyDesc.setText(company.desc);
        } else {
            tvCompanyDesc.setText("——");
            tvCompanyDesc.setVisibility(View.GONE);
            tvCompanyDescTitle.setVisibility(View.GONE);
            vDiv2.setVisibility(View.GONE);
        }
        if (!StringUtil.isNullOrEmpty(company.website)) {
            tvCompanyUrl
                    .setTextColor(getResources().getColor(R.color.color_dc));
            tvCompanyUrl.setText(company.website);
            TextViewAutoLinkUtil.formatLink(tvCompanyUrl, null);

            tvCompanyUrl.setVisibility(View.VISIBLE);
            tvCompanyUrlTitle.setVisibility(View.VISIBLE);
            vDiv2.setVisibility(View.VISIBLE);
        } else {
            tvCompanyUrl
                    .setTextColor(getResources().getColor(R.color.color_f2));
            tvCompanyUrl.setText("——");

            tvCompanyUrl.setVisibility(View.GONE);
            tvCompanyUrlTitle.setVisibility(View.GONE);
            vDiv2.setVisibility(View.GONE);
        }
        ImageWorkFactory.getFetcher().loadImage(company.logo, tvLogo,
                R.drawable.profile_img_logo_show);
    }

    /**
     * 公司发展状态 赋值
     */
    private void updateCompanyType() {
        ArrayList<CompanyState> stateList = Dict.getInstance().getCompanyState();
        if (company != null && company.stage != null) {
            tvCompanyType.setVisibility(View.VISIBLE);
            String isLaunched = CompanyState.FLAG_UN_LAUNCHED;
            if (stateList != null) {
                for (CompanyState cs : stateList) {
                    if (company.stage.equals(cs.tagId)) {
                        isLaunched = cs.custom;
                        tvCompanyType.setText(cs.tagName);
                    }
                }
            }
            if (company.stockCode != null
                    && CompanyState.FLAG_LAUNCHED.equals(isLaunched)) {
                tvCompanyType.setVisibility(View.GONE);
                llStock.setVisibility(View.VISIBLE);
                if (StringUtil.isNullOrEmpty(company.stockCode)) {
                    tvStockCode.setVisibility(View.GONE);
                } else {
                    tvStockCode.setVisibility(View.VISIBLE);
                    tvStockCode.setText(company.stockCode);
                }
            } else {
                llStock.setVisibility(View.GONE);
            }
        } else {
            tvCompanyType.setVisibility(View.GONE);
        }

    }

    /**
     * 新建标签，如公司行业、公司所在城市
     */
    private void updateTag() {
        List<String> tags = new ArrayList<String>();
        TagAdapter<String> adapter = createSelectAdapter(tags);
        llTag.removeAllViews();
        llTag.setAdapter(adapter);
        if (company == null) {
            return;
        }
        if (company.cityId != null && company.cityId != 0) {
            tags.add(City.getNameByCode(company.cityId));
        }
        if (company.industry != null) {
            tags.add(company.industry.name);
        }
        adapter.notifyDataChanged();
    }

    /**
     * 标签的adapter
     *
     * @param tags
     * @return
     */
    private TagAdapter<String> createSelectAdapter(List<String> tags) {
        return new TagAdapter<String>(tags) {
            @Override
            public View getView(
                    com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
                    int position, String tag) {
                TextView textView = (TextView) LayoutInflater.from(
                        getActivity()).inflate(R.layout.tag_text, null);
                MarginLayoutParams params = new MarginLayoutParams(
                        MarginLayoutParams.WRAP_CONTENT,
                        MarginLayoutParams.WRAP_CONTENT);
                textView.setTextColor(getActivity().getResources().getColor(
                        R.color.color_f2));
                DensityUtil.setTextSize(textView, R.dimen.txt_12);
                textView.setBackgroundResource(R.drawable.rect_sf2_clarge);
                int dpi10 = DensityUtil.dip2px(8);
                int dpi5 = DensityUtil.dip2px(2);
                params.rightMargin = dpi10;
                params.topMargin = dpi10;
                textView.setPadding(dpi10, dpi5, dpi10, dpi5);
                textView.setLayoutParams(params);
                textView.setText(tag);
                return textView;
            }
        };
    }

    /**
     * 初始化公司详情的的Title和头图缩放
     *
     * @param headImgHeight
     */
    private void initTitleAndZoom(int headImgHeight) {
        int headerHeight = headImgHeight;
        int titleHeight = getResources().getDimensionPixelOffset(
                R.dimen.title_height);

        // 设置pullzoom的属性
        pullToZoomScrollView.reset();
        pullToZoomScrollView.setZoomView(ivHeadBg);
        pullToZoomScrollView.setScrollContentView(llScrollBody);
        pullToZoomScrollView.setHeaderViewSize(LayoutParams.MATCH_PARENT,
                headerHeight);

        // 设置标题栏渐变效果
        ScrollViewExTitleListener titleAlphaListener = new ScrollViewExTitleListener();
        titleAlphaListener.setTitledOffset(headerHeight - titleHeight);
        titleAlphaListener.setTitleView(rlTitle);

        // 设置标题栏点击监听事件
        rlTitle.setLeftRes(R.drawable.sel_btn_back_white,
                R.drawable.sel_btn_back);
        rlTitle.setLeftClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        // 查看别人的公司，没有编辑这个按钮
        if (isSelf) {
            rlTitle.showRightButton();
            rlTitle.setRightRes(R.drawable.sel_btn_nav_pencil,
                    R.drawable.sel_btn_pencil);
            rlTitle.setRightClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragUserCompanyCreateOrUpdate.invoke(getActivity(),
                            company, FragUserCompanyCreateOrUpdate.TYPE_EDIT);
                }
            });
        } else {
            rlTitle.hideRightButton();
        }
        pullToZoomScrollView.addOnScrollChangedListener(titleAlphaListener);

        llScrollBody.scrollTo(0, DensityUtil.dip2px(30));
    }

    @Override
    public String getPageName() {
        return "ProfileCompanyDetail";
    }

    @Override
    public boolean onBackPressed() {
        back();
        return true;
    }

    public void back() {
        if (getActivity() != null)
            getActivity().finish();
    }

    public void onEventMainThread(EBCompany eb) {
        switch (eb.getType()) {
            case EBCompany.TYPE_COMPANY_EDIT:
                company = (Company) eb.getCompany();
                DBMgr.getMgr().getCacheDao()
                        .set(company.companyId + Company.POSTFIX, company);
                updateBody();
                break;
            default:
                break;
        }
    }
}
