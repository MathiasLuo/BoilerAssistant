package me.mathiasluo.page.weather.data;

import org.json.JSONObject;

/**
 * Created by yxy on 15/6/29.
 */
public class Channel implements JSONPopulator {

    private Units units;
    private Item item;
    private String location;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void populate(JSONObject data) {
        location = data.optJSONObject("location").optString("city");
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
