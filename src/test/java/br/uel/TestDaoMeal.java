package br.uel;

import br.uel.dao.*;

import br.uel.model.Dish;
import br.uel.model.Meal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class TestDaoMeal {

    private MealDao mealDao = new SqliteMealDao(Robolectric.application);

    private Dao<Dish> dishDao = new SqliteDishDao(Robolectric.application);

    @After
    public void closeDatabase() {
        DbHelper.getInstance(Robolectric.application).getWritableDatabase().close();
    }

    @Test
    public void testNumberMealCreation() {
        Meal meal = new Meal();
        meal.setDate(Calendar.getInstance());

        assertThat(mealDao.count(), equalTo(0));

        mealDao.insert(meal);
        mealDao.insert(meal);
        mealDao.insert(meal);

        assertThat(mealDao.count(), equalTo(3));
    }

    @Test
    public void testMealCreationProperties() {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);

        Meal meal = new Meal(Calendar.getInstance());
        long id = mealDao.insert(meal);

        Meal newMeal = mealDao.findById(id);

        assertThat(newMeal.getDate().getTimeInMillis(), equalTo(meal.getDate().getTimeInMillis()));
    }

    @Test
    public void testMealCreationWithDishes() throws Exception {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);
        Meal meal = new Meal(Calendar.getInstance());

        meal.addDish(new Dish("Beans"));
        meal.addDish(new Dish("Rice"));
        meal.addDish(new Dish("Pasta"));

        mealDao.insert(meal);

        assertThat(dishDao.count(), equalTo(3));

        Dish dish = dishDao.findById(2);
        assertThat(dish.getDishName(), equalTo("Rice"));
    }

    @Test
    public void testMealDeletion() throws Exception {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);

        Meal meal = new Meal(Calendar.getInstance());
        long id = mealDao.insert(meal);
        mealDao.delete(id);

        assertThat(mealDao.count(), equalTo(0));
    }

}
