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
import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.util.CustomMatchers;
import eu.rafalolszewski.simplyweather.util.FileHelper;
import eu.rafalolszewski.simplyweather.util.ImageMapper;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Rafał Olszewski on 21.03.16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RefreshCurrentWeatherTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    MainActivity mainActivity;

    WeatherCurrentData testData;

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
        stringsProvider = applicationComponent.stringsProvider();
        imageMapper = activityComponent.imageMapper();
        mainPresenter = activityComponent.mainPresenter();
    }

    /** refresh view. We must use main thread*/
    private void refreshWeatherView(final WeatherCurrentData testData) throws Throwable {
        mainActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Get to the WeatherViewInterface and refreshCurrentWeather()
                mainPresenter.getViewInterace().refreshCurrentWeather(testData);
            }
        });
    }

    /** Get current weather date from JSON file */
    private WeatherCurrentData getTestWeatherData() throws Exception {
        String currentWeatherJson = FileHelper.readJsonFile(mainActivity, "test_jsons/currentweather.json");
        Gson gson =  new Gson();
        return gson.fromJson(currentWeatherJson, WeatherCurrentData.class);
    }

    @Test
    public void showTemeratureTest() throws Throwable {
        String tempString = stringsProvider.getTempString(testData.measurements.temp);

        onView(withId(R.id.current_temp)).check(matches(withText(tempString)));
    }

    @Test
    public void showPressureTest() throws Throwable{
        String pressureString = stringsProvider.getPressureString(testData.measurements.pressure);

        onView(withId(R.id.current_pressure)).check(matches(withText(pressureString)));
    }

    @Test
    public void showWindTest() {
        String windString = stringsProvider.getWindString(testData.wind.speed, testData.wind.direction);

        onView(withText(windString)).check(matches(isDisplayed()));
    }

    @Test
    public void showImageTest(){
        //get image resource id from weather description
        int imageResourceId = imageMapper.getImageResourceId(testData.weather[0].image);
        //get drawable from image resource id
        Drawable drawable = ContextCompat.getDrawable(mainActivityTestRule.getActivity(), imageResourceId);

        onView(withId(R.id.weather_image)).check(matches(CustomMatchers.isImageTheSame(drawable)));
    }

    @Test
    public void showCitynameTest(){
        onView(withId(R.id.cityname)).check(matches(withText(testData.cityName)));
    }



}
