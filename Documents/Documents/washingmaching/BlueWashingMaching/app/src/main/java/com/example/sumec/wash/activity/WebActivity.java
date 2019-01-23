package com.example.sumec.wash.activity;


import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.view.ProgressWebView;
import com.example.sumec.wash.view.ToolBarView;

public class WebActivity extends BaseActivity {
    private ToolBarView toolBarView;
    private ProgressWebView mWebView;
    private String url;
    private String title;

    @Override
    public int getContentView() {
        return R.layout.activity_shop;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolbarView();
        mWebView = (ProgressWebView) findViewById(R.id.shopWebView);
        setWebView();
    }

    private void setWebView() {
        mWebView.setInitialScale(25);//设置最小缩放等级
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setSavePassword(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setUseWideViewPort(true);
        initData();
    }

    private void initData() {
        if(url!=null) {
            mWebView.loadUrl(url);
        }
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void setToolbarView() {
        toolBarView.setTvDoneVisibility(View.GONE);
        toolBarView.setTitleText(title);
        toolBarView.setImageMoreVisibility(View.GONE);
       toolBarView.setImageBackOnListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
            else{
                finish();
//                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView!=null) {
            mWebView.removeAllViews();
            ((LinearLayout) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView=null;
        }
    }
}
