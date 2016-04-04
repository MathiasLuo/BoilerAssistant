package me.mathiasluo.page.weather.service;

import me.mathiasluo.page.weather.data.Channel;

/**
 * Created by yxy on 15/6/29.
 */
public interface WeatherServiceCallback {

    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
