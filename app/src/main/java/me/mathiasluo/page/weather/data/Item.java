package me.mathiasluo.page.weather.data;

import org.json.JSONObject;

/**
 * Created by yxy on 15/6/29.
 */
public class Item implements JSONPopulator {

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
