package br.uel.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.uel.DbHelper;
import br.uel.model.Meal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Calendar;
import java.util.List;

public class SqliteMealDao extends SqliteDao<Meal> implements MealDao {

    public SqliteMealDao(Context context) {
        super(context);
    }

    @Override
    public List<Meal> mealsOfTheWeek(Calendar calendar) {
        throw new NotImplementedException();
    }

    @Override
    public List<Meal> mealsOfThisWeek() {
        throw new NotImplementedException();
    }

    @Override
    protected void populateValues(ContentValues values, Meal meal) {
//      Check if it is inserting
        if(meal.getId() != 0) {
            values.put(DbHelper.ID_MEAL, meal.getId());
        }
        values.put(DbHelper.DATE, meal.getDate().toString());
    }

    @Override
    protected String getTableName() {
        return DbHelper.TABLE_MEAL;
    }

    @Override
    protected Meal buildObject(Cursor cursor) {
        Meal meal = new Meal();
        meal.setId(cursor.getLong(cursor.getColumnIndex(DbHelper.ID_MEAL)));

        //TODO Calendar
//        meal.setCalenda(cursor.getLong(cursor.getColumnIndex(DbHelper.ID_MEAL)));
        meal.setDate(Calendar.getInstance());
        return meal;
    }

}
