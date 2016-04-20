package eu.rafalolszewski.simplyweather.views.activities;

/**
 * Created by Rafał Olszewski on 19.03.16.
 */
public interface MainActivityInterface {

    public void onGoogleApiConnectionFail();

    public void onCantGetGooglePlace();

    public void cantGetCurrentPosition();

}
