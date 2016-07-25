package br.uel.easymenu;

import android.app.Application;

import br.uel.easymenu.ioc.AppComponent;
import br.uel.easymenu.ioc.AppModule;
import br.uel.easymenu.ioc.DaggerAppComponent;

public class App extends Application {

    public static final String TAG = "meals_app";

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent component() {
        return appComponent;
    }
}
