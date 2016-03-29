package eu.rafalolszewski.simplyweather.presenter;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

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
    private WeatherViewInterface viewInterace;

    private MainActivity mainActivity;

    private GoogleApiClient googleApiClient;

    private OpenWeatherApi openWeatherApi;

    private CurrentLocationProvider currentLocationProvider;

    public MainPresenter(Activity activity, GoogleApiClient googleApiClient, OpenWeatherApi openWeatherApi, CurrentLocationProvider currentLocationProvider) {
        this.mainActivity = (MainActivity) activity;
        this.googleApiClient = googleApiClient;
        this.openWeatherApi = openWeatherApi;
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
        viewInterace.setCurrentWeatherProgressIndicator(true);
        openWeatherApi.getCurrentWeather(place.getLatLng().latitude, place.getLatLng().longitude);
        viewInterace.setListProgressIndicator(true);
        openWeatherApi.getFiveDaysWeather(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: can't get google places. Status: " + status.getStatusMessage());
        mainActivity.onCantGetGooglePlace();
    }


    @Override
    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData) {
        Log.d(TAG, "onGetCurrentWeather: current weather data recived for city = " + weatherCurrentData.cityName);
        viewInterace.setCurrentWeatherProgressIndicator(false);
        viewInterace.refreshCurrentWeather(weatherCurrentData);
    }

    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        Log.d(TAG, "onGetFiveDaysWeather: 5 days weather data recived " + weatherFiveDaysData);
        viewInterace.setListProgressIndicator(false);
        if (weatherFiveDaysData != null) viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);
    }

    @Override
    public void onGetWeatherFailure(Throwable t) {
        Log.e(TAG, "onGetWeatherFailure: can't get weather. Error: " + t.getMessage());
        viewInterace.setCurrentWeatherProgressIndicator(false);
        viewInterace.setListProgressIndicator(false);
        viewInterace.cantConnectWeatherApi();
    }

    public void setViewInterace(WeatherViewInterface viewInterace) {
        this.viewInterace = viewInterace;
    }

    public WeatherViewInterface getViewInterace() {
        return viewInterace;
    }
}
