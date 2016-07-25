package br.uel.easymenu.ioc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Named;

import br.uel.easymenu.R;
import br.uel.easymenu.dao.DishDao;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.dao.SqliteDishDao;
import br.uel.easymenu.dao.SqliteMealDao;
import br.uel.easymenu.dao.SqliteUniversityDao;
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.service.MealService;
import br.uel.easymenu.service.NetworkService;
import br.uel.easymenu.utils.CalendarUtils;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides public MealDao provideMealDao() {
       return new SqliteMealDao(this.context);
    }

    @Provides public DishDao provideDishDao() {
       return new SqliteDishDao(this.context);
    }

    @Provides public UniversityDao provideUniversityDao() {
        return new SqliteUniversityDao(this.context);
    }

    @Provides public GoogleCloudMessaging provideGcm() {
        return GoogleCloudMessaging.getInstance(this.context);
    }

    @Provides public ObjectMapper provideObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(CalendarUtils.SDF);
        return mapper;
    }

    @Provides public RequestQueue provideRequestQueue() {
        return Volley.newRequestQueue(context);
    }

    @Provides public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides @Named("url.weekly_meals") public String provideWeeklyUrl() {
        String ip = context.getResources().getString(R.string.ip);
        String weeklyMealsUrl = context.getResources().getString(R.string.url_current_meal);
        String university_name = context.getResources().getString(R.string.university_name);
        String url = String.format(weeklyMealsUrl, university_name);
        return ip + url;
    }

    @Provides public NetworkService provideNetworkService(RequestQueue queue,
                                                          @Named("url.weekly_meals") String urlWeeklyMeals,
                                                          MealService mealSevice,
                                                          EventBus eventBus) {
        return new NetworkService(queue, urlWeeklyMeals, mealSevice, eventBus);
    }

    @Provides public MealService provideMealService(ObjectMapper mapper, MealDao mealDao, EventBus eventBus, UniversityDao universityDao) {
        return new MealService(mapper, mealDao, eventBus, universityDao);
    }

    @Provides public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
