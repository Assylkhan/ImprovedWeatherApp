package com.sample.weatherapp.app.model;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.util.WeatherUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayWeather {
    private Coord coord;
    private Sys sys;
    private List<Weather> weather = new ArrayList<Weather>();
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private float dt;
    private int id;
    private String name;
    private float cod;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The coord
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     *
     * @param coord
     * The coord
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    /**
     *
     * @return
     * The sys
     */
    public Sys getSys() {
        return sys;
    }

    /**
     *
     * @param sys
     * The sys
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    /**
     *
     * @return
     * The weather
     */
    public List<Weather> getWeather() {
        return weather;
    }

    /**
     *
     * @param weather
     * The weather
     */
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    /**
     *
     * @return
     * The base
     */
    public String getBase() {
        return base;
    }

    /**
     *
     * @param base
     * The base
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     *
     * @return
     * The main
     */
    public Main getMain() {
        return main;
    }

    /**
     *
     * @param main
     * The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     *
     * @return
     * The wind
     */
    public Wind getWind() {
        return wind;
    }

    /**
     *
     * @param wind
     * The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     *
     * @return
     * The clouds
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     *
     * @param clouds
     * The clouds
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    /**
     *
     * @return
     * The dt
     */
    public float getDt() {
        return dt;
    }

    /**
     *
     * @param dt
     * The dt
     */
    public void setDt(float dt) {
        this.dt = dt;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The cod
     */
    public float getCod() {
        return cod;
    }

    /**
     *
     * @param cod
     * The cod
     */
    public void setCod(float cod) {
        this.cod = cod;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "DayWeather{" +
                "coord=" + coord +
                ", sys=" + sys +
                ", weather=" + weather +
                ", base='" + base + '\'' +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                ", additionalProperties=" + additionalProperties +
                '}';
    }

    public void transform(Context context){
        String temp = WeatherUtil.temperatureEdit(Double.valueOf(main.getTemp()));
        main.setTemp(temp);
        main.setPressure(context.getString(R.string.pressure) + String.valueOf(main.getPressure())
                + context.getString(R.string.pressureUnit));
        main.setHumidity(context.getString(R.string.humidity) + String.valueOf(main.getHumidity())
                + context.getString(R.string.percent));
        wind.setSpeed(context.getString(R.string.wind) + " " +
                context.getString(WeatherUtil.windDir((int) (double) Double.valueOf(wind.getDeg()))) + " "
                + wind.getSpeed() + " "  +  context.getString(R.string.speedUnit));
        weather.get(0).setDescription(context.getString(context.getResources().getIdentifier(
                "d" + String.valueOf(weather.get(0).getId()), "string",
                context.getPackageName())));
        int drawable = context.getResources().getIdentifier(
                "w_" + weather.get(0).getIcon(), "drawable",
                context.getPackageName());
        weather.get(0).setIcon(String.valueOf(drawable));
    }
}
