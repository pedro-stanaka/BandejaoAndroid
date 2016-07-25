package br.uel.easymenu.tables;

public class UniversityTable {

    public static final String NAME = "universities";

    public static final String ID_UNIVERSITY = "_id";
    public static final String UNIVERSITY_NAME = "name";
    public static final String FULL_NAME = "full_name";
    public static final String WEBSITE = "website";

    private static final String CREATE_TABLE_MEALS = "CREATE TABLE " + NAME + " (" +
            ID_UNIVERSITY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UNIVERSITY_NAME + " TEXT, " + FULL_NAME + " TEXT, " +
            WEBSITE + " TEXT, " +
            " UNIQUE(" + UNIVERSITY_NAME + "))";

    public static String onCreate() {
        return CREATE_TABLE_MEALS;
    }
}
