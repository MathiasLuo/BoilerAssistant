package me.mathiasluo.page.news;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.ViewGroup;

import java.util.List;

import me.mathiasluo.page.news.newsutil.DefaultNewsCategories;

/**
 * Created by David on 2015/2/1.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final List<DefaultNewsCategories> titleList;
    private final List<Fragment> fragmentsList;
    private Context context;

    /**
     * @param fm
     */
    public TabPagerAdapter(final FragmentManager fm, List<Fragment> fragmentsList, List<DefaultNewsCategories> titleList, Context context) {
        super(fm);
        this.fragmentsList = fragmentsList;
        this.titleList = titleList;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return (fragmentsList == null || fragmentsList.size() == 0) ? null : fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList == null ? 0 : fragmentsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable dImage = context.getResources().getDrawable(titleList.get(position).drawableResource());
        dImage.setBounds(0, 0, dImage.getIntrinsicWidth(), dImage.getIntrinsicHeight());
        //这里前面加的空格就是为图片显示
        SpannableString sp = new SpannableString("  " + titleList.get(position).name());
        ImageSpan imageSpan = new ImageSpan(dImage, ImageSpan.ALIGN_BOTTOM);
        sp.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }
}
