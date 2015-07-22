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
import com.sample.weatherapp.app.util.MyListener;

import java.util.*;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "mainActivity";
    private static final int PAGE_COUNT = 2;

    public static Data sNewData;
//    todo: from friendly to private
    private ViewPager mPager;
//    todo: from friendly to private
    private PagerAdapter mPagerAdapter;
    public ImageView mImv;
    public boolean mRefreshAnim = false;
    public MenuItem mRefreshItem;
    public boolean mVisibleOnScreen = false;

    public boolean mShowNewData = false;
    public SharedPreferences mSettings;
    private RequestQueue mQueue;
    private JsonObjectRequest mCurrentWeatherRequest;
    private JsonObjectRequest mForecastRequest;

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mVisibleOnScreen = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (mShowNewData) {
            afterUrlTask();
            mShowNewData = false;
        }
        mVisibleOnScreen = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setContentView(R.layout.first_activity);

        mQueue = Volley.newRequestQueue(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(FragmentNow.newInstance(0));
        fragments.add(FragmentForecast.newInstance(1));
        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private Fragment mFragment;


            @Override
            public void onPageSelected(int position) {

//                Log.d(TAG, "onPageSelected, position = " + position);
//                Fragment.instantiate(MainActivity.this, "FragmentNow");
                /*Fragment fragment = ((FragmentPagerAdapter) mPager.getAdapter()).getItem(position);
                if (fragment != null) {
                    fragment.onResume();
                }*/
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mSettings = getSharedPreferences("LAST_DATA", Context.MODE_PRIVATE);
        sNewData = new Data();
        if (mSettings.contains("title")) {            // get last save data
            Log.d(TAG, "get last save data");
            sNewData.title = mSettings.getString("title", null);
            sNewData.urlStrDay = mSettings.getString("urlStrDay", null);
            sNewData.urlStrForecast = mSettings
                    .getString("urlStrForecast", null);

            DayWeather dw;
            try {
                Log.d(TAG, "try pars from last data");
                Gson gson = new Gson();

                dw = gson.fromJson(mSettings.getString("jsonDay", null), DayWeather.class);
                sNewData.setNowWeather(dw);

                WeatherForecast weatherForecast = gson.fromJson(mSettings.getString(
                        "jsonForecast", null), WeatherForecast.class);

                sNewData.setForecast(weatherForecast);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {

            // ask user chose city or coordinate
            Log.d(TAG, "startChangeActivity");
            startChangeActivity();
        }
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null) return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentNow.newInstance(position);
                case 1:
                    return FragmentForecast.newInstance(position);
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
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

        if (null == mRefreshItem) {
            // first time create menu
            mRefreshItem = menu.findItem(R.id.refresh);
            return true;
        }

//        MenuItemCompat.getActionView(mRefreshItem)
        if (!mRefreshAnim && null != mRefreshItem.getActionView()) {
            // stop animation
            Log.d("anim", "set action view null");
            mRefreshItem.getActionView().clearAnimation();
            return true;
        } else {
            // start animation
            mRefreshItem = menu.findItem(R.id.refresh);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImv = (ImageView) inflater.inflate(R.layout.imv_refresh, null);
            Animation an = AnimationUtils.loadAnimation(this,
                    R.anim.loadingrotate);
            an.setRepeatCount(Animation.INFINITE);
            mImv.startAnimation(an);

            Log.d("anim", "set action view mImv");
            MenuItemCompat.setActionView(mRefreshItem, mImv);
            mRefreshItem.setIcon(null);
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
        Log.d(TAG, "startChangeActivity");
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("flag", "Main");
        startActivityForResult(intent, 1);
//        finish();
    }

    private void refresh() {
        // enough data to query
        if (null == sNewData.urlStrForecast || null == sNewData.urlStrDay
                || null == sNewData.title) {

            Toast.makeText(this, "�������� �����", Toast.LENGTH_LONG).show();

            return;
        }
        stopQuery();

        // start new query with last data
        mQueue.add(mCurrentWeatherRequest);
        mQueue.add(mForecastRequest);
    }

    public void afterUrlTask() {
        if (mPager != null) {
            mPagerAdapter.notifyDataSetChanged();
            mPager.setCurrentItem(0);
//            bar.setTitle(sNewData.title);
        }
    }

    private void stopQuery() {// stop another query
        /*if (null != mQueue
                && (!dataHandler.isCancelled() || AsyncTask.Status.FINISHED != dataHandler
                .getStatus()))
            dataHandler.cancel(false);*/
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult");
        MyListener currentWeatherListener = null;
        MyListener forecastListener = null;
        switch (resultCode) {
            case 1:
                final String city = intent.getStringExtra("city");

                // save query string and title to refresh data next time
                sNewData.urlStrDay = sNewData.STR_CURRENT_WEATHER + "q=" + city;
                sNewData.urlStrForecast = sNewData.STR_FORECAST + "q=" + city + "&cnt=14";
                sNewData.city = city;

                Log.d(TAG, "mCity " + city);
//                stop other queries
                stopQuery();
                Log.d(TAG, "request by city");

                currentWeatherListener = new MyListener(sNewData, WeatherType.CURRENT_WEATHER, this);
                forecastListener = new MyListener(sNewData, WeatherType.FORECAST, this);

                break;
            case 2:
                // start new query with coordinate
                sNewData.lat = intent.getStringExtra("lat");
                sNewData.lon = intent.getStringExtra("lon");
                sNewData.urlStrDay = sNewData.STR_CURRENT_WEATHER + "lat=" + sNewData.lat + "&lon=" + sNewData.lon;
                sNewData.urlStrForecast = sNewData.STR_FORECAST + "lat="
                        + sNewData.lat + "&lon=" + sNewData.lon + "&cnt=14";
                Log.d(TAG, "request by lat and lon");
//              stop other queries
                stopQuery();
                currentWeatherListener = new MyListener(sNewData, WeatherType.CURRENT_WEATHER, this);
                forecastListener = new MyListener(sNewData, WeatherType.FORECAST, this);
                break;
        }

        Response.ErrorListener myErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        };

        mCurrentWeatherRequest = new JsonObjectRequest(
                Request.Method.GET, sNewData.urlStrDay, (String) null, currentWeatherListener, myErrorListener);

        mForecastRequest = new JsonObjectRequest(
                Request.Method.GET, sNewData.urlStrForecast, (String) null, forecastListener, myErrorListener);

        mQueue.add(mCurrentWeatherRequest);
        mQueue.add(mForecastRequest);
    }

    public void loadAnimationStart() {
        // start loading animation
        if (!mRefreshAnim) {
            Log.d("anim", "animation successful start ");
            mRefreshAnim = true;
            supportInvalidateOptionsMenu();
        }
    }

    public void loadAnimationStop() {
        // stop loading animation
        if (mRefreshAnim) {
            Log.d("anim", "animation successful stop");
            mRefreshAnim = false;
            supportInvalidateOptionsMenu();
        }
    }
}
