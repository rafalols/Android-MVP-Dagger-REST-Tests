package eu.rafalolszewski.simplyweather.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by rafal on 11.03.16.
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
