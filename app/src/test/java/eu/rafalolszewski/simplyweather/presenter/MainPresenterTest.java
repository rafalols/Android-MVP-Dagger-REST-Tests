package eu.rafalolszewski.simplyweather.presenter;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;
import eu.rafalolszewski.simplyweather.util.SharedPreferencesManager;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherViewInterface;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Rafał Olszewski on 19.03.16.
 */
public class MainPresenterTest {

    @Mock
    private Place place;

    @Mock
    private WeatherViewInterface weatherView;

    @Mock
    private MainActivity mainActivity;

    @Mock
    GoogleApiClient googleApiClient;

    @Mock
    OpenWeatherApi openWeatherApi;

    @Mock
    CurrentLocationProvider currentLocationProvider;

    @Mock
    SharedPreferencesManager sharedPreferencesManager;


    private MainPresenter mainPresenter;

    @Before
    public void setupPresenter(){
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(Log.class);

        mainPresenter = new MainPresenter(mainActivity, googleApiClient, openWeatherApi, currentLocationProvider, sharedPreferencesManager);
        mainPresenter.setViewInterace(weatherView);

        mockTestPlace();
    }

    private void mockTestPlace() {
        when(place.getName()).thenReturn("TestCity");
        LatLng latLon = new LatLng(10d, 20d);
        when(place.getLatLng()).thenReturn(latLon);
        when(place.getLatLng()).thenReturn(latLon);
    }

    @Test
    public void onClickWeatherListItemTest(){
        //TODO: Handle this action
    }

    @Test
    public void getCurrentPositionWeatherTest(){
        //Check weather for current position
        when(currentLocationProvider.getCurrentLatLong()).thenReturn(new double[]{5d, 6d});

        mainPresenter.getCurrentPositionWeather();
        verify(currentLocationProvider).getCurrentLatLong();

        verify(openWeatherApi).getCurrentWeather(5d, 6d);
        verify(openWeatherApi).getFiveDaysWeather(5d, 6d);
    }

    @Test
    public void onClickFavoritesTest(){
        //TODO: Handle this action
    }

    @Test
    public void onClickHistoryTest(){
        //TODO: Handle this action
    }

    @Test
    public void onConnectionFailedTest(){
        //Connection to Google Places Api failed
        mainPresenter.onConnectionFailed(new ConnectionResult(0));
        verify(mainActivity).onGoogleApiConnectionFail();
    }

    @Test
    public void onPlaceSelectedTest(){
        mainPresenter.onPlaceSelected(place);

        verify(weatherView).setCurrentWeatherProgressIndicator(true);
        verify(weatherView).setListProgressIndicator(true);

        verify(openWeatherApi).getCurrentWeather(place.getLatLng().latitude, place.getLatLng().longitude);
        verify(openWeatherApi).getFiveDaysWeather(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    @Test
    public void onError(){
        //Error when getting place from google api
        mainPresenter.onError(new Status(0));
        verify(mainActivity).onCantGetGooglePlace();
    }

    @Test
    public void onGetCurrentWeatherTest(){
        WeatherCurrentData weather = new WeatherCurrentData();
        mainPresenter.onGetCurrentWeather(weather);

        verify(weatherView).setCurrentWeatherProgressIndicator(false);

        verify(weatherView).refreshCurrentWeather(weather);
    }

    @Test
    public void onGetFiveDaysWeatherTest(){
        WeatherFiveDaysData weather = new WeatherFiveDaysData();
        mainPresenter.onGetFiveDaysWeather(weather);

        verify(weatherView).setListProgressIndicator(false);

        verify(weatherView).refreshFiveDaysWeather(weather);
    }

    @Test
    public void onGetCurrentWeatherFailureTest(){
        //To avoid reconnection:
        mainPresenter.setCurrentWeatherRetry(MainPresenter.NUMBER_OF_RETRY_CONNECTIONS);
        //Tested method:
        mainPresenter.onGetCurrentWeatherFailure(new Throwable());

        verify(weatherView).setCurrentWeatherProgressIndicator(false);
        verify(weatherView).cantGetCurrentWeatherData();
    }

    @Test
    public void onGetFivedaysWeatherFailureTest(){
        //To avoid reconnection:
        mainPresenter.setFivedaystWeatherRetry(MainPresenter.NUMBER_OF_RETRY_CONNECTIONS);
        //Tested method:
        mainPresenter.onGetFiveDaysWeatherFailure(new Throwable());

        verify(weatherView).setListProgressIndicator(false);
        verify(weatherView).cantGetFiveDaysWeatherData();
    }

    @Test
    public void connectGoogleApiTest(){
        mainPresenter.connectGoogleApi();
        verify(googleApiClient).connect();
    }

    @Test
    public void disconnectGoogleApiTest(){
        mainPresenter.disconnectGoogleApi();
        verify(googleApiClient).disconnect();
    }


}
