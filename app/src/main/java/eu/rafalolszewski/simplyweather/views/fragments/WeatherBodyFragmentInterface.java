package eu.rafalolszewski.simplyweather.views.fragments;

import eu.rafalolszewski.simplyweather.model.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.WeatherFiveDaysData;

/**
 * Created by rafal on 17.03.16.
 */
public interface WeatherBodyFragmentInterface {

    public void setProgressIndicator(boolean active);

    public void showConnectionError();

    public void refreshCurrentWeather(WeatherCurrentData weatherCurrentData);

    public void refreshFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData);

}
