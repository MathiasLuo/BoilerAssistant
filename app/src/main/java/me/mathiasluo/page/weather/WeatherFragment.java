package me.mathiasluo.page.weather;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.BaseFragment;
import me.mathiasluo.page.weather.weather.HourlyForecastData;
import me.mathiasluo.page.weather.weather.WeatherDataErrorDialog;
import me.mathiasluo.page.weather.weather.WeatherUIHelper;


/**
 * Created by Dudaizhong on 2016/3/27.
 */
public class WeatherFragment extends BaseFragment {

    private Handler timeTaskHandler = new Handler();
    private final ArrayList<HourlyForecastData> hourlyDataList = new ArrayList<HourlyForecastData>();
    // Used to prevent UI action from occuring after the onStop method has been called.
    private boolean appHasStopped = false;

    private View view;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getLayoutRes() {
        return R.layout.weather_page_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Initialize the colors for the weekly forecast viewer
        // WeatherUIHelper.colorizeWeeklyForecastView(getActivity());
        mToolbar.setTitle(getString(R.string.weather));
        ((MainActivity) getActivity()).setToolbar(mToolbar);

        timeTaskHandler.removeCallbacks(updateTimeTask);
        timeTaskHandler.postDelayed(updateTimeTask, 500);

        final TextView hourlyTime = (TextView) view.findViewById(R.id.hourlyTime);
        final TextView hourlyTemp = (TextView) view.findViewById(R.id.hourlyTemp);
        final TextView hourlySummary = (TextView) view.findViewById(R.id.hourlySummary);
        final SeekBar seekbar = (SeekBar) view.findViewById(R.id.hourlySeekBar);
        final TextView temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        temperatureTextView.setTextColor(getActivity().getResources().getColor(R.color.white));

        // Retrieve the weather data either from cache or web and fill in the UI fields
        updateWeatherData();

        // Add a gradient effect to the temperature text view
       // temperatureTextView.getPaint().setShader(new LinearGradient(0, temperatureTextView.getTextSize() * 3, 0, 0, Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));

        // Display differing hourly stats when seeker bar is changed
        seekbar.setEnabled(false);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar hourlyseekbar, int currentProgress, boolean userInitiatedChange) {
                // TODO Auto-generated method stub
                if (!hourlyDataList.isEmpty()) {
                    hourlyTime.setText(hourlyDataList.get(currentProgress).getTime());
                    String currentHourlyTemp = WeatherUIHelper.getScaledTempString(getActivity(), hourlyDataList.get(currentProgress).getTemp());
                    hourlyTemp.setText(currentHourlyTemp);
                    hourlySummary.setText(hourlyDataList.get(currentProgress).getSummary());

                    WeatherUIHelper.updateWeatherIcon((ImageView) view.findViewById(R.id.hourlyIcon), hourlyDataList.get(currentProgress).getIconTitle());
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            // Get the current date and time and update the date/time text views
            final TextView dateTextView = (TextView) view.findViewById(R.id.date);
            Calendar cal = Calendar.getInstance();
            dateTextView.setText(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ". " + cal.get(Calendar.DAY_OF_MONTH));
            final TextView timeTextView = (TextView) view.findViewById(R.id.time);
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
        final TextView hourlyTime = (TextView) view.findViewById(R.id.hourlyTime);
        final TextView hourlyTemp = (TextView) view.findViewById(R.id.hourlyTemp);
        final TextView hourlySummary = (TextView) view.findViewById(R.id.hourlySummary);
        final SeekBar seekbar = (SeekBar) view.findViewById(R.id.hourlySeekBar);
      //  final TextView descriptionTextView = (TextView) view.findViewById(R.id.weatherDescTextView);
        final TextView temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);

        // Create the weather data loading dialogue
        final ProgressDialog weatherPD = new ProgressDialog(getActivity());
        weatherPD.setTitle("");
        weatherPD.setCancelable(false);
        weatherPD.setMessage("Loading Weather Data...");

        final Bundle args = new Bundle();  // Use argument bundle to pass message to weather dialogue
        final WeatherDataErrorDialog dataErrorDialog = new WeatherDataErrorDialog();


        // Check if network connection is available to retrieve the weather data
        if (!isNetworkAvailable() && !me.mathiasluo.page.weather.weather.WeatherAPI.isCachedDataValid(me.mathiasluo.page.weather.weather.WeatherAPI.readJSONFromCache(getActivity(), "cachedWeatherJSON.json"))) {
            args.putString("message", "No network connection. Please ensure you are connected to a network and try again.");
            dataErrorDialog.setArguments(args);
            dataErrorDialog.show(getActivity().getFragmentManager(), "weather_error");
        } else {
            // Make an HTTP GET request to retrieve the JSON weather data
            weatherPD.show();
            new Thread(new Runnable() {
                public void run() {
                    final String weatherResponse = me.mathiasluo.page.weather.weather.WeatherAPI.getWeatherData(getActivity().getApplicationContext());
                    // If the weather data was retrieved successfully, parse and place the data into the UI
                    Handler handler = new Handler(Looper.getMainLooper());
                    // Handler is necessary to gain reference to UI thread.
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (weatherResponse != null) {

                                    // Parse the weekly forecast data and update the weekly forecast view
                                    // WeatherUIHelper.updateWeeklyForecastView(getActivity(), weatherResponse);

                                    WeatherUIHelper.updateWeeklyForecastView(view, weatherResponse);
                                    JSONObject object = (JSONObject) new JSONTokener(weatherResponse).nextValue();
                                    // JSON parsing to get the current weather in West Lafayette
                                    String weatherDescription = (String) object.getJSONObject("currently").get("summary");
                                    String currentIconTitle = (String) object.getJSONObject("currently").get("icon");
                                    double currentTemp = Double.parseDouble(object.getJSONObject("currently").get("temperature") + "");

                                    // Parse the hourly forecast data and store data in hourlyDataList
                                    me.mathiasluo.page.weather.weather.WeatherAPI.parseHourlyData(object, hourlyDataList);

                                    // Update the UI components with the parsed data
                                    WeatherUIHelper.updateWeatherIcon((ImageView) view.findViewById(R.id.currentWeatherIcon), currentIconTitle);
                                    weatherPD.dismiss();
                                    WeatherUIHelper.updateWeatherIcon((ImageView) view.findViewById(R.id.hourlyIcon), hourlyDataList.get(seekbar.getProgress()).getIconTitle());
                                    temperatureTextView.setText(WeatherUIHelper.getScaledTempString(getActivity(), currentTemp));
                                   // descriptionTextView.setText("{ " + weatherDescription + " }");
                                    hourlyTime.setText(hourlyDataList.get(seekbar.getProgress()).getTime());
                                    hourlyTemp.setText(WeatherUIHelper.getScaledTempString(getActivity(), hourlyDataList.get(seekbar.getProgress()).getTemp()));
                                    hourlySummary.setText(hourlyDataList.get(seekbar.getProgress()).getSummary());
                                    seekbar.setEnabled(true);
                                } else {
                                    // Unable to retrieve data
                                    weatherPD.dismiss();
                                    if (!appHasStopped) {
                                        args.putString("message", "Failed to retrieve weather data.");
                                        dataErrorDialog.setArguments(args);
                                        dataErrorDialog.show(getActivity().getFragmentManager(), "weather_error");
                                    }
                                }
                            } catch (Exception e) {
                                // Unable to retrieve data
                                weatherPD.dismiss();
                                if (!appHasStopped) {
                                    args.putString("message", "Failed to retrieve weather data.");
                                    dataErrorDialog.setArguments(args);
                                    dataErrorDialog.show(getActivity().getFragmentManager(), "weather_error");
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
