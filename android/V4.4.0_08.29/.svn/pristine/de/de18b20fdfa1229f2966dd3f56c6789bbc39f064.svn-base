package com.zhisland.android.blog.wxapi;

import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhisland.android.blog.common.util.WechatUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zxinsight.share.activity.MWWXHandlerActivity;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends MWWXHandlerActivity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, WechatUtil.WE_CHAT_APP_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            handleShareResp(resp);
        }
    }

    /**
     * 分享到微信的结果处理
     */
    private void handleShareResp(BaseResp resp) {
        finish();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                // 分享成功
                ToastUtil.showShort("分享成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // 分享取消
                ToastUtil.showShort("分享取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                // 分享拒绝
                ToastUtil.showShort("分享拒绝");
                break;
        }
    }
}
