package eu.rafalolszewski.simplyweather.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.SimplyWeatherApp;
import eu.rafalolszewski.simplyweather.dagger.components.ActivityComponent;
import eu.rafalolszewski.simplyweather.dagger.components.ApplicationComponent;
import eu.rafalolszewski.simplyweather.dagger.components.DaggerActivityComponent;
import eu.rafalolszewski.simplyweather.dagger.modules.ActivityModule;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherBodyFragment;

public class MainActivity extends BaseActivity implements MainActivityController {

    private static final String TAG = "MainActivity";

    private ActivityComponent activityComponent;

    PlaceAutocompleteFragment autocompleteFragment;

    public WeatherBodyFragment weatherBodyFragment;

    MainPresenter mainPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupInjection();

        InitialFragments();
    }


    private void setupInjection() {
        //Create ActivityComponent
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        //Inject Presenter
        mainPresenter = activityComponent.mainPresenter();
        ButterKnife.bind(this, this);
    }

    private ApplicationComponent getApplicationComponent(){
        return  ((SimplyWeatherApp)getApplication()).getApplicationComponent();
    }

    private void InitialFragments() {
        initialAutocompleteFragment();
        initialWeatherBodyFragment();
    }

    private void initialWeatherBodyFragment() {
        weatherBodyFragment = (WeatherBodyFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
        activityComponent.inject(weatherBodyFragment);
        mainPresenter.setViewInterace(weatherBodyFragment);
    }

    private void initialAutocompleteFragment() {
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint));
        autocompleteFragment.setFilter(createAutocompleteFilter());
        autocompleteFragment.setOnPlaceSelectedListener(mainPresenter);
    }

    @OnClick(R.id.button_position)
    public void currentPositionButtonClick(){
        mainPresenter.getCurrentPositionWeather();
    }

    @NonNull
    private AutocompleteFilter createAutocompleteFilter() {
        // Create filter to search only cities
        return new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.connectGoogleApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.disconnectGoogleApi();
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGoogleApiConnectionFail() {
        Toast.makeText(this, getString(R.string.cantConnectGoogeApi), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onCantGetGooglePlace() {
        Toast.makeText(this, getString(R.string.cantGetPlace), Toast.LENGTH_LONG).show();

    }

    @Override
    public void cantGetCurrentPosition() {
        Toast.makeText(this, getString(R.string.cant_get_position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationPermissionsGranted() {
        mainPresenter.getCurrentPositionWeather();
    }

    @Override
    public void onLocationPermissionsDenied() {
        Toast.makeText(this, getString(R.string.cantGetCurrentLocation), Toast.LENGTH_LONG).show();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
