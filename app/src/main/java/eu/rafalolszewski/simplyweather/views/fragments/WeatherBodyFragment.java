package eu.rafalolszewski.simplyweather.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.model.City;
import eu.rafalolszewski.simplyweather.util.StringFormatter;

/**
 * Created by rafal on 04.03.16.
 */
public class WeatherBodyFragment extends Fragment {

    @Bind(R.id.container_current_weather)
    RelativeLayout currentWeatherContainer;

    @Bind(R.id.listview)
    ListView listView;

    @Bind(R.id.weather_image)
    ImageView currentWeatherImage;

    @Bind(R.id.current_temp)
    TextView currentTemp;

    @Bind(R.id.current_from_to)
    TextView curentTempFromTo;

    @Bind(R.id.current_wind)
    TextView currentWind;

    public WeatherBodyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_body, container, false);
        ButterKnife.bind(this, view);
        return view;
    }





}
