package com.sample.weatherapp.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.model.DayWeather;

public class FragmentNow extends Fragment {
    View myFragmentView;
    LinearLayout mLL;
    TextView mCity;
    ImageView mImWeatherIcon;
    TextView mTemperature;
    TextView mDescript;
    TextView mWindSpeed;
    TextView mHumidity;
    TextView mPressure;
    ProgressBar mProgressBar;
    DayWeather mDw;
    final String GREG = "FragmentNow";
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    static FragmentNow newInstance(int page) {
        FragmentNow pageFragment = new FragmentNow();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(GREG, "FragmentNow started");
        myFragmentView = inflater.inflate(R.layout.fragment_now, null);
        mLL = (LinearLayout) myFragmentView.findViewById(R.id.FNLinearL);
        mLL.setVisibility(View.GONE);
        mProgressBar = (ProgressBar) myFragmentView.findViewById(R.id.FNProgressBar);
        if (null != MainActivity.sNewData.getNowWeather()) {

            Log.d(GREG, "get data");
            mDw = MainActivity.sNewData.getNowWeather();

            Log.d(GREG, "FragmentNow post.run setText");

            mCity = (TextView) myFragmentView.findViewById(R.id.city);
            mImWeatherIcon = (ImageView) myFragmentView
                    .findViewById(R.id.imWeatherIcon);
            mTemperature = (TextView) myFragmentView
                    .findViewById(R.id.temperature);
            mDescript = (TextView) myFragmentView.findViewById(R.id.description);
            mWindSpeed = (TextView) myFragmentView.findViewById(R.id.windSpeed);
            mHumidity = (TextView) myFragmentView.findViewById(R.id.humidity);
            mPressure = (TextView) myFragmentView.findViewById(R.id.pressure);

            mCity.setText(mDw.getName());
            mImWeatherIcon.setImageResource(mDw.getWeather().get(0).getIcon());
            mTemperature.setText(mDw.getMain().getTemp() + "C");
            mDescript.setText(mDw.getWeather().get(0).getDescription());
            mWindSpeed.setText(mDw.getWind().getSpeed().toString());
            mHumidity.setText(mDw.getMain().getHumidity().toString());
            mPressure.setText(mDw.getMain().getPressure().toString());
            mLL.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
        return myFragmentView;
    }
}
