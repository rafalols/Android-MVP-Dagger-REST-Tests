package eu.rafalolszewski.simplyweather.api;

import eu.rafalolszewski.simplyweather.model.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.WeatherFiveDaysData;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by rafal on 12.03.16.
 */
public interface OpenWeatherService {

    @GET("/data/2.5/weather")
    Call<WeatherCurrentData> getCurrentWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String key);

    @GET("/data/2.5/forecast")
    Call<WeatherFiveDaysData> getWeatherForFiveDays(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String key);

}
