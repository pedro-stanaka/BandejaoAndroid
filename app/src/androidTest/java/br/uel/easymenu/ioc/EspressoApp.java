package br.uel.easymenu.ioc;

import android.content.Context;

import com.squareup.okhttp.mockwebserver.MockWebServer;

import br.uel.easymenu.TestAppRule;
import br.uel.easymenu.TestMenuActivityEspresso;
import dagger.Component;

public class EspressoApp {

    @Component(modules = AppModule.class)
    public interface EspressoComponent extends AppComponent {
        void inject(TestAppRule appRule);
    }

    public static EspressoComponent component(Context context, MockWebServer webServer) {
        return DaggerEspressoApp_EspressoComponent
                .builder()
                .appModule(new MockHttpModule(context, webServer))
                .build();
    }

}
