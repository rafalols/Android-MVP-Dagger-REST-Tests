package eu.rafalolszewski.simplyweather.dagger.modules;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.view.LayoutInflater;

import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.dagger.scopes.PerActivity;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;
import eu.rafalolszewski.simplyweather.util.ImageMapper;
import eu.rafalolszewski.simplyweather.util.SharedPreferencesManager;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import eu.rafalolszewski.simplyweather.views.adapters.FiveDaysWeatherListAdapter;

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
    MainPresenter providesMainPresenter(GoogleApiClient googleApiClient, OpenWeatherApi openWeatherApi,
                                        CurrentLocationProvider currentLocationProvider, SharedPreferencesManager preferencesManager){
        return new MainPresenter(activity, googleApiClient, openWeatherApi, currentLocationProvider, preferencesManager);
    }

    @Provides
    @PerActivity
    ImageMapper providesImageMapper(){
        return new ImageMapper(activity);
    }

    @Provides
    @PerActivity
    StringsProvider providesStringProvider(SharedPreferences sharedPreferences){
        return new StringsProvider(sharedPreferences, activity);
    }

    @Provides
    @PerActivity
    FiveDaysWeatherListAdapter providesFiveDaysWeatherListAdapter(ImageMapper imageMapper, StringsProvider stringsProvider){
        return new FiveDaysWeatherListAdapter(LayoutInflater.from(activity), imageMapper, stringsProvider);
    }

}
