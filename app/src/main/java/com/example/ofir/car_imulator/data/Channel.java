package com.example.ofir.car_imulator.data;


import org.json.JSONObject;

/**
 * Created by ofir elarat on 14-Feb-16.
 */
public class Channel implements JSONPopulator {
    private Item item;
    private Units units;

    public Units getUnits() {
        return units;
    }
    public Item getItem() {
        return item;
    }

    @Override
    public void populate(JSONObject data) {

        units=new Units();
        units.populate(data.optJSONObject("units"));


        item=new Item();
        item.populate(data.optJSONObject("item"));
    }

}
