package eu.rafalolszewski.simplyweather.model;

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
