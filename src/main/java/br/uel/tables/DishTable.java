package br.uel.tables;

public class DishTable {

    public static final String NAME = "dishes";

    public static final String ID_DISH = "_id";
    public static final String DISH_NAME = "name";
    public static final String MEAL_ID = "meal_id";

    private static final String CREATE_TABLE_DISHES = "CREATE TABLE " + NAME + " (" +
            ID_DISH + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DISH_NAME + " TEXT, " + MEAL_ID + " INTEGER)";

    public static String onCreate() {
        return CREATE_TABLE_DISHES;
    }
}
