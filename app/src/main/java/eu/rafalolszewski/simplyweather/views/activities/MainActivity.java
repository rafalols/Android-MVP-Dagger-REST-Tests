package eu.rafalolszewski.simplyweather.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Places;

import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.model.City;
import eu.rafalolszewski.simplyweather.views.fragments.CurrentCityFooterFragment;
import eu.rafalolszewski.simplyweather.views.fragments.SearchFragment;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherBodyFragment;
import eu.rafalolszewski.simplyweather.views.fragments.callbacks.SearchCallback;

public class MainActivity extends AppCompatActivity implements SearchCallback, GoogleApiClient.OnConnectionFailedListener {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;

    SearchFragment searchFragment;

    WeatherBodyFragment weatherBodyFragment;

    CurrentCityFooterFragment footerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        searchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.fragment_search);
        weatherBodyFragment = (WeatherBodyFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
        footerFragment = (CurrentCityFooterFragment) getFragmentManager().findFragmentById(R.id.fragment_footer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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


    @Override
    public void onCitySelected(City city) {
        weatherBodyFragment.setCity(city);
    }

    @Override
    public void onError(Status status) {
        Log.d(TAG, "onError: " + status.getStatusMessage());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }



}
