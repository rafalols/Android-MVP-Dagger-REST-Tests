package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
public class Rain {

    /**
     * Rain volume for the last 3 hours
     */
    @SerializedName("3h")
    public float volume;

}
