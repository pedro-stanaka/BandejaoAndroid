package br.uel.easymenu.tables;

public class MealTable {

    public static final String NAME = "meals";

    public static final String ID_MEAL = "_id";
    public static final String DATE_MEAL = "date_meal";
    public static final String PERIOD = "period";
    public static final String UNIVERSITY_ID = "university_id";

    private static final String CREATE_TABLE_MEALS = "CREATE TABLE " + NAME + " (" +
            ID_MEAL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + UNIVERSITY_ID + " INTEGER, " +
            DATE_MEAL + " TEXT, " + PERIOD + " TEXT ," +
            "UNIQUE(" + DATE_MEAL + ", " + PERIOD + ") ON CONFLICT REPLACE)";

    public static String onCreate() {
        return CREATE_TABLE_MEALS;
    }
}
