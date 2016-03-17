package eu.rafalolszewski.simplyweather.dagger.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import eu.rafalolszewski.simplyweather.dagger.scopes.PerActivity;
import eu.rafalolszewski.simplyweather.presenter.MainPresenter;
import eu.rafalolszewski.simplyweather.views.activities.MainActivity;

/**
 * Created by rafal on 17.03.16.
 */
@Module
public class ActivityModule {

    private final MainActivity mainActivity;

    public ActivityModule(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Provides
    @PerActivity
    Activity providesMainActivity(){
        return mainActivity;
    }



    @Provides
    @PerActivity
    MainPresenter providesMainPresenter(){
        return new MainPresenter();
    }

}
