package eu.rafalolszewski.simplyweather.api;

import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by rafal on 12.03.16.
 */
public interface OpenWeatherService {

    /**
     * Get current weather by latitude and longitude
     * Used in selecting from google autocomplete searcher
     */
    @GET("/data/2.5/weather")
    Call<WeatherCurrentData> getCurrentWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String key);

    /**
     * Get 5 days weather by latitude and longitude
     * Used in selecting from google autocomplete searcher
     */
    @GET("/data/2.5/forecast")
    Call<WeatherFiveDaysData> getWeatherForFiveDays(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String key);


    /**
     * Get current weather by id
     * Used in favorites and history
     */
    @GET("/data/2.5/weather")
    Call<WeatherCurrentData> getCurrentWeather(@Query("id") int id, @Query("APPID") String key);

    /**
     * Get 5 days weather by id
     * Used in favorites and history
     */
    @GET("/data/2.5/forecast")
    Call<WeatherFiveDaysData> getWeatherForFiveDays(@Query("id") int id, @Query("APPID") String key);

}
