package com.sample.weatherapp.app.model;

import android.util.Log;

import java.util.*;

public class Data {
    String greg = "dataClass";

    private DayWeather nowWeather;
    private WeatherForecast forecast;

    public final String STR_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/weather?";
    public final String STR_FORECAST = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    public String urlStrDay;
    public String urlStrForecast;
    public String title;
    public QueryType queryType;
    public String city;
    public String lat;
    public String lon;

    public DayWeather getNowWeather() {
        return nowWeather;
    }

    public void setNowWeather(DayWeather nowWeather) {
        Log.d(greg, "setWeather");
        this.nowWeather = nowWeather;
    }

    public WeatherForecast getForecast() {
        return forecast;
    }

    public void setForecast(WeatherForecast forecast) {
        Log.d(greg, "setForecast");
        this.forecast = forecast;
    }
}
