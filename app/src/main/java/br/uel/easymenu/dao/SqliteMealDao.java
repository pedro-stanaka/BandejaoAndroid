package br.uel.easymenu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.Meal;
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
    public List<Meal> mealsOfTheWeek(java.util.Calendar calendar) {

        String calendarQueryString = CalendarUtils.fromCalendarToString(calendar);

        String sql = "SELECT * FROM " + MealTable.NAME +
                " WHERE (" + SAME_WEEK + ") AND ( " + SAME_YEAR + ")" +
                " GROUP BY " + MealTable.DATE_MEAL + ", " + MealTable.PERIOD +
                " ORDER BY " + MealTable.DATE_MEAL;

        String[] params = new String[]{calendarQueryString, calendarQueryString};

        Cursor cursor = database.rawQuery(sql, params);
        return fetchObjectsFromCursor(cursor);
    }

    @Override
    public GroupedMeals mealsOfTheWeekGroupedByDay(java.util.Calendar calendar) {
        List<Meal> weeklyMeals = mealsOfTheWeek(calendar);
        return new GroupedMeals(weeklyMeals);
    }

    @Override
    protected void populateValues(ContentValues values, Meal meal) {

        if (meal.getId() != 0)
            values.put(MealTable.ID_MEAL, meal.getId());

        String dateString = CalendarUtils.fromCalendarToString(meal.getDate());
        values.put(MealTable.DATE_MEAL, dateString);

        values.put(MealTable.PERIOD, meal.getPeriod());
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

        return meal;
    }
}
