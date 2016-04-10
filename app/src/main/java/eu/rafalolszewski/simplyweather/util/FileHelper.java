package eu.rafalolszewski.simplyweather.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
public class FileHelper {

    /**
     * Read json from file
     * @param context
     * @param path
     * @return Json as string
     * @throws IOException
     */
    public static String readJsonFile(Context context, String path) throws IOException {
        InputStream is = context.getAssets().open(path);
        String jsonString = convertStreamToString(is);
        is.close();
        return jsonString;
    }

    public static String readJsonFile(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        String jsonString = convertStreamToString(is);
        is.close();
        return jsonString;
    }

    private static String convertStreamToString(InputStream is) throws IOException {
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
