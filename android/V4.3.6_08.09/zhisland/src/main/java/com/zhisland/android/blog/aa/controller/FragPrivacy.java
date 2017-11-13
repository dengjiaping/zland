package com.zhisland.android.blog.aa.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

/**
 * 用户协议
 */
public class FragPrivacy extends FragBase{

	public static void invoke(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragPrivacy.class;
		param.enableBack = true;
		param.title = "用户协议";
		Intent intent = CommonFragActivity.createIntent(context, param);
		context.startActivity(intent);
	}
	
	@Override
	protected String getPageName() {
		return "EntranceRegisterAgreement";
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ScrollView scrollView = new ScrollView(getActivity());
		scrollView.setBackgroundColor(getResources().getColor(R.color.color_bg1));
		TextView textView = new TextView(getActivity());
		textView.setPadding(DensityUtil.dip2px(15), DensityUtil.dip2px(20), DensityUtil.dip2px(15), DensityUtil.dip2px(20));
		textView.setText(getContent());
		textView.setAutoLinkMask(Linkify.ALL);
		textView.setTextColor(getResources().getColor(R.color.color_f1));
		DensityUtil.setTextSize(textView, R.dimen.txt_20);
		scrollView.addView(textView);
		return scrollView;
	}
	
	public String getContent(){
		String str = "　　本《\"正和岛”用户许可协议》（\"本协议”）是您与北京正和岛信息技术有限公司(\"北京正和岛”)之间有约束力的合法协议。您为安卓、iOS或其他移动平台（视适用者而定）下载、安装或使用本应用程序（\"本软件”），即代表您同意接受本协议各项条款的约束。如您不接受本协议，则请不要点击\"我接受协议条款”方框，并且请不要使用本软件。\n\n您同意，安装或使用本软件表明您已经阅读、理解本协议并同意接受本协议的约束。\n\n本软件在本协议项下提供给您，仅供您用于私人、非商业目的。机构使用本软件或\"正和岛”内容、信息、用户功能或任何其他服务（\"正和岛服务”）或使用本软件的多个拷贝（不包括备份拷贝）需要取得软件的商业许可。\n\n1. 本软件说明\n\n本软件是一款可以下载的软件应用程序，通过使用本软件，您能够直接从您的安卓、iPhone、iPad或北京正和岛支持的其他移动设备（\"设备”）上直接访问 \"正和岛”功能。 无论您是否使用正和岛服务，您均可下载本软件，但是您必须将本软件与您的\"正和岛”账户关联，才能启用本软件的全部功能。\n\n2. 许可\n\n根据本协议条款和条件，北京正和岛在此向您授予一项非排他的、不可转让的个人许可，准许您：\n\n•	为个人目的使用本软件；\n\n•	将本软件安装到单独一台设备上；\n\n为明确起见，前文内容并非意在禁止您在下列其他设备上安装或备份本软件，即：您已经在该等设备上同意接受本协议。您每同意一次本协议，即授予您上述的在一台设备上安装、使用和备份一份本软件的权利。\n\n3. 知识产权\n\n•	\"知识产权”是指与正和岛服务相关的各种类的过去有效的、现行有效的、或即将产生的知识产权，包括但不限于发明专利、商标、著作权、实用新型、外观设计、布图设计、商业秘密、其他知识产权以及相关申请的权利。\n\n•	北京正和岛为与正和岛服务相关的全部产权的权利人，对正和岛服务提供过程中包含的全部知识产权，包括但不限于任何文本、图片、图形、音频和/或视频资料享有及保留完整、独立的全部权利。未经正和岛公司同意，用户不得在任何媒体直接或间接发布、播放、出于播放或发布目的而改写或再发行正和岛公司享有知识产权的或者正和岛服务提供任何资料和信息，也不得将前述资料和信息用于任何商业目的。\n\n•	对于用户本人创作并上传到正和岛平台的任何文本、图片、图形、音频和视频等，正和岛公司保留对其内容进行实时监控的权利，并有权依正和岛公司独立判断对任何违反本协议约定或涉嫌违法、违规的内容实施删除。正和岛公司对于删除用户作品引起的任何后果或导致用户的任何损失不负任何责任。\n\n•	本条款非经正和岛公司书面同意将持续有效，不因用户关闭正和岛服务账户或者停止使用正和岛服务而失效。\n\n4. 限制\n\n您理解并同意，您对本软件的使用应遵守中华人民共和国所有适用的法律法规。\n\n您不得：\n\n•	基于本软件创作衍生作品；\n\n•	将本软件用于与您的\"正和岛”账户无关的其他任何目的；\n\n•	拷贝或复制本软件，但按照本协议中所述的方式进行的除外；\n\n•	以任何形式向任何第三方出售、转让、许可、披露、散布或以其他方式转让或提供本软件；\n\n•	对本软件进行篡改、翻译、反编译、反汇编或反向工程，且亦不得试图做出上述任何行为；\n\n•	删除或篡改本软件上的任何专有权声明或标记。\n\n5. 个人信息及隐私保护\n\n在您下载、使用本软件的过程中，我们可能会请您提供有关您的某些信息。您提供给我们的所有个人信息均适用《\"正和岛”用户服务协议及隐私政策》。选择使用本软件和/或正和岛服务，表明您理解并接受《\"正和岛”用户服务协议及隐私政策》。" +
				"您理解并同意，如法律要求北京正和岛进行有关披露，或若北京正和岛有合理依据认为有关披露是为了遵守法律法规的规定、强制执行本协议的条款或为了保护北京正和岛、其用户或公众的合法权利、财产或人身安全所必需的，北京正和岛可以披露有关信息。\n\n6．用户信息条款\n\n6.1、用户个人信息。用户个人信息包括下列信息：用户真实姓名，公司，职务，头像，手机号码，IP地址等。\n\n6.2、非用户个人信息。用户对正和岛服务的操作状态、使用记录、使用习惯等反映在北京正和岛服务器端的全部记录信息，及其他一切本条第1款所述的用户个人信息范围外的信息，均为普通信息，均不属于用户个人信息。\n\n6.3、重要提示：为向客户提供正和岛服务，北京正和岛将可能合理使用用户个人信息、非用户个人信息（以下合称\"用户信息”）。用户一旦注册、登陆、使用正和岛服务，将视为用户完全了解、同意并接受北京正和岛通过包括但不限于收集、统计、分析、使用等方式合理使用用户信息。\n\n6.4、用户个人信息的使用目的、方式和范围。用户承诺其已完全了解：北京正和岛使用用户信息的目的在于：为用户提供包括好友、部落圈子、活动、资讯、印象、人脉分布和关系链等正和岛服务的各项功能；北京正和岛使用用户信息的方式包括但不限于：收集、统计、分析、商业用途的使用等方式；北京正和岛使用用户信息的范围包括但不限于：本条第1、2款所定义的用户个人信息、非用户个人信息等。\n\n6.5、用户查询、更正、添加信息的渠道。用户可通过正和岛服务中的【修改资料】相关功能查询、更正、添加其授权正和岛服务使用的用户信息。\n\n6.6、用户拒绝提供信息的方式及后果。用户可以通过停止使用正和岛服务，不再向北京正和岛提供用户信息。但是，用户此前同意北京正和岛使用的用户信息，北京正和岛不承担主动删除、销毁的责任。用户拒绝提供用户信息的，北京正和岛可以随时停止提供服务。\n\n6.7、用户授权的明确性。用户注册、登陆、使用正和岛服务的行为，即视为明确同意北京正和岛收集和使用其用户信息，无需其他意思表示。北京正和岛对用户信息的使用无需向用户支付任何费用。\n\n6.8、特别说明：用户信息删除的选择权。正和岛注册用户在使用过程中发现有任何不妥或者不满意之处，有权通过服务经理或者电话的方式提出申请，进行相关信息删除。（附：电话：4001009737。）\n\n6.9、北京正和岛承诺。北京正和岛尊重授权用户的合法权利，尊重授权用户的自由选择权，不会以违反法律、行政法规以及本协议约定的方式收集、使用用户信息。\n\n7．正和岛服务使用规则\n\n7.1、用户完全了解并同意：用户对以其帐号注册、登陆、使用正和岛服务的全部行为，包括其提供给北京正和岛的全部用户信息、以其正和岛服务账号进行的所有行为和事件独立承担完全的法律责任。\n\n7.2、用户注册成功后，应妥善保管其\"正和岛”帐户及密码，定期或不定期修改密码，以确保帐户安全。\n\n7.3、用户同意在使用正和岛服务过程中，严格遵守以下规则：\n\n（1）遵守中国法律、法规、行政规章及规范性文件；\n\n（2）遵守北京正和岛所有与正和岛服务有关的协议、规定、程序、通知、使用守则等全部文件；\n\n（3）不得为任何违法、犯罪目的而使用服务；\n\n（4）不得以任何形式使用正和岛服务侵犯北京正和岛的合法权利；\n\n（5）不得利用正和岛服务进行任何可能对互联网正常运转造成不利影响的行为\n\n（6）不得利用北京正和岛提供的服务上传、展示或传播任何虚假的、骚扰性的、中伤他人的、种族歧视性的、辱骂性的、恐吓性的、情色的或其他任何非法的信息资料；\n\n（7）不得以任何方式侵犯其他任何人依法享有的专利权、著作权、商标权、商业秘密等知识产权，第三人的名誉权或其他任何第三方的合法权益。\n\n（8）保证在正和岛服务上展示的相关信息内容，包括但是不限于在前条\"" +
				"6、用户信息条款”中的内容，没有违反相关法律规定，没有侵犯第三方的合法权利。\n\n7.4、北京正和岛有权对用户使用正和岛服务的情况进行审查和监督，如用户在使用正和岛服务时违反任何上述约定，北京正和岛有权要求用户改正或直接单方面采取一切必要措施，以消除或减轻用户不当行为给北京正和岛、第三方、互联网服务或社会公共利益造成的任何影响。北京正和岛在进行前述操作前，操作过程中或操作完成后不对用户进行任何方式的通知。\n\n7.5、用户在使用北京正和岛各项服务的同时，同意接受北京正和岛发布、推送、提供的各类信息。\n\n7.6、用户同意，北京正和岛有权随时变更、中止或终止部分或全部的正和岛服务，北京正和岛无需就此通知用户，且北京正和岛不因此对用户或任何第三方承担任何责任。\n\n8．适用法律和争议解决\n\n•	本协议的成立、生效、履行、解释及因本协议产生的任何争议，均适用中华人民共和国法律（不包括港澳台地区法律）。\n\n•	用户和北京正和岛之间与本协议有关的任何争议，首先应友好协商解决，在争议发生之日起三十日内仍不能通过协商达成一致的，用户在此完全、不可撤销地同意将前述争议提交北京正和岛信息技术有限公司住所地的人民法院进行诉讼。\n\n9．服务条款的修改\n\n北京正和岛有权不定期的制定、修改本协议及相关服务规则，一旦条款及服务内容、规则产生变动，北京正和岛将会以app公告的方式进行公示。\n\n10．通知和公告\n\n•	北京正和岛向用户发送的所有通告，将通过页面公告、app消息推送、电子邮件或手机短信的方式传送。北京正和岛的服务信息也将不定期通过上述方式向用户发送。" +
				"用户协议条款的修改、服务变更、或其它重要事件的通告都会以此形式进行。\n\n•	" +
				"用户可以选择接受或不接受北京正和岛通过邮件和短信的形式向其发送活动、资讯等信息。用户选择不接受的，可以随时通知北京正和岛取消发送。\n\n11．其他规定\n\n•	如本协议中的任何条款因任何原因完全或部分无效或不具有执行力，均不影响本协议其他条款的效力。\n\n•	本协议及本协议任何条款内容的最终解释权及修改权归北京正和岛所有。若您对北京正和岛及本服务有任何意见，" +
				"欢迎垂询北京正和岛客服中心。服务热线：【4001009737】，服务网站：【http://www.zhisland.com】。";
		return str;
	}
}