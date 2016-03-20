package eu.rafalolszewski.simplyweather.views.activities;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

import eu.rafalolszewski.simplyweather.util.CurrentLocationProvider;

/**
 * Created by Rafał Olszewski on 20.03.16.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case CurrentLocationProvider.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    onLocationPermissionsGranted();

                } else {

                    onLocationPermissionsDenied();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    public abstract void onLocationPermissionsGranted();

    public abstract void onLocationPermissionsDenied();

}
