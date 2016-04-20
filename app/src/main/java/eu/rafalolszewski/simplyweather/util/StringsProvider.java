package eu.rafalolszewski.simplyweather.util;

import android.content.SharedPreferences;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eu.rafalolszewski.simplyweather.model.PlaceCords;
import eu.rafalolszewski.simplyweather.views.activities.SettingsActivity;

/**
 * Created by rafal on 05.03.16.
 */
public class StringsProvider {

    public static final String[] DIRECTIONS_SHORT = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

    private SharedPreferences sharedPreferences;

    public StringsProvider(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public static String latOrLongToString(double latOrLong){
        return String.format(Locale.US, "%.6f", latOrLong);
    }

    /**
     * TEMPERATURE STRING
     */
    public String getTempString(float tempInKelvin){
        switch (getTempUnit()){
            case SettingsActivity.TEMP_UNIT_CELSIUS:
                return getCelsiusStringFromKelvin(tempInKelvin);
            case SettingsActivity.TEMP_UNIT_KELVIN:
                return getKelvinString(tempInKelvin);
            case SettingsActivity.TEMP_UNIT_FAHRENHEIT:
                return getFahrenheitFromKelvin(tempInKelvin);
            default:
                return getCelsiusStringFromKelvin(tempInKelvin);
        }
    }

    public String getTempMinMaxString(float tempMinInKelvin, float tempMaxInKelvin){
        return getTempString(tempMinInKelvin) + " - " + getTempString(tempMaxInKelvin);
    }

    private String getCelsiusStringFromKelvin(float kelvin){
        float celsius = kelvinToCelsius(kelvin);
        return String.format(Locale.US, "%.0f", celsius) + "℃";
    }

    private String getKelvinString(float tempInKelvin){

        return String.format(Locale.US, "%.0f", tempInKelvin) + " K";
    }

    private String getFahrenheitFromKelvin(float tempInKelvin) {
        float fahrenheit = kelvinToFahrenheit(tempInKelvin);
        return String.format(Locale.US, "%.0f", fahrenheit) + "°F";
    }

    private float kelvinToCelsius(float kelvin) {
        return (kelvin - (float) 273.15);
    }

    private float kelvinToFahrenheit(float tempInKelvin) {
        return (tempInKelvin * (9f / 5f)) - 459.67f;
    }

    /**
     * WIND STRING
     */
    public String getWindString(float speedInMpS, float direction){
        switch (getSpeedUnit()){
            case SettingsActivity.SPEED_UNIT_KMH:
                return getWindDirection(direction) + " " + getSpeedInKMpH(speedInMpS);
            case SettingsActivity.SPEED_UNIT_MPS:
                return getWindDirection(direction) + " " + getSpeedInMpS(speedInMpS);
            case SettingsActivity.SPEED_UNIT_MPH:
                return getWindDirection(direction) + " " + getSpeedInMIpH(speedInMpS);
            default:
                return getWindDirection(direction) + " " + getSpeedInKMpH(speedInMpS);
        }
    }

    private String getSpeedInMpS(float speed){
        return String.format(Locale.US,"%.0f", speed) + " m/s";
    }

    private String getSpeedInKMpH(float speed){
        float kmph = (speed / 1000) * 3600;
        return String.format(Locale.US, "%.0f", kmph) + " km/h";
    }

    private static String getSpeedInMIpH(float speed){
        float miph = (speed / 1609.344f) * 3600;
        return String.format(Locale.US, "%.0f", miph) + " km/h";
    }

    private static String getWindDirection(float windDirection){
        int dir = (int) Math.floor((windDirection / 22.5) + 0.5);
        return DIRECTIONS_SHORT[dir%16];
    }

    private String getSpeedUnit() {
        return sharedPreferences.getString(SettingsActivity.SPEED_UNIT_KEY, SettingsActivity.SPEED_UNIT_DEFAULT);
    }

    private String getTempUnit() {
        return sharedPreferences.getString(SettingsActivity.TEMP_UNIT_KEY, SettingsActivity.TEMP_UNIT_DEFAULT);
    }

    /**
     * PRESSURE STRING
     */
    public String getPressureString(float pressure){
        return String.format(Locale.US, "%.0f", pressure) + " hPa";
    }

    /**
     * DATE STRING
     */
    public String getHour(long dateLong){
        Date date = new Date(dateLong * 1000L);
        return new SimpleDateFormat("HH", Locale.US).format(date) + "h";
    }
}
