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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.util.ImageMapper;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import eu.rafalolszewski.simplyweather.views.adapters.FiveDaysWeatherListAdapter;

/**
 * Created by rafal on 04.03.16.
 */
public class WeatherBodyFragment extends Fragment implements WeatherFragmentInterface {

    private static final String TAG = "WeatherBodyFragment";

    @Bind(R.id.container_current_weather)
    View currentWeatherContainer;

    @Bind(R.id.listview)
    ListView listView;

    @Bind(R.id.cityname)
    TextView cityName;

    @Bind(R.id.weather_image)
    ImageView currentWeatherImage;

    @Bind(R.id.current_temp)
    TextView currentTemp;

    @Bind(R.id.current_pressure)
    TextView currentPressure;

    @Bind(R.id.current_wind)
    TextView currentWind;

    @Bind(R.id.current_progress)
    ProgressBar currentProgress;

    @Bind(R.id.list_progress)
    ProgressBar listProgress;

    @Inject
    MainPresenter mainPresenter;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    ImageMapper imageMapper;

    @Inject
    StringsProvider stringsProvider;

    @Inject
    FiveDaysWeatherListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_body, container, false);

        setupInjection(view);

        listView.setAdapter(listAdapter);

        if (listAdapter == null) Log.e(TAG, "onCreateView: listAdapter null!!!!!!!!!" );

        return view;
    }

    private void setupInjection(View view) {
        //ButterKnife
        ButterKnife.bind(this, view);
    }

    @Override
    public void setCurrentWeatherProgressIndicator(boolean active) {
        setVisibility(active, currentProgress);
    }

    @Override
    public void setListProgressIndicator(boolean active) {
        setVisibility(active, listProgress);
    }

    private void setVisibility(boolean active, View view) {
        int visibility;
        if (active){
            visibility = View.VISIBLE;
        }else {
            visibility = View.GONE;
        }
        view.setVisibility(visibility);
    }

    @Override
    public void cantGetCurrentWeatherData() {
        Log.w(TAG, "cantGetFiveDaysWeatherData: ");
    }

    @Override
    public void cantGetFiveDaysWeatherData() {
        Log.w(TAG, "cantGetFiveDaysWeatherData: ");
    }

    @Override
    public void refreshCurrentWeather(WeatherCurrentData weatherCurrentData) {
        Log.d(TAG, "refreshCurrentWeather: " + weatherCurrentData.toString());

        cityName.setText(weatherCurrentData.cityName);
        currentTemp.setText(stringsProvider.getTempString(weatherCurrentData.measurements.temp));
        currentPressure.setText(stringsProvider.getPressureString(weatherCurrentData.measurements.pressure));
        currentWind.setText(stringsProvider.getWindString(
                weatherCurrentData.wind.speed,
                weatherCurrentData.wind.direction ));
        currentWeatherImage.setImageResource(imageMapper.getImageResourceId(weatherCurrentData.weather[0].image));
    }

    @Override
    public void refreshFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        if (listView.getAdapter() == null) listView.setAdapter(listAdapter);
        Log.d(TAG, "refreshFiveDaysWeather: " + weatherFiveDaysData.toString());

        listAdapter.setWeatherData(weatherFiveDaysData);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void setInfoToCurrentWeatherContainer(String info) {
        //TODO
    }

}
