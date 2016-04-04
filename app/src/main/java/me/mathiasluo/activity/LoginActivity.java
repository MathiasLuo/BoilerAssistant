package me.mathiasluo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.mathiasluo.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.login_progress)
    ProgressBar mProgressBar;
    @InjectView(R.id.username)
    AutoCompleteTextView mUserName;
    @InjectView(R.id.password)
    AutoCompleteTextView mPassword;
    @InjectView(R.id.email_sign_in_button)
    Button mLoginButton;

    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initView();
       // StatusBarCompat.compat(this, R.color.primary_dark);
    }


    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.primary_dark));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    public void initView() {
        setSupportActionBar(mToolbar);
        mLoginButton.setOnClickListener(this);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                if (!mUserName.getText().toString().isEmpty() &&
                        !mPassword.getText().toString().isEmpty()) {
                    userName = mUserName.getText().toString().toLowerCase();
                    password = mPassword.getText().toString();
                    logInWithConfig();
                } else {
                    showSnackBar(this, getResources().getString(R.string.form_error));
                }


                break;
        }
    }

    protected void logInWithConfig() {
        showLoading();


    }

    private void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showSnackBar(Context context, String msg) {
        View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar
                .make(rootView, R.string.error_title, Snackbar.LENGTH_LONG)
                .setText(msg)
                .show();
    }
}

