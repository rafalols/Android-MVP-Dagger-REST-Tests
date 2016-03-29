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

import eu.rafalolszewski.simplyweather.model.openweather.WeatherCurrentData;
import eu.rafalolszewski.simplyweather.util.CustomMatchers;
import eu.rafalolszewski.simplyweather.util.FileHelper;
import eu.rafalolszewski.simplyweather.util.ImageMapper;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;
import eu.rafalolszewski.simplyweather.views.activities.SettingsActivity;

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

    WeatherCurrentData testData;

    SharedPreferences sharedPreferences;

    MainActivity mainActivity;

    @Before
    public void setupTest() throws Throwable {
        mainActivity = mainActivityTestRule.getActivity();
        sharedPreferences = ((SimplyWeatherApp)mainActivityTestRule.getActivity().getApplication()).getApplicationComponent().sharedPreferences();
        testData = getTestCurrentWeather();
        refreshWeatherView(testData);
    }

    /** refresh view. We must use main thread*/
    private void refreshWeatherView(final WeatherCurrentData testData) throws Throwable {
        mainActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Get to the WeatherViewInterface and refreshCurrentWeather()
                mainActivity.activityComponent.mainPresenter().getViewInterace().refreshCurrentWeather(testData);
            }
        });
    }

    /** Get current weather date from JSON file */
    private WeatherCurrentData getTestCurrentWeather() throws Exception {
        String currentWeatherJson = FileHelper.readJsonFile(mainActivity, "test_jsons/currentweather.json");
        Gson gson =  new Gson();
        WeatherCurrentData weatherCurrentData = gson.fromJson(currentWeatherJson, WeatherCurrentData.class);
        return weatherCurrentData;
    }

    @Test
    public void showTemeratureTest() throws Throwable {
        String tempUnit = sharedPreferences.getString(SettingsActivity.TEMP_UNIT_KEY, SettingsActivity.TEMP_UNIT_DEFAULT);
        String tempString = StringsProvider.getTempString(testData.measurements.temp, tempUnit);

        onView(withId(R.id.current_temp)).check(matches(withText(tempString)));
    }

    @Test
    public void showFromToTemperatureTest() throws Throwable {
        String tempUnit = sharedPreferences.getString(SettingsActivity.TEMP_UNIT_KEY, SettingsActivity.TEMP_UNIT_DEFAULT);
        String tempMinMaxString = StringsProvider.getTempMinMaxString(testData.measurements.temp_min, testData.measurements.temp_max, tempUnit);

        onView(withId(R.id.current_min_max)).check(matches(withText(tempMinMaxString)));
    }

    @Test
    public void showWindTest() {
        String windUnit = sharedPreferences.getString(SettingsActivity.SPEED_UNIT_KEY, SettingsActivity.SPEED_UNIT_DEFAULT);
        String windString = StringsProvider.getWindString(testData.wind.speed, testData.wind.direction, windUnit);

        onView(withText(windString)).check(matches(isDisplayed()));
    }

    @Test
    public void showImageTest(){
        //get image resource id from weather description
        int imageResourceId = ImageMapper.getImageResourceId(testData.weather[0].image, mainActivityTestRule.getActivity());
        //get drawable from image resource id
        Drawable drawable = ContextCompat.getDrawable(mainActivityTestRule.getActivity(), imageResourceId);

        onView(withId(R.id.weather_image)).check(matches(CustomMatchers.isImageTheSame(drawable)));
    }

}
