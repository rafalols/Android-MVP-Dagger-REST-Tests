package eu.rafalolszewski.simplyweather.util;

import android.content.SharedPreferences;

import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;

import eu.rafalolszewski.simplyweather.model.PlaceCords;

/**
 * Created by Rafał Olszewski on 05.04.16.
 */
public class SharedPreferencesManager {

    public static final String LASTPLACE_LAT = "lastplaceLat";
    public static final String LASTPLACE_LON = "lastplaceLon";
    public static final String LAST_SEARCH_WAS_CURRENT_PLACE = "lastSearchWasCurrentPlace";
    public static final String PLACE_TIME_ZONE_PREFIX = "timezoneOf";
    public static final String JSON_CURRENTWEATHER = "currentweatherJson";
    public static final String JSON_FIVEDAYSWEATHER = "fivedaysweatherJson";
    public static final long WRONG_LAT_LON = 1000000L;
    public static final int DEFAULT_WRONG_PLACE_TIMEZONE = -1;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Gson gson;

    public SharedPreferencesManager(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
        this.gson = gson;
    }

    public void saveLastSearchedPlace(Place place){
        editor.putBoolean(LAST_SEARCH_WAS_CURRENT_PLACE, false);
        editor.putLong(LASTPLACE_LAT, Double.doubleToRawLongBits(place.getLatLng().latitude));
        editor.putLong(LASTPLACE_LON, Double.doubleToRawLongBits(place.getLatLng().longitude));
        editor.commit();
    }

    public void saveLastSearchWasCurrentPlace(){
        editor.putBoolean(LAST_SEARCH_WAS_CURRENT_PLACE, true);
        editor.commit();
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public PlaceCords getLastPlaceCords(){
        long latInRawLong = sharedPreferences.getLong(LASTPLACE_LAT, WRONG_LAT_LON);
        long lonInRawLong = sharedPreferences.getLong(LASTPLACE_LON, WRONG_LAT_LON);
        // Return null if not find in sharedPreferences (get default values)
        if (latInRawLong == WRONG_LAT_LON || lonInRawLong == WRONG_LAT_LON) return null;

        return new PlaceCords(Double.longBitsToDouble(latInRawLong),
                Double.longBitsToDouble(lonInRawLong));
    }

    public int getPlaceTimeZoneOffset(String placeId){
        return sharedPreferences.getInt(PLACE_TIME_ZONE_PREFIX + placeId, DEFAULT_WRONG_PLACE_TIMEZONE);
    }


    public void saveTimeZoneOffset(String placeId, int offset) {
        editor.putInt(PLACE_TIME_ZONE_PREFIX + placeId, offset);
        editor.apply();
    }

    public void saveString(String string, String key){
        editor.putString(key, string);
        editor.apply();
    }

    public void saveObjectAsJson(Object object, String key){
        String jsonString = toJson(object);
        System.out.print(jsonString);
        saveString(jsonString, key);
    }

    private String toJson(Object object) {
        return gson.toJson(object, object.getClass());
    }

    public <T> T loadObjectFromJson(String key, Class<T> classOfT){
        String jsonString = sharedPreferences.getString(key, null);
        return gson.fromJson(jsonString, classOfT);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
