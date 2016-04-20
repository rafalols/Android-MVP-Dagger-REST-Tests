package eu.rafalolszewski.simplyweather.api;

import android.app.Application;

import java.security.Timestamp;

import eu.rafalolszewski.simplyweather.api.callback.TimeZoneCallback;
import eu.rafalolszewski.simplyweather.model.PlaceCords;
import eu.rafalolszewski.simplyweather.model.TimeZoneApiData;
import eu.rafalolszewski.simplyweather.util.MetaDataProvider;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Rafał Olszewski on 17.04.16.
 */
public class TimeZoneApi {

    public static final String TIME_ZONE_API_URL = "https://maps.googleapis.com/maps/api/timezone";
    private final static String GOOGLE_API_KEY_IN_MANIFEST = "com.google.android.geo.API_KEY";
    private static final String TAG = "TimeZoneApi";

    private final String google_api_key;

    private TimeZoneService timeZoneService;

    private TimeZoneCallback callback;


    public TimeZoneApi(TimeZoneService timeZoneService, Application application){
        this.timeZoneService = timeZoneService;
        google_api_key = MetaDataProvider.getMetaDataStringFromKey(application, GOOGLE_API_KEY_IN_MANIFEST);
    }

    public void getTimeZoneOffsetFromUTC(PlaceCords cords, Timestamp timestamp){
        Call<TimeZoneApiData> call = timeZoneService.getTimeZoneData(
                StringsProvider.placeCordsToStringWithComma(cords),
                timestamp.toString(),
                google_api_key);
        call.enqueue(new CallbackTimeZone());
    }

    private class CallbackTimeZone implements Callback<TimeZoneApiData>{

        @Override
        public void onResponse(Response<TimeZoneApiData> response, Retrofit retrofit) {
            if (!response.isSuccess()) {
                callback.onGetTimeZoneData(null);
            }else {
                TimeZoneApiData timeZoneApiData = response.body();
                callback.onGetTimeZoneData(timeZoneApiData);
            }
        }

        @Override
        public void onFailure(Throwable t) {
            callback.onGetTimeZoneDataFailure(t);
        }
    }

    public void setCallback(TimeZoneCallback timeZoneCallback){
        this.callback = timeZoneCallback;
    }

}
