package br.uel;

import br.uel.dao.*;

import br.uel.model.Dish;
import br.uel.model.Meal;
import br.uel.tables.DbHelper;
import org.junit.After;
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

    private DishDao dishDao = new SqliteDishDao(Robolectric.application);

    @After
    public void closeDatabase() {
        DbHelper.getInstance(Robolectric.application).getWritableDatabase().close();
    }

    @Test
    public void testNumberMealCreation() {
        Meal meal1 = new Meal();
        meal1.setDate(Calendar.getInstance());

        Meal meal2 = new Meal();
        meal2.setDate(Calendar.getInstance());

        Meal meal3 = new Meal();
        meal3.setDate(Calendar.getInstance());

        assertThat(mealDao.count(), equalTo(0));

        mealDao.insert(meal1);
        mealDao.insert(meal2);
        mealDao.insert(meal3);

        assertThat(mealDao.count(), equalTo(3));
    }

    @Test
    public void testMealCreationProperties() {
        Meal meal = new Meal(Calendar.getInstance());
        long id = mealDao.insert(meal);

        Meal newMeal = mealDao.findById(id);

        assertThat(newMeal.getDate().getTimeInMillis(), equalTo(meal.getDate().getTimeInMillis()));
    }

    @Test
    public void testMealCreationWithDishes() throws Exception {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);
        Meal meal = new Meal(Calendar.getInstance());

        Dish dish1 = new Dish("Beans");
        Dish dish2 = new Dish("Rice");
        Dish dish3 = new Dish("Pasta");

        meal.addDish(dish1);
        meal.addDish(dish2);
        meal.addDish(dish3);

        long mealId = mealDao.insert(meal);

        assertThat(dishDao.count(), equalTo(3));

        Dish dish = dishDao.findById(dish2.getId());
        assertThat(dish.getDishName(), equalTo("Rice"));

        List<Dish> dishes = dishDao.findDishesByMealId(mealId);
        assertThat(dishes.get(1).getDishName(), equalTo("Rice"));

        Meal newMeal = mealDao.findById(mealId);
        assertThat(newMeal.getDishes().get(1).getDishName(), equalTo("Rice"));
    }

    @Test
    public void testMealDeletion() throws Exception {
        Meal meal = new Meal(Calendar.getInstance());
        long id = mealDao.insert(meal);
        mealDao.delete(id);

        assertThat(mealDao.count(), equalTo(0));
    }

}
