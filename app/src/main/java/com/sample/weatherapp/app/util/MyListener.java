package com.sample.weatherapp.app.util;

import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.sample.weatherapp.app.activity.MainActivity;
import com.sample.weatherapp.app.model.*;

public class MyListener<JSONObject> implements Response.Listener<JSONObject> {
    static final String TAG = "MainActivity";
    private Data myData;
    private DayWeather mNowWeather;
    private WeatherForecast mWeatherForecast;
    private WeatherType mWeatherType;
    private MainActivity mMainActivity;

    public MyListener(Data myData, WeatherType mWeatherType, MainActivity mMainActivity) {
        this.myData = myData;
        this.mWeatherType = mWeatherType;
        this.mMainActivity = mMainActivity;
    }

    @Override
    public void onResponse(JSONObject jsonResponse) {
        // TODO Auto-generated method stub
        switch (mWeatherType) {
            case CURRENT_WEATHER:
                try {// jsonDay
                    mMainActivity.loadAnimationStart();
                    Log.d(TAG, mMainActivity.sNewData.urlStrDay);
                    String stringedResponse = jsonResponse.toString();
                    mNowWeather = JsonParserUtil.parseDay(stringedResponse, DayWeather.class, mMainActivity);
                    mMainActivity.sNewData.setNowWeather(mNowWeather);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                break;
            case FORECAST:
                try {// jsonForecast
                    Log.d(TAG, mMainActivity.sNewData.urlStrForecast);
                    String stringedResponse = jsonResponse.toString();
                    mWeatherForecast = JsonParserUtil.parseForecast(stringedResponse, WeatherForecast.class, mMainActivity);
                    mMainActivity.sNewData.setForecast(mWeatherForecast);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                break;
        }

        // save title to refresh data next time
        if (mMainActivity.sNewData.city == null)
            mMainActivity.sNewData.title = mMainActivity.sNewData.lat + " " + mMainActivity.sNewData.lon;
        else mMainActivity.sNewData.title = mMainActivity.sNewData.city;

        SharedPreferences.Editor editor = mMainActivity.mSettings.edit();
        editor.putString("title", mMainActivity.sNewData.title);
        editor.putString("urlStrDay", mMainActivity.sNewData.urlStrDay);
        editor.putString("urlStrForecast", mMainActivity.sNewData.urlStrForecast);

        Gson gson = new Gson();
        String jsDay = gson.toJson(mMainActivity.sNewData.getNowWeather());
        String jsForecast = gson.toJson(mMainActivity.sNewData.getForecast());
        editor.putString("jsonDay", jsDay);
        editor.putString("jsonForecast", jsForecast);
        editor.apply();

        // refresh visible activity
        if (mMainActivity.mVisibleOnScreen)
            mMainActivity.afterUrlTask();
        else
            mMainActivity.mShowNewData = true;

        if (mWeatherType == WeatherType.FORECAST)
        mMainActivity.loadAnimationStop();
    }

    public Data getMyData() {
        return mMainActivity.sNewData;
    }

    public DayWeather getmNowWeather() {
        return mNowWeather;
    }

    public WeatherForecast getmWeatherForecast() {
        return mWeatherForecast;
    }

    public WeatherType getmWeatherType() {
        return mWeatherType;
    }
}
