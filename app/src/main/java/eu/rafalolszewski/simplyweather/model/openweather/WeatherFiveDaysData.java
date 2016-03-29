package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rafal on 12.03.16.
 */
public class WeatherFiveDaysData {

    @SerializedName("city")
    public City city;

    @SerializedName("list")
    public WeatherList[] weatherLists;


}
