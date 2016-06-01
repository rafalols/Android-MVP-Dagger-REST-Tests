package eu.rafalolszewski.simplyweather.presenter;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;

import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.model.PlaceCords;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;
import eu.rafalolszewski.simplyweather.util.FileHelper;
import eu.rafalolszewski.simplyweather.util.SharedPreferencesManager;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherFragmentInterface;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Rafał Olszewski on 19.03.16.
 */
public class MainPresenterTest {

    @Mock
    private Place place;

    @Mock
    private WeatherFragmentInterface weatherView;

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

    WeatherCurrentData currentData;

    WeatherFiveDaysData fiveDaysData;

    @Before
    public void setupPresenter() throws Exception{
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(Log.class);

        mainPresenter = new MainPresenter(mainActivity, googleApiClient, openWeatherApi, currentLocationProvider, sharedPreferencesManager);
        mainPresenter.setViewInterface(weatherView);

        mockTestPlace();
        getTestData();
    }

    private void getTestData() throws Exception {
        currentData = getCurrentData();
        fiveDaysData = getFiveDaysData();
    }

    private WeatherFiveDaysData getFiveDaysData() throws IOException {
        String fiveDaysData = FileHelper.readJsonFile("src/main/assets/test_jsons/fivedaysweather.json");
        Gson gson =  new Gson();
        return gson.fromJson(fiveDaysData, WeatherFiveDaysData.class);
    }

    private WeatherCurrentData getCurrentData() throws Exception{
        String currentWeatherJson = FileHelper.readJsonFile("src/main/assets/test_jsons/currentweather.json");
        Gson gson =  new Gson();
        return gson.fromJson(currentWeatherJson, WeatherCurrentData.class);
    }

    private void mockTestPlace() {
        when(place.getName()).thenReturn("TestCity");
        LatLng latLon = new LatLng(10d, 20d);
        when(place.getLatLng()).thenReturn(latLon);
        when(place.getLatLng()).thenReturn(latLon);
    }

    @Test
    public void getCurrentPositionWeatherTest(){
        PlaceCords placeCords = new PlaceCords(5d, 6d);
        when(currentLocationProvider.getCurrentLatLong()).thenReturn(placeCords);

        mainPresenter.getCurrentPositionWeather();
        verify(currentLocationProvider).getCurrentLatLong();

        verify(openWeatherApi).getCurrentWeather(5d, 6d);
        verify(openWeatherApi).getFiveDaysWeather(5d, 6d);
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
        mainPresenter.onGetCurrentWeather(currentData);

        verify(weatherView).setCurrentWeatherProgressIndicator(false);

        verify(weatherView).refreshCurrentWeather(currentData);
    }

    @Test
    public void onGetFiveDaysWeatherTest(){
        mainPresenter.onGetFiveDaysWeather(fiveDaysData);

        verify(weatherView).setListProgressIndicator(false);

        verify(weatherView).refreshFiveDaysWeather(fiveDaysData);
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

    @Test
    public void onCreateWithoutSavedStateTest(){

        PlaceCords placeCords = new PlaceCords(1d, 2d);
        when(sharedPreferencesManager.getLastPlaceCords()).thenReturn(placeCords);

        mainPresenter.onCreate(null);

        //Check preferences for last saved data:
        verify(sharedPreferencesManager).loadObjectFromJson
                (SharedPreferencesManager.JSON_CURRENTWEATHER, WeatherCurrentData.class);
        verify(sharedPreferencesManager).loadObjectFromJson
                (SharedPreferencesManager.JSON_FIVEDAYSWEATHER, WeatherFiveDaysData.class);

        //Check refresh data
        verify(sharedPreferencesManager).getLastPlaceCords();
        verify(openWeatherApi).getCurrentWeather(placeCords.lat, placeCords.lon);
        verify(openWeatherApi).getFiveDaysWeather(placeCords.lat, placeCords.lon);
    }


//     TODO
//    @Test
//    public void onCreateWithSavedStateTest(){
//        WeatherCurrentData weatherCurrentData = new WeatherCurrentData();
//        WeatherFiveDaysData weatherFiveDaysData = new WeatherFiveDaysData();
//
//        Bundle bundle = setupBundle(weatherCurrentData, weatherFiveDaysData);
//        mainPresenter.onCreate(bundle);
//
//        verify(weatherView).refreshCurrentWeather(weatherCurrentData);
//        verify(weatherView).refreshFiveDaysWeather(weatherFiveDaysData);
//    }
//
//    private Bundle setupBundle(WeatherCurrentData weatherCurrentData, WeatherFiveDaysData weatherFiveDaysData) {
//
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(SharedPreferencesManager.JSON_CURRENTWEATHER, Parcels.wrap(weatherCurrentData));
//        bundle.putParcelable(SharedPreferencesManager.JSON_FIVEDAYSWEATHER, Parcels.wrap(weatherFiveDaysData));
//        return bundle;
//    }

}
