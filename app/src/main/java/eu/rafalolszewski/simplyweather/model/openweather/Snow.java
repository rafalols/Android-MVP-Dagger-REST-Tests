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

}
