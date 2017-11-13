package com.zhisland.android.blog.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhisland.android.blog.common.util.WechatUtil;
import com.zhisland.android.blog.wxapi.eb.EBWxPayRes;

import de.greenrobot.event.EventBus;

/**
 * 微信客户端回调activity示例
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, WechatUtil.WE_CHAT_APP_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            handlePayResp(resp);
        }
    }

    /**
     * 微信支付的结果处理
     */
    private void handlePayResp(BaseResp resp) {
        finish();
        if(resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){
            EventBus.getDefault().post(new EBWxPayRes(EBWxPayRes.RESULT_CANCEL, null));
        }else if(resp.errCode == BaseResp.ErrCode.ERR_OK){
            EventBus.getDefault().post(new EBWxPayRes(EBWxPayRes.RESULT_OK, null));
        }else { //非成功或取消的状态，都视为失败。
            EventBus.getDefault().post(new EBWxPayRes(EBWxPayRes.RESULT_FAIL, null));
        }
    }
}