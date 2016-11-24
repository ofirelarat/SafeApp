package com.example.ofir.car_imulator.data;

import org.json.JSONObject;

/**
 * Created by ofir elarat on 14-Feb-16.
 */
public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature=data.optString("temperature");
    }
}
