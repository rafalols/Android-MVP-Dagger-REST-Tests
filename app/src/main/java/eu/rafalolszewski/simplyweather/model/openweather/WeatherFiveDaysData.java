package eu.rafalolszewski.simplyweather.model.openweather;

import java.util.List;

/**
 * Created by rafal on 12.03.16.
 */
public class WeatherFiveDaysData {

    public City city;
    public List<WeatherList> weatherLists;

    @Override
    public String toString() {
        //TODO: Napisać ładną metodę toString()
        return "name = " + city.name + ", temp = " + weatherLists.get(0).measurements.temp + ", date = " + weatherLists.get(0).date;
    }

}
