package eu.rafalolszewski.simplyweather.api.callback;

import eu.rafalolszewski.simplyweather.model.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.WeatherFiveDaysData;

/**
 * Created by rafal on 13.03.16.
 */
public interface WeatherApiCallback {

    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData);

    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData);

    public void onGetWeatherFailure(Throwable t);
}
