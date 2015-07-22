package com.sample.weatherapp.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.util.GPSTracker;

public class LocationActivity extends Activity implements OnClickListener, LocationListener {

    private final String GREG = "mLocationActivity";
    private Activity mLocationActivity = this;
    private AutoCompleteTextView mTvEnterCity;
    private EditText mLat;
    private EditText mLon;
    private LocationManager mLocationManager;
    private String mProvider;
    private GPSTracker mGps;
    private boolean mFromMain;
    public SharedPreferences mSettings;

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
        Log.d(GREG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent intent = getIntent();
        String checkFlag = intent.getStringExtra("flag");
        if (checkFlag != null && checkFlag.equals("Main")) mFromMain = true;

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
        if (mSettings.contains("title")){
            String city = mSettings.getString("city", null);
            String lat = mSettings.getString("lat", null);
            String lon = mSettings.getString("lon", null);
            if (city != null && !city.isEmpty()){
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
        Button btnSearchByCrd = (Button) findViewById(R.id.btnSearchByCrd);
        btnSearchByCrd.setOnClickListener(this);
    }

    public void onLocationChanged(Location location) {
        int latitude = (int) (location.getLatitude());
        int longitude = (int) (location.getLongitude());
        mLat.setText(String.valueOf(latitude));
        mLon.setText(String.valueOf(longitude));
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
        Intent intent = null;
        if (mFromMain) intent = new Intent();
        else intent = new Intent(this, MainActivity.class);
        switch (v.getId()) {
            case R.id.btnSearchByCity:
                String str = mTvEnterCity.getText().toString();
                Log.d(GREG, "read city " + str);
                intent.putExtra("city", str);
                setResult(1, intent);
                break;
            case R.id.btnSearchByCrd:
                String lat = this.mLat.getText().toString();
                String lon = this.mLon.getText().toString();
                Log.d(GREG, "read mLat " + lat + "mLon " + lon);
                intent.putExtra("mLat", lat);
                intent.putExtra("mLon", lon);
                setResult(2, intent);
                break;
        }
        if (!mFromMain) startActivity(intent);
        finish();
    }
}
