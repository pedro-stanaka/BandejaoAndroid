package br.uel.easymenu.service;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.uel.easymenu.App;
import br.uel.easymenu.model.Meal;

import static br.uel.easymenu.service.NetworkEvent.NetworkErrorType;

public class NetworkService {

    private RequestQueue requestQueue;

    private String urlWeeklyMeals;

    private MealService mealService;

    private EventBus eventBus;

    @Inject
    public NetworkService(RequestQueue queue,
                          @Named("url.weekly_meals") String urlWeeklyMeals,
                          MealService mealService,
                          EventBus eventBus) {
        this.requestQueue = queue;
        this.urlWeeklyMeals = urlWeeklyMeals;
        this.mealService = mealService;
        this.eventBus = eventBus;
    }

    public void persistCurrentMealsFromServer(final NetworkServiceListener listener) {

        StringRequest request = new StringRequest(Request.Method.GET, urlWeeklyMeals, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                List<Meal> meals = mealService.deserializeMeal(response);
                mealService.replaceMealsFromCurrentWeek(meals);

                if (listener != null) {
                    listener.onSuccess();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkErrorType errorType = errorMessage(error);

                if (listener != null) {
                    listener.onError(errorType);
                }
                NetworkEvent event = new NetworkEvent(errorType);
                eventBus.post(event);
                Log.e(App.TAG, "Error: " + errorType + "");
            }
        });

        requestQueue.add(request);
    }


    private NetworkErrorType errorMessage(VolleyError error) {
        NetworkErrorType errorType;
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            errorType = NetworkErrorType.NO_CONNECTION;
        } else if (error instanceof AuthFailureError) {
            errorType = NetworkErrorType.AUTH_ERROR;
        } else if (error instanceof ServerError) {
            errorType = NetworkErrorType.SERVER_ERROR;
        } else if (error instanceof NetworkError) {
            errorType = NetworkErrorType.GENERIC_ERROR;
        } else if (error instanceof ParseError) {
            errorType = NetworkErrorType.PARSE_ERROR;
        } else {
            errorType = NetworkErrorType.UNKNOWN_ERROR;
        }
        return errorType;
    }

    public void persistCurrentMealsFromServer() {
        this.persistCurrentMealsFromServer(null);
    }

    public interface NetworkServiceListener {

        public abstract void onSuccess();

        public abstract void onError(NetworkErrorType error);
    }

}
