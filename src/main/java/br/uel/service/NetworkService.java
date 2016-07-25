package br.uel.service;

import android.util.Log;
import br.uel.App;
import br.uel.R;
import br.uel.model.Meal;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.inject.Inject;
import roboguice.inject.InjectResource;

import java.util.List;

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
                mealService.persistMeals(meals);

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
