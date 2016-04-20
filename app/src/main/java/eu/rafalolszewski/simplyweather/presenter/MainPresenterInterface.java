package eu.rafalolszewski.simplyweather.presenter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import eu.rafalolszewski.simplyweather.api.callback.TimeZoneCallback;
import eu.rafalolszewski.simplyweather.api.callback.WeatherApiCallback;

/**
 * Created by rafal on 17.03.16.
 */
public interface MainPresenterInterface extends GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener, WeatherApiCallback, TimeZoneCallback {

    public void getCurrentPositionWeather();

    public void connectGoogleApi();

    public void disconnectGoogleApi();

    public void refreshStateFromLastRun();

}
