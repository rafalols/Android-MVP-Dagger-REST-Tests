package eu.rafalolszewski.simplyweather.views.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.rafalolszewski.simplyweather.R;

/**
 * Created by rafal on 04.03.16.
 */
public class CurrentCityFooterFragment extends Fragment {

    public CurrentCityFooterFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_footer, container, false);
    }

}
