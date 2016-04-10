package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class Snow {

    /**
     * Snow volume for the last 3 hours
     */
    @SerializedName("3h")
    public float volume;


    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(Snow.class)) return false;
        //Check params
        Snow objectToCompare = (Snow) o;
        if (volume != objectToCompare.volume) return false;

        return true;
    }
}
