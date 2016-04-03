package eu.rafalolszewski.simplyweather.views.list_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherList;
import eu.rafalolszewski.simplyweather.util.ImageMapper;
import eu.rafalolszewski.simplyweather.util.StringsProvider;

/**
 * Created by rafal on 15.03.16.
 */
public class FiveDaysWeatherListAdapter extends BaseAdapter{

    private WeatherFiveDaysData weatherData;

    private LayoutInflater inflater;

    private ImageMapper imageMapper;

    private StringsProvider stringsProvider;


    public FiveDaysWeatherListAdapter(LayoutInflater inflater, ImageMapper imageMapper, StringsProvider stringsProvider) {
        this.inflater = inflater;
        this.imageMapper = imageMapper;
        this.stringsProvider = stringsProvider;
    }

    public void setWeatherData(WeatherFiveDaysData weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public int getCount() {
        if (weatherData == null) return 0;
        return weatherData.weatherLists.length;
    }

    @Override
    public WeatherList getItem(int position) {
        return weatherData.weatherLists[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View  view = getWeatherView(position, parent);

        return view;
    }

    private View getWeatherView(int position, ViewGroup parent) {

        WeatherList data = getItem(position);

        View view = inflater.inflate(R.layout.listitem_weather_5days, parent, false);

        TextView hour = (TextView) view.findViewById(R.id.hour);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView small_label = (TextView) view.findViewById(R.id.small_label);
        TextView small_value = (TextView) view.findViewById(R.id.small_value);
        TextView temp = (TextView) view.findViewById(R.id.temp);

        hour.setText(stringsProvider.getHour(data.date));

        image.setImageResource(imageMapper.getImageResourceId(data.weather[0].image));

        //TODO: this should be diffrent related to weather (wind, snow, rain)
        small_label.setText("Wind");
        small_value.setText(stringsProvider.getWindString(data.wind.speed, data.wind.direction));

        temp.setText(stringsProvider.getTempString(data.measurements.temp));

        return view;

    }


}
