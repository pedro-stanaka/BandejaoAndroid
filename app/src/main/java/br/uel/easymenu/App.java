package br.uel.easymenu;

import android.app.Application;

import br.uel.easymenu.dao.AppModule;
import roboguice.RoboGuice;

public class App extends Application {

    public static final String TAG = "meals_app";

    @Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this), new AppModule(this));
    }
}
