package me.mathiasluo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.iconics.context.IconicsContextWrapper;

import butterknife.ButterKnife;
import icepick.Icepick;
import me.mathiasluo.DaggerModule;

public abstract class BaseActivity extends AppCompatActivity {

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate layout
        setContentView(getLayoutRes());
        // inject dependencies
        DaggerModule.getObjectGraph().inject(this);
        // inject views
        ButterKnife.inject(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    public abstract int getLayoutRes();

    public void setToolbar(Toolbar toolbar) {

    }
}