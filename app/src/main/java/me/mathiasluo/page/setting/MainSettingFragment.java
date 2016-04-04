package me.mathiasluo.page.setting;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import me.mathiasluo.APP;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.BaseFragment;
import me.mathiasluo.model.Music;
import me.mathiasluo.utils.CacheUtil;
import me.mathiasluo.utils.LanguageUtil;
import me.mathiasluo.utils.SPUtil;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by mathiasluo on 16-3-28.
 */
public class MainSettingFragment extends BaseFragment {


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frame, new PreferenceFragment())
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setTitle("Email");
        ((MainActivity) getActivity()).setToolbar(mToolbar);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_frame;
    }




    public static class PreferenceFragment extends android.preference.PreferenceFragment {

        private ListPreference mListPreference;
        public Preference mMusicPreference;
        private Preference mCealrPreference;
        List<Music> musics;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.mainsteting);
            mListPreference = (ListPreference) findPreference("language");
            mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    switch ((String) o) {
                        case "English":
                            LanguageUtil.changeLanguage(LanguageUtil.LANGUAGE_ENGLISH);
                            break;
                        case "汉语":
                            LanguageUtil.changeLanguage(LanguageUtil.LANGUAGE_CHINESE);
                            break;
                    }
                    preference.getSharedPreferences().edit().putString("language", (String) o).commit();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                    return true;
                }
            });
        }


        public void showMusicSelectDialog(List<String> mMusics) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.music_dialog)
                    .items(mMusics)
                    .theme(Theme.LIGHT)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which != -1)SPUtil.putMusicPath(APP.getInstance(), musics.get(which).getPath());
                            return true;
                        }
                    })
                    .positiveText(R.string.choose)
                    .show();
        }

        public void showCacheDialog(String cache) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.layout_title)
                    .content(getString(R.string.cache) + cache + "\n" + getString(R.string.cache_sure))
                    .theme(Theme.LIGHT)
                    .positiveText("ok")
                    .negativeText("cancle")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //在这里清除账号消息
                            CacheUtil.clearCache();
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

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            switch (preference.getKey()) {
                case "tone":
                    musics = Init(APP.getInstance());
                    Observable.just(musics)
                            .map(new Func1<List<Music>, List<String>>() {
                                @Override
                                public List<String> call(List<Music> musics) {
                                    List<String> list = new ArrayList<String>();
                                    for (Music music : musics) list.add(music.getName());
                                    return list;
                                }
                            }).subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(List<String> strings) {
                            showMusicSelectDialog(strings);
                        }
                    });
                    break;
                case "Caching":
                    showCacheDialog(CacheUtil.getCacheSize());
                    break;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        public  List<Music> Init(Context context) {
            List<Music> musics = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA}, MediaStore.Audio.Media.MIME_TYPE + "=? or " + MediaStore.Audio.Media.MIME_TYPE + "=?", new String[]{"audio/mpeg", "audio/x-ms-wma"}, null);
            //查询的字段分别是音乐名字,音乐的长度和歌曲文件的全路径
            if (cursor.moveToFirst()) {
                do {
                    Log.i("Song", cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
                    musics.add(new Music(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return musics;
        }

    }





}
