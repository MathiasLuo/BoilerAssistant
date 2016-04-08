package me.mathiasluo.page.textbook;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.List;

import butterknife.InjectView;
import me.mathiasluo.APP;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.BaseFragment;
import me.mathiasluo.page.calendar.bean.DataModel;
import me.mathiasluo.widget.ProgressWeb;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class TextBookFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_textbook;
    }


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.webView)
    ProgressWeb mWebView;
    @InjectView(R.id.fab)
    FloatingActionButton floatingActionButton;

    String BASEURL = "https://www.amazon.cn/s/ref=nb_sb_ss_i_5_4?__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99&url=search-alias%3Dstripbooks&field-keywords=purdue ";


    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setTitle(getString(R.string.textbook));
        ((MainActivity) getActivity()).setToolbar(mToolbar);
        floatingActionButton.setOnClickListener(this);

        mWebView.getWebView().setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl("https://www.amazon.cn/%E5%9B%BE%E4%B9%A6/b?ie=UTF8&node=658390051");
       /* mWebView.loadUrl("https://login.microsoftonline.com");*/
        mWebView.loadUrl(BASEURL + "大学语文");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showSelect();
                break;
        }
    }

    private void showSelect() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.addCalendarEvent)
                .theme(Theme.LIGHT)
                .items(R.array.selectBook)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();
                                showListBooks();
                                break;
                            case 1:
                                dialog.dismiss();
                                showEditBook();
                                break;
                            case -1:
                                Toast.makeText(getActivity(), getString(R.string.todo), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
    }


    private void showListBooks() {
        List<String> names = DataModel.getAllBookName();
        if (names == null) {
            Toast.makeText(getActivity(), getString(R.string.alert_booksname), Toast.LENGTH_SHORT).show();
        } else {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.book_title)
                    .theme(Theme.LIGHT)
                    .items(names)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            mWebView.loadUrl(BASEURL + text+"textbook");
                        }
                    })
                    .show();
        }


    }


    private void showEditBook() {

        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.book_title)
                .customView(R.layout.editbook, wrapInScrollView).build();

        View view = dialog.getCustomView();
        EditText edit = (EditText) view.findViewById(R.id.edit);
        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_btn);
        cancelButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        cancelButton.setOnClickListener(view1 -> dialog.dismiss());

        TextView saveButton = (TextView) view.findViewById(R.id.save_btn);
        saveButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mWebView.loadUrl(BASEURL + edit.getText().toString()+"textbook");
            }
        });

        dialog.show();

    }

}
