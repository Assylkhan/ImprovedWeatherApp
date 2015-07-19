package com.sample.weatherapp.app.util;

import android.content.Context;
import android.util.Log;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.model.DayWeather;
import com.sample.weatherapp.app.model.WeatherForecast;
import org.json.JSONException;

public class WeatherUtil {

    public static String temperatureEdit(Double t) {
        int T = (int) Math.round(t - 273.15);
        String str = String.valueOf(T);
        if (T > 0)
            str = "+" + str;
        return str;
    };

    public static int windDir(int i) {
        if (i <= 22 | i > 337)
            return (R.string.N);
        if (i > 22 && i <= 67)
            return (R.string.NE);
        if (i > 67 && i <= 112)
            return (R.string.E);
        if (i > 112 && i <= 157)
            return (R.string.SE);
        if (i > 157 && i <= 202)
            return (R.string.S);
        if (i > 202 && i <= 247)
            return (R.string.SW);
        if (i > 247 && i <= 292)
            return (R.string.W);
        if (i > 292 && i <= 337)
            return (R.string.NW);
        return (R.string.hello_world);
    }


}

