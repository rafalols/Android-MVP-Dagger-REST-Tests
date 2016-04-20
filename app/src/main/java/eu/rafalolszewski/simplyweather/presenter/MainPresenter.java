package eu.rafalolszewski.simplyweather.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

import org.parceler.Parcels;

import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.model.PlaceCords;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;
import eu.rafalolszewski.simplyweather.util.SharedPreferencesManager;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherFragmentInterface;

/**
 * Created by rafal on 17.03.16.
 */
public class MainPresenter implements MainPresenterInterface{

    private static final String TAG = "MainPresenter";
    private static final String SAVEDSTATE_CURRENT_WEATHER = "savedstateCurrentWeather";
    private static final String SAVEDSTATE_FIVEDAYS_WEATHER = "savedstateFiveDaysWeather";

    public static final int NUMBER_OF_RETRY_CONNECTIONS = 5;

    private WeatherCurrentData weatherCurrentData;
    private WeatherFiveDaysData weatherFiveDaysData;

    private WeatherFragmentInterface viewInterace;

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
    public void getCurrentPositionWeather() {
        PlaceCords placeCords = currentLocationProvider.getCurrentLatLong();
        if (placeCords != null){
            callApiForWeatherData(placeCords);
        }else {
            mainActivity.cantGetCurrentPosition();
        }
    }

    protected void callApiForWeatherData(PlaceCords placeCords) {
        openWeatherApi.getCurrentWeather(placeCords.lat, placeCords.lon);
        openWeatherApi.getFiveDaysWeather(placeCords.lat, placeCords.lon);
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
    public void onSaveInstanceState(Bundle outState) {
        if (weatherCurrentData != null)
            outState.putParcelable(SAVEDSTATE_CURRENT_WEATHER, Parcels.wrap(weatherCurrentData));
        if (weatherFiveDaysData != null)
            outState.putParcelable(SAVEDSTATE_FIVEDAYS_WEATHER, Parcels.wrap(weatherFiveDaysData));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (checkInSavedState(savedInstanceState)) return;

        checkAndSetLastResultFromSharedPreference();

        refreshLastSearch();
    }


    private boolean checkInSavedState(Bundle savedInstanceState) {
        Log.d(TAG, "checkInSavedState: ");
        if (savedInstanceState != null){
            weatherCurrentData = Parcels.unwrap(savedInstanceState.getParcelable(SAVEDSTATE_CURRENT_WEATHER));
            weatherFiveDaysData = Parcels.unwrap(savedInstanceState.getParcelable(SAVEDSTATE_FIVEDAYS_WEATHER));
            if (weatherCurrentData != null && weatherFiveDaysData != null){
                viewInterace.refreshCurrentWeather(weatherCurrentData);
                viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);
                return true;
            }
        }
        return false;
    }

    private void checkAndSetLastResultFromSharedPreference() {
        Log.d(TAG, "checkAndSetLastResultFromSharedPreference: ");
        WeatherCurrentData weatherCurrentData = preferencesManager.loadObjectFromJson
                (SharedPreferencesManager.JSON_CURRENTWEATHER, WeatherCurrentData.class);
        WeatherFiveDaysData weatherFiveDaysData = preferencesManager.loadObjectFromJson
                (SharedPreferencesManager.JSON_FIVEDAYSWEATHER, WeatherFiveDaysData.class);

        if (weatherCurrentData != null && weatherFiveDaysData != null) {
            viewInterace.refreshCurrentWeather(weatherCurrentData);
            viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);
        }
    }

    private void refreshLastSearch() {
        Log.d(TAG, "refreshLastSearch: ");
        if (preferencesManager.getBoolean(SharedPreferencesManager.LAST_SEARCH_WAS_CURRENT_PLACE)){
            //Last search was current place
            getCurrentPositionWeather();
        }else{
            //Last search was city from google searcher
            PlaceCords placeCords = preferencesManager.getLastPlaceCords();
            if (placeCords != null) callApiForWeatherData(placeCords);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (weatherCurrentData != null && weatherFiveDaysData != null) {
            preferencesManager.saveObjectAsJson(weatherCurrentData, SharedPreferencesManager.JSON_CURRENTWEATHER);
            preferencesManager.saveObjectAsJson(weatherFiveDaysData, SharedPreferencesManager.JSON_FIVEDAYSWEATHER);
        }
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
        if (weatherCurrentData == null || !weatherCurrentData.isValid()){
            onFailureCurrentDataViewCallbacks();
            return;
        }
//        saveWeatherToSharedPreferences(weatherCurrentData, JSON_CURRENTWEATHER);
        viewInterace.setCurrentWeatherProgressIndicator(false);
        viewInterace.refreshCurrentWeather(weatherCurrentData);
        this.weatherCurrentData = weatherCurrentData;
    }


    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        if (weatherFiveDaysData == null || !weatherFiveDaysData.isValid()) {
            onFailureFiveDaysWeatherViewCallbacks();
            return;
        }
//        saveWeatherToSharedPreferences(weatherFiveDaysData, JSON_FIVEDAYSWEATHER);
        viewInterace.setListProgressIndicator(false);
        viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);
        this.weatherFiveDaysData = weatherFiveDaysData;
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

    @Override
    public void setViewInterface(WeatherFragmentInterface viewInterace) {
        this.viewInterace = viewInterace;
    }

    public WeatherFragmentInterface getViewInterace() {
        return viewInterace;
    }

    public void setCurrentWeatherRetry(int currentWeatherRetry) {
        this.currentWeatherRetry = currentWeatherRetry;
    }

    public void setFivedaystWeatherRetry(int fivedaystWeatherRetry) {
        this.fivedaystWeatherRetry = fivedaystWeatherRetry;
    }
}
