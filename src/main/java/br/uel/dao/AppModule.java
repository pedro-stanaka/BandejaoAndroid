package br.uel.dao;

import android.content.Context;
import br.uel.service.NetworkService;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Binder;
import com.google.inject.Module;

import java.text.SimpleDateFormat;

public class AppModule implements Module {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Override
    public void configure(Binder binder) {
        SqliteMealDao sqliteMealDao = new SqliteMealDao(context);
        binder.bind(MealDao.class).toInstance(sqliteMealDao);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        binder.bind(RequestQueue.class).toInstance(requestQueue);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        binder.bind(GoogleCloudMessaging.class).toInstance(gcm);

        ObjectMapper mapper = new ObjectMapper();
//        Required for Jackson not consider time zones
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        binder.bind(ObjectMapper.class).toInstance(mapper);
    }
}
