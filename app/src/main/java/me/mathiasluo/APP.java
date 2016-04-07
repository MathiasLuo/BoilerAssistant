package me.mathiasluo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class APP extends Application {
    public static APP instance;

    public final static APP getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // setup logging
        Timber.uprootAll();
        Timber.plant(new Timber.DebugTree());
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }
}