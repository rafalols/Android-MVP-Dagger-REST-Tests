package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class WeatherList {

    @SerializedName("main")
    public MainMeasurements measurements;

    public Weather weather;

    public Wind wind;

    public Clouds clouds;

    public Rain rain;

    public Snow snow;

    @SerializedName("dt")
    public long date;

}
