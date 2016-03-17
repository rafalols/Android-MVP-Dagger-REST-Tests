package eu.rafalolszewski.simplyweather.views.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import javax.inject.Inject;

import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.SimplyWeatherApp;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.api.callback.WeatherApiCallback;
import eu.rafalolszewski.simplyweather.model.City;
import eu.rafalolszewski.simplyweather.model.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.model.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherBodyFragment;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener, WeatherApiCallback {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";


    PlaceAutocompleteFragment autocompleteFragment;

    WeatherBodyFragment weatherBodyFragment;


    @Inject
    OpenWeatherApi openWeatherApi;

    @Inject
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Dagger ApiComponent
        ((SimplyWeatherApp)getApplication()).getApiComponent().inject(this);

        InitialApis();

        InitialFragments();
    }

    private void InitialApis() {
        mGoogleApiClient.registerConnectionFailedListener(this);
        mGoogleApiClient.connect();
        openWeatherApi.setCallback(this);
    }

    private void InitialFragments() {
        initialAutocompleteFragment();
        weatherBodyFragment = (WeatherBodyFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
    }

    private void initialAutocompleteFragment() {
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint));
        autocompleteFragment.setFilter(createAutocompleteFilter());
        autocompleteFragment.setOnPlaceSelectedListener(this);

    }

    @NonNull
    private AutocompleteFilter createAutocompleteFilter() {
        // Create filter to search only cities
        return new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * On Google Place Api connection failed
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    /** Google Place Autocomplete - selected */
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "onPlaceSelected: place:" + place.getName() + "  log, lat:" + place.getLatLng().toString());
        City city = new City(place);
        openWeatherApi.getCurrentWeather(city.getLat(), city.getLon());
        openWeatherApi.getFiveDaysWeather(city.getLat(), city.getLon());

    }

    /** Google Place Autocomplete - error */
    @Override
    public void onError(Status status) {
        Log.w(TAG, "onCitySelectError: " + status.getStatusMessage());
    }


    /** Open Weather - Current weather callback  */
    @Override
    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData) {
        Log.i(TAG, "onGetCurrentWeather: " + weatherCurrentData.toString());
    }

    /** Open Weather - Five Days weather callback  */
    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        Log.i(TAG, "onGetFiveDaysWeather: " + weatherFiveDaysData.toString());
    }

    /** Open weather - Fail */
    @Override
    public void onGetWeatherFailure(Throwable t) {
        Log.e(TAG, "onGetWeatherFailure: ", t);
    }
}
