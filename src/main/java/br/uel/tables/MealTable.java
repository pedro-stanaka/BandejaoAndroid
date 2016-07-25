package br.uel.tables;

public class MealTable {

    public static final String NAME = "meals";

    public static final String ID_MEAL = "_id";
    public static final String DATE_MEAL = "date";

    private static final String CREATE_TABLE_MEALS = "CREATE TABLE " + NAME + " (" +
            ID_MEAL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE_MEAL + " INTEGER)";

    public static String onCreate() {
        return CREATE_TABLE_MEALS;
    }
}
