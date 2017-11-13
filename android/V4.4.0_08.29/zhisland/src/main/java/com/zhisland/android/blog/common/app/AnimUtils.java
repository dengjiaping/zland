package com.zhisland.android.blog.common.app;

import android.app.Activity;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.controller.ActRegisterAndLogin;
import com.zhisland.android.blog.aa.controller.FragCreateBasicInfo;
import com.zhisland.android.blog.aa.controller.FragLoginByPwd;
import com.zhisland.android.blog.aa.controller.FragLoginByVerifyCode;
import com.zhisland.android.blog.aa.controller.FragPrivacy;
import com.zhisland.android.blog.aa.controller.FragVisitContactsRefuse;
import com.zhisland.android.blog.common.view.select.FragSelectActivity;
import com.zhisland.android.blog.contacts.controller.FragContactList;
import com.zhisland.android.blog.event.controller.ActEventCancelReasion;
import com.zhisland.android.blog.event.controller.ActEventCreate;
import com.zhisland.android.blog.event.controller.ActEventDetail;
import com.zhisland.android.blog.event.controller.ActSignUpEvents;
import com.zhisland.android.blog.event.controller.FragEditCategory;
import com.zhisland.android.blog.event.controller.FragEventOfflinePayment;
import com.zhisland.android.blog.event.controller.FragEventOnlinePayment;
import com.zhisland.android.blog.event.controller.FragEventSpread;
import com.zhisland.android.blog.event.controller.FragInitiatedEvents;
import com.zhisland.android.blog.event.controller.FragResultPage;
import com.zhisland.android.blog.event.controller.FragSignConfirm;
import com.zhisland.android.blog.event.controller.FragSignUpEvents;
import com.zhisland.android.blog.event.controller.FragSignUpMembers;
import com.zhisland.android.blog.event.controller.FragSignedList;
import com.zhisland.android.blog.feed.view.impl.ActCreateFeed;
import com.zhisland.android.blog.feed.view.impl.ActFeedDetail;
import com.zhisland.android.blog.find.controller.ActAllBoss;
import com.zhisland.android.blog.find.controller.ActAllRes;
import com.zhisland.android.blog.find.controller.ActContactFriend;
import com.zhisland.android.blog.find.controller.ActSearch;
import com.zhisland.android.blog.find.controller.FragBossHome;
import com.zhisland.android.blog.find.controller.FragContactNear;
import com.zhisland.android.blog.freshtask.view.impl.FragAddResource;
import com.zhisland.android.blog.freshtask.view.impl.FragContactNoPower;
import com.zhisland.android.blog.freshtask.view.impl.FragFriendAdd;
import com.zhisland.android.blog.freshtask.view.impl.FragFriendCall;
import com.zhisland.android.blog.freshtask.view.impl.FragFriendComment;
import com.zhisland.android.blog.freshtask.view.impl.FragIntroduction;
import com.zhisland.android.blog.freshtask.view.impl.FragInviteRequest;
import com.zhisland.android.blog.freshtask.view.impl.FragInviteRequestNoPower;
import com.zhisland.android.blog.im.controller.ActChat;
import com.zhisland.android.blog.info.view.impl.ActInfoDetail;
import com.zhisland.android.blog.info.view.impl.ActReportType;
import com.zhisland.android.blog.info.view.impl.FragCommentDetail;
import com.zhisland.android.blog.info.view.impl.FragLinkEdit;
import com.zhisland.android.blog.info.view.impl.FragRecommendGuide;
import com.zhisland.android.blog.info.view.impl.FragRecommendOk;
import com.zhisland.android.blog.info.view.impl.FragRecommendReason;
import com.zhisland.android.blog.info.view.impl.FragReportOk;
import com.zhisland.android.blog.info.view.impl.FragReportReason;
import com.zhisland.android.blog.invitation.view.impl.FragApproveHaiKe;
import com.zhisland.android.blog.invitation.view.impl.FragHaikeConfirm;
import com.zhisland.android.blog.invitation.view.impl.FragInvitationDealedList;
import com.zhisland.android.blog.invitation.view.impl.FragInviteByPhone;
import com.zhisland.android.blog.invitation.view.impl.FragInviteRegist;
import com.zhisland.android.blog.invitation.view.impl.FragReqApproveFriends;
import com.zhisland.android.blog.invitation.view.impl.FragSearchInvite;
import com.zhisland.android.blog.message.view.impl.FragFansMessageList;
import com.zhisland.android.blog.message.view.impl.FragInteractionMessage;
import com.zhisland.android.blog.message.view.impl.FragSystemMessageList;
import com.zhisland.android.blog.profile.controller.ActProfileIntroduction;
import com.zhisland.android.blog.profile.controller.ActSelTags;
import com.zhisland.android.blog.profile.controller.FragProfileInterests;
import com.zhisland.android.blog.profile.controller.FragProfileIntroduction;
import com.zhisland.android.blog.profile.controller.FragProfileSpecialty;
import com.zhisland.android.blog.profile.controller.FragProfileWantFriend;
import com.zhisland.android.blog.profile.controller.FragSelectTagsList;
import com.zhisland.android.blog.profile.controller.FragSelfCard;
import com.zhisland.android.blog.profile.controller.comment.ActUserCommentList;
import com.zhisland.android.blog.profile.controller.comment.ActWriteUserComment;
import com.zhisland.android.blog.profile.controller.comment.FragUserCommentEdit;
import com.zhisland.android.blog.profile.controller.contact.FragUserAssistant;
import com.zhisland.android.blog.profile.controller.contact.FragUserContactInfo;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profile.controller.drip.ActUserDripList;
import com.zhisland.android.blog.profile.controller.honor.FragHonorEdit;
import com.zhisland.android.blog.profile.controller.honor.FragUserHonor;
import com.zhisland.android.blog.profile.controller.position.ActCompanyDetail;
import com.zhisland.android.blog.profile.controller.position.ActUserCompany;
import com.zhisland.android.blog.profile.controller.position.FragUserCompanyCreateOrUpdate;
import com.zhisland.android.blog.profile.controller.position.FragUserCompanySimpleAdd;
import com.zhisland.android.blog.profile.controller.position.FragUserCompanySimpleEdit;
import com.zhisland.android.blog.profile.controller.resource.FragDemandEdit;
import com.zhisland.android.blog.profile.controller.resource.FragSupplyEdit;
import com.zhisland.android.blog.profile.controller.resource.FragUserResource;
import com.zhisland.android.blog.profilemvp.view.impl.ActAddPhoto;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyAttention;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyFans;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyHot;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyPhoto;
import com.zhisland.android.blog.setting.controller.ActModifyPwd;
import com.zhisland.android.blog.setting.controller.FragAboutUs;
import com.zhisland.android.blog.setting.controller.FragFeedBack;
import com.zhisland.android.blog.setting.controller.FragSetVisiable;
import com.zhisland.android.blog.setting.controller.FragSettings;

