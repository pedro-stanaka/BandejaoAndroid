package br.uel.dao;

import android.content.Context;
import br.uel.service.NetworkService;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.inject.Binder;
import com.google.inject.Module;

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
    }
}
