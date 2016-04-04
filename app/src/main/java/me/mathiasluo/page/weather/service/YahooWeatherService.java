package me.mathiasluo.page.weather.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import me.mathiasluo.page.weather.data.Channel;


/**
 * Created by yxy on 15/6/29.
 */
public class YahooWeatherService {


    private WeatherServiceCallback callback;
    private Context context;
    private Exception error;
    private String location;

    public String getLocation() {
        return location;
    }

    public YahooWeatherService(WeatherServiceCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    public YahooWeatherService(WeatherServiceCallback callback) {
        this.callback = callback;
    }

    public void refreshWeather(final String location, final Boolean useF) {

        this.location = location;

        new AsyncTask<String, Void, Channel>() {

            @Override
            protected Channel doInBackground(String... strings) {

                String location = strings[0];
                Channel channel = new Channel();

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", location);
                if (!useF) {
                    YQL += " and u=\"c\"";
                }
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject data = new JSONObject(result.toString());
                    JSONObject queryResults = data.optJSONObject("query");
                    int count = queryResults.optInt("count");
                    if (count == 0) {
                        // means there is no such location
                        error = new LocationNotFoundException("No weather information found for " + location);
                        return null;
                    }
                    channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                    return channel;

                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Channel channel) {

                if (error != null && channel == null) {
                    callback.serviceFailure(error);
                } else {
                    callback.serviceSuccess(channel);
                }
            }
        }.execute(location);
    }

    public class LocationNotFoundException extends Exception {
        public LocationNotFoundException(String detailMessage) {
            super(detailMessage);
        }
    }

}


