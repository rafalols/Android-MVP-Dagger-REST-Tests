package eu.rafalolszewski.simplyweather.presenter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

import eu.rafalolszewski.simplyweather.model.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.WeatherFiveDaysData;

/**
 * Created by rafal on 17.03.16.
 */
public class MainPresenter implements MainPresenterInterface{



    @Override
    public void onClickWeatherListItem(int id) {

    }

    @Override
    public void onClickCurrentGpsPosition() {

    }

    @Override
    public void onClickFavorites() {

    }

    @Override
    public void onClickHistory() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }

    @Override
    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData) {

    }

    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {

    }

    @Override
    public void onGetWeatherFailure(Throwable t) {

    }
}
