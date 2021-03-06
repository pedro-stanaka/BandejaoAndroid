package br.uel.easymenu;

import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.rules.ExternalResource;

import javax.inject.Inject;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.ioc.EspressoApp;

public class TestAppRule extends ExternalResource {

    @Inject
    UniversityDao universityDao;
    @Inject
    MealDao mealDao;
    @Inject
    SharedPreferences sharedPreferences;
    private MockWebServer webServer;

    @Override
    protected void before() throws Throwable {
        webServer = new MockWebServer();

        App app = (App) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        EspressoApp.EspressoComponent component = EspressoApp.component(app, webServer);
        app.setComponent(component);
        component.inject(this);

        mealDao.deleteAll();
        universityDao.deleteAll();
        sharedPreferences.edit().clear().commit();

        webServer.start();
    }

    public void enqueueRequest(String file) throws Exception {
        enqueueRequest(200, file);
    }

    public void enqueueRequest(int code, String request) {
        webServer.enqueue(new MockResponse().setResponseCode(code).setBody(request));
    }
}