import java.util.HashMap;

public class AnimUtils {

	private static final int RIGHT = 0;
	private static final int LEFT = 3;
	private static final int BOTTOM_UP = 1;
	private static final int FADE_IN_FADE_OUT = 2;
	private static final HashMap<String, Integer> MAP_ANIMS = new HashMap<String, Integer>();
	static {
		MAP_ANIMS.put(FragSelectActivity.class.getName(), RIGHT);
		MAP_ANIMS.put(ActProfileIntroduction.class.getName(), RIGHT);
		MAP_ANIMS.put(ActSelTags.class.getName(), RIGHT);

		MAP_ANIMS.put(ActEventCancelReasion.class.getName(), RIGHT);
		MAP_ANIMS.put(ActEventCreate.class.getName(), RIGHT);
		MAP_ANIMS.put(FragEditCategory.class.getName(), RIGHT);
		MAP_ANIMS.put(FragInitiatedEvents.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSignUpEvents.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSignUpMembers.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSignedList.class.getName(), RIGHT);
		MAP_ANIMS.put(FragResultPage.class.getName(), RIGHT);
		MAP_ANIMS.put(FragEventSpread.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSignConfirm.class.getName(), RIGHT);

		MAP_ANIMS.put(ActEventDetail.class.getName(), RIGHT);
		MAP_ANIMS.put(FragContactNear.class.getName(), RIGHT);
		MAP_ANIMS.put(FragContactList.class.getName(), RIGHT);
		MAP_ANIMS.put(FragProfileInterests.class.getName(), RIGHT);
		MAP_ANIMS.put(FragProfileIntroduction.class.getName(), RIGHT);
		MAP_ANIMS.put(FragProfileSpecialty.class.getName(), RIGHT);
		MAP_ANIMS.put(FragProfileWantFriend.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSelectTagsList.class.getName(), RIGHT);
		MAP_ANIMS.put(ActModifyPwd.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSettings.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSetVisiable.class.getName(), RIGHT);
		MAP_ANIMS.put(FragAboutUs.class.getName(), RIGHT);
		MAP_ANIMS.put(FragFeedBack.class.getName(), RIGHT);
		MAP_ANIMS.put(FragPrivacy.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSelfCard.class.getName(), RIGHT);

		MAP_ANIMS.put(ActChat.class.getName(), RIGHT);

		MAP_ANIMS.put(FragUserCompanySimpleEdit.class.getName(), RIGHT);
		MAP_ANIMS.put(FragUserCompanySimpleAdd.class.getName(), RIGHT);
		MAP_ANIMS.put(FragUserCommentEdit.class.getName(), RIGHT);
		MAP_ANIMS.put(ActUserCommentList.class.getName(), RIGHT);
		MAP_ANIMS.put(ActWriteUserComment.class.getName(), RIGHT);

		MAP_ANIMS.put(FragHonorEdit.class.getName(), RIGHT);
		MAP_ANIMS.put(FragUserHonor.class.getName(), RIGHT);
		MAP_ANIMS.put(FragDemandEdit.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSupplyEdit.class.getName(), RIGHT);
		MAP_ANIMS.put(FragUserResource.class.getName(), RIGHT);
		MAP_ANIMS.put(FragUserAssistant.class.getName(), RIGHT);
		MAP_ANIMS.put(FragUserContactInfo.class.getName(), RIGHT);

		MAP_ANIMS.put(ActUserCompany.class.getName(), RIGHT);
		MAP_ANIMS.put(ActCompanyDetail.class.getName(), RIGHT);
		MAP_ANIMS.put(FragUserCompanyCreateOrUpdate.class.getName(), RIGHT);
		MAP_ANIMS.put(ActUserDripList.class.getName(), RIGHT);
		MAP_ANIMS.put(ActProfileDetail.class.getName(), RIGHT);
		MAP_ANIMS.put(FragMyPhoto.class.getName(), RIGHT);
        MAP_ANIMS.put(FragEventOnlinePayment.class.getName(), RIGHT);
        MAP_ANIMS.put(FragEventOfflinePayment.class.getName(), RIGHT);
		MAP_ANIMS.put(ActSignUpEvents.class.getName(), RIGHT);
		MAP_ANIMS.put(FragMyAttention.class.getName(), RIGHT);
		MAP_ANIMS.put(FragMyFans.class.getName(), RIGHT);
		MAP_ANIMS.put(FragMyHot.class.getName(), RIGHT);
		MAP_ANIMS.put(ActAddPhoto.class.getName(), RIGHT);

		MAP_ANIMS.put(FragCreateBasicInfo.class.getName(), RIGHT);
        MAP_ANIMS.put(FragVisitContactsRefuse.class.getName(), RIGHT);
        MAP_ANIMS.put(FragInviteByPhone.class.getName(), RIGHT);
        MAP_ANIMS.put(FragLoginByVerifyCode.class.getName(), RIGHT);
        MAP_ANIMS.put(FragLoginByPwd.class.getName(), RIGHT);
        MAP_ANIMS.put(FragInviteRegist.class.getName(), RIGHT);

		MAP_ANIMS.put(ActSearch.class.getName(), RIGHT);
		MAP_ANIMS.put(ActContactFriend.class.getName(), RIGHT);
		MAP_ANIMS.put(FragBossHome.class.getName(), RIGHT);
		MAP_ANIMS.put(ActAllBoss.class.getName(), RIGHT);
		MAP_ANIMS.put(ActAllRes.class.getName(), RIGHT);

		MAP_ANIMS.put(FragAddResource.class.getName(), RIGHT);
		MAP_ANIMS.put(FragIntroduction.class.getName(), RIGHT);
		MAP_ANIMS.put(FragFriendAdd.class.getName(), RIGHT);
		MAP_ANIMS.put(FragFriendCall.class.getName(), RIGHT);
		MAP_ANIMS.put(FragContactNoPower.class.getName(), RIGHT);
		MAP_ANIMS.put(FragFriendComment.class.getName(), RIGHT);

		MAP_ANIMS.put(ActInfoDetail.class.getName(), RIGHT);
		MAP_ANIMS.put(FragCommentDetail.class.getName(), RIGHT);
		MAP_ANIMS.put(FragLinkEdit.class.getName(), RIGHT);
		MAP_ANIMS.put(FragRecommendGuide.class.getName(), RIGHT);
		MAP_ANIMS.put(FragRecommendOk.class.getName(), RIGHT);
		MAP_ANIMS.put(FragRecommendReason.class.getName(), RIGHT);
		MAP_ANIMS.put(FragReportOk.class.getName(), RIGHT);
		MAP_ANIMS.put(FragReportReason.class.getName(), RIGHT);
		MAP_ANIMS.put(ActReportType.class.getName(), RIGHT);
		MAP_ANIMS.put(ActRegisterAndLogin.class.getName(), RIGHT);
		MAP_ANIMS.put(FragInviteRequest.class.getName(), RIGHT);
		MAP_ANIMS.put(FragInviteRequestNoPower.class.getName(), RIGHT);
		MAP_ANIMS.put(FragHaikeConfirm.class.getName(), RIGHT);

		MAP_ANIMS.put(FragSearchInvite.class.getName(), RIGHT);
		MAP_ANIMS.put(FragApproveHaiKe.class.getName(), RIGHT);
		MAP_ANIMS.put(FragReqApproveFriends.class.getName(), RIGHT);
		MAP_ANIMS.put(FragInvitationDealedList.class.getName(), RIGHT);
		MAP_ANIMS.put(ActCreateFeed.class.getName(), RIGHT);
		MAP_ANIMS.put(ActFeedDetail.class.getName(), RIGHT);
		MAP_ANIMS.put(FragFansMessageList.class.getName(), RIGHT);
		MAP_ANIMS.put(FragInteractionMessage.class.getName(), RIGHT);
		MAP_ANIMS.put(FragSystemMessageList.class.getName(), RIGHT);
		MAP_ANIMS.put(com.zhisland.android.blog.feed.view.impl.FragCommentDetail.class.getName(), RIGHT);
	}

