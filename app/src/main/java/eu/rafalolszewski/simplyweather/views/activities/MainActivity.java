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
import com.google.android.gms.location.places.Places;
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
import eu.rafalolszewski.simplyweather.views.fragments.CurrentCityFooterFragment;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherBodyFragment;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener, WeatherApiCallback {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;

    PlaceAutocompleteFragment autocompleteFragment;

    WeatherBodyFragment weatherBodyFragment;

    CurrentCityFooterFragment footerFragment;

    @Inject
    OpenWeatherApi openWeatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Google Api Client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, 0, this)
                .build();
        mGoogleApiClient.connect();

        //Set Dagger ApiComponent
        ((SimplyWeatherApp)getApplication()).getApiComponent().inject(this);

        InitialFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();

        openWeatherApi.setCallback(this);
    }

    private void InitialFragments() {
        initialAutocompleteFragment();
        weatherBodyFragment = (WeatherBodyFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
        footerFragment = (CurrentCityFooterFragment) getFragmentManager().findFragmentById(R.id.fragment_footer);
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
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "onPlaceSelected: place:" + place.getName() + "  log, lat:" + place.getLatLng().toString());
        City city = new City(place);
        openWeatherApi.getCurrentWeather(city.getLat(), city.getLon());
        openWeatherApi.getFiveDaysWeather(city.getLat(), city.getLon());

    }

    @Override
    public void onError(Status status) {
        Log.w(TAG, "onCitySelectError: " + status.getStatusMessage());
    }


    @Override
    public void onGetCurrentWeather(WeatherCurrentData weatherCurrentData) {
        Log.i(TAG, "onGetCurrentWeather: " + weatherCurrentData.toString());
    }

    @Override
    public void onGetFiveDaysWeather(WeatherFiveDaysData weatherFiveDaysData) {
        Log.i(TAG, "onGetFiveDaysWeather: " + weatherFiveDaysData.toString());
    }

    @Override
    public void onGetWeatherFailure(Throwable t) {
        Log.e(TAG, "onGetWeatherFailure: ", t);
    }
}
