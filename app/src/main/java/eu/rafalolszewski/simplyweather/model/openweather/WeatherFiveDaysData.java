package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by rafal on 12.03.16.
 */
@Parcel
public class WeatherFiveDaysData {

    @SerializedName("city")
    public City city;

    @SerializedName("list")
    public WeatherList[] weatherLists;


    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(WeatherFiveDaysData.class)) return false;
        //Check params
        WeatherFiveDaysData objectToCompare = (WeatherFiveDaysData) o;
        if (city != null && !city.equals(objectToCompare.city)) return false;
        if (weatherLists != null && weatherLists.length > 0) {
            for (int i = 0; i < weatherLists.length; i++) {
                if (!weatherLists[i].equals(objectToCompare.weatherLists[i])) return false;
            }
        }
        return true;
    }

}
