package eu.rafalolszewski.simplyweather.model;

import com.google.android.gms.location.places.Place;

/**
 * Created by rafal on 05.03.16.
 */
public class CityMapper {

    public static City plateToCity(Place place){
        return new City(place.getName().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
    }

}
