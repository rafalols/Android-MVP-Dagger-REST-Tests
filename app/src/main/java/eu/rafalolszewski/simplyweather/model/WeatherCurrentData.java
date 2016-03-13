package eu.rafalolszewski.simplyweather.model;

/**
 * Created by rafal on 12.03.16.
 */
public class WeatherCurrentData {

    public String name;
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public Rain rain;
    public Snow snow;
    public long dt;

    /** CONSTRUCTOR*/
    public WeatherCurrentData(){
    }

    /** INNER CLASSES */
    public class Main{
        public float temp;
        public float pressure;
        public float humidity;
        public float temp_min;
        public float temp_max;
    }

    public class Wind{
        public float speed;
        public float deg;
    }

    public class Clouds{
        public float all;
    }

    public class Rain{
        public float _3h;
    }

    public class Snow{
        public float _3h;
    }

    @Override
    public String toString() {
        return "name = " + name + ", temp = " + main.temp;
    }
}
