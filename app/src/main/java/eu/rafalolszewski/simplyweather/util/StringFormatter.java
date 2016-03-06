package eu.rafalolszewski.simplyweather.util;

import java.text.DecimalFormat;

/**
 * Created by rafal on 05.03.16.
 */
public class StringFormatter {

    public static String latLongToString(double latOrLong){
        DecimalFormat formatter = new DecimalFormat("0.000000");
        return formatter.format(latOrLong);
    }

}
