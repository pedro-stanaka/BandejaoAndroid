package br.uel.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.inject.Inject;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

public class MainActivity extends RoboActivity {

    private static final String URL_MEALS = "http://cardapioru.apiary.io/meals/current_week";

    @Inject
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RoboGuice.getInjector(this).injectMembers(this);
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

//        Checar se a base de dados está populada
//        Se não estiver populada, enviar uma requsição para o servidor
//        Depois checar se o registration_id está tudo certo
//        Se não estiver, requisitar para o google e enviar para o servidor
//        Lembrar de checar se a versão está tudo certo

    }
}
