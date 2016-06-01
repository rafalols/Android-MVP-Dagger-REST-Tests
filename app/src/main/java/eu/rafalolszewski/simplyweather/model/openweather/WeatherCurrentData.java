package eu.rafalolszewski.simplyweather.model.openweather;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by rafal on 12.03.16.
 */
@Parcel
public class WeatherCurrentData{

    @SerializedName("name")
    public String cityName;

    @SerializedName("main")
    public MainMeasurements measurements;

    public Weather[] weather;

    public Wind wind;

    public Clouds clouds;

    public Rain rain;

    public Snow snow;

    @SerializedName("dt")
    public long date;


    /** CONSTRUCTOR*/
    public WeatherCurrentData(){
    }

    @Override
    public String toString() {
        if (cityName == null || measurements == null) return "";
        return "name = " + cityName + ", temp = " + measurements.temp;
    }

    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(WeatherCurrentData.class)) return false;
        //Check params
        WeatherCurrentData objectToCompare = (WeatherCurrentData) o;
        if (cityName != null && !cityName.equals(objectToCompare.cityName)) return false;
        if (measurements != null && !measurements.equals(objectToCompare.measurements)) return false;
        if (wind != null && !wind.equals(objectToCompare.wind)) return false;
        if (clouds != null && !clouds.equals(objectToCompare.clouds)) return false;
        if (rain != null && !rain.equals(objectToCompare.rain)) return false;
        if (snow != null && !snow.equals(objectToCompare.snow)) return false;
        if (date != objectToCompare.date) return false;

        if (weather != null && weather.length > 0){
            for (int i = 0; i < weather.length; i++){
                if (!weather[i].equals(objectToCompare.weather[i])) return false;
            }
        }

        return true;
    }


    public boolean isValid() {
        if (cityName != null
                && measurements != null
                && weather != null
                && weather[0] != null
                && weather[0].isValid()
                && wind != null){
            return true;
        }else {
            return false;
        }
    }
}
