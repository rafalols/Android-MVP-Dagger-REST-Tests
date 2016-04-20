package eu.rafalolszewski.simplyweather.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.rafalolszewski.simplyweather.util.SharedPreferencesManager;

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

    @Provides
    @Singleton
    Gson providesGson(){
        return new Gson();
    }

    @Provides
    @Singleton
    SharedPreferencesManager providesSharedPreferencesManager(SharedPreferences sharedPreferences, Gson gson){
        return new SharedPreferencesManager(sharedPreferences, gson);
    }

}
