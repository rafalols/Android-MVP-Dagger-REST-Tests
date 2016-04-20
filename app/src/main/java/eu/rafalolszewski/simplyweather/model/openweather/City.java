package eu.rafalolszewski.simplyweather.model.openweather;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 22.03.16.
 */
@Parcel
public class City {

    public int id;

    public String name;

    public String country;

    @Override
    public boolean equals(Object o) {
        //Check class
        if (!o.getClass().equals(City.class)) return false;
        //Check params
        City objectToCompare = (City) o;
        if (id != objectToCompare.id) return false;
        if (!name.equals(objectToCompare.name)) return false;
        if (!country.equals(objectToCompare.country)) return false;

        return true;
    }

    public boolean isValid() {
        return name != null;
    }
}
