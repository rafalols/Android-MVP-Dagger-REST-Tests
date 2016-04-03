package eu.rafalolszewski.simplyweather.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;

import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherViewInterface;

/**
 * Created by rafal on 17.03.16.
 */
public class MainPresenter implements MainPresenterInterface{

    private static final String TAG = "MainPresenter";
    private static final int NUMBER_OF_RETRY_CONNECTIONS = 5;

    private static final String LASTPLACE_LAT = "lastplaceLat";
    private static final String LASTPLACE_LON = "lastplaceLon";

    private static final String JSON_CURRENTWEATHER = "currentweatherJson";
    private static final String JSON_FIVEDAYSWEATHER = "fivedaysweatherJson";

    private WeatherViewInterface viewInterace;

    private MainActivity mainActivity;

    private GoogleApiClient googleApiClient;

    private OpenWeatherApi openWeatherApi;

    private CurrentLocationProvider currentLocationProvider;

    private SharedPreferences sharedPreferences;

    private Place lastPlace;
    private int currentWeatherRetry = 0;
    private int fivedaystWeatherRetry = 0;

    public MainPresenter(Activity activity, GoogleApiClient googleApiClient, OpenWeatherApi openWeatherApi,
                         CurrentLocationProvider currentLocationProvider, SharedPreferences sharedPreferences) {
        this.mainActivity = (MainActivity) activity;
        this.googleApiClient = googleApiClient;
        this.openWeatherApi = openWeatherApi;
        this.sharedPreferences = sharedPreferences;
        openWeatherApi.setCallback(this);
        this.currentLocationProvider = currentLocationProvider;
    }

    @Override
    public void onClickWeatherListItem(int id) {
        //TODO: add this functionality
    }

    @Override
    public void getCurrentPositionWeather() {
        double[] latAndLong = currentLocationProvider.getCurrentLatLong();
        Log.d(TAG, "getCurrentPositionWeather: lat = " + latAndLong[0] + ", lon = " + latAndLong[1]);
        openWeatherApi.getCurrentWeather(latAndLong[0], latAndLong[1]);
        openWeatherApi.getFiveDaysWeather(latAndLong[0], latAndLong[1]);
    }

    @Override
    public void onClickFavorites() {
        //TODO: add this functionality
    }

    @Override
    public void onClickHistory() {
        //TODO: add this functionality
    }

    @Override
    public void connectGoogleApi() {
        Log.d(TAG, "connectGoogleApi");
        googleApiClient.connect();
    }

    @Override
    public void disconnectGoogleApi() {
        Log.d(TAG, "disconnectGoogleApi");
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mainActivity.onGoogleApiConnectionFail();
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.d(TAG, "onPlaceSelected: place selected: city = " + place.getName()
                + ", lat = " + place.getLatLng().latitude
                + ", lon = " + place.getLatLng().longitude);
        saveLastPlace(place);
        viewInterace.setCurrentWeatherProgressIndicator(true);
        openWeatherApi.getCurrentWeather(place.getLatLng().latitude, place.getLatLng().longitude);
        viewInterace.setListProgressIndicator(true);
        openWeatherApi.getFiveDaysWeather(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    private void saveLastPlace(Place place) {
        lastPlace = place;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(LASTPLACE_LAT, (float) place.getLatLng().latitude);
        editor.putFloat(LASTPLACE_LON, (float) place.getLatLng().longitude);
        editor.apply();
    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: can't get google places. Status: " + status.getStatusMessage());
        mainActivity.onCantGetGooglePlace();
    }


    @Override
    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData) {
        if (weatherCurrentData == null){
            Log.e(TAG, "onGetCurrentWeather: Get null current weather data!");
            onFailureCurrentDataViewCallbacks();
            return;
        }
        Log.d(TAG, "onGetCurrentWeather: current weather data recived for city = " + weatherCurrentData.cityName);
        saveWeatherToSharedPreferences(weatherCurrentData, JSON_CURRENTWEATHER);
        viewInterace.setCurrentWeatherProgressIndicator(false);
        viewInterace.refreshCurrentWeather(weatherCurrentData);
    }

    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        if (weatherFiveDaysData == null) {
            Log.e(TAG, "onGetCurrentWeather: Get null five days weather data!");
            onFailureFiveDaysWeatherViewCallbacks();
            return;
        }
        Log.d(TAG, "onGetFiveDaysWeather: 5 days weather data recived " + weatherFiveDaysData);
        saveWeatherToSharedPreferences(weatherFiveDaysData, JSON_FIVEDAYSWEATHER);
        viewInterace.setListProgressIndicator(false);
        viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);
    }

    @Override
    public void onGetCurrentWeatherFailure(Throwable t) {
        Log.e(TAG, "onGetCurrentWeatherFailure: can't get current weather. Error: " + t.getMessage());
        onFailureCurrentDataViewCallbacks();
    }

    private void saveWeatherToSharedPreferences(Object object, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    private void onFailureCurrentDataViewCallbacks() {
        if (!retryCurrentWeather()){
            //If getting weather failed NUMBER_OF_RETRY_CONNECTIONS times
            viewInterace.setCurrentWeatherProgressIndicator(false);
            viewInterace.cantGetCurrentWeatherData();
        }
    }

    private boolean retryCurrentWeather() {
        if (currentWeatherRetry < NUMBER_OF_RETRY_CONNECTIONS){
            currentWeatherRetry++;
            Log.i(TAG, "retryCurrentWeather: retry current weather. " + currentWeatherRetry + " time");
            openWeatherApi.getCurrentWeather(lastPlace.getLatLng().latitude, lastPlace.getLatLng().longitude);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onGetFiveDaysWeatherFailure(Throwable t) {
        Log.e(TAG, "onGetCurrentWeatherFailure: can't get five days weather. Error: " + t.getMessage());
        onFailureFiveDaysWeatherViewCallbacks();
    }

    private void onFailureFiveDaysWeatherViewCallbacks() {
        if (!retryFivedaysWeather()){
            //If getting weather failed NUMBER_OF_RETRY_CONNECTIONS times
            viewInterace.setListProgressIndicator(false);
            viewInterace.cantGetFiveDaysWeatherData();
        }
    }

    private boolean retryFivedaysWeather() {
        if (fivedaystWeatherRetry < NUMBER_OF_RETRY_CONNECTIONS){
            fivedaystWeatherRetry++;
            Log.i(TAG, "retryFivedaysWeather: retry five days weather. " + fivedaystWeatherRetry + " time");
            openWeatherApi.getFiveDaysWeather(lastPlace.getLatLng().latitude, lastPlace.getLatLng().longitude);
            return true;
        }else {
            return false;
        }
    }

    public void setViewInterace(WeatherViewInterface viewInterace) {
        this.viewInterace = viewInterace;
    }

    public WeatherViewInterface getViewInterace() {
        return viewInterace;
    }
}
