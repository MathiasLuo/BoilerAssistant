package me.mathiasluo.page.weather.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import me.mathiasluo.R;


// Free icon packs downloaded from:
// http://bharathp666.deviantart.com/art/Android-Weather-Extended-191851717
// http://bharathp666.deviantart.com/art/Android-Weather-Icons-180719113

// TODO Add caching of weather data. Example: If the weather API we use only updates once an hour, we should cache the request per hour.

public class WeatherActivity extends Activity {
    private Handler timeTaskHandler = new Handler();
    private final ArrayList<HourlyForecastData> hourlyDataList = new ArrayList<HourlyForecastData>();
    // Used to prevent UI action from occuring after the onStop method has been called.
    private boolean appHasStopped = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_page_layout);

        // Initialize the colors for the weekly forecast viewer
        WeatherUIHelper.colorizeWeeklyForecastView(this);

        this.setTitle("Purdue Weather");

        timeTaskHandler.removeCallbacks(updateTimeTask);
        timeTaskHandler.postDelayed(updateTimeTask, 500);

        final TextView hourlyTime = (TextView) findViewById(R.id.hourlyTime);
        final TextView hourlyTemp = (TextView) findViewById(R.id.hourlyTemp);
        final TextView hourlySummary = (TextView) findViewById(R.id.hourlySummary);
        final SeekBar seekbar = (SeekBar) findViewById(R.id.hourlySeekBar);
        final TextView temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);

        final WeatherActivity weatherActivity = this;

        // Retrieve the weather data either from cache or web and fill in the UI fields
        updateWeatherData();

        // Add a gradient effect to the temperature text view
        //temperatureTextView.getPaint().setShader(new LinearGradient(0, temperatureTextView.getTextSize() * 3, 0, 0, Color.BLACK, Color.WHITE, TileMode.CLAMP));

        // Display differing hourly stats when seeker bar is changed
        seekbar.setEnabled(false);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar hourlyseekbar, int currentProgress, boolean userInitiatedChange) {
                // TODO Auto-generated method stub
                if (!hourlyDataList.isEmpty()) {
                    hourlyTime.setText(hourlyDataList.get(currentProgress).getTime());
                    String currentHourlyTemp = WeatherUIHelper.getScaledTempString(weatherActivity, hourlyDataList.get(currentProgress).getTemp());
                    hourlyTemp.setText(currentHourlyTemp);
                    hourlySummary.setText(hourlyDataList.get(currentProgress).getSummary());

                    WeatherUIHelper.updateWeatherIcon((ImageView) findViewById(R.id.hourlyIcon), hourlyDataList.get(currentProgress).getIconTitle());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
    }

    @Override
    public void onStop() {
        super.onStop();
        appHasStopped = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        appHasStopped = false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            // Get the current date and time and update the date/time text views
            final TextView dateTextView = (TextView) findViewById(R.id.date);
            Calendar cal = Calendar.getInstance();
            dateTextView.setText(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ". " + cal.get(Calendar.DAY_OF_MONTH));
            final TextView timeTextView = (TextView) findViewById(R.id.time);
            String hour = cal.get(Calendar.HOUR) != 0 ? ("" + cal.get(Calendar.HOUR)) : "12";
            String minute = cal.get(Calendar.MINUTE) >= 10 ? ("" + cal.get(Calendar.MINUTE)) : ("0" + cal.get(Calendar.MINUTE));

            // The time is a spannable string so the AM/PM suffix can be given a smaller font
            SpannableString timeString = new SpannableString(hour + ":" + minute + (cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
            timeString.setSpan(new RelativeSizeSpan(.5f), timeString.length() - 2, timeString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  //Reduce size of AM/PM

            timeTextView.setText(timeString);
            timeTaskHandler.postDelayed(updateTimeTask, 500);
        }
    };

    /*
     * Retrieves weather data from cache or from web and updates the UI components with the data.
     * If there is valid cached weather data that is less than an hour old, use it. Otherwise, use web data.
     */
    protected void updateWeatherData() {
        final TextView hourlyTime = (TextView) findViewById(R.id.hourlyTime);
        final TextView hourlyTemp = (TextView) findViewById(R.id.hourlyTemp);
        final TextView hourlySummary = (TextView) findViewById(R.id.hourlySummary);
        final SeekBar seekbar = (SeekBar) findViewById(R.id.hourlySeekBar);
       // final TextView descriptionTextView = (TextView) findViewById(R.id.weatherDescTextView);
        final TextView temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);

        // Create the weather data loading dialogue
        final ProgressDialog weatherPD = new ProgressDialog(this);
        weatherPD.setTitle("");
        weatherPD.setCancelable(false);
        weatherPD.setMessage("Loading Weather Data...");

        final Bundle args = new Bundle();  // Use argument bundle to pass message to weather dialogue
        final WeatherDataErrorDialog dataErrorDialog = new WeatherDataErrorDialog();

        final WeatherActivity weatherActivity = this;

        // Check if network connection is available to retrieve the weather data
        if (!isNetworkAvailable() && !WeatherAPI.isCachedDataValid(WeatherAPI.readJSONFromCache(this, "cachedWeatherJSON.json"))) {
            args.putString("message", "No network connection. Please ensure you are connected to a network and try again.");
            dataErrorDialog.setArguments(args);
            dataErrorDialog.show(getFragmentManager(), "weather_error");
        } else {
            // Make an HTTP GET request to retrieve the JSON weather data
            weatherPD.show();
            new Thread(new Runnable() {
                public void run() {
                    final String weatherResponse = WeatherAPI.getWeatherData(weatherActivity.getApplicationContext());
                    // If the weather data was retrieved successfully, parse and place the data into the UI
                    Handler handler = new Handler(Looper.getMainLooper());
                    // Handler is necessary to gain reference to UI thread.
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (weatherResponse != null) {

                                    // Parse the weekly forecast data and update the weekly forecast view
                                    //WeatherUIHelper.updateWeeklyForecastView(weatherActivity, weatherResponse);

                                    JSONObject object = (JSONObject) new JSONTokener(weatherResponse).nextValue();
                                    // JSON parsing to get the current weather in West Lafayette
                                    String weatherDescription = (String) object.getJSONObject("currently").get("summary");
                                    String currentIconTitle = (String) object.getJSONObject("currently").get("icon");
                                    double currentTemp = Double.parseDouble(object.getJSONObject("currently").get("temperature") + "");

                                    // Parse the hourly forecast data and store data in hourlyDataList
                                    WeatherAPI.parseHourlyData(object, hourlyDataList);

                                    // Update the UI components with the parsed data
                                    WeatherUIHelper.updateWeatherIcon((ImageView) findViewById(R.id.currentWeatherIcon), currentIconTitle);
                                    weatherPD.dismiss();
                                    WeatherUIHelper.updateWeatherIcon((ImageView) findViewById(R.id.hourlyIcon), hourlyDataList.get(seekbar.getProgress()).getIconTitle());
                                    temperatureTextView.setText(WeatherUIHelper.getScaledTempString(weatherActivity, currentTemp));
                                   // descriptionTextView.setText("{ " + weatherDescription + " }");
                                    hourlyTime.setText(hourlyDataList.get(seekbar.getProgress()).getTime());
                                    hourlyTemp.setText(WeatherUIHelper.getScaledTempString(weatherActivity, hourlyDataList.get(seekbar.getProgress()).getTemp()));
                                    hourlySummary.setText(hourlyDataList.get(seekbar.getProgress()).getSummary());
                                    seekbar.setEnabled(true);
                                } else {
                                    // Unable to retrieve data
                                    weatherPD.dismiss();
                                    if (!appHasStopped) {
                                        args.putString("message", "Failed to retrieve weather data.");
                                        dataErrorDialog.setArguments(args);
                                        dataErrorDialog.show(getFragmentManager(), "weather_error");
                                    }
                                }
                            } catch (Exception e) {
                                // Unable to retrieve data
                                weatherPD.dismiss();
                                if (!appHasStopped) {
                                    args.putString("message", "Failed to retrieve weather data.");
                                    dataErrorDialog.setArguments(args);
                                    dataErrorDialog.show(getFragmentManager(), "weather_error");
                                }
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        }
    }
}