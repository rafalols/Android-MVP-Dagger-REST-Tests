package eu.rafalolszewski.simplyweather.util;

import java.text.DecimalFormat;

import eu.rafalolszewski.simplyweather.views.activities.SettingsActivity;

/**
 * Created by rafal on 05.03.16.
 */
public class StringFormatter {

    public static final String[] DIRECTION_SHORT = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};


    public static String latOrLongToString(double latOrLong){
        DecimalFormat formatter = new DecimalFormat("0.000000");
        return formatter.format(latOrLong);
    }


    /**
     * TEMPERATURE STRING
     */
    public static String getTempString(float tempInKelvin, int unit){
        switch (unit){
            case SettingsActivity.TEMP_UNIT_CELSIUS:
                return getCelsiusStringFromKelvin(tempInKelvin);
            case SettingsActivity.TEMP_UNIT_KELVIN:
                return getKelvinString(tempInKelvin);
            default:
                return getCelsiusStringFromKelvin(tempInKelvin);
        }
    }

    public static String getTempMinMaxString(float tempMinInKelvin, float tempMaxInKelvin, int unit){
        switch (unit){
            case SettingsActivity.TEMP_UNIT_CELSIUS:
                return getCelsiusStringFromKelvin(tempMinInKelvin) + " - " + getCelsiusStringFromKelvin(tempMaxInKelvin);
            case SettingsActivity.TEMP_UNIT_KELVIN:
                return getKelvinString(tempMinInKelvin) + " - " + getKelvinString(tempMaxInKelvin);
            default:
                return getCelsiusStringFromKelvin(tempMinInKelvin) + " - " + getCelsiusStringFromKelvin(tempMaxInKelvin);

        }
    }

    private static String getCelsiusStringFromKelvin(float kelvin){
        float celsius = kelvinToCelsius(kelvin);
        return String.format("%.1f", celsius) + "\u2103";
    }

    private static String getKelvinString(float tempInKelvin){
        return String.format("%.1f", tempInKelvin) + " K";
    }

    /**
     * WIND STRING
     */
    public static String getWindString(float speed, float direction, int unit){
        switch (unit){
            case SettingsActivity.WIND_UNIT_KMH:
                return getWindDirection(direction) + " " + getSpeedInKMpH(speed);
            case SettingsActivity.WIND_UNIT_MPS:
                return getWindDirection(direction) + " " + getSpeedInMpS(speed);
            default:
                return getWindDirection(direction) + " " + getSpeedInKMpH(speed);
        }
    }

    private static String getSpeedInMpS(float speed){
        return String.format("%.0f", speed) + " m/s";
    }

    private static String getSpeedInKMpH(float speed){
        float result = (speed / 1000) * 3600;
        return String.format("%.0f", result) + " km/h";
    }

    private static String getWindDirection(float windDirection){
        int dir = (int) Math.floor((windDirection / 22.5) + 0.5);
        return DIRECTION_SHORT[dir%16];
    }

    private static float kelvinToCelsius(float kelvin) {
        return (kelvin - (float) 273.15);
    }



}
