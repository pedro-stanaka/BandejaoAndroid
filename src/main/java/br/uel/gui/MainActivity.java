package br.uel.gui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.uel.R;
import br.uel.model.Meal;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.spothero.volley.JacksonRequest;
import com.spothero.volley.JacksonRequestListener;
import roboguice.activity.RoboActivity;

import java.util.List;

public class MainActivity extends RoboActivity {

    private static final String URL_MEALS = "http://cardapioru.apiary.io/meals/current_week";
    @Inject
    private SharedPreferences sharedPreferences;

    private final static String IS_FIRST_LAUNCH = "first_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        requestQueue.add(new JacksonRequest<>(Request.Method.GET, URL_MEALS, new JacksonRequestListener<List<Meal>>() {
            @Override
            public void onResponse(List<Meal> response, int statusCode, VolleyError error) {
                Log.d("OLA", statusCode+"");
                Toast.makeText(MainActivity.this, response.get(0).getDishes().get(0).getDishName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public JavaType getReturnType() {
                return new ObjectMapper().getTypeFactory().
                        constructCollectionType(List.class, Meal.class);
            }
        }));
//        Checar se a base de dados está populada
//        Se não estiver populada, enviar uma requsição para o servidor
//        Depois checar se o registration_id está tudo certo
//        Se não estiver, requisitar para o google e enviar para o servidor
//        Lembrar de checar se a versão está tudo certo

    }
}
