package com.zhisland.android.blog.profile.controller.detail;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserAssistant;
import com.zhisland.android.blog.profile.dto.UserContactInfo;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.view.block.ProfileBaseBlock;
import com.zhisland.android.blog.profile.view.block.UserAssistantBlock;
import com.zhisland.android.blog.profile.view.block.UserCommentBlock;
import com.zhisland.android.blog.profile.view.block.UserCommonFriendBlock;
import com.zhisland.android.blog.profile.view.block.UserContactBlock;
import com.zhisland.android.blog.profile.view.block.UserContactGroupBlock;
import com.zhisland.android.blog.profile.view.block.UserDemandBlock;
import com.zhisland.android.blog.profile.view.block.UserDingBlock;
import com.zhisland.android.blog.profile.view.block.UserDripBlock;
import com.zhisland.android.blog.profile.view.block.UserHonorBlock;
import com.zhisland.android.blog.profile.view.block.UserIntroduceBlock;
import com.zhisland.android.blog.profile.view.block.UserPhotoBlock;
import com.zhisland.android.blog.profile.view.block.UserPositionBlock;
import com.zhisland.android.blog.profile.view.block.UserResourceBlock;
import com.zhisland.android.blog.profile.view.block.UserSupplyBlock;
import com.zhisland.android.blog.profilemvp.bean.RelationConstants;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

/**
 * 个人封面 fragment
 */
public class FragProfileCover extends FragBase {

    private static final String PAGE_NAME = "ProfileDetail";

    // -----block 填充区域-----
    @InjectView(R.id.llCoverContainer)
    LinearLayout llCoverContainer;

    private UserDetail userDetail;
    private User user;
    private boolean isUserSelf;
    private boolean commentEnable = true;

