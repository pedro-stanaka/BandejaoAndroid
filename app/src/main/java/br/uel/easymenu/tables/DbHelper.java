package br.uel.easymenu.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance = null;

    private static final String DATABASE_NAME = "cardapio.db";

    private static final int DATABASE_VERSION = 1;

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context) {

        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DishTable.onCreate());
        db.execSQL(MealTable.onCreate());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MealTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DishTable.NAME);

        onCreate(db);
    }
}
