package eu.rafalolszewski.simplyweather.util;

import java.text.DecimalFormat;

import eu.rafalolszewski.simplyweather.views.activities.SettingsActivity;

/**
 * Created by rafal on 05.03.16.
 */
public class StringsProvider {

    public static final String[] DIRECTIONS_SHORT = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};


    public static String latOrLongToString(double latOrLong){
        DecimalFormat formatter = new DecimalFormat("0.000000");
        return formatter.format(latOrLong);
    }

    /**
     * TEMPERATURE STRING
     */
    public static String getTempString(float tempInKelvin, String unit){
        switch (unit){
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

    public static String getTempMinMaxString(float tempMinInKelvin, float tempMaxInKelvin, String unit){
        return getTempString(tempMinInKelvin, unit) + " - " + getTempString(tempMaxInKelvin, unit);
    }

    private static String getCelsiusStringFromKelvin(float kelvin){
        float celsius = kelvinToCelsius(kelvin);
        return String.format("%.0f", celsius) + "℃";
    }

    private static String getKelvinString(float tempInKelvin){

        return String.format("%.0f", tempInKelvin) + " K";
    }

    private static String getFahrenheitFromKelvin(float tempInKelvin) {
        float fahrenheit = kelvinToFahrenheit(tempInKelvin);
        return String.format("%.0f", fahrenheit) + "°F";
    }

    private static float kelvinToCelsius(float kelvin) {
        return (kelvin - (float) 273.15);
    }

    private static float kelvinToFahrenheit(float tempInKelvin) {
        return (tempInKelvin * (9f / 5f)) - 459.67f;
    }

    /**
     * WIND STRING
     */
    public static String getWindString(float speedInMpS, float direction, String unit){
        switch (unit){
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

    private static String getSpeedInMpS(float speed){
        return String.format("%.0f", speed) + " m/s";
    }

    private static String getSpeedInKMpH(float speed){
        float kmph = (speed / 1000) * 3600;
        return String.format("%.0f", kmph) + " km/h";
    }

    private static String getSpeedInMIpH(float speed){
        float miph = (speed / 1609.344f) * 3600;
        return String.format("%.0f", miph) + " km/h";
    }

    private static String getWindDirection(float windDirection){
        int dir = (int) Math.floor((windDirection / 22.5) + 0.5);
        return DIRECTIONS_SHORT[dir%16];
    }

}
