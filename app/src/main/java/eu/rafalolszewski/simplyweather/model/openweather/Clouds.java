package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
public class Clouds {

    /**
     * Cloudiness, %
     */
    @SerializedName("all")
    public float cloudiness;

}
