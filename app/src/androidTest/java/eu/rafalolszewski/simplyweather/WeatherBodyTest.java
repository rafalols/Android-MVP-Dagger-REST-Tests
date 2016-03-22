package eu.rafalolszewski.simplyweather;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;

/**
 * Created by Rafał Olszewski on 21.03.16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class WeatherBodyTest {

    @Rule
    ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    MainPresenter mainPresenter;

    @Before
    public void setupTests(){
        mainPresenter = mainActivityTestRule.getActivity().activityComponent.mainPresenter();
    }

    @Test
    public void getWeatherData(){

//        mainPresenter.onGetCurrentWeather();

    }


}
