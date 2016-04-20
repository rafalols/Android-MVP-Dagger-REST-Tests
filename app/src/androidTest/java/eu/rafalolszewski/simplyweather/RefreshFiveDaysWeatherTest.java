package eu.rafalolszewski.simplyweather;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.rafalolszewski.simplyweather.dagger.components.ActivityComponent;
import eu.rafalolszewski.simplyweather.dagger.components.ApplicationComponent;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherFiveDaysData;
import eu.rafalolszewski.simplyweather.model.openweather.WeatherList;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.util.CustomMatchers;
import eu.rafalolszewski.simplyweather.util.FileHelper;
import eu.rafalolszewski.simplyweather.util.ImageMapper;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by Rafał Olszewski on 02.04.16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RefreshFiveDaysWeatherTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    MainActivity mainActivity;

    WeatherFiveDaysData testData;

    SharedPreferences sharedPreferences;

    StringsProvider stringsProvider;

    ImageMapper imageMapper;

    MainPresenter mainPresenter;

    @Before
    public void setupTest() throws Throwable {
        mainActivity = mainActivityTestRule.getActivity();
        injectDependecies();
        testData = getTestWeatherData();
        refreshWeatherView(testData);
    }

    private void injectDependecies(){
        ApplicationComponent applicationComponent =  ((SimplyWeatherApp)mainActivityTestRule.getActivity().getApplication()).getApplicationComponent();
        ActivityComponent activityComponent = mainActivityTestRule.getActivity().getActivityComponent();
        sharedPreferences = applicationComponent.sharedPreferences();
        stringsProvider = activityComponent.stringsProvider();
        imageMapper = activityComponent.imageMapper();
        mainPresenter = activityComponent.mainPresenter();
    }

    /** Get current weather date from JSON file */
    private WeatherFiveDaysData getTestWeatherData() throws Exception {
        String weatherJson = FileHelper.readJsonFile(mainActivity, "test_jsons/fivedaysweather.json");
        Gson gson =  new Gson();
        return gson.fromJson(weatherJson, WeatherFiveDaysData.class);
    }

    /** refresh view. We must use main thread*/
    private void refreshWeatherView(final WeatherFiveDaysData testData) throws Throwable {
        mainActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Get to the WeatherFragmentInterface and refreshCurrentWeather()
                mainPresenter.getViewInterace().refreshFiveDaysWeather(testData);
            }
        });
    }

    @Test
    public void checkNumberOfItems(){
        int lastItem = testData.weatherLists.length - 1;

        onData(instanceOf(WeatherList.class)).inAdapterView(withId(R.id.listview))
                .atPosition(lastItem).check(matches(isDisplayed()));
    }

    @Test
    public void checkValidData(){
        for (int i = 0; i < testData.weatherLists.length; i++){
            WeatherList weather = testData.weatherLists[i];
            checkDate(i, weather);
            checkImage(i, weather);
            checkWind(i, weather);
            checkTemp(i, weather);
        }
    }

    private void checkTemp(int i, WeatherList weather) {
        onData(instanceOf(WeatherList.class)).inAdapterView(withId(R.id.listview))
                .atPosition(i)
                .onChildView(withId(R.id.temp))
                .check(matches(withText(stringsProvider.getTempString(weather.measurements.temp))));
    }

    private void checkWind(int i, WeatherList weather) {
        onData(instanceOf(WeatherList.class)).inAdapterView(withId(R.id.listview))
                .atPosition(i)
                .onChildView(withId(R.id.small_value))
                .check(matches(withText(stringsProvider.getWindString(weather.wind.speed, weather.wind.direction))));
    }

    private void checkImage(int i,WeatherList weather) {
        //get image resource id from weather description
        int imageResourceId = imageMapper.getImageResourceId(weather.weather[0].image);
        //get drawable from image resource id
        Drawable drawable = ContextCompat.getDrawable(mainActivityTestRule.getActivity(), imageResourceId);

        onData(instanceOf(WeatherList.class)).inAdapterView(withId(R.id.listview))
                .atPosition(i)
                .onChildView(withId(R.id.image))
                .check(matches(CustomMatchers.isImageTheSame(drawable)));
    }

    private void checkDate(int i, WeatherList weather) {
        onData(instanceOf(WeatherList.class)).inAdapterView(withId(R.id.listview))
                .atPosition(i)
                .onChildView(withId(R.id.hour))
                .check(matches(withText(stringsProvider.getHour(weather.date))));
    }

}
