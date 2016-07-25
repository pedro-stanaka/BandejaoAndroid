package br.uel.easymenu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.uel.easymenu.App;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;
import br.uel.easymenu.tables.MealTable;
import br.uel.easymenu.utils.CalendarUtils;

public class SqliteMealDao extends SqliteDao<Meal> implements MealDao {

    private static String DEFAULT_FILTER = "'+1 day',  'weekday 0', '-7 day'";

    private static String SAME_YEAR = "strftime('%Y', date(?, " + DEFAULT_FILTER + ")) = " +
            "strftime('%Y', date(" + MealTable.DATE_MEAL + ", " + DEFAULT_FILTER + "))";

    private static String SAME_WEEK = "strftime('%W', date(?, " + DEFAULT_FILTER + ")) = " +
            "strftime('%W', date(" + MealTable.DATE_MEAL + ", " + DEFAULT_FILTER + "))";

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
    public List<Meal> mealsOfTheWeek(DateTime dateTime, University university) {
        // Avoid propagating null
        if (university == null) {
            return new ArrayList<>();
        }

        String calendarQueryString = CalendarUtils.fromCalendarToString(dateTime);

        String sql = "SELECT * FROM " + MealTable.NAME +
                " WHERE (" + SAME_WEEK + ") AND ( " + SAME_YEAR + ")" +
                " AND " + MealTable.UNIVERSITY_ID + " = ? " +
                " GROUP BY " + MealTable.DATE_MEAL + ", " + MealTable.PERIOD;

        String[] params = new String[]{calendarQueryString, calendarQueryString, university.getId() + ""};

        Cursor cursor = database.rawQuery(sql, params);
        List<Meal> meals = fetchObjectsFromCursor(cursor);

        // I know this should be in a service layer.
        // But we would be adding complexity if we add another layer to the system
        // Also, the clause to ORDER BY period would be more complicated than to sort the objects in memory
        Collections.sort(meals);

        return meals;
    }

    @Override
    public GroupedMeals mealsOfTheWeekGroupedByDay(DateTime dateTime, University university) {
        List<Meal> weeklyMeals = mealsOfTheWeek(dateTime, university);
        return new GroupedMeals(weeklyMeals);
    }

    @Override
    protected void populateValues(ContentValues values, Meal meal) {

        if (meal.getId() != 0)
            values.put(MealTable.ID_MEAL, meal.getId());

        String dateString = CalendarUtils.fromCalendarToString(meal.getDate());
        values.put(MealTable.DATE_MEAL, dateString);

        values.put(MealTable.PERIOD, meal.getPeriod());

        // Non persisted university
        if (meal.getUniversity().getId() != 0)
            values.put(MealTable.UNIVERSITY_ID, meal.getUniversity().getId());
        else
            Log.w(App.TAG, "Non persisted university " + meal.getUniversity());
    }

    @Override
    protected Meal buildObject(Cursor cursor) {
        Meal meal = new Meal();

        meal.setId(getLongFromColumn(MealTable.ID_MEAL, cursor));
        String calendarString = getStringFromColumn(MealTable.DATE_MEAL, cursor);
        meal.setDate(CalendarUtils.fromStringToCalendar(calendarString));
        meal.setPeriod(getStringFromColumn(MealTable.PERIOD, cursor));

        SqliteDishDao dishDao = new SqliteDishDao(context);
        List<Dish> dishes = dishDao.findDishesByMealId(meal.getId());
        meal.setDishes(dishes);

        // Watch out! Don't call meal.getUniversity because it will be null
        return meal;
    }
}
