package eu.rafalolszewski.simplyweather.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.rafalolszewski.simplyweather.R;
import eu.rafalolszewski.simplyweather.model.CityMapper;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import eu.rafalolszewski.simplyweather.views.fragments.callbacks.SearchCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {


    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "SearchFragment";
    private SearchCallback callback;


    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint(getString(R.string.autocomplete_hint));

        autocompleteFragment.setFilter(createAutocompleteFilter());

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListenerImp());

        return view;
    }

    @OnClick(R.id.button_position)
    public void onPositionClick(){
        Log.d(TAG, "onPositionClick: CLICK!");
    }

    @OnClick(R.id.button_favorite)
    public void onFavoriteClick(){
        Log.d(TAG, "onFavoriteClick: CLICK!");
    }


    @NonNull
    private AutocompleteFilter createAutocompleteFilter() {
        // Create filter to search only cities
        return new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
    }


    private class PlaceSelectionListenerImp implements PlaceSelectionListener {

        @Override
        public void onPlaceSelected(Place place) {
            Log.d(TAG, "onPlaceSelected: place:" + place.getName() + "  log, lat:" + place.getLatLng().toString());
            callback.onCitySelected(CityMapper.plateToCity(place));
        }

        @Override
        public void onError(Status status) {
            Log.d(TAG, "onActivityResult: Result error " + status.getStatusMessage());
            callback.onError(status);
        }
    }

    @Override
    public void onAttach(Context context) {
        callback = (MainActivity) context;
    }

}