    public FragProfileCover() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_profile_cover, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (userDetail == null) {
            return;
        }
        setDataToView();
    }

    /**
     * 将user数据填充到view
     */
    private void setDataToView() {
        setDataToBlockView();
    }

    /**
     * 判断block是否显示。
     *
     * @return true：显示；false：不显示
     */
    private boolean isBlockVisibility(SimpleBlock block) {
        if (block == null) {
            return false;
        }
        List datas = block.data;
        // 看别人的个人页，除了评论，其它block数据为空时不显示
        if (!isUserSelf) {
            if (block.type == SimpleBlock.TYPE_USER_COMMENT) {
                // 评论无论是否为空都显示
                return true;
            } else {
                // 除了评论，其它block数据为空时不显示
                if (datas == null || datas.size() == 0 || datas.get(0) == null) {
                    return false;
                }
                if (block.type == SimpleBlock.TYPE_ASSISTANT) {
                    // block为助理，判断是否有权限显示。
                    UserAssistant assistant = (UserAssistant) datas.get(0);
                    //可见范围如果为空，默认为岛邻可见
                    if (assistant.visibleRange == null) {
                        assistant.visibleRange = UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_ALL;
                    }
                    if (assistant.visibleRange == UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_ALL) {
                        if (DBMgr.getMgr().getUserDao().getSelfUser().isYuZhuCe()) {
                            return false;
                        }
                    } else if (assistant.visibleRange == UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_FRIEND) {
                        if (!com.zhisland.im.data.DBMgr.getHelper().getUserDao().isRelation(user.uid, RelationConstants.BOTH_FLLOWED + "")) {
                            return false;
                        }
                    } else if (assistant.visibleRange == UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_NO) {
                        return false;
                    }
                } else if (block.type == SimpleBlock.TYPE_CONTACT) {
                    // block为联系方式时，判断是否有权限显示。
                    UserContactInfo contactInfo = (UserContactInfo) datas
                            .get(0);
                    // 如果联系方式可见范围为空，默认设置为岛邻可见
                    if (contactInfo.visibleRange == null) {
                        contactInfo.visibleRange = UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_ALL;
                    }
                    if (contactInfo.visibleRange == UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_ALL) {
                        if (DBMgr.getMgr().getUserDao().getSelfUser().isYuZhuCe()) {
                            return false;
                        }
                    } else if (contactInfo.visibleRange == UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_FRIEND) {
                        if (!com.zhisland.im.data.DBMgr.getHelper().getUserDao().isRelation(user.uid, RelationConstants.BOTH_FLLOWED + "")) {
                            return false;
                        }
                    } else if (contactInfo.visibleRange == UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_NO) {
                        return false;
                    }
                }
            }
        } else {
            // 看自己的个人页，如果服务岛丁为空，则不显示
            if (block.type == SimpleBlock.TYPE_DING) {
                if (datas == null || datas.size() == 0 || datas.get(0) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * block
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setDataToBlockView() {
        llCoverContainer.removeAllViews();

        List<SimpleBlock> blocks = userDetail.blocks;
        View lastDividerView = null;
        if (blocks != null) {
            for (SimpleBlock block : blocks) {
                if (block == null) {
                    continue;
                }
                ProfileBaseBlock blockView = null;
                //判断block是否显示
                if (!isBlockVisibility(block)) {
                    continue;
                }
                switch (block.type) {
                    case SimpleBlock.TYPE_USER_INTRODUCTION:
                        blockView = new UserIntroduceBlock(getActivity(), userDetail,
                                block);
                        break;
                    case SimpleBlock.TYPE_USER_COMMENT:
                        blockView = new UserCommentBlock(getActivity(), userDetail, block);
                        ((UserCommentBlock) blockView).setCommentEnable(commentEnable);
                        PrefUtil.Instance().setCommentMaxTop(block.maxCount);
                        break;
                    case SimpleBlock.TYPE_COMMON_FRIENDS:
                        blockView = new UserCommonFriendBlock(getActivity(), userDetail,
                                block);
                        break;
                    case SimpleBlock.TYPE_POSITION:
                        blockView = new UserPositionBlock(getActivity(), userDetail,
                                block);
                        break;
                    case SimpleBlock.TYPE_HONOR:
                        blockView = new UserHonorBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_SUPPLY:
                        blockView = new UserSupplyBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_DEMAND:
                        blockView = new UserDemandBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_RESOURCE:
                        blockView = new UserResourceBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_DRIP:
                        blockView = new UserDripBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_CONTACT:
                        blockView = new UserContactBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_ASSISTANT:
                        blockView = new UserAssistantBlock(getActivity(), userDetail,
                                block);
                        break;
                    case SimpleBlock.TYPE_DING:
                        blockView = new UserDingBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_PHOTO_GALLERY:
                        blockView = new UserPhotoBlock(getActivity(), userDetail, block);
                        break;
                    case SimpleBlock.TYPE_CONTACT_GROUP:
                        blockView = new UserContactGroupBlock(getActivity(), userDetail, block);
                        break;
                }

                if (blockView != null) {
                    blockView.setTag(block.type);
                    llCoverContainer.addView(blockView);
                    lastDividerView = getBlockDividerView();
                    llCoverContainer.addView(lastDividerView);
                }
            }
            if (lastDividerView != null) {
                llCoverContainer.removeView(lastDividerView);
            }
        }
    }

    /**
     * block divider view
     */
    private View getBlockDividerView() {
        View dividerView = new View(getActivity());
        dividerView.setBackgroundColor(getResources().getColor(
                R.color.color_bg2));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(10));
        dividerView.setLayoutParams(layoutParams);
        return dividerView;
    }

    /**
     * profile框架 传过来的user
     */
    public void updateUser(UserDetail userDetail) {
        this.userDetail = userDetail;
        this.user = userDetail.user;
        this.isUserSelf = user.uid == PrefUtil.Instance().getUserId();
        if (this.isAdded()) {
            setDataToView();
        }
    }

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    public void setCommentEnable(boolean commentEnable) {
        this.commentEnable = commentEnable;
        Object blockView = llCoverContainer.findViewWithTag(SimpleBlock.TYPE_USER_COMMENT);
        if (blockView != null && blockView instanceof UserCommentBlock) {
            ((UserCommentBlock) blockView).setCommentEnable(commentEnable);
        }
    }

}
