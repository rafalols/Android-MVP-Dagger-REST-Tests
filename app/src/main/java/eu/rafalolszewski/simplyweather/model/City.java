package eu.rafalolszewski.simplyweather.model;

import com.google.android.gms.location.places.Place;

/**
 * Created by rafal on 05.03.16.
 */
public class City {

    private String cityName;
    private double lat;
    private double lon;

    public City(String cityName, double lat, double lon) {
        this.cityName = cityName;
        this.lat = lat;
        this.lon = lon;
    }

    public City(Place place){
        this.cityName = place.getName().toString();
        this.lat = place.getLatLng().latitude;
        this.lon = place.getLatLng().longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
