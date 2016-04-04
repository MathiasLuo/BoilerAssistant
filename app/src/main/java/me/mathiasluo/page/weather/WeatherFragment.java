package me.mathiasluo.page.weather;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.InjectView;
import me.mathiasluo.APP;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.BaseFragment;
import me.mathiasluo.page.weather.data.Channel;
import me.mathiasluo.page.weather.data.Item;
import me.mathiasluo.page.weather.service.WeatherServiceCallback;
import me.mathiasluo.page.weather.service.YahooWeatherService;


/**
 * Created by Dudaizhong on 2016/3/27.
 */
public class WeatherFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, WeatherServiceCallback {


    @InjectView(R.id.toolbar)
    Toolbar mToolBar;
    @InjectView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.loading_progress)
    ProgressBar mProgressBar;


    private ImageView weatherIconImageView;
    private TextView locationTextView;
    private TextView conditionTextView;
    private TextView temperatureTextView;
    private LocationManager locationManager;
    private String provider;
    public static final int SHOW_LOCATION = 0;

    public static String address = null;

    private YahooWeatherService service;

    private Location location;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOCATION:
                    String currentPosition = (String) msg.obj;

                    service.refreshWeather("47906", false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_weather;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        init(view);
        service.refreshWeather("47906", false);
        // showLocation(location);

        locationManager.requestLocationUpdates(provider, 5000, 1, listener);
    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //showLocation(location);
            service.refreshWeather("47906", false);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void init(View view) {
        mToolBar.setTitle(getString(R.string.weather));
        ((MainActivity) getActivity()).setToolbar(mToolBar);
        refreshLayout.setColorSchemeColors(R.color.open);
        refreshLayout.setOnRefreshListener(this);
        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);//用于显示天气图片
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);//用于显示地理位置
        locationTextView.setTextColor(getContext().getResources().getColor(R.color.white));
        conditionTextView = (TextView) view.findViewById(R.id.conditionTextView);//用于显示天气状况
        conditionTextView.setTextColor(getContext().getResources().getColor(R.color.white));
        temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);//用于显示天气温度
        temperatureTextView.setTextColor(getContext().getResources().getColor(R.color.white));
        service = new YahooWeatherService(this);
        final EditText newCityEditText = (EditText) view.findViewById(R.id.newCitypEditText);//输入查询城市
        final Button button = (Button) view.findViewById(R.id.updateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                String city = newCityEditText.getText().toString();
                service.refreshWeather(city, false);
                newCityEditText.setText("");
            }
        });

        Switch unitSwitch = (Switch) view.findViewById(R.id.unitSwitch);
        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showLoading();
                if (isChecked) {
                    service.refreshWeather(service.getLocation(), true);
                } else {
                    service.refreshWeather(service.getLocation(), false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        requestWeatherData();
    }

    public void requestWeatherData() {
        showLoading();
        if (location != null) {
            //showLocation(location);
            service.refreshWeather("47906", false);
        } else closeLoading();
    }

    public void showLoading() {
        if (!refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(true);
    }

    public void closeLoading() {
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void serviceSuccess(Channel channel) {
        closeLoading();
        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getContext().getPackageName());
        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(channel.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        closeLoading();
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }


   /* private void showLocation(final Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try {
                    StringBuilder str = new StringBuilder();
                    str.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    str.append(location.getLatitude()).append(",");
                    str.append(location.getLongitude());
                    str.append("&sensor=false");

                    URL url = new URL(str.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject jsonObject = new JSONObject(response.toString());
                    //获取results结点下的位置信息
                    JSONArray resultArray = jsonObject.getJSONArray("results");
                    if (resultArray.length() > 0) {
                        JSONObject subObject = resultArray.getJSONObject(0);
                        //取出格式化后的位置信息
                        address = subObject.getString("formatted_address");
                        Message message = new Message();
                        message.what = SHOW_LOCATION;
                        message.obj = address;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}
