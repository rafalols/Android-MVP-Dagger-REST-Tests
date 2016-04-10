package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class Rain {

    /**
     * Rain volume for the last 3 hours
     */
    @SerializedName("3h")
    public float volume;

    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(Rain.class)) return false;
        //Check params
        Rain objectToCompare = (Rain) o;
        if (volume != objectToCompare.volume) return false;

        return true;
    }

}
