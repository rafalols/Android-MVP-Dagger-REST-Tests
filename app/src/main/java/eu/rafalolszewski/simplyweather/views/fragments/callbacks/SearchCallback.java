package eu.rafalolszewski.simplyweather.views.fragments.callbacks;

import com.google.android.gms.common.api.Status;

import eu.rafalolszewski.simplyweather.model.City;

/**
 * Created by rafal on 05.03.16.
 */
public interface SearchCallback {

    public void onCitySelected(City city);

    public void onError(Status status);

}
