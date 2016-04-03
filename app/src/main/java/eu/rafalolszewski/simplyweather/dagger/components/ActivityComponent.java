package eu.rafalolszewski.simplyweather.dagger.components;

import dagger.Component;
import eu.rafalolszewski.simplyweather.dagger.modules.ActivityModule;
import eu.rafalolszewski.simplyweather.dagger.scopes.PerActivity;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.util.ImageMapper;
import eu.rafalolszewski.simplyweather.util.StringsProvider;
import eu.rafalolszewski.simplyweather.views.fragments.WeatherBodyFragment;
import eu.rafalolszewski.simplyweather.views.list_adapter.FiveDaysWeatherListAdapter;

/**
 * Created by Rafał Olszewski on 18.03.16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent{

    MainPresenter mainPresenter();

    ImageMapper imageMapper();

    StringsProvider stringsProvider();

    FiveDaysWeatherListAdapter fiveDaysWeatherListAdapter();

    void inject(WeatherBodyFragment weatherBodyFragment);

}
