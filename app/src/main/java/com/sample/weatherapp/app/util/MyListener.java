package com.sample.weatherapp.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.sample.weatherapp.app.activity.MainActivity;
import com.sample.weatherapp.app.model.*;
import org.json.JSONObject;

public class MyListener<JSONObject> implements Response.Listener<JSONObject> {
    static final String TAG = "MainActivity";
    private Data myData;
    private DayWeather nowWeather;
    private WeatherForecast weatherForecast;
    private WeatherType weatherType;
    private MainActivity mainActivity;

    public MyListener(Data myData, WeatherType weatherType, MainActivity mainActivity) {
        this.myData = myData;
        this.weatherType = weatherType;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onResponse(JSONObject jsonResponse) {
        mainActivity.loadAnimationStart();
        // TODO Auto-generated method stub
        switch (weatherType) {
            case CURRENT_WEATHER:
                try {// jsonDay
                    Log.d(TAG, myData.urlStrDay);
                    String stringedResponse = jsonResponse.toString();
                    nowWeather = JsonParserUtil.parseDay(stringedResponse, DayWeather.class, mainActivity);
                    myData.setNowWeather(nowWeather);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                break;
            case FORECAST:
                try {// jsonForecast
                    Log.d(TAG, myData.urlStrForecast);
                    String stringedResponse = jsonResponse.toString();
                    weatherForecast = JsonParserUtil.parseForecast(stringedResponse, WeatherForecast.class, mainActivity);
                    myData.setForecast(weatherForecast);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                break;
        }

        // save title to refresh data next time
        if (myData.city == null)
            myData.title = myData.lat + " " + myData.lon;
        else myData.title = myData.city;

        SharedPreferences.Editor editor = mainActivity.mSettings.edit();
        editor.putString("title", mainActivity.newData.title);
        editor.putString("urlStrDay", mainActivity.newData.urlStrDay);
        editor.putString("urlStrForecast", mainActivity.newData.urlStrForecast);

        Gson gson = new Gson();
        String jsDay = gson.toJson(mainActivity.newData.getNowWeather());
        String jsForecast = gson.toJson(mainActivity.newData.getForecast());
        editor.putString("jsonDay", jsDay);
        editor.putString("jsonForecast", jsForecast);
        editor.apply();

        // refresh visible activity
        if (mainActivity.visibleOnScreen)
            mainActivity.afterUrlTask();
        else
            mainActivity.showNewData = true;

        mainActivity.loadAnimationStop();
    }

    public Data getMyData() {
        return myData;
    }

    public DayWeather getNowWeather() {
        return nowWeather;
    }

    public WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }
}
