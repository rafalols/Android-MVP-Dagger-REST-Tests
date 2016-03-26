package eu.rafalolszewski.simplyweather.views.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.util.StringFormatter;
import eu.rafalolszewski.simplyweather.views.activities.SettingsActivity;

/**
 * Created by rafal on 04.03.16.
 */
public class WeatherBodyFragment extends Fragment implements WeatherViewInterface{

    private static final String TAG = "WeatherBodyFragment";
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

    @Inject
    MainPresenter mainPresenter;

    @Inject
    SharedPreferences sharedPreferences;

    public WeatherBodyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_body, container, false);

        setupInjection(view);

        return view;
    }

    private void setupInjection(View view) {
        //ButterKnife
        ButterKnife.bind(this, view);
    }


    @Override
    public void setCurrentWeatherProgressIndicator(boolean active) {
        Log.i(TAG, "setCurrentWeatherProgressIndicator: " + active);
    }

    @Override
    public void setListProgressIndicator(boolean active) {
        Log.i(TAG, "setListProgressIndicator: "  + active);
    }

    @Override
    public void cantConnectWeatherApi() {
        Log.i(TAG, "cantConnectWeatherApi: ");
    }

    @Override
    public void refreshCurrentWeather(WeatherCurrentData weatherCurrentData) {
        Log.d(TAG, "refreshCurrentWeather: " + weatherCurrentData.toString());
        // TEMP
        int tempUnit = getTempUnit();
        currentTemp.setText(StringFormatter.getTempString(weatherCurrentData.measurements.temp, tempUnit));
    }

    private int getTempUnit() {
        return sharedPreferences.getInt(SettingsActivity.TEMP_UNIT_KEY, SettingsActivity.TEMP_UNIT_DEFAULT);
    }

    @Override
    public void refreshFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        Log.d(TAG, "refreshFiveDaysWeather: " + weatherFiveDaysData.toString());
    }
}
