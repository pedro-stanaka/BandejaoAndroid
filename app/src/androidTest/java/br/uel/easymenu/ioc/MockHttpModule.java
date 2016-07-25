package br.uel.easymenu.ioc;

import android.content.Context;

import com.squareup.okhttp.mockwebserver.MockWebServer;

public class MockHttpModule extends AppModule {

    private MockWebServer server;

    public MockHttpModule(Context context, MockWebServer server) {
        super(context);
        this.server = server;
    }

    @Override
    public String provideWeeklyUrl() {
        return this.server.url("/").toString();
    }
}
