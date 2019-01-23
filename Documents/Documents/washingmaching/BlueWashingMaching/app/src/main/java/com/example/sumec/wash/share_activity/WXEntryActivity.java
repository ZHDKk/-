package com.example.sumec.wash.share_activity;


import android.content.Intent;

import com.example.sumec.wash.utils.ToastUtil;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

public class WXEntryActivity extends WechatHandlerActivity {
    @Override
    public void onGetMessageFromWXReq(WXMediaMessage msg) {

        Intent launchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(launchMyself);
    }

    @Override
    public void onShowMessageFromWXReq(WXMediaMessage msg) {

        if (msg != null && msg.mediaObject != null && (msg.mediaObject instanceof WXAppExtendObject)) {

            WXAppExtendObject object = (WXAppExtendObject) msg.mediaObject;
            ToastUtil.showToast(this, object.extInfo);
        }
    }
}
