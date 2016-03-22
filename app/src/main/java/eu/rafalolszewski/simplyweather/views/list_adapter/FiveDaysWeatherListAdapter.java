package eu.rafalolszewski.simplyweather.views.list_adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;

/**
 * Created by rafal on 15.03.16.
 */
public class FiveDaysWeatherListAdapter extends BaseAdapter{

    private WeatherFiveDaysData weatherData;

    public void setWeatherData(WeatherFiveDaysData weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public int getCount() {
        return weatherData.weatherLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}
