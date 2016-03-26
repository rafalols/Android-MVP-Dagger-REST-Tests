package eu.rafalolszewski.simplyweather.presenter;

import android.app.Activity;

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

    }

    @Override
    public void getCurrentPositionWeather() {
        double[] latAndLong = currentLocationProvider.getCurrentLatLong();
        openWeatherApi.getCurrentWeather(latAndLong[0], latAndLong[1]);
        openWeatherApi.getFiveDaysWeather(latAndLong[0], latAndLong[1]);
    }

    @Override
    public void onClickFavorites() {

    }

    @Override
    public void onClickHistory() {

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

        viewInterace.setCurrentWeatherProgressIndicator(true);
        openWeatherApi.getCurrentWeather(place.getLatLng().latitude, place.getLatLng().longitude);
        viewInterace.setListProgressIndicator(true);
        openWeatherApi.getFiveDaysWeather(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    @Override
    public void onError(Status status) {
        mainActivity.onCantGetGooglePlace();
    }



    @Override
    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData) {
        viewInterace.setCurrentWeatherProgressIndicator(false);
        viewInterace.refreshCurrentWeather(weatherCurrentData);
    }

    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        viewInterace.setListProgressIndicator(false);
        viewInterace.refreshFiveDaysWeather(weatherFiveDaysData);
    }

    @Override
    public void onGetWeatherFailure(Throwable t) {
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
