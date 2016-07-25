package br.uel.easymenu.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.inject.Inject;

import java.util.List;

import br.uel.easymenu.App;
import br.uel.easymenu.R;
import br.uel.easymenu.model.Meal;
import roboguice.inject.InjectResource;

public class NetworkService {

    @Inject
    private RequestQueue requestQueue;

    @InjectResource(R.string.ip)
    private String ip;

    @InjectResource(R.string.url_current_meal)
    private String currentMealUrl;

    @Inject
    private MealService mealService;

    public void persistCurrentMealsFromServer(final NetworkServiceListener listener) {
        String url = ip + currentMealUrl;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                List<Meal> meals = mealService.deserializeMeal(response);
                mealService.replaceMealsFromCurrentWeek(meals);

                if (listener != null) {
                    listener.onSuccess();
                }
                Log.i(App.TAG, "Received meals: " + meals);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(App.TAG, "Error: " + error.getLocalizedMessage());
            }
        });

        requestQueue.add(request);
    }

    public void persistCurrentMealsFromServer() {
        this.persistCurrentMealsFromServer(null);
    }

    public interface NetworkServiceListener {

        public abstract void onSuccess();

        public abstract void onError(String errorMessage);
    }

}
