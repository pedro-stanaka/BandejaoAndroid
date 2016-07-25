package br.uel;

import br.uel.dao.*;

import br.uel.model.Dish;
import br.uel.model.Meal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class TestDaoMeal {

    private MealDao mealDao;

    @Test
    public void testNumberMealCreation() {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);

        Meal meal = new Meal();
        meal.setDate(Calendar.getInstance());

        assertThat(mealDao.fetchAll().size(), equalTo(0));

        mealDao.insert(meal);
        mealDao.insert(meal);
        mealDao.insert(meal);

        assertThat(mealDao.fetchAll().size(), equalTo(3));
    }

    @Test
    public void testMealCreationProperties() {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);

        Calendar calendar = Calendar.getInstance();
        Meal meal =new Meal();
        meal.setId(1);
        meal.setDate(calendar);

        mealDao.insert(meal);

        meal = mealDao.findById(1);

        assertThat(meal.getDate().getTimeInMillis(), equalTo(calendar.getTimeInMillis()));
    }

    @Test
    public void testMealCreationWithDishes() throws Exception {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);
        Meal meal = new Meal(1, Calendar.getInstance());

        meal.addDish(new Dish(1, "Beans"));
        meal.addDish(new Dish(2, "Rice"));
        meal.addDish(new Dish(3, "Pasta"));

        mealDao.insert(meal);

        Dao<Dish> dishDao = new SqliteDishDao(Robolectric.application);
        assertThat(dishDao.fetchAll().size(), equalTo(3));

        Dish dish = dishDao.findById(2);
        assertThat(dish.getDishName(), equalTo("Rice"));
    }

    @Test
    public void testMealDeletion() throws Exception {
        MealDao mealDao = new SqliteMealDao(Robolectric.application);

        Meal meal = new Meal(10, Calendar.getInstance());
        mealDao.insert(meal);
        mealDao.delete(10);

        assertThat(mealDao.fetchAll().size(), equalTo(0));
    }
}
