package eu.rafalolszewski.simplyweather;

import android.app.Application;

import eu.rafalolszewski.simplyweather.dagger.components.ApiComponent;
import eu.rafalolszewski.simplyweather.dagger.components.DaggerApiComponent;
import eu.rafalolszewski.simplyweather.dagger.modules.ApplicationModule;

/**
 * Created by rafal on 13.03.16.
 */
public class SimplyWeatherApp extends Application {

    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        apiComponent = DaggerApiComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApiComponent getApiComponent() {
        return apiComponent;
    }
}
