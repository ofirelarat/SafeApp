package com.example.ofir.car_imulator.service;


import com.example.ofir.car_imulator.data.Channel;

/**
 * Created by ofir elarat on 14-Feb-16.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
