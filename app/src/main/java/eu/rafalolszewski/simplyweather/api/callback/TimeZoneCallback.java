package eu.rafalolszewski.simplyweather.api.callback;

import eu.rafalolszewski.simplyweather.model.TimeZoneApiData;

/**
 * Created by Rafał Olszewski on 17.04.16.
 */
public interface TimeZoneCallback {

    void onGetTimeZoneData(TimeZoneApiData timeZoneApiData);

    void onGetTimeZoneDataFailure(Throwable t);
}
