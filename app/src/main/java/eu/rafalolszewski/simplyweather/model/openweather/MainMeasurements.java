package eu.rafalolszewski.simplyweather.model.openweather;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class MainMeasurements {

    /**
     * Temperature. unit: Kelvin
     */
    public float temp;

    /**
     *  Atmospheric pressure. Unit: hPa
     */
    public float pressure;

    /**
     * Humidity, %
     */
    public float humidity;

    /**
     * Minimum temperature at the moment.
     * This is deviation from current temp that is possible for large cities and megalopolises
     * geographically expanded (use these parameter optionally). Unit Default: Kelvin
     */
    public float temp_min;

    /**
     * Maximum temperature at the moment.
     * This is deviation from current temp that is possible for large cities and megalopolises
     * geographically expanded (use these parameter optionally). Unit Default: Kelvin
     */
    public float temp_max;

}
