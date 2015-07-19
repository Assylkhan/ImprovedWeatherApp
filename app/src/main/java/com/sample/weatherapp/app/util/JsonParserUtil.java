package com.sample.weatherapp.app.util;

import android.content.Context;
import com.google.gson.Gson;
import com.sample.weatherapp.app.model.DayWeather;
import com.sample.weatherapp.app.model.WeatherForecast;

import java.lang.reflect.Type;

public class JsonParserUtil {

    public static WeatherForecast parseForecast(String jsonWeather, Class<WeatherForecast> type, Context context) {
        Gson gson = new Gson();
        WeatherForecast weatherForecast = gson.fromJson(jsonWeather, type);
        weatherForecast.transform(context);
        return weatherForecast;
    }

    public static DayWeather parseDay(String jsonWeather, Class<DayWeather> type , Context context) {
        Gson gson = new Gson();
        DayWeather dayWeather = gson.fromJson(jsonWeather, type);
        dayWeather.transform(context);
        return dayWeather;
    }

}
