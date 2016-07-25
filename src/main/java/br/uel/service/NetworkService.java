package br.uel.service;

import br.uel.dao.MealDao;
import br.uel.model.Meal;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.spothero.volley.JacksonRequest;
import com.spothero.volley.JacksonRequestListener;

import java.util.List;

public class NetworkService {

    @Inject
    private RequestQueue requestQueue;

    @Inject
    private MealDao mealDao;

    public void persistCurrentMealsFromServer(String url, final NetworkServiceListener listener) {
        JacksonRequest request = new JacksonRequest<>(Request.Method.GET, url, new JacksonRequestListener<List<Meal>>() {
            @Override
            public void onResponse(List<Meal> response, int statusCode, VolleyError error) {
                NetworkService.this.mealDao.insert(response);

                if (listener != null) {
                    listener.onSuccess();
                }
            }

            @Override
            public JavaType getReturnType() {
                return new ObjectMapper().getTypeFactory().
                        constructCollectionType(List.class, Meal.class);
            }
        });

        requestQueue.add(request);
    }

    public void persistCurrentMealsFromServer(String url) {
        this.persistCurrentMealsFromServer(url, null);
    }

    public interface NetworkServiceListener {

        public abstract void onSuccess();

        public abstract void onError(String errorMessage);
    }

}
