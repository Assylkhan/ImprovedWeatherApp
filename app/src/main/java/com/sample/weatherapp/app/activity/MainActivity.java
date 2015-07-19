package com.sample.weatherapp.app.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.model.*;
import com.sample.weatherapp.app.util.JsonParserUtil;
import com.sample.weatherapp.app.util.MyListener;

import java.util.*;
import java.util.List;

public class MainActivity extends FragmentActivity {

    static final String TAG = "MainActivity";
    public static Data newData;

    static final int PAGE_COUNT = 2;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    public ImageView imv;
    boolean refreshAnim = false;
    MenuItem refreshItem;
    public boolean visibleOnScreen = false;
    public boolean showNewData = false;
    public SharedPreferences mSettings;
    private static final String GREG = "mainActivity";
    RequestQueue queue;
    JsonObjectRequest currentWeatherRequest;
    JsonObjectRequest forecastRequest;

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(GREG, "onPause");
        visibleOnScreen = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(GREG, "onResume");
        if (showNewData) {
            afterUrlTask();
            showNewData = false;
        }
        visibleOnScreen = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GREG, "OnCreate");
        setContentView(R.layout.first_activity);

        queue = Volley.newRequestQueue(this);
        pager = (ViewPager) findViewById(R.id.pager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(FragmentNow.newInstance(0));
        fragments.add(FragmentForecast.newInstance(1));
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);

        final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
                pagerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        pager.addOnPageChangeListener(onPageChangeListener);
        onPageChangeListener.onPageSelected(pager.getCurrentItem());
        pager.post(new Runnable(){

            @Override
            public void run() {
                onPageChangeListener.onPageSelected(pager.getCurrentItem());
            }
        });

        mSettings = getSharedPreferences("LAST_DATA", Context.MODE_PRIVATE);
        newData = new Data();
        if (mSettings.contains("title")) {            // get last save data
            Log.d(GREG, "get last save data");
            newData.title = mSettings.getString("title", null);
            newData.urlStrDay = mSettings.getString("urlStrDay", null);
            newData.urlStrForecast = mSettings
                    .getString("urlStrForecast", null);

            DayWeather dw;
            try {
                Log.d(GREG, "try pars from last data");
                Gson gson = new Gson();

                dw = gson.fromJson(mSettings.getString("jsonDay", null), DayWeather.class);
                newData.setNowWeather(dw);

                WeatherForecast weatherForecast = gson.fromJson(mSettings.getString(
                        "jsonForecast", null), WeatherForecast.class);

                newData.setForecast(weatherForecast);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {

            // ask user chose city or coordinate
            Log.d(GREG, "startChangeActivity");
            startChangeActivity();
        }
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private java.util.List<Fragment> fragments;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "сейчас";
                case 1:
                    return "прогноз";
                default:
                    return null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        Log.d("anim", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (null == refreshItem) {
            // first time create menu
            refreshItem = menu.findItem(R.id.refresh);
            return true;
        }

//        MenuItemCompat.getActionView(refreshItem)
        if (!refreshAnim && null != refreshItem.getActionView()) {
            // stop animation
            Log.d("anim", "set action view null");
            refreshItem.getActionView().clearAnimation();
            return true;
        } else {
            // start animation
            refreshItem = menu.findItem(R.id.refresh);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imv = (ImageView) inflater.inflate(R.layout.imv_refresh, null);
            Animation an = AnimationUtils.loadAnimation(this,
                    R.anim.loadingrotate);
            an.setRepeatCount(Animation.INFINITE);
            imv.startAnimation(an);

            Log.d("anim", "set action view imv");
            MenuItemCompat.setActionView(refreshItem, imv);
            refreshItem.setIcon(null);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editCity:
                startChangeActivity();
                break;
            case R.id.refresh:
                refresh();
                break;
        }
        return true;
    }

    public void startChangeActivity() {
        Log.d(GREG, "startChangeActivity");
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("flag", "Main");
        startActivityForResult(intent, 1);
//        finish();
    }

    private void refresh() {
        // enough data to query
        if (null == newData.urlStrForecast || null == newData.urlStrDay
                || null == newData.title) {

            Toast.makeText(this, "�������� �����", Toast.LENGTH_LONG).show();

            return;
        }
        stopQuery();

        // start new query with last data
        queue.add(currentWeatherRequest);
        queue.add(forecastRequest);
    }

    public void afterUrlTask() {
        if (pager != null) {
            pager.setCurrentItem(0);
//            bar.setTitle(newData.title);
        }
    }

    private void stopQuery() {// stop another query
        /*if (null != queue
                && (!dataHandler.isCancelled() || AsyncTask.Status.FINISHED != dataHandler
                .getStatus()))
            dataHandler.cancel(false);*/
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(GREG, "onActivityResult");
        MyListener currentWeatherListener = null;
        MyListener forecastListener = null;
        switch (resultCode) {
            case 1:
                final String city = intent.getStringExtra("city");

                // save query string and title to refresh data next time
                newData.urlStrDay = newData.STR_CURRENT_WEATHER + "q=" + newData.city;
                newData.urlStrForecast = newData.STR_FORECAST + "q=" + newData.city + "&cnt=14";
                newData.city = city;

                Log.d(GREG, "city " + city);
//                stop other queries
                stopQuery();
                Log.d(GREG, "request by city");

                currentWeatherListener = new MyListener(newData, WeatherType.CURRENT_WEATHER, this);
                forecastListener = new MyListener(newData, WeatherType.FORECAST, this);

                break;
            case 2:
                // start new query with coordinate
                newData.lat = intent.getStringExtra("lat");
                newData.lon = intent.getStringExtra("lon");
                newData.urlStrDay = newData.STR_CURRENT_WEATHER + "lat=" + newData.lat + "&lon=" + newData.lon;
                newData.urlStrForecast = newData.STR_FORECAST + "lat="
                                         + newData.lat + "&lon=" + newData.lon + "&cnt=14";
                Log.d(GREG, "request by lat and lon");
//              stop other queries
                stopQuery();
                currentWeatherListener = new MyListener(newData, WeatherType.CURRENT_WEATHER, this);
                forecastListener = new MyListener(newData, WeatherType.FORECAST, this);
                break;
        }

        Response.ErrorListener myErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        };

        currentWeatherRequest = new JsonObjectRequest(
                Request.Method.GET, newData.urlStrDay, (String) null, currentWeatherListener, myErrorListener);

        forecastRequest = new JsonObjectRequest(
                Request.Method.GET, newData.urlStrForecast, (String) null, forecastListener, myErrorListener);

        queue.add(currentWeatherRequest);
        queue.add(forecastRequest);
    }

    public void loadAnimationStart() {
        // start loading animation
        if (!refreshAnim) {
            Log.d("anim", "animation successful start ");
            refreshAnim = true;
            supportInvalidateOptionsMenu();
        }
    }

    public void loadAnimationStop() {
        // stop loading animation
        if (refreshAnim) {
            Log.d("anim", "animation successful stop");
            refreshAnim = false;
            supportInvalidateOptionsMenu();
        }
    }
}
