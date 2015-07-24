package com.sample.weatherapp.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.util.GPSTracker;

public class LocationActivity extends FragmentActivity implements OnClickListener, LocationListener {

    private static final String TAG = "LocationActivity";
    public static final int SEARCH_BY_CITY = 1;
    public static final int SEARCH_BY_COORDINATES = 2;
    public static final String FLAG = "flag";
    public SharedPreferences mSettings;
    private Activity mLocationActivity = this;
    private AutoCompleteTextView mTvEnterCity;
    private EditText mLat;
    private EditText mLon;
    private String mMapLat;
    private String mMapLon;
    private LocationManager mLocationManager;
    private String mProvider;
    private GPSTracker mGps;
    private boolean mFromMain;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;


    private String formatLocation(Location location) {
        if (location == null) return "";
        return
                String.format(
                        "%1$.4f, %2$.4f",
                        location.getLatitude(), location.getLongitude());
//        new Date(
//                        location.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent intent = getIntent();
        mFromMain = intent.getBooleanExtra(FLAG, false);

        mTvEnterCity = (AutoCompleteTextView) findViewById(R.id.autoCompleteCity);
        String[] cities = getResources().getStringArray(R.array.city);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, cities);
        mTvEnterCity.setAdapter(adapter);

        Button btnSearchByCity = (Button) findViewById(R.id.btnSearchByCity);
        btnSearchByCity.setOnClickListener(this);

        mLat = (EditText) findViewById(R.id.editLat);
        mLon = (EditText) findViewById(R.id.editLon);

        mSettings = getSharedPreferences("LAST_DATA", Context.MODE_PRIVATE);
        if (mSettings.contains("title")) {
            String city = mSettings.getString("city", null);
            String lat = mSettings.getString("lat", null);
            String lon = mSettings.getString("lon", null);
            mMapLat = lat;
            mMapLon = lon;
            if (city != null && !city.isEmpty()) {
                mTvEnterCity.setText(city);
            } else {
                mLat.setText(lat);
                mLon.setText(lon);
            }
        }

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                Toast toast = Toast.makeText(mLocationActivity,
                        "Пишите на английском",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                for (int i = start; i < end; i++) {
                    if (!isEnglish(source.charAt(i))) {
                        toast.show();
                        return "";
                    }
                }
                return null;
            }

            private boolean isEnglish(char charAt) {
                String validationString = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM, ";
                if (validationString.indexOf(charAt) == -1)
                    return false;
                else
                    return true;
            }
        };
        mTvEnterCity.setFilters(new InputFilter[]{filter});

        // Get the location manager
        /*mGps = new GPSTracker(LocationActivity.this);
        if (mGps.canGetLocation()) {
            mLat.setText(Double.toString(mGps.getLatitude()));
            mLon.setText(String.valueOf(mGps.getLongitude()));
        }*/

        try {

            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
            if (mMap == null) {
                finish();
                return;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        initMap();

        Button btnSearchByCrd = (Button) findViewById(R.id.btnSearchByCrd);
        btnSearchByCrd.setOnClickListener(this);

        Button btnSearchByMap = (Button) findViewById(R.id.btnSearchByMap);
        btnSearchByMap.setOnClickListener(this);
    }

    public void onLocationChanged(Location location) {
        int latitude = (int) (location.getLatitude());
        int longitude = (int) (location.getLongitude());
        mLat.setText(String.valueOf(latitude));
        mLon.setText(String.valueOf(longitude));
    }

    private void initMap() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                mMapLat = Location.convert(latLng.latitude, Location.FORMAT_DEGREES);
                try {
                    mMapLon = Location.convert(latLng.longitude, Location.FORMAT_DEGREES);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                final LatLng PERTH = new LatLng(latLng.latitude, latLng.longitude);
                Marker perth = mMap.addMarker(new MarkerOptions()
                        .position(PERTH)
                        .draggable(true));

                Toast.makeText(LocationActivity.this, "Место выбрано", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onMapClick: " + mMapLat + "," + mMapLon);
            }
        });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new mProvider " + mProvider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled mProvider " + mProvider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (mFromMain) intent = new Intent();
        else intent = new Intent(this, MainActivity.class);
        switch (v.getId()) {
            case R.id.btnSearchByCity:
                String city = mTvEnterCity.getText().toString();
                if (city == null || city.isEmpty()) {
                    mTvEnterCity.setError("заполните поле");
                    return;
                }
                Log.d(TAG, "read city " + city);
                intent.putExtra("city", city);
                setResult(SEARCH_BY_CITY, intent);
                break;
            case R.id.btnSearchByCrd:
                String lat = this.mLat.getText().toString();
                String lon = this.mLon.getText().toString();
                if (lat == null || lat.isEmpty()) {
                    mLat.setError("заполните поле");
                }
                if (lon == null || lon.isEmpty()) {
                    mLon.setError("заполните поле");
                }
                if (lat == null || lat.isEmpty() || lon == null || lon.isEmpty()) {
                    return;
                }
                Log.d(TAG, "read mLat " + lat + "mLon " + lon);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                setResult(SEARCH_BY_COORDINATES, intent);
                break;
            case R.id.btnSearchByMap:
                intent.putExtra("lat", mMapLat);
                intent.putExtra("lon", mMapLon);
                setResult(SEARCH_BY_COORDINATES, intent);
                break;
        }
        if (!mFromMain) startActivity(intent);
        finish();
    }
}
