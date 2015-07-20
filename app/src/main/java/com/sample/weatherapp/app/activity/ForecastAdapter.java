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

import java.util.List;

public class ForecastAdapter extends BaseExpandableListAdapter {
    List<com.sample.weatherapp.app.model.List> mWeathersForecast;
    Context mCtx;
    LayoutInflater mLInflater;
    String GREG = "ForecastAdapter";
    private ChildViewHolder mChildViewHolder;
    private GroupViewHolder mGroupViewHolder;

    public ForecastAdapter(Activity mCtx, List<com.sample.weatherapp.app.model.List> mWeathersForecast) {
        this.mWeathersForecast = mWeathersForecast;
        this.mCtx = mCtx;
        this.mLInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mWeathersForecast.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mWeathersForecast.get(groupPosition);
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        Log.d(GREG, "getChild" + arg0);
        return mWeathersForecast.get(arg0);
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
            convertView = mLInflater.inflate(R.layout.list_item_day, parent, false);
            mGroupViewHolder = new GroupViewHolder();

            //        .setText(mWeathersForecast[groupPosition].date)
            mGroupViewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);

            mGroupViewHolder.tvDayTemperature = (TextView) convertView.findViewById(R.id.tvDayTemperature);

            mGroupViewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

//        .setImageResource(mWeathersForecast[groupPosition].imageId)
            mGroupViewHolder.imDay = (ImageView) convertView.findViewById(R.id.imDay);
            convertView.setTag(mGroupViewHolder);
        } else {
            mGroupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        com.sample.weatherapp.app.model.List dayForecast = mWeathersForecast.get(groupPosition);

        if (dayForecast != null) {
            mGroupViewHolder.tvDate.setText(dayForecast.getDt());
            mGroupViewHolder.tvDayTemperature.setText(
                    dayForecast.getTemp().getDay() + "..." + dayForecast.getTemp().getNight() + "C");
            mGroupViewHolder.tvDescription.setText(dayForecast.getWeather().get(0).getDescription());
            mGroupViewHolder.imDay.setImageResource(dayForecast.getWeather().get(0).getId());
        }

        Log.d(GREG, "start try at getView ");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup parent) {
        Log.d(GREG, "start getChildView: ");
        if (convertView == null) {
            Log.d(GREG, "if viewA==null: ");
            convertView = mLInflater.inflate(R.layout.extendable_list_item_day,
                    parent, false);
            mChildViewHolder = new ChildViewHolder();
            mChildViewHolder.elTvMornTemperature = (TextView) convertView.findViewById(R.id.ELtvMornTemperature);
            mChildViewHolder.elTvDayTemperature = (TextView) convertView.findViewById(R.id.ELtvDayTemperature);
            mChildViewHolder.elTvNightTemperature = (TextView) convertView.findViewById(R.id.ELtvNightTemperature);
            mChildViewHolder.elTvEveTemperature = (TextView) convertView.findViewById(R.id.ELtvEveTemperature);
            mChildViewHolder.elTvWindSpeed = (TextView) convertView.findViewById(R.id.ELtvWindSpeed);
            mChildViewHolder.elTvHumidity = (TextView) convertView.findViewById(R.id.ELtvHumidity);
            mChildViewHolder.elTvPressure = (TextView) convertView.findViewById(R.id.ELtvPressure);
            convertView.setTag(mChildViewHolder);
        } else {
            mChildViewHolder = (ChildViewHolder) convertView.getTag();
        }

//        DayWeather dayWeather = mWeathersForecast[groupPosition];
        com.sample.weatherapp.app.model.List dayWeather = mWeathersForecast.get(groupPosition);
        if (dayWeather != null) {
            mChildViewHolder.elTvMornTemperature.setText(dayWeather.getTemp().getMorn().toString());
            mChildViewHolder.elTvDayTemperature.setText(dayWeather.getTemp().getDay().toString());
            mChildViewHolder.elTvNightTemperature.setText(dayWeather.getTemp().getNight().toString());
            mChildViewHolder.elTvEveTemperature.setText(dayWeather.getTemp().getEve().toString());
            mChildViewHolder.elTvWindSpeed.setText(dayWeather.getSpeed().toString());
            mChildViewHolder.elTvHumidity.setText(dayWeather.getHumidity().toString());
            mChildViewHolder.elTvPressure.setText(dayWeather.getPressure().toString());
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
