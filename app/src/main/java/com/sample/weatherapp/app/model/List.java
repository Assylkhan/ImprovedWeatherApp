package com.sample.weatherapp.app.model;

import android.content.Context;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.util.WeatherUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class List {
    private String dt;
    private Temp temp;
    private String pressure;
    private String humidity;
    private java.util.List<Weather> weather = new ArrayList<Weather>();
    private String speed;
    private Integer deg;
    private Integer clouds;
    private Double rain;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     *
     * @return
     * The dt
     */
    public String getDt() {
        return dt;
    }

    /**
     *
     * @param dt
     * The dt
     */
    public void setDt(String dt) {
        this.dt = dt;
    }

    /**
     *
     * @return
     * The temp
     */
    public Temp getTemp() {
        return temp;
    }

    /**
     *
     * @param temp
     * The temp
     */
    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    /**
     *
     * @return
     * The pressure
     */
    public String getPressure() {
        return pressure;
    }

    /**
     *
     * @param pressure
     * The pressure
     */
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    /**
     *
     * @return
     * The humidity
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     * The humidity
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     *
     * @return
     * The weather
     */
    public java.util.List<Weather> getWeather() {
        return weather;
    }

    /**
     *
     * @param weather
     * The weather
     */
    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    /**
     *
     * @return
     * The speed
     */
    public String getSpeed() {
        return speed;
    }

    /**
     *
     * @param speed
     * The speed
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     *
     * @return
     * The deg
     */
    public Integer getDeg() {
        return deg;
    }

    /**
     *
     * @param deg
     * The deg
     */
    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    /**
     *
     * @return
     * The clouds
     */
    public Integer getClouds() {
        return clouds;
    }

    /**
     *
     * @param clouds
     * The clouds
     */
    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    void transform(Context context){
        temp.setMorn(WeatherUtil.temperatureEdit(Double.valueOf(temp.getMorn())));
        temp.setDay(WeatherUtil.temperatureEdit(Double.valueOf(temp.getDay())));
        temp.setEve(WeatherUtil.temperatureEdit(Double.valueOf(temp.getEve())));
        temp.setNight(WeatherUtil.temperatureEdit(Double.valueOf(temp.getNight())));
        dt = new java.text.SimpleDateFormat("dd.MM.yyyy")
                .format(new java.util.Date(Long.valueOf(dt) * 1000));
        pressure = context.getString(R.string.pressure)
                + pressure
                + context.getString(R.string.pressureUnit);
        setPressure(context.getString(R.string.pressure) + String.valueOf(pressure)
                + context.getString(R.string.pressureUnit));
        weather.get(0).setId(context.getResources().getIdentifier(
                "w_" + String.valueOf(weather.get(0).getIcon()), "drawable",
                context.getPackageName()));
        /*weather.get(0).setDescription(context.getString(context.getResources().getIdentifier(
                "d" + String.valueOf(weather.get(0).getId()), "string", context.getPackageName())));*/

    }
}
