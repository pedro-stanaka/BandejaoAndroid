package br.uel.easymenu.ioc;

import android.content.Context;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.File;
import java.util.concurrent.Executors;

public class MockHttpModule extends AppModule {

    private MockWebServer server;

    private Context context;

    public MockHttpModule(Context context, MockWebServer server) {
        super(context);
        this.server = server;
        this.context = context;
    }

    @Override
    public String provideWeeklyUrl() {
        return this.server.url("/").toString();
    }

    @Override
    public RequestQueue provideRequestQueue() {
        // Robolectric was not dispatching the response
        // The Executors was coupled with the main thread and Robolectric did not run it
        // Based on https://stackoverflow.com/questions/16816600/getting-robolectric-to-work-with-volley
        // TODO: Check why Robolectric was not dispatching the response
        final File cacheDir = new File(context.getCacheDir(), "volley");
        final Network network = new BasicNetwork(new HurlStack());
        final ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());

        final RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network, 4, responseDelivery);
        queue.start();
        return queue;
    }
}
