package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rafal on 12.03.16.
 */
public class WeatherCurrentData {

    @SerializedName("name")
    public String cityName;

    @SerializedName("main")
    public MainMeasurements measurements;

    public Weather[] weather;

    public Wind wind;

    public Clouds clouds;

    public Rain rain;

    public Snow snow;

    @SerializedName("dt")
    public long date;


    /** CONSTRUCTOR*/
    public WeatherCurrentData(){
    }

    @Override
    public String toString() {
        //TODO: Napisać ładną metodę toString()
        if (cityName == null || measurements == null) return "";
        return "name = " + cityName + ", temp = " + measurements.temp;
    }
}
