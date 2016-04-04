package me.mathiasluo.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.base.BaseActivity;
import me.mathiasluo.page.setting.SettingFragment;

public class DiningSettingActivity extends BaseActivity {

    @InjectView(R.id.fagment)
    FrameLayout mFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        init();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_dining_setting;
    }

    private void init() {
        Fragment fragemnt = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "setting");
        fragemnt.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fagment, fragemnt)
                .commit();
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

    @Override
    public void setToolbar(Toolbar toolbar) {
        super.setToolbar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiningSettingActivity.this.finish();
            }
        });
    }
}
