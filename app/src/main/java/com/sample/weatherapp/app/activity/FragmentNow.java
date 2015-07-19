package com.sample.weatherapp.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.model.DayWeather;

public class FragmentNow extends Fragment {
    LinearLayout LL;
    TextView city;
    ImageView imWeatherIcon;
    TextView temperature;
    TextView descript;
    TextView windSpeed;
    TextView humidity;
    TextView pressure;
    ProgressBar progressBar;
    DayWeather dw;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(GREG, "FragmentNow started");
        final View v = inflater.inflate(R.layout.fragment_now, null);

        LL = (LinearLayout) v.findViewById(R.id.FNLinearL);
        LL.setVisibility(View.GONE);
        progressBar = (ProgressBar) v.findViewById(R.id.FNProgressBar);
        if (null != MainActivity.newData.getNowWeather()) {

            Log.d(GREG, "get data");
            dw = MainActivity.newData.getNowWeather();

            Log.d(GREG, "FragmentNow post.run setText");

            city = (TextView) v.findViewById(R.id.city);
            imWeatherIcon = (ImageView) v
                    .findViewById(R.id.imWeatherIcon);
            temperature = (TextView) v
                    .findViewById(R.id.temperature);
            descript = (TextView) v.findViewById(R.id.description);
            windSpeed = (TextView) v.findViewById(R.id.windSpeed);
            humidity = (TextView) v.findViewById(R.id.humidity);
            pressure = (TextView) v.findViewById(R.id.pressure);

            city.setText(dw.getName());
            imWeatherIcon.setImageResource(dw.getWeather().get(0).getIcon());
            temperature.setText(dw.getMain().getTemp()+ "C");
            descript.setText(dw.getWeather().get(0).getDescription());
            windSpeed.setText(dw.getWind().getSpeed().toString());
            humidity.setText(dw.getMain().getHumidity().toString());
            pressure.setText(dw.getMain().getPressure().toString());
            LL.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        return v;
    }
}
