package eu.rafalolszewski.simplyweather.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Rafał Olszewski on 20.03.16.
 */
public class CurrentLocationProvider {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;

    private Activity activity;
    private LocationManager locationManager;

    public CurrentLocationProvider(Activity activity, LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public double[] getCurrentLatLong() {
        double[] latAndLong = new double[2];

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //When permissions are granted:
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(getCriteria(), false));
            latAndLong[0] = location.getLatitude();
            latAndLong[1] = location.getLongitude();

            return latAndLong;

        }else {
            //When permissions are denied.
            //Request permissions - result is in onRequestPermissionsResult() in BaseActivity
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return null;

        }
    }

    private Criteria getCriteria(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
}
