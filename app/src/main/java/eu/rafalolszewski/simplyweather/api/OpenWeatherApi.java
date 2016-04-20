package eu.rafalolszewski.simplyweather.api;

import android.app.Application;

import eu.rafalolszewski.simplyweather.api.callback.WeatherApiCallback;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.util.MetaDataProvider;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rafal on 12.03.16.
 */
public class OpenWeatherApi {

    public static final String OPENWEATHER_API_URL = "http://api.openweathermap.org/";
    private static final String OPENWEATHER_API_KEY_IN_MANIFEST = "openweather.API_KEY";
    private static final String TAG = "OpenWeatherApi";

    private final String openweather_key;
    private OpenWeatherService service;
    private WeatherApiCallback callback;

    public OpenWeatherApi(OpenWeatherService service, Application application){
        this.service  = service;
        openweather_key = MetaDataProvider.getMetaDataStringFromKey(application, OPENWEATHER_API_KEY_IN_MANIFEST);
    }

    public void getCurrentWeather(double lat, double lon) {
        Call<WeatherCurrentData> call = service.getCurrentWeather(
                StringsProvider.latOrLongToString(lat),
                StringsProvider.latOrLongToString(lon), openweather_key);
        call.enqueue(new CallbackCurrentWeather());
    }

    public void getCurrentWeather(int id) {
        Call<WeatherCurrentData> call = service.getCurrentWeather(id, openweather_key);
        call.enqueue(new CallbackCurrentWeather());
    }

    public void getFiveDaysWeather(double lat, double lon){
        Call<WeatherFiveDaysData> call = service.getWeatherForFiveDays(
                StringsProvider.latOrLongToString(lat),
                StringsProvider.latOrLongToString(lon), openweather_key);
        call.enqueue(new CallbackFiveDaysWeather());
    }

    public void getFiveDaysWeather(int id){
        Call<WeatherFiveDaysData> call = service.getWeatherForFiveDays(id, openweather_key);
        call.enqueue(new CallbackFiveDaysWeather());
    }


    private class CallbackCurrentWeather implements Callback<WeatherCurrentData> {
        @Override
        public void onResponse(Response<WeatherCurrentData> response, Retrofit retrofit) {
            if (!response.isSuccess()) {
                callback.onGetCurrentWeatherFailure(null);
            }else {
                WeatherCurrentData weatherCurrentData = response.body();
                callback.onGetCurrentWeather(weatherCurrentData);
            }
        }
        @Override
        public void onFailure(Throwable t) {
            callback.onGetCurrentWeatherFailure(t);
        }
    }

    private class CallbackFiveDaysWeather implements Callback<WeatherFiveDaysData>{
        @Override
        public void onResponse(Response<WeatherFiveDaysData> response, Retrofit retrofit) {
            if (!response.isSuccess()) {
                callback.onGetFiveDaysWeather(null);
            }else {
                WeatherFiveDaysData weatherFiveDaysData = response.body();
                callback.onGetFiveDaysWeather(weatherFiveDaysData);
            }
        }
        @Override
        public void onFailure(Throwable t) {
            callback.onGetFiveDaysWeatherFailure(t);
        }
    }

    public void setCallback(WeatherApiCallback callback) {
        this.callback = callback;
    }

}
