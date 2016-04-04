package me.mathiasluo.widget;

import android.app.DownloadManager;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import me.mathiasluo.R;

/**
 * Created by MathiasLuo on 2016/3/9.
 */
public class ProgressWeb extends SwipeRefreshLayout {
    WebView mWebView;

    DownloadManager downloadManager;
    public ProgressWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mWebView = new WebView(context);
        mWebView.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mWebView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebView.getSettings().setAppCacheEnabled(true);//是否使用缓存
        mWebView.getSettings().setDomStorageEnabled(true);//DOM Storage
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        addView(mWebView);
        initWebView();
    }
    public void initWebView() {
        setWebChromeClient(new ProgressWebChomeClient(this));
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.loadUrl(mWebView.getUrl());
            }
        });
        setColorSchemeColors(R.color.open);
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void setWebChromeClient(WebChromeClient wenChomeClient) {
        mWebView.setWebChromeClient(wenChomeClient);
    }

    public void setWebClient(WebViewClient webClient) {
        mWebView.setWebViewClient(webClient);
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public class ProgressWebChomeClient extends WebChromeClient {
        ProgressWeb mProgressWeb;

        public ProgressWebChomeClient(ProgressWeb mProgressWeb) {
            this.mProgressWeb = mProgressWeb;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressWeb.setRefreshing(false);
            } else if (!mProgressWeb.isRefreshing()) mProgressWeb.setRefreshing(true);

            super.onProgressChanged(view, newProgress);
        }
    }

    private void syncCookie(Context context, String url) {
        try {
            Log.d("Nat: webView.syncCookie.url", url);

            CookieSyncManager.createInstance(context);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", "INPUT YOUR JSESSIONID STRING"));
            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s", "INPUT YOUR PATH STRING"));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                Log.d("Nat: webView.syncCookie.newCookie", newCookie);
            }
        } catch (Exception e) {
            Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }
}
