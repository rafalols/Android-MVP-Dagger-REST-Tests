package eu.rafalolszewski.simplyweather.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
public class FileHelper {

    public static String readJsonFile(Context context, String path) throws Exception {
        InputStream is = context.getAssets().open(path);
        String jsonString = convertStreamToString(is);
        //Make sure you close all streams.
        is.close();
        return jsonString;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
