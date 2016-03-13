package eu.rafalolszewski.simplyweather.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import eu.rafalolszewski.simplyweather.api.callback.WeatherApiCallback;
import eu.rafalolszewski.simplyweather.model.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.util.StringFormatter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rafal on 12.03.16.
 */
public class OpenWeatherApi {

    private static final String KEY_OF_METADATA_OPENWEATHER_KEY = "openweather.API_KEY";

    private String key;
    private OpenWeatherService service;
    private WeatherApiCallback callback;

    public OpenWeatherApi(OpenWeatherService service, Application application){
        this.service  = service;
        key = getKeyFromMetaData(application);
    }

    private String getKeyFromMetaData(Application application) {
        try{
            ApplicationInfo appInfo = application.getPackageManager().getApplicationInfo(
                    application.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(KEY_OF_METADATA_OPENWEATHER_KEY);
            }else {
                return null;
            }
        }catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public void getCurrentWeather(double lat, double lon) {
            Call<WeatherCurrentData> call = service.getCurrentWeather(
                    StringFormatter.latOrLongToString(lat),
                    StringFormatter.latOrLongToString(lon), key);
            call.enqueue(new CallbackCurrentWeather());
    }

    public void getFiveDaysWeather(double lat, double lon){
        Call<WeatherFiveDaysData> call = service.getWeatherForFiveDays(
                StringFormatter.latOrLongToString(lat),
                StringFormatter.latOrLongToString(lon), key);
        call.enqueue(new CallbackFiveDaysWeather());
    }

    private class CallbackCurrentWeather implements Callback<WeatherCurrentData> {
        @Override
        public void onResponse(Response<WeatherCurrentData> response, Retrofit retrofit) {
            WeatherCurrentData weatherCurrentData = response.body();
            callback.onGetCurrentWeather(weatherCurrentData);
        }
        @Override
        public void onFailure(Throwable t) {
            callback.onGetWeatherFailure(t);
        }
    }

    private class CallbackFiveDaysWeather implements Callback<WeatherFiveDaysData>{
        @Override
        public void onResponse(Response<WeatherFiveDaysData> response, Retrofit retrofit) {
            WeatherFiveDaysData weatherFiveDaysData = response.body();
            callback.onGetFiveDaysWeather(weatherFiveDaysData);
        }
        @Override
        public void onFailure(Throwable t) {
            callback.onGetWeatherFailure(t);
        }
    }

    public void setCallback(WeatherApiCallback callback) {
        this.callback = callback;
    }

}
