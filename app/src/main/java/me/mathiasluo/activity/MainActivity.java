package me.mathiasluo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import javax.inject.Inject;

import butterknife.InjectView;
import icepick.Icicle;
import me.mathiasluo.MapFragment;
import me.mathiasluo.R;
import me.mathiasluo.base.BaseActivity;
import me.mathiasluo.page.calendar.CalendarFragment;
import me.mathiasluo.page.email.EmailFragment;
import me.mathiasluo.page.location.dining.DiningLocationListFragmentBuilder;
import me.mathiasluo.page.news.NewsFragment;
import me.mathiasluo.page.placeholder.PlaceholderFragmentBuilder;
import me.mathiasluo.page.setting.MainSetingActivity;
import me.mathiasluo.page.setting.MainSettingFragment;
import me.mathiasluo.page.textbook.TextBookFragment;
import me.mathiasluo.page.weather.WeatherFragment;
import me.mathiasluo.utils.LanguageUtil;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    Handler drawerActionHandler;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.content_frame)
    FrameLayout contentFrame;

    @InjectView(R.id.navigation_view)
    NavigationView navigationView;

    @Icicle
    int selectedItem;

    SparseArray<Fragment> fragmentMap;
    private FragmentManager mFragmentManager;
    Fragment mCurrentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        navigationView.setNavigationItemSelectedListener(this);
        fragmentMap = new SparseArray<>();
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            selectedItem = R.id.nav_item_today;
            MenuItem menuItem = navigationView.getMenu().findItem(selectedItem);
            //launchPage(choosePage(menuItem), 1);
            showFragment(menuItem);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MenuItem menuItem = navigationView.getMenu().findItem(selectedItem);
        if (menuItem != null)
            selectMenuItem(menuItem);
    }

    @Override
    public int getLayoutRes() {
        String language = PreferenceManager.getDefaultSharedPreferences(this).getString("language", "English");
        LanguageUtil.changeLanguage(language);
        return R.layout.activity_main;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == selectedItem)
            return false;
        //  final Fragment newPage = choosePage(menuItem);
        selectMenuItem(menuItem);
        closeDrawer();
        //  launchPage(newPage, 250);
        showFragment(menuItem);
        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void selectMenuItem(MenuItem menuItem) {
        if (menuItem == null)
            return;
        menuItem.setChecked(true);
        selectedItem = menuItem.getItemId();
    }


    @Override
    public void setToolbar(Toolbar bar) {
        setSupportActionBar(bar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, bar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                // startActivity(new Intent(MainActivity.this, LoginActivity.class));
                showFragment(item);
                break;
            case R.id.action_setthing:
                startActivity(new Intent(MainActivity.this, MainSetingActivity.class));
                break;
        }
        return true;
    }

    public void showFragment(MenuItem menuItem) {




        String title = menuItem.getTitle().toString();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        if (fragmentMap.get(menuItem.getItemId()) != null) {
            mCurrentFragment = fragmentMap.get(menuItem.getItemId());
        } else {
            switch (menuItem.getItemId()) {
                case R.id.nav_item_today:
                    //日历+课表
                    mCurrentFragment = new CalendarFragment();
                    break;
                case R.id.nav_item_dining_courts:
                    //餐厅
                    mCurrentFragment = new DiningLocationListFragmentBuilder(title).build();
                    break;
                case R.id.nav_item_cafes:
                    //news
                    //mCurrentFragment = new RetailLocationListFragmentBuilder("Cafés", title).build();
                    mCurrentFragment = new NewsFragment();
                    break;
                case R.id.nav_item_restaurants:
                    // mCurrentFragment = new RetailLocationListFragmentBuilder("Restaurants", title).build();
                    mCurrentFragment = new MapFragment();
                    break;
                case R.id.nav_item_markets:
                    // mCurrentFragment = new RetailLocationListFragmentBuilder("Markets", title).build();
                    mCurrentFragment = new WeatherFragment();
                    break;
                case R.id.nav_item_settings:
                    //mCurrentFragment = new SettingFragmentBuilder(title).build();
                    mCurrentFragment = new MainSettingFragment();
                    break;

                case R.id.nav_item_email:
                    //mCurrentFragment = new AboutFragmentBuilder(title).build();
                    mCurrentFragment = new EmailFragment();
                    break;
                case R.id.nav_item_layout:
                    showCloseDialog();
                    return;
                case R.id.nav_item_textbook:
                    mCurrentFragment = new TextBookFragment();
                    break;
                case R.id.action_login:
                    // startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    mCurrentFragment = new EmailFragment();
                    break;
                default:
                    mCurrentFragment = new PlaceholderFragmentBuilder(title).build();
                    Snackbar.make(contentFrame, "Operation not yet supported", Snackbar.LENGTH_SHORT).show();
            }
            transaction.add(R.id.content_frame, mCurrentFragment);
            fragmentMap.put(menuItem.getItemId(), mCurrentFragment);
        }
        transaction.show(mCurrentFragment);
        transaction.commit();
    }

    private void showCloseDialog() {

        new MaterialDialog.Builder(this)
                .title(R.string.layout_title)
                .content(R.string.logout_log)
                .theme(Theme.LIGHT)
                .positiveText("ok")
                .negativeText("cancle")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.e("===============>>>>>>>>>>>>", "click positive");
                        //在这里清除账号消息
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.e("===============>>>>>>>>>>>>", "click Negative");
                        //暂时还没发现干嘛...
                    }
                })
                .show();

    }

}
