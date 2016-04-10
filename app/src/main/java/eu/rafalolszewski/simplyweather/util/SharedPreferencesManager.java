package eu.rafalolszewski.simplyweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;

import eu.rafalolszewski.simplyweather.model.PlaceCords;

/**
 * Created by Rafał Olszewski on 05.04.16.
 */
public class SharedPreferencesManager {

    public static final String LASTPLACE_LAT = "lastplaceLat";
    public static final String LASTPLACE_LON = "lastplaceLon";
    public static final String JSON_CURRENTWEATHER = "currentweatherJson";
    public static final String JSON_FIVEDAYSWEATHER = "fivedaysweatherJson";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveLastSearchedPlace(Place place){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(LASTPLACE_LAT, (float) place.getLatLng().latitude);
        editor.putFloat(LASTPLACE_LON, (float) place.getLatLng().longitude);
        editor.commit();
    }

    public PlaceCords getLastPlaceCords(){
        return new PlaceCords(sharedPreferences.getFloat(LASTPLACE_LAT, 0),
                              sharedPreferences.getFloat(LASTPLACE_LON, 0));
    }

    public void saveString(String string, String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, string);
        editor.apply();
    }

    public void saveObjectAsJson(Object object, String key){
        String jsonString = toJson(object);
        System.out.print(jsonString);
        saveString(jsonString, key);
    }

    private String toJson(Object object) {
        return new Gson().toJson(object, object.getClass());
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
