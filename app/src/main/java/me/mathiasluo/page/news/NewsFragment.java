package me.mathiasluo.page.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.BaseFragment;
import me.mathiasluo.page.news.newsutil.DefaultNewsCategories;

/**
 * Created by mathiasluo on 16-3-26.
 */
public class NewsFragment extends BaseFragment {
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tabs)
    TabLayout tabLayout;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;


    private List<SingleNewsFragment> list;

    public static List<DefaultNewsCategories> tabList = new ArrayList<>();
    public static final int NEW_KEY = 00001;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setTitle("RssItem");
        ((MainActivity) getActivity()).setToolbar(mToolbar);
        initTabLayout();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initTabLayout() {
       /* List<Fragment> fragmentList = new ArrayList<>();
        for (DefaultNewsCategories defaultNewsCategories : DefaultNewsCategories.values()) {
            tabList.add(defaultNewsCategories);
           // tabLayout.addTab(tabLayout.newTab().setText(defaultNewsCategories.title()));
        }

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            SingleNewsFragment singleNewsFragment = new SingleNewsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SingleNewsFragment.BUNDLE_KEY, i);
            singleNewsFragment.setArguments(bundle);
            fragmentList.add(singleNewsFragment);
        }
*/
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
/*
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), fragmentList, tabList);

        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(tabPagerAdapter);
        *//*tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(tabPagerAdapter);*/

/*

        LayoutInflater inflater = LayoutInflater.from(getContext());
        List<View> list = new ArrayList<>();
        for (DefaultNewsCategories defaultNewsCategories : DefaultNewsCategories.values()) {
            tabList.add(defaultNewsCategories);
            // tabLayout.addTab(tabLayout.newTab().setText(defaultNewsCategories.title()));
            View view = inflater.inflate(R.layout.list_item_news, null);
            list.add(view);
            tabLayout.addTab(tabLayout.newTab().setText(defaultNewsCategories.title()));
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        ViewAdapter adapter = new ViewAdapter(list);
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(list.size());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
*/

        List<Fragment> list = new ArrayList<>();
        for (DefaultNewsCategories defaultNewsCategories : DefaultNewsCategories.values()) {
            tabList.add(defaultNewsCategories);
            // tabLayout.addTab(tabLayout.newTab().setText(defaultNewsCategories.title()));
            tabLayout.addTab(tabLayout.newTab().setText(defaultNewsCategories.title()));
        }


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            SingleNewsFragment singleNewsFragment = new SingleNewsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SingleNewsFragment.BUNDLE_KEY, i);
            singleNewsFragment.setArguments(bundle);
            list.add(singleNewsFragment);
        }

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        /*ViewAdapter adapter = new ViewAdapter(list);*/
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), list, tabList,getContext());
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(list.size());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

}
