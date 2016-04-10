package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class WeatherList {

    @SerializedName("main")
    public MainMeasurements measurements;

    public Weather[] weather;

    public Wind wind;

    public Clouds clouds;

    public Rain rain;

    public Snow snow;

    @SerializedName("dt")
    public long date;

    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(WeatherList.class)) return false;
        //Check params
        WeatherList objectToCompare = (WeatherList) o;
        if (measurements != null && !measurements.equals(objectToCompare.measurements)) return false;
        if (wind != null && !wind.equals(objectToCompare.wind)) return false;
        if (clouds != null && !clouds.equals(objectToCompare.clouds)) return false;
        if (rain != null && !rain.equals(objectToCompare.rain)) return false;
        if (snow != null && !snow.equals(objectToCompare.snow)) return false;
        if (date != objectToCompare.date) return false;
        if (weather != null && weather.length > 0) {
            for (int i = 0; i < weather.length; i++) {
                if (!weather[i].equals(objectToCompare.weather[i])) return false;
            }
        }

        return true;
    }

}
