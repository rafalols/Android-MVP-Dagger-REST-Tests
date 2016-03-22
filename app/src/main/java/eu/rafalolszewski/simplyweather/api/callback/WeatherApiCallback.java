package eu.rafalolszewski.simplyweather.api.callback;

import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;

/**
 * Created by rafal on 13.03.16.
 */
public interface WeatherApiCallback {

    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData);

    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData);

    public void onGetWeatherFailure(Throwable t);
}
