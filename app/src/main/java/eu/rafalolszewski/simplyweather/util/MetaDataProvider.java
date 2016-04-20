package eu.rafalolszewski.simplyweather.util;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Rafał Olszewski on 17.04.16.
 */
public class MetaDataProvider {


    /**
     * get String from AndroidManifest metadata
     */
    public static String getMetaDataStringFromKey(Application application, String key){
        try{
            ApplicationInfo appInfo = application.getPackageManager().getApplicationInfo(
                    application.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(key);
            }else {
                return null;
            }
        }catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
