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

import java.util.HashMap;
import java.util.Map;

import br.uel.easymenu.App;

public class VolleyNetworkRequest implements NetworkRequest {

    private RequestQueue requestQueue;

    public VolleyNetworkRequest(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void get(String url, final NetworkServiceListener listener) {
        Log.d(App.TAG, "Sending request " + url);
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replaceAll("\\\\", "");
                Log.d(App.TAG, "Receiving response of size " + response.length());
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkEvent.NetworkErrorType errorType = errorMessage(error);
                listener.onError(errorType);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(request);
    }

    private NetworkEvent.NetworkErrorType errorMessage(VolleyError error) {
        NetworkEvent.NetworkErrorType errorType;
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            errorType = NetworkEvent.NetworkErrorType.NO_CONNECTION;
        } else if (error instanceof AuthFailureError) {
            errorType = NetworkEvent.NetworkErrorType.AUTH_ERROR;
        } else if (error instanceof ServerError) {
            errorType = NetworkEvent.NetworkErrorType.SERVER_ERROR;
        } else if (error instanceof NetworkError) {
            errorType = NetworkEvent.NetworkErrorType.GENERIC_ERROR;
        } else if (error instanceof ParseError) {
            errorType = NetworkEvent.NetworkErrorType.PARSE_ERROR;
        } else {
            errorType = NetworkEvent.NetworkErrorType.UNKNOWN_ERROR;
        }
        return errorType;
    }
}
