package me.mathiasluo.page.weather.data;

import org.json.JSONObject;

/**
 * Created by yxy on 15/6/29.
 */
public interface JSONPopulator {
    void populate (JSONObject data);

    JSONObject toJSON();
}
