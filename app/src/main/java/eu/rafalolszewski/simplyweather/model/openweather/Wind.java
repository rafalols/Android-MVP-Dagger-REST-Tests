package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class Wind {

    /**
     * Wind speed. Unit: meter/sec,
     */
    public float speed;

    /**
     * Wind direction, degrees (meteorological)
     */
    @SerializedName("deg")
    public float direction;

}
