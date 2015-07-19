package com.sample.weatherapp.app.activity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sample.improvedweatherapp.app.R;
import com.sample.weatherapp.app.model.DayWeather;
import com.sample.weatherapp.app.model.Weather;

import java.util.List;

public class ForecastAdapter extends BaseExpandableListAdapter {
    List<com.sample.weatherapp.app.model.List> weathersForecast;
    Context ctx;
    LayoutInflater lInflater;
    String GREG = "ForecastAdapter";
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    public ForecastAdapter(Activity ctx, List<com.sample.weatherapp.app.model.List> weathersForecast) {
        this.weathersForecast = weathersForecast;
        this.ctx = ctx;
        this.lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return weathersForecast.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return weathersForecast.get(groupPosition);
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        Log.d(GREG, "getChild" + arg0);
        return weathersForecast.get(arg0);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Log.d(GREG, "getChildId" + childPosition);
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Log.d(GREG, "start getView: ");
        if (convertView == null) {
            Log.d(GREG, "if convertView==null: ");
            convertView = lInflater.inflate(R.layout.list_item_day, parent, false);
            groupViewHolder = new GroupViewHolder();

            //        .setText(weathersForecast[groupPosition].date)
            groupViewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);

            groupViewHolder.tvDayTemperature = (TextView) convertView.findViewById(R.id.tvDayTemperature);

            groupViewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

//        .setImageResource(weathersForecast[groupPosition].imageId)
            groupViewHolder.imDay = (ImageView) convertView.findViewById(R.id.imDay);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        com.sample.weatherapp.app.model.List dayForecast = weathersForecast.get(groupPosition);

        if (dayForecast != null) {
            groupViewHolder.tvDate.setText(dayForecast.getDt());
            groupViewHolder.tvDayTemperature.setText(
                    dayForecast.getTemp().getDay() + "..." + dayForecast.getTemp().getNight() + "C");
            groupViewHolder.tvDescription.setText(dayForecast.getWeather().get(0).getDescription());
            groupViewHolder.imDay.setImageResource(dayForecast.getWeather().get(0).getId());
        }

        Log.d(GREG, "start try at getView ");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup parent) {
        Log.d(GREG, "start getChildView: ");
        if (convertView == null) {
            Log.d(GREG, "if viewA==null: ");
            convertView = lInflater.inflate(R.layout.extendable_list_item_day,
                    parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.elTvMornTemperature = (TextView) convertView.findViewById(R.id.ELtvMornTemperature);
            childViewHolder.elTvDayTemperature = (TextView) convertView.findViewById(R.id.ELtvDayTemperature);
            childViewHolder.elTvNightTemperature = (TextView) convertView.findViewById(R.id.ELtvNightTemperature);
            childViewHolder.elTvEveTemperature = (TextView) convertView.findViewById(R.id.ELtvEveTemperature);
            childViewHolder.elTvWindSpeed = (TextView) convertView.findViewById(R.id.ELtvWindSpeed);
            childViewHolder.elTvHumidity = (TextView) convertView.findViewById(R.id.ELtvHumidity);
            childViewHolder.elTvPressure = (TextView) convertView.findViewById(R.id.ELtvPressure);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

//        DayWeather dayWeather = weathersForecast[groupPosition];
        com.sample.weatherapp.app.model.List dayWeather = weathersForecast.get(groupPosition);
        if (dayWeather != null) {
            childViewHolder.elTvMornTemperature.setText(dayWeather.getTemp().getMorn().toString());
            childViewHolder.elTvDayTemperature.setText(dayWeather.getTemp().getDay().toString());
            childViewHolder.elTvNightTemperature.setText(dayWeather.getTemp().getNight().toString());
            childViewHolder.elTvEveTemperature.setText(dayWeather.getTemp().getEve().toString());
            childViewHolder.elTvWindSpeed.setText(dayWeather.getSpeed().toString());
            childViewHolder.elTvHumidity.setText(dayWeather.getHumidity().toString());
            childViewHolder.elTvPressure.setText(dayWeather.getPressure().toString());
        }

        Log.d(GREG, "start try at getView ");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static class GroupViewHolder {
        public TextView tvDate;
        public TextView tvDayTemperature;
        public TextView tvDescription;
        public ImageView imDay;
    }

    private static class ChildViewHolder {
        public TextView elTvMornTemperature;
        public TextView elTvDayTemperature;
        public TextView elTvNightTemperature;
        public TextView elTvEveTemperature;
        public TextView elTvWindSpeed;
        public TextView elTvHumidity;
        public TextView elTvPressure;
    }
}