	/**
	 *
	 * @param activity
	 * @param anim1
	 *            animation to in
	 * @param anim2
	 *            animation to out
	 */
	private static void configActivity(Activity activity, int anim1, int anim2) {
		if (activity.getParent() == null) {
			activity.overridePendingTransition(anim1, anim2);
		} else {
			activity.getParent().overridePendingTransition(anim1, anim2);
		}
	}

	public static void configAnim(Activity activity, String className,
			boolean entered) {

		int type = FADE_IN_FADE_OUT; // default animation type
		if (MAP_ANIMS.containsKey(className)) {
			type = MAP_ANIMS.get(className);
		}
		int anim1, anim2;
		if (entered) {
			// start activity

			switch (type) {
			case LEFT: {
				anim1 = R.anim.act_left_in;
				anim2 = R.anim.act_hold;
				break;
			}
			case RIGHT: {
				anim1 = R.anim.act_right_in;
				anim2 = R.anim.act_hold;
				break;
			}
			case BOTTOM_UP: {
				anim1 = R.anim.act_bottom_in;
				anim2 = R.anim.act_hold;
				break;
			}
			case FADE_IN_FADE_OUT:
			default: {
				anim1 = android.R.anim.fade_in;
				anim2 = R.anim.act_hold;
				break;
			}
			}

		} else {
			// stop activity
			switch (type) {
			case LEFT: {
				anim1 = R.anim.act_hold;
				anim2 = R.anim.act_left_out;
				break;
			}
			case RIGHT: {
				anim1 = R.anim.act_hold;
				anim2 = R.anim.act_right_out;
				break;
			}
			case BOTTOM_UP: {
				anim1 = R.anim.act_hold;
				anim2 = R.anim.act_top_out;
				break;
			}
			case FADE_IN_FADE_OUT: {
				anim1 = R.anim.act_hold;
				anim2 = R.anim.act_fade_out;
				break;
			}
			default: {
				anim1 = R.anim.act_hold;
				anim2 = R.anim.act_pop_out;
				break;
			}
			}
		}
		AnimUtils.configActivity(activity, anim1, anim2);
	}
}
