package com.sample.weatherapp.app.model;

import com.sample.weatherapp.app.util.WeatherUtil;

import java.util.HashMap;
import java.util.Map;

public class Temp {

    private String day;
    private Double min;
    private Double max;
    private String night;
    private String eve;
    private String morn;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The day
     */
    public String getDay() {
        return day;
    }

    /**
     *
     * @param day
     * The day
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     *
     * @return
     * The min
     */
    public Double getMin() {
        return min;
    }

    /**
     *
     * @param min
     * The min
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     *
     * @return
     * The max
     */
    public Double getMax() {
        return max;
    }

    /**
     *
     * @param max
     * The max
     */
    public void setMax(Double max) {
        this.max = max;
    }

    /**
     *
     * @return
     * The night
     */
    public String getNight() {
        return night;
    }

    /**
     *
     * @param night
     * The night
     */
    public void setNight(String night) {
        this.night = night;
    }

    /**
     *
     * @return
     * The eve
     */
    public String getEve() {
        return eve;
    }

    /**
     *
     * @param eve
     * The eve
     */
    public void setEve(String eve) {
        this.eve = eve;
    }

    /**
     *
     * @return
     * The morn
     */
    public String getMorn() {
        return morn;
    }

    /**
     *
     * @param morn
     * The morn
     */
    public void setMorn(String morn) {
        this.morn = morn;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
