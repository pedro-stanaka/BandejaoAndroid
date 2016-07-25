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
import br.uel.easymenu.service.DefaultResponseHandler;
import br.uel.easymenu.service.JacksonSerializer;
import br.uel.easymenu.service.MealService;
import br.uel.easymenu.service.NetworkRequest;
import br.uel.easymenu.service.Serializer;
import br.uel.easymenu.service.UniversityService;
import br.uel.easymenu.service.VolleyNetworkRequest;
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

    @Provides public RequestQueue provideRequestQueue() {
        return Volley.newRequestQueue(context);
    }

    @Provides public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides @Named("url.weekly_meals") public String provideWeeklyUrl() {
        String url = context.getString(R.string.url_weekly_meal);
        return returnUrl(url);
    }

    @Provides @Named("url.weekly_universities") public String provideUniversitiesUrl() {
        String url = context.getString(R.string.url_weekly_university);
        return returnUrl(url);
    }

    private String returnUrl(String url) {
        String ip = context.getString(R.string.ip);
        String university_name = context.getString(R.string.university_name);
        String uri = String.format(url, university_name);
        return ip + uri;
    }

    @Provides public MealService provideMealService(@Named("url.weekly_meals") String mealsUrl,
                                                    MealDao mealDao,
                                                    DefaultResponseHandler handler) {
        return new MealService(mealsUrl, mealDao, handler);
    }

    @Provides public UniversityService provideUniversityService(@Named("url.weekly_universities") String universitiesUrl,
                                                                UniversityDao universityDao,
                                                                MealService mealService,
                                                                DefaultResponseHandler handler) {
        return new UniversityService(universitiesUrl, universityDao, mealService, handler);
    }

    @Provides public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides public DefaultResponseHandler provideDefaultResponseHandler(Serializer serializer,
                                                                          EventBus eventBus,
                                                                          NetworkRequest networkRequest) {
        return new DefaultResponseHandler(serializer, eventBus, networkRequest);
    }

    @Provides public NetworkRequest provideNetworkRequest(RequestQueue requestQueue) {
        return new VolleyNetworkRequest(requestQueue);
    }

    @Provides public Serializer provideSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(CalendarUtils.SDF);
        return new JacksonSerializer(mapper);
    }
}
