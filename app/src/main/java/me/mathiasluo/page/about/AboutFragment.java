package me.mathiasluo.page.about;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hannesdorfmann.fragmentargs.annotation.FragmentArgsInherited;
import com.mikepenz.aboutlibraries.LibsBuilder;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.base.MainFragment;

@FragmentArgsInherited
public class AboutFragment extends MainFragment<AboutView, AboutPresenter> implements AboutView {


    @Override
    public AboutPresenter createPresenter() {
        return new AboutPresenter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, buildAboutFragment())
                .commit();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_frame;
    }

    private Fragment buildAboutFragment() {
        return new LibsBuilder()
                .withFields(R.string.class.getFields())
                .withAutoDetect(true)
                .withListener(getPresenter())
                .withAnimations(false)
                .withLicenseDialog(true)
                .withLicenseShown(true)
                .fragment();
    }
}
