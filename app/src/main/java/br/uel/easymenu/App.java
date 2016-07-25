package br.uel.easymenu;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import net.danlew.android.joda.JodaTimeAndroid;

import br.uel.easymenu.ioc.AppComponent;
import br.uel.easymenu.ioc.AppModule;
import br.uel.easymenu.ioc.DaggerAppComponent;

public class App extends Application {

    public static final String TAG = "meals_app";

    private AppComponent component = defaultComponent(this);

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        JodaTimeAndroid.init(this);
    }

    public void setComponent(AppComponent component) {
        this.component = component;
    }

    public AppComponent component() {
        return component;
    }

    private AppComponent defaultComponent(Context context) {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(context))
                .build();
    }
}
