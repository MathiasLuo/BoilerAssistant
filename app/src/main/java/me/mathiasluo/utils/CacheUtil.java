package me.mathiasluo.utils;


import com.bumptech.glide.Glide;

import java.io.File;

import me.mathiasluo.APP;

/**
 * Created by mathiasluo on 16-3-31.
 */
public class CacheUtil {


    public final static String getCacheSize() {
        return FileSizeUtil.getAutoFileOrFilesSize(Glide.getPhotoCacheDir(APP.getInstance()).getPath());
    }

    public final static void clearCache() {
        RecursionDeleteFile(Glide.getPhotoCacheDir(APP.getInstance()));
    }

    public static void RecursionDeleteFile(File file) {

        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

}




