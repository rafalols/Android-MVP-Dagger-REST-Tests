package eu.rafalolszewski.simplyweather;

import android.app.Application;

import eu.rafalolszewski.simplyweather.dagger.components.ApplicationComponent;
import eu.rafalolszewski.simplyweather.dagger.components.DaggerApplicationComponent;
import eu.rafalolszewski.simplyweather.dagger.modules.ApplicationModule;

/**
 * Created by rafal on 13.03.16.
 */
public class SimplyWeatherApp extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
