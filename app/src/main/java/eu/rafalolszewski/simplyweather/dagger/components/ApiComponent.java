package eu.rafalolszewski.simplyweather.dagger.components;

import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Singleton;

import dagger.Component;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.dagger.modules.ApiModule;
import eu.rafalolszewski.simplyweather.dagger.modules.ApplicationModule;
import eu.rafalolszewski.simplyweather.dagger.scopes.PerApplication;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import retrofit2.Retrofit;

/**
 * Created by rafal on 11.03.16.
 */
@Singleton
@Component(modules = {ApiModule.class, ApplicationModule.class})
public interface ApiComponent {

    OpenWeatherApi getOpenWeatherApi();

    GoogleApiClient getGoogleApiClient();

    void inject(MainActivity activity);

}
