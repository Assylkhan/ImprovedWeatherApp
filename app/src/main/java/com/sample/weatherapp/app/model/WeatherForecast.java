package com.sample.weatherapp.app.model;

import android.content.Context;
import android.util.Log;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.activity.MainActivity;
import com.sample.weatherapp.app.util.WeatherUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeatherForecast {
    private String cod;
    private Double message;
    private City city;
    private Integer cnt;
    private java.util.List<List> list = new ArrayList<List>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The cod
     */
    public String getCod() {
        return cod;
    }

    /**
     * @param cod The cod
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    /**
     * @return The message
     */
    public Double getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(Double message) {
        this.message = message;
    }

    /**
     * @return The city
     */
    public City getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * @return The cnt
     */
    public Integer getCnt() {
        return cnt;
    }

    /**
     * @param cnt The cnt
     */
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    /**
     * @return The list
     */
    public java.util.List<List> getList() {
        return list;
    }

    /**
     * @param list The list
     */
    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void transform(Context context) {
        Log.d(WeatherForecast.class.getSimpleName(), "try parsing jsonForecast ");
        for (List dayForecast : list) {
            dayForecast.transform(context);
//            todo: invalid long 17.06.2015
            dayForecast.setHumidity(context.getString(R.string.humidity) + String.valueOf(dayForecast.getHumidity())
                    + context.getString(R.string.percent));
            dayForecast.setSpeed(context.getString(R.string.wind) + " " +
                    context.getString(WeatherUtil.windDir(dayForecast.getDeg())) + " "
                    + String.valueOf(dayForecast.getSpeed()) +   " "
                    + context.getString(R.string.speedUnit));

        }
    }
}
