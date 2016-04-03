package eu.rafalolszewski.simplyweather.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafał Olszewski on 27.03.16.
 */
public class ImageMapper {

    private static final Map<String, String> imageMap;
    private static final String TAG = "ImageMapper";

    Context context;

    public ImageMapper(Context context) {
        this.context = context;
    }

    static {
        imageMap =  new HashMap<String, String>(){{
            //Day
            put("01d", "sun");
            put("02d", "few_clouds");
            put("03d", "scattered_clouds");
            put("04d", "clouds");
            put("09d", "shower_rain");
            put("10d", "rain");
            put("11d", "thunderstorm");
            put("13d", "snow");
            put("50d", "fog");
            //Night. TODO: add images for night weather
            put("01n", "sun");
            put("02n", "few_clouds");
            put("03n", "scattered_clouds");
            put("04n", "clouds");
            put("09n", "shower_rain");
            put("10n", "rain");
            put("11n", "thunderstorm");
            put("13n", "snow");
            put("50n", "fog");
        }};
    }

    public int getImageResourceId(String imageString){
        String imageName = getImageTitle(imageString);
        Log.d(TAG, "getImageResourceId: description = " + imageString + " , imageName = " + imageName);
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", "eu.rafalolszewski.simplyweather");
        return resourceId;
    }

    private static String getImageTitle(String description){
        if (imageMap.containsKey(description)) return imageMap.get(description);
        return null;
    }

}
