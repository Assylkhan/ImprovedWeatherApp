package com.sample.weatherapp.app.util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sample.weatherapp.app.activity.MainActivity;
import com.sample.weatherapp.app.model.Data;
import com.sample.weatherapp.app.model.DayWeather;
import com.sample.weatherapp.app.model.WeatherForecast;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;

public class DataHandler {
    private String urlStrDay;
    private String urlStrForecast;
    private String title;
    private Data data;

    private MainActivity mainActivity;
    private String greg = "UrlTask";
    private DayWeather nowWeather;
    private WeatherForecast weatherForecast;
    String jsonDay;
    String jsonForecast;

    public DataHandler(MainActivity mainActivity, Data d, String urlStrDay,
                       String urlStrForecast, String title) {
        Log.d(greg, "constructor ");
        data = d;
        this.title = title;
        this.urlStrDay = urlStrDay; // jsonDay
        this.urlStrForecast = urlStrForecast; // jsonForecast
        this.mainActivity = mainActivity;
    }

    protected Integer doInBackground(Void... voids) {
        try {// jsonDay
            Log.d(greg, urlStrDay);
            jsonDay = urlConnect2(urlStrDay);
            parseDay(jsonDay);
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        try {// jsonForecast
            Log.d(greg, urlStrForecast);
            jsonForecast = urlConnect2(urlStrForecast);
            parseForecast(jsonForecast);
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        } catch (RuntimeException e){
            e.printStackTrace();
            Log.e(DataHandler.class.getSimpleName(), "exception", e);
        }
        return 0;
    }

    protected void onPostExecute(Integer integer) {
        switch (integer) {
            // if data was not received
            case 2:
                Toast.makeText(mainActivity, "данные не были получены",
                        Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(mainActivity,
                        "данные не были получены",
                        Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(mainActivity,
                        "error",
                        Toast.LENGTH_LONG).show();
                break;

            // if data has been successfully received
            case 0:
                // save query string and title to refresh data next time
                data.urlStrDay = urlStrDay;
                data.urlStrForecast = urlStrForecast;
                data.title = title;
                data.setForecast(weatherForecast);
                data.setNowWeather(nowWeather);

                SharedPreferences.Editor editor = mainActivity.mSettings.edit();
                editor.putString("title", title);
                editor.putString("urlStrDay", urlStrDay);
                editor.putString("urlStrForecast", urlStrForecast);

                Gson gson = new Gson();
                String jsDay = gson.toJson(nowWeather);
                String jsForecast = gson.toJson(weatherForecast);
                editor.putString("jsonDay", jsDay);
                editor.putString("jsonForecast", jsForecast);
                editor.apply();

                // refresh visible activity
                if (mainActivity.visibleOnScreen)
                    mainActivity.afterUrlTask();
                else
                    mainActivity.showNewData = true;
                break;
        }
        mainActivity.loadAnimationStop();
    }

    void parseForecast(String jsonWeather) throws JSONException {
        /*Log.d(greg, "try parse jsonForecast: " + jsonWeather);
        WeatherForecast weatherForecast = JsonParserUtil.parse(jsonWeather, WeatherForecast.class);
        weatherForecast.transform(mainActivity);
        this.weatherForecast = weatherForecast;*/
    }

    void parseDay(String jsonWeather) throws JSONException {
        /*Log.d(greg, "try parse jsonDay: " + jsonWeather);
        DayWeather weather = JsonParserUtil.parse(jsonWeather, DayWeather.class);
        weather.transform(mainActivity);
        nowWeather = weather;*/
    }

    String urlConnect2(String url) throws
            IOException, JSONException, IOException {
        String response = EntityUtils.toString(new DefaultHttpClient().execute(
                new HttpGet(url)).getEntity());
        return response;
    }
}
