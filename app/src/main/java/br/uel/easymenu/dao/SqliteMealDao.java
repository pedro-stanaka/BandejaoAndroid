package br.uel.easymenu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.tables.MealTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SqliteMealDao extends SqliteDao<Meal> implements MealDao {

    //    The date is stored as a string with this format in the database
    private static String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    /**
     * Most common case.
     * Checks if the date in database in the same week number and the same year of the date query
     * IMPORTANT: Sqlite considers sunday as the last day of week.
     * Therefore, sunday will not have the same week number as the week number of the current week
     */
    private static String QUERY_MIDDLE_OF_YEAR = "strftime('%W'," + MealTable.DATE_MEAL + ") = strftime('%W', ?) " +
            " AND strftime('%Y'," + MealTable.DATE_MEAL + ") = ?";
    /**
     * The SQLite treats the final week of the year as 53 and the first week of the year as 0
     * Checks if the date in the database is between the final week of the PREVIOUS year and the first week of THIS year
     * This is required for date queries of the form yyyy-01-0x
     */
    private static String QUERY_FIRST_WEEK_OF_YEAR = " " + MealTable.DATE_MEAL + " BETWEEN date( ?, 'start of year', 'start of month', 'weekday 6', '-6 day') " +
            " AND date( ? ,'start of year', 'start of month', 'weekday 6')";
    /**
     * Checks if the date in the database is between the final week of the THIS year and the first week of NEXT year
     * This is required for date queries of the form yyyy-12-3x
     */
    private static String QUERY_FINAL_WEEK_OF_YEAR = " " + MealTable.DATE_MEAL + " BETWEEN date( ?, 'start of year', '+11 month', '+30 day', 'weekday 6', '-6 day') " +
            "AND date( ?,'start of year', '+11 month', '+30 day', 'weekday 6')";
    private Context context;

    public SqliteMealDao(Context context) {
        super(context, MealTable.NAME);
        this.context = context;
    }

    @Override
    public long insert(Meal meal) {
        long id = super.insert(meal);
        meal.setId(id);

        SqliteDishDao dishDao = new SqliteDishDao(context);
        for (Dish dish : meal.getDishes()) {
            dish.setMeal(meal);
            long dishId = dishDao.insert(dish);
            dish.setId(dishId);
        }

        return id;
    }

    @Override
    public List<Meal> mealsOfTheWeek(Calendar calendar) {

        String calendarQueryString = dateFormat.format(calendar.getTime());

        String sql = "SELECT * FROM " + MealTable.NAME +
                " WHERE (" + QUERY_MIDDLE_OF_YEAR + ")" +
                " OR (" + QUERY_FIRST_WEEK_OF_YEAR + ")" +
                " OR (" + QUERY_FINAL_WEEK_OF_YEAR + ")" +
                " GROUP BY " + MealTable.DATE_MEAL +
                " ORDER BY " + MealTable.DATE_MEAL;

        String[] params = new String[]{calendarQueryString, calendar.get(Calendar.YEAR) + "",
                calendarQueryString, calendarQueryString, calendarQueryString, calendarQueryString};

        Cursor cursor = database.rawQuery(sql, params);
        return fetchObjectsFromCursor(cursor);
    }

    @Override
    protected void populateValues(ContentValues values, Meal meal) {

        if (meal.getId() != 0)
            values.put(MealTable.ID_MEAL, meal.getId());

        String dateString = dateFormat.format(meal.getDate().getTime());
        values.put(MealTable.DATE_MEAL, dateString);
    }

    @Override
    protected Meal buildObject(Cursor cursor) {
        Meal meal = new Meal();

        meal.setId(getLongFromColumn(MealTable.ID_MEAL, cursor));
        meal.setDate(parseDateToCalendar(cursor));

        SqliteDishDao dishDao = new SqliteDishDao(context);
        List<Dish> dishes = dishDao.findDishesByMealId(meal.getId());
        meal.setDishes(dishes);

        return meal;
    }

    private Calendar parseDateToCalendar(Cursor cursor) {
        Calendar calendar = Calendar.getInstance();

        String dateString = getStringFromColumn(MealTable.DATE_MEAL, cursor);
        try {
            Date date = dateFormat.parse(dateString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

}
