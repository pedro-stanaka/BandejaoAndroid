package br.uel.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.uel.DbHelper;
import br.uel.model.Dish;

public class SqliteDishDao extends SqliteDao<Dish>{

    public SqliteDishDao(Context context) {
        super(context, DbHelper.TABLE_DISH);
    }

    @Override
    protected void populateValues(ContentValues values, Dish dish) {
        if(dish.getId() != 0 )
            values.put(DbHelper.ID_DISH, dish.getId());
        values.put(DbHelper.DISH_NAME, dish.getDishName());
    }

    @Override
    protected Dish buildObject(Cursor cursor) {
        Dish dish = new Dish();

        dish.setId(getLongFromColumn(DbHelper.ID_DISH, cursor));
        dish.setDishName(getStringFromColumn(DbHelper.DISH_NAME, cursor));

        return dish;
    }
}
