package eu.rafalolszewski.simplyweather.views.fragments;

import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;

/**
 * Created by Rafał Olszewski on 17.03.16.
 */
public interface WeatherFragmentInterface {

    public void setCurrentWeatherProgressIndicator(boolean active);

    public void setListProgressIndicator(boolean active);

    public void cantGetCurrentWeatherData();

    public void cantGetFiveDaysWeatherData();

    public void refreshCurrentWeather(WeatherCurrentData weatherCurrentData);

    public void refreshFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData);

    public void setInfoToCurrentWeatherContainer(String info);

}
