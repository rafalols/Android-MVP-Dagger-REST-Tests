package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class Clouds {

    /**
     * Cloudiness, %
     */
    @SerializedName("all")
    public float cloudiness;

    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(Clouds.class)) return false;
        //Check params
        Clouds objectToCompare = (Clouds) o;
        if (cloudiness != objectToCompare.cloudiness) return false;

        return true;
    }

}
