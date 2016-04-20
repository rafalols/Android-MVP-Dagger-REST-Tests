package eu.rafalolszewski.simplyweather.api;

import eu.rafalolszewski.simplyweather.model.TimeZoneApiData;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Rafał Olszewski on 17.04.16.
 */
public interface TimeZoneService {

    /**
     * Get current weather by latitude and longitude
     * Used in selecting from google autocomplete searcher
     */
    @GET("/json")
    Call<TimeZoneApiData> getTimeZoneData(@Query("location") String location, @Query("timestamp") String timestamp, @Query("key") String key);

}
