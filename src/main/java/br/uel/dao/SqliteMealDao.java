package br.uel.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.uel.DbHelper;
import br.uel.model.Meal;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class SqliteMealDao extends SqliteDao<Meal> implements MealDao {

    private Context context;

    public SqliteMealDao(Context context) {
        super(context, DbHelper.TABLE_MEAL);
        this.context = context;
    }

    @Override
    public void insert(Meal meal) {
        super.insert(meal);

        SqliteDishDao dishDao = new SqliteDishDao(context);
        dishDao.insert(meal.getDishes());
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
            values.put(DbHelper.ID_MEAL, meal.getId());

        values.put(DbHelper.DATE_MEAL, meal.getDate().getTimeInMillis());
    }

    @Override
    protected Meal buildObject(Cursor cursor) {
        Meal meal = new Meal();

        meal.setId(getLongFromColumn(DbHelper.ID_MEAL, cursor));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getLongFromColumn(DbHelper.DATE_MEAL, cursor));
        meal.setDate(calendar);

        return meal;
    }

}
