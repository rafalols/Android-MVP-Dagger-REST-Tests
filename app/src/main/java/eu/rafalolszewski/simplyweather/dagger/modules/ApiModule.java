package eu.rafalolszewski.simplyweather.dagger.modules;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.api.OpenWeatherService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by rafal on 05.03.16.
 */

@Module
public class ApiModule {

    private static final String OPENWEATHER_API_URL = "http://api.openweathermap.org/";

    @Provides
    @Singleton
    Retrofit providesRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(OPENWEATHER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OpenWeatherService providesOpenWeatherService(Retrofit retrofit){
        return retrofit.create(OpenWeatherService.class);
    }

    @Provides
    @Singleton
    OpenWeatherApi providesOpenWeatherApi(OpenWeatherService service, Application application){
        return new OpenWeatherApi(service, application);
    }

    @Provides
    @Singleton
    GoogleApiClient googleApiClient(Application application){
        return new GoogleApiClient
                .Builder(application)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

}
