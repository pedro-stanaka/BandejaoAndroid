package br.uel.dao;

import android.content.Context;
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
    }
}
