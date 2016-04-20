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


    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(Weather.class)) return false;
        //Check params
        Weather objectToCompare = (Weather) o;
        if (id != objectToCompare.id) return false;
        if (!shortDesc.equals(objectToCompare.shortDesc)) return false;
        if (!description.equals(objectToCompare.description)) return false;
        if (!image.equals(objectToCompare.image)) return false;

        return true;
    }

    public boolean isValid() {
        if (image != null){
            return true;
        }
        return false;
    }
}
