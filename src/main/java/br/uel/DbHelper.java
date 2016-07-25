package br.uel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance = null;

    private static final String DATABASE_NAME = "cardapio.db";

    private static final int DATABASE_VERSION = 2;

    //  Table Names
    public static final String TABLE_MEAL = "meals";
    public static final String TABLE_DISH = "dishes";

    //  Meal Columns
    public static final String ID_MEAL = "_id";
    public static final String DATE_MEAL = "date";

    //  Dishes Columns
    public static final String ID_DISH = "_id";
    public static final String DISH_NAME = "name";

    //  Table creation
    private static final String CREATE_TABLE_MEALS = "CREATE TABLE " + TABLE_MEAL + " (" +
            ID_MEAL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE_MEAL + " INTEGER )";

    private static final String CREATE_TABLE_DISHES = "CREATE TABLE " + TABLE_DISH + " (" +
            ID_DISH + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DISH_NAME + " TEXT )";

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context) {

        if(instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEALS);
        db.execSQL(CREATE_TABLE_DISHES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISH);

        onCreate(db);
    }
}
