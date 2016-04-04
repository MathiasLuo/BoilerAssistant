package me.mathiasluo.page.weather.data;

import org.json.JSONObject;

/**
 * Created by yxy on 15/6/29.
 */
public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");

    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
