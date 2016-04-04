package me.mathiasluo.page.news.newsutil;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;


/**
 * Created by hughe127
 */

public class RssFeedTask extends AsyncTask<String, Void, List<RssItem>> {

    public interface RssFeedResultListener {
        public void onRssResult(List<RssItem> results, Exception ex);
    }

    private Context c;
    private RssFeedResultListener listener;

    // An exception which might get thrown during the parse process
    private Exception ex;

    public RssFeedTask(Context c) {
        this.c = c;
    }

    public RssFeedTask(Context c, RssFeedResultListener listener) {
        this.c = c;
        this.listener = listener;
    }

    @Override
    protected List<RssItem> doInBackground(String... params) {

        RssReader rssReader = new RssReader(params[0]);

        try {
            return rssReader.getItems();
        } catch (Exception ex) {
            this.ex = ex;
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<RssItem> rssItemList) {
        listener.onRssResult(rssItemList, ex);
    }

}
