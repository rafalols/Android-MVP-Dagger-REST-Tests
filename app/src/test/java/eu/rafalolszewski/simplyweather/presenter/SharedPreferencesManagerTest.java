package eu.rafalolszewski.simplyweather.presenter;

import android.content.SharedPreferences;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eu.rafalolszewski.simplyweather.model.PlaceCords;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.util.FileHelper;
import eu.rafalolszewski.simplyweather.util.SharedPreferencesManager;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by Rafał Olszewski on 05.04.16.
 */
public class SharedPreferencesManagerTest {

    private static final String JSON_CURRENTWEATHER_TEST = "currentweatherJsonTest";

    SharedPreferencesManager sharedPreferencesManager;

    WeatherCurrentData testData;

    @Mock
    Place mockecdPlace;

    @Mock
    SharedPreferences sharedPreferences;

    @Mock
    SharedPreferences.Editor editor;

    Gson gson = new Gson();

    @Before
    public void setupComponents() throws Exception{
        MockitoAnnotations.initMocks(this);

        when(sharedPreferences.edit()).thenReturn(editor);
        sharedPreferencesManager = new SharedPreferencesManager(sharedPreferences, gson);
        testData = getTestWeatherData();
        mockTestPlace();
    }

    private void mockTestPlace() {
        LatLng latLon = new LatLng(10d, 20d);
        when(mockecdPlace.getLatLng()).thenReturn(latLon);
    }

    /** Get current weather date from JSON file */
    private WeatherCurrentData getTestWeatherData() throws Exception {
        String currentWeatherJson = FileHelper.readJsonFile("src/main/assets/test_jsons/currentweather.json");
        Gson gson =  new Gson();
        return gson.fromJson(currentWeatherJson, WeatherCurrentData.class);
    }


    @Test
    public void saveObjectAsJsonTest() throws InterruptedException {
        sharedPreferencesManager.saveObjectAsJson(testData, JSON_CURRENTWEATHER_TEST);

        String jsonString = gson.toJson(testData, WeatherCurrentData.class);

        verify(sharedPreferences.edit()).putString(JSON_CURRENTWEATHER_TEST, jsonString);
    }

    @Test
    public void saveLastSearchedPlaceTest(){
        sharedPreferencesManager.saveLastSearchedPlace(mockecdPlace);

        PlaceCords placeCords = new PlaceCords(mockecdPlace.getLatLng().latitude,
                mockecdPlace.getLatLng().longitude);

        verify(sharedPreferences.edit()).putLong(SharedPreferencesManager.LASTPLACE_LAT, Double.doubleToRawLongBits(placeCords.lat));
        verify(sharedPreferences.edit()).putLong(SharedPreferencesManager.LASTPLACE_LON, Double.doubleToRawLongBits(placeCords.lon));
    }

    @Test
    public void saveLastSearchWasCurrentPlaceTest(){
        sharedPreferencesManager.saveLastSearchWasCurrentPlace();
        verify(sharedPreferences.edit()).putBoolean(SharedPreferencesManager.LAST_SEARCH_WAS_CURRENT_PLACE, true);
    }


}
