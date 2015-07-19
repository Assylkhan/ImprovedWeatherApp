package com.sample.weatherapp.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.model.DayWeather;
import com.sample.weatherapp.app.model.Weather;
import com.sample.weatherapp.app.model.WeatherForecast;

import java.util.List;

public class FragmentForecast extends Fragment {
    public View v;
    String greg = "FragmentForecast";
    ProgressBar progressBar;
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    static FragmentForecast newInstance(int page) {
        FragmentForecast fragmentForecast = new FragmentForecast();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        fragmentForecast.setArguments(arguments);
        return fragmentForecast;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(greg, "start");
        v = inflater.inflate(R.layout.fragment_forecast, null);
        progressBar = (ProgressBar) v.findViewById(R.id.ProgressBarFF);

        if (null != MainActivity.newData.getForecast()) {
            Log.d(greg, "get data");
            WeatherForecast forecast = MainActivity.newData.getForecast();
            ForecastAdapter adapter = new ForecastAdapter(getActivity(), forecast.getList());
            ExpandableListView elvDay = (ExpandableListView) v
                    .findViewById(R.id.ELday);
            elvDay.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }

        return v;
    }
}