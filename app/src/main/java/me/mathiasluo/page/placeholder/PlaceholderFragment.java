package me.mathiasluo.page.placeholder;


import com.hannesdorfmann.fragmentargs.annotation.FragmentArgsInherited;

import me.mathiasluo.R;
import me.mathiasluo.base.MainFragment;

@FragmentArgsInherited
public class PlaceholderFragment extends MainFragment<PlaceholderView, PlaceholderPresenter> implements PlaceholderView {

    @Override
    public PlaceholderPresenter createPresenter() {
        return new PlaceholderPresenter();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_placeholder;
    }
}

