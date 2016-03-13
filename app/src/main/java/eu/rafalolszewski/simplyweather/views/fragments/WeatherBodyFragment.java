package eu.rafalolszewski.simplyweather.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Bind(R.id.city_name) TextView cityNameView;
    @Bind(R.id.city_lat) TextView cityLatView;
    @Bind(R.id.city_long) TextView cityLongView;

    public WeatherBodyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_body, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setCity(City city){
        cityNameView.setText(city.getCityName());
        cityLatView.setText(StringFormatter.latOrLongToString(city.getLat()));
        cityLongView.setText(StringFormatter.latOrLongToString(city.getLon()));
    }

}
