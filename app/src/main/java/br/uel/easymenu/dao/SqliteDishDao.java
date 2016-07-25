package br.uel.easymenu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.tables.DishTable;

public class SqliteDishDao extends SqliteDao<Dish> implements DishDao {

    public SqliteDishDao(Context context) {
        super(context, DishTable.NAME);
    }

    @Override
    protected void populateValues(ContentValues values, Dish dish) {
        if (dish.getId() != 0)
            values.put(DishTable.ID_DISH, dish.getId());
        values.put(DishTable.DISH_NAME, dish.getDishName());
        values.put(DishTable.MEAL_ID, dish.getMeal().getId());
    }

    @Override
    protected Dish buildObject(Cursor cursor) {
        Dish dish = new Dish();

        dish.setId(getLongFromColumn(DishTable.ID_DISH, cursor));
        dish.setDishName(getStringFromColumn(DishTable.DISH_NAME, cursor));
//        Watch out!!! We are not setting the meal object

        return dish;
    }

    @Override
    public List<Dish> findDishesByMealId(long mealId) {
        String sql = "SELECT * FROM " + DishTable.NAME + " WHERE " + DishTable.MEAL_ID + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{mealId + ""});
        return fetchObjectsFromCursor(cursor);
    }
}
