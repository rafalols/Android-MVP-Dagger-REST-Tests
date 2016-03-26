package eu.rafalolszewski.simplyweather.dagger.components;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Singleton;

import dagger.Component;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.dagger.modules.ApiModule;
import eu.rafalolszewski.simplyweather.dagger.modules.ApplicationModule;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;

/**
 * Created by rafal on 11.03.16.
 */
@Singleton
@Component(modules = {ApiModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    OpenWeatherApi openWeatherApi();

    GoogleApiClient googleApiClient();

    SharedPreferences sharedPreferences();

}
