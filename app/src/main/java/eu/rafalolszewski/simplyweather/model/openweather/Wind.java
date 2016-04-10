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


    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(Wind.class)) return false;
        //Check params
        Wind objectToCompare = (Wind) o;
        if (speed != objectToCompare.speed) return false;
        if (direction != objectToCompare.direction) return false;

        return true;
    }

}
