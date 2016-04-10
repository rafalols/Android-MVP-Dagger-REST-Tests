package eu.rafalolszewski.simplyweather.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;

import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;
import eu.rafalolszewski.simplyweather.util.SharedPreferencesManager;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherViewInterface;

/**
 * Created by rafal on 17.03.16.
 */
public class MainPresenter implements MainPresenterInterface{

    private static final String TAG = "MainPresenter";
    public static final int NUMBER_OF_RETRY_CONNECTIONS = 5;


    private WeatherViewInterface viewInterace;

    private MainActivity mainActivity;

    private GoogleApiClient googleApiClient;

    private OpenWeatherApi openWeatherApi;

    private CurrentLocationProvider currentLocationProvider;

    private SharedPreferencesManager preferencesManager;



    private int currentWeatherRetry = 0;
    private int fivedaystWeatherRetry = 0;

    public MainPresenter(Activity activity, GoogleApiClient googleApiClient, OpenWeatherApi openWeatherApi,
                         CurrentLocationProvider currentLocationProvider, SharedPreferencesManager preferencesManager) {
        this.mainActivity = (MainActivity) activity;
        this.googleApiClient = googleApiClient;
        this.openWeatherApi = openWeatherApi;
        this.preferencesManager = preferencesManager;
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
        if (latAndLong != null){
            openWeatherApi.getCurrentWeather(latAndLong[0], latAndLong[1]);
            openWeatherApi.getFiveDaysWeather(latAndLong[0], latAndLong[1]);
        }else {
            mainActivity.cantGetCurrentPosition();
        }
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
        googleApiClient.connect();
    }

    @Override
    public void disconnectGoogleApi() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mainActivity.onGoogleApiConnectionFail();
    }

    @Override
    public void onPlaceSelected(Place place) {
        saveLastPlace(place);
        viewInterace.setCurrentWeatherProgressIndicator(true);
        openWeatherApi.getCurrentWeather(place.getLatLng().latitude, place.getLatLng().longitude);
        viewInterace.setListProgressIndicator(true);
        openWeatherApi.getFiveDaysWeather(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    private void saveLastPlace(Place place) {
        preferencesManager.saveLastSearchedPlace(place);
    }

    @Override
    public void onError(Status status) {
        mainActivity.onCantGetGooglePlace();
    }


    @Override
    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData) {
        if (weatherCurrentData == null){
            onFailureCurrentDataViewCallbacks();
            return;
        }
//        saveWeatherToSharedPreferences(weatherCurrentData, JSON_CURRENTWEATHER);
        viewInterace.setCurrentWeatherProgressIndicator(false);
        viewInterace.refreshCurrentWeather(weatherCurrentData);
    }

    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        if (weatherFiveDaysData == null) {
            onFailureFiveDaysWeatherViewCallbacks();
            return;
        }
//        saveWeatherToSharedPreferences(weatherFiveDaysData, JSON_FIVEDAYSWEATHER);
        viewInterace.setListProgressIndicator(false);
        viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);
    }

    @Override
    public void onGetCurrentWeatherFailure(Throwable t) {
        onFailureCurrentDataViewCallbacks();
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
//            openWeatherApi.getCurrentWeather(lastPlace.getLatLng().latitude, lastPlace.getLatLng().longitude);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onGetFiveDaysWeatherFailure(Throwable t) {
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
//            openWeatherApi.getFiveDaysWeather(lastPlace.getLatLng().latitude, lastPlace.getLatLng().longitude);
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

    public void setCurrentWeatherRetry(int currentWeatherRetry) {
        this.currentWeatherRetry = currentWeatherRetry;
    }

    public void setFivedaystWeatherRetry(int fivedaystWeatherRetry) {
        this.fivedaystWeatherRetry = fivedaystWeatherRetry;
    }
}
