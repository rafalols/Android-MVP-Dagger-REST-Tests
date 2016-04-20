package eu.rafalolszewski.simplyweather.dagger.modules;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.rafalolszewski.simplyweather.api.OpenWeatherApi;
import eu.rafalolszewski.simplyweather.api.OpenWeatherService;
import eu.rafalolszewski.simplyweather.api.TimeZoneApi;
import eu.rafalolszewski.simplyweather.api.TimeZoneService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Rafał Olszewski on 05.03.16.
 */

@Module
public class ApiModule {

    @Provides
    @Singleton
    OpenWeatherService providesOpenWeatherService(){
        Retrofit retrofit = buildRetrofit(OpenWeatherApi.OPENWEATHER_API_URL);
        return retrofit.create(OpenWeatherService.class);
    }

    @Provides
    @Singleton
    TimeZoneService providesTimeZoneService(){
        Retrofit retrofit = buildRetrofit(TimeZoneApi.TIME_ZONE_API_URL);
        return retrofit.create(TimeZoneService.class);
    }

    private Retrofit buildRetrofit(String serviceUrl){
        return new Retrofit.Builder()
                .baseUrl(serviceUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OpenWeatherApi providesOpenWeatherApi(OpenWeatherService service, Application application){
        return new OpenWeatherApi(service, application);
    }

    @Provides
    @Singleton
    TimeZoneApi providesTimeZoneApi(TimeZoneService timeZoneService, Application application){
        return new TimeZoneApi(timeZoneService, application);
    }

    @Provides
    @Singleton
    GoogleApiClient providesGoogleApiClient(Application application){
        return new GoogleApiClient
                .Builder(application)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

}
