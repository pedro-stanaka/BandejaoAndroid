package br.uel.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.uel.tables.DbHelper;
import br.uel.model.Dish;
import br.uel.model.Meal;
import br.uel.tables.MealTable;

import java.util.Calendar;
import java.util.List;

public class SqliteMealDao extends SqliteDao<Meal> implements MealDao {

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
        throw new RuntimeException("Método não implementado");
    }

    @Override
    public List<Meal> mealsOfThisWeek() {
        throw new RuntimeException("Método não implementado");
    }

    @Override
    protected void populateValues(ContentValues values, Meal meal) {

        if (meal.getId() != 0)
            values.put(MealTable.ID_MEAL, meal.getId());

        values.put(MealTable.DATE_MEAL, meal.getDate().getTimeInMillis());
    }

    @Override
    protected Meal buildObject(Cursor cursor) {
        Meal meal = new Meal();

        meal.setId(getLongFromColumn(MealTable.ID_MEAL, cursor));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getLongFromColumn(MealTable.DATE_MEAL, cursor));
        meal.setDate(calendar);

        SqliteDishDao dishDao = new SqliteDishDao(context);
        List<Dish> dishes = dishDao.findDishesByMealId(meal.getId());
        meal.setDishes(dishes);

        return meal;
    }

}
