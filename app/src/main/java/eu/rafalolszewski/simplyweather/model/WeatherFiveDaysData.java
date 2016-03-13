package eu.rafalolszewski.simplyweather.model;

import java.util.List;

/**
 * Created by rafal on 12.03.16.
 */
public class WeatherFiveDaysData {

    public City city;
    public java.util.List<List> list;

    public class City{
        public int id;
        public String name;
    }

    public class List{

        public Main main;
        public Wind wind;
        public Clouds clouds;
        public Rain rain;
        public Snow snow;
        public String dt_txt;

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

    }

    @Override
    public String toString() {
        return "name = " + city.name + ", temp = " + list.get(0).main.temp + ", date = " + list.get(0).dt_txt;
    }

}
