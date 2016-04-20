package eu.rafalolszewski.simplyweather.presenter;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.api.TimeZoneApi;
import eu.rafalolszewski.simplyweather.model.PlaceCords;
import eu.rafalolszewski.simplyweather.model.TimeZoneApiData;
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

    private TimeZoneApi timeZoneApi;

    private CurrentLocationProvider currentLocationProvider;

    private SharedPreferencesManager preferencesManager;



    private int currentWeatherRetry = 0;
    private int fivedaystWeatherRetry = 0;

    public MainPresenter(Activity activity, GoogleApiClient googleApiClient, OpenWeatherApi openWeatherApi, TimeZoneApi timeZoneApi,
                         CurrentLocationProvider currentLocationProvider, SharedPreferencesManager preferencesManager) {
        this.mainActivity = (MainActivity) activity;
        this.googleApiClient = googleApiClient;
        this.openWeatherApi = openWeatherApi;
        this.timeZoneApi = timeZoneApi;
        this.preferencesManager = preferencesManager;
        openWeatherApi.setCallback(this);
        timeZoneApi.setCallback(this);
        this.currentLocationProvider = currentLocationProvider;
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
    public void connectGoogleApi() {
        googleApiClient.connect();
    }

    @Override
    public void disconnectGoogleApi() {
        googleApiClient.disconnect();
    }

    @Override
    public void refreshStateFromLastRun() {
        //Check and set last results:
        checkAndSetLastResult();
        //Refresh last search
        refreshLastSearch();
    }

    private void checkAndSetLastResult() {
        WeatherCurrentData weatherCurrentData = preferencesManager.loadObjectFromJson
                (SharedPreferencesManager.JSON_CURRENTWEATHER, WeatherCurrentData.class);
        WeatherFiveDaysData weatherFiveDaysData = preferencesManager.loadObjectFromJson
                (SharedPreferencesManager.JSON_FIVEDAYSWEATHER, WeatherFiveDaysData.class);
        if (weatherCurrentData != null) {
            viewInterace.refreshCurrentWeather(weatherCurrentData);
        }else {
            viewInterace.setInfoToCurrentWeatherContainer(mainActivity.getString(R.string.choose_city_or_current_position));
        }
        if (weatherFiveDaysData != null) viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);

    }

    private void refreshLastSearch() {
        if (preferencesManager.getBoolean(SharedPreferencesManager.LAST_SEARCH_WAS_CURRENT_PLACE)){
            //Last search was current place
            getCurrentPositionWeather();
        }else{
            //Last search was city from google searcher
            PlaceCords placeCords = preferencesManager.getLastPlaceCords();
            openWeatherApi.getCurrentWeather(placeCords.lat, placeCords.lon);
            openWeatherApi.getFiveDaysWeather(placeCords.lat, placeCords.lon);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mainActivity.onGoogleApiConnectionFail();
    }

    @Override
    public void onPlaceSelected(Place place) {
        saveLastPlace(place);
        if (!checkTimeZoneOffsetOfPlace(place)){
            checkForPlaceTimeZoneOffset();
        }
        callApiForWeatherData(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    private void checkForPlaceTimeZoneOffset() {

    }

    private boolean checkTimeZoneOffsetOfPlace(Place place) {
        if (getPlaceTimeZoneOffset(place) == SharedPreferencesManager.DEFAULT_WRONG_PLACE_TIMEZONE){
            return false;
        }else{
            return true;
        }
    }

    private int getPlaceTimeZoneOffset(Place place) {
        return preferencesManager.getPlaceTimeZoneOffset(place.getId());
    }

    private void saveTimeZoneOfPlace(Place place, TimeZoneApiData data) {
        int offset = data.daylightSavingsTime + data.timeZoneTime;
        preferencesManager.saveTimeZoneOffset(place.getId(), offset);
    }

    public void callApiForWeatherData(double lat, double lon) {
        viewInterace.setCurrentWeatherProgressIndicator(true);
        openWeatherApi.getCurrentWeather(lat, lon);
        viewInterace.setListProgressIndicator(true);
        openWeatherApi.getFiveDaysWeather(lat, lon);
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

    @Override
    public void onGetTimeZoneData(TimeZoneApiData timeZoneApiData) {

    }

    @Override
    public void onGetTimeZoneDataFailure(Throwable t) {

    }


    //Getters and Setters:

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
