package eu.rafalolszewski.simplyweather.dagger.modules;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.dagger.scopes.PerActivity;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;

/**
 * Created by rafal on 17.03.16.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity providesMainActivity(){
        return activity;
    }

    @Provides
    @PerActivity
    LocationManager providesLocationManager(){
        return (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    @PerActivity
    CurrentLocationProvider locationProvider(LocationManager locationManager){
        return new CurrentLocationProvider(activity, locationManager);
    }

    @Provides
    @PerActivity
    MainPresenter providesMainPresenter(GoogleApiClient googleApiClient, OpenWeatherApi openWeatherApi, CurrentLocationProvider currentLocationProvider){
        return new MainPresenter(activity, googleApiClient, openWeatherApi, currentLocationProvider);
    }

}
