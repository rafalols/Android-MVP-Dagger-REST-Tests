package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class Weather {

    /**
     * Weather condition id
     */
    public int id;

    /**
     * Group of weather parameters (Rain, Snow, Extreme etc.)
     */
    @SerializedName("main")
    public String shortDesc;

    /**
     * Weather condition within the group
     */
    @SerializedName("description")
    public String description;

    @SerializedName("icon")
    public String image;

}
