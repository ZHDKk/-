package com.example.sumec.wash.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sumec.wash.R;
import com.example.sumec.wash.activity.GameActivity;
import com.example.sumec.wash.activity.MainActivity;
import com.example.sumec.wash.activity.WebActivity;
import com.example.sumec.wash.base.BaseFragment;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by zhdk on 2019/1/14.
 */

public class FindFragment extends BaseFragment implements View.OnClickListener {
    private View findView;
    private LinearLayout onlineShopLl, gameLl, shareLl;

    @Override
    public View initView(LayoutInflater inflater) {
        if (findView == null) {
            findView = inflater.inflate(R.layout.fragment_find, null);
        } else {
            return findView;
        }
        return findView;
    }

    @Override
    protected void initFindViewById(View view) {
        onlineShopLl = (LinearLayout) view.findViewById(R.id.onlineShopLl);
        gameLl = (LinearLayout) view.findViewById(R.id.gameLl);
        shareLl = (LinearLayout) view.findViewById(R.id.shareLl);
        onlineShopLl.setOnClickListener(this);
        gameLl.setOnClickListener(this);
        shareLl.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.onlineShopLl:
                startActivityParam(getActivity(), WebActivity.class, "title", "京东", "url", "https://shop.m.jd.com/?shopId=769958");
                break;
            case R.id.gameLl:
                startActivity(getActivity(), GameActivity.class);
                break;
            case R.id.shareLl:
                showShare();
                break;
        }
    }

    private void showShare() {
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
////        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("清洗机分享功能测试");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
////        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("佳孚无线自带水桶高压清洗机");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("https://shop.m.jd.com/?shopId=769958");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
////        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("36V");
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("https://shop.m.jd.com/?shopId=769958");
//
//// 启动分享GUI
//        oks.show(getActivity());


        OnekeyShare oks = new OnekeyShare();
        /*oks.addHiddenPlatform(QQ.NAME);
        oks.setSilent(true);*/
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setText("玩美夏日，护肤也要肆意玩酷！" + "www.mob.com");
//                    paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
                    paramsToShare.setImagePath("/storage/emulated/0/test.jpg");
                }
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setTitle("标题");
                    paramsToShare.setText("我是共用的参数，这几个平台都有text参数要求，提取出来啦");
                    /*Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                    paramsToShare.setImageData(imageData);*/
                    paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
                    paramsToShare.setUrl("http://sharesdk.cn");
//                    paramsToShare.setWxMiniProgramType(0);
                   /* paramsToShare.setWxPath("这个也可以在gralde文件中设置");
                    paramsToShare.setWxUserName("这个也可以在gralde文件中设置");*/
//                    paramsToShare.setWxWithShareTicket(true);
//                    paramsToShare.setShareType(Platform.OPEN_WXMINIPROGRAM);
//                    paramsToShare.setShareType(Platform.SHARE_WXMINIPROGRAM);
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                    Log.d("ShareSDK", paramsToShare.toMap().toString());
                    //Toast.makeText(MainActivity.this, "点击微信分享啦", Toast.LENGTH_SHORT).show();
                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setTitle("标题");
                    paramsToShare.setText("我是共用的参数，这几个平台都有text参数要求，提取出来啦");
                    paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
                    paramsToShare.setUrl("http://sharesdk.cn");
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
                if ("QQ".equals(platform.getName())) {
                    paramsToShare.setTitle("标题");
                    paramsToShare.setTitleUrl("http://sharesdk.cn");
                    paramsToShare.setText("我是共用的参数，这几个平台都有text参数要求，提取出来啦");
                    paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                    paramsToShare.setImagePath("/storage/emulated/0/abcd.gif");
                    Log.d("ShareSDK", paramsToShare.toMap().toString());
                }
            }
        });
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d("ShareLogin", "onComplete ---->  分享成功");
                platform.getName();
                Toast.makeText(getActivity(), "HHHHHHHHHH", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("ShareLogin", "onError ---->  失败" + throwable.getStackTrace());
                Log.d("ShareLogin", "onError ---->  失败" + throwable.getMessage());
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("ShareLogin", "onCancel ---->  分享取消");
            }
        });

// 启动分享GUI
        oks.show(getActivity());
    }


}
