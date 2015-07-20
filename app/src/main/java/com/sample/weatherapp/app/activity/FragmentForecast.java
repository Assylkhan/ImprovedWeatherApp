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
import com.sample.weatherapp.app.model.WeatherForecast;

public class FragmentForecast extends Fragment {
    public View mV;
    String GREG = "FragmentForecast";
    ProgressBar mProgressBar;
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    @Override
    public void onResume() {

        super.onResume();
    }

    static FragmentForecast newInstance(int page) {
        FragmentForecast fragmentForecast = new FragmentForecast();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        fragmentForecast.setArguments(arguments);
        return fragmentForecast;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(GREG, "start");
        mV = inflater.inflate(R.layout.fragment_forecast, null);
        mProgressBar = (ProgressBar) mV.findViewById(R.id.ProgressBarFF);

        if (null != MainActivity.sNewData.getForecast()) {
            Log.d(GREG, "get data");
            WeatherForecast forecast = MainActivity.sNewData.getForecast();
            ForecastAdapter adapter = new ForecastAdapter(getActivity(), forecast.getList());
            ExpandableListView elvDay = (ExpandableListView) mV
                    .findViewById(R.id.ELday);
            elvDay.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
        }
        return mV;
    }
}