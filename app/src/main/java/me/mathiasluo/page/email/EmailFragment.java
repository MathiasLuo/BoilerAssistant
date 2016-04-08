package me.mathiasluo.page.email;

import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.BaseFragment;
import me.mathiasluo.widget.ProgressWeb;

/**
 * Created by mathiasluo on 16-3-27.
 */
public class EmailFragment extends BaseFragment {


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.webView)
    ProgressWeb mWebView;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_web;
    }


    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setTitle(getString(R.string.email));
        ((MainActivity) getActivity()).setToolbar(mToolbar);


        mWebView.getWebView().setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
         mWebView.loadUrl("https://outlook.live.com");
       /* mWebView.loadUrl("https://login.microsoftonline.com");*/
    }
}
