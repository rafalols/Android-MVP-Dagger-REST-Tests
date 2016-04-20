package eu.rafalolszewski.simplyweather.presenter;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import eu.rafalolszewski.simplyweather.api.callback.WeatherApiCallback;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherFragmentInterface;

/**
 * Created by rafal on 17.03.16.
 */
public interface MainPresenterInterface extends GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener, WeatherApiCallback {

    public void getCurrentPositionWeather();

    public void connectGoogleApi();

    public void disconnectGoogleApi();

    public void onCreate(Bundle savedInstanceState);

    public void onSaveInstanceState(Bundle outState);

    public void onDestroy();

    public void setViewInterface(WeatherFragmentInterface viewInterace);

}
