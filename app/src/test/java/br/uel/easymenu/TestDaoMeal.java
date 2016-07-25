package br.uel.easymenu;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.uel.easymenu.dao.DishDao;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.dao.SqliteDishDao;
import br.uel.easymenu.dao.SqliteMealDao;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.tables.DbHelper;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestDaoMeal {

    private MealDao mealDao = new SqliteMealDao(RuntimeEnvironment.application);

    private DishDao dishDao = new SqliteDishDao(RuntimeEnvironment.application);

    @After
    public void closeDatabase() {
        DbHelper.getInstance(RuntimeEnvironment.application).getWritableDatabase().close();
    }

    @Test
    public void testNumberMealCreation() {
        assertThat(mealDao.count(), equalTo(0));

        MealBuilder builder = new MealBuilder();
        mealDao.insert(builder.withPeriod(Meal.LUNCH).build());
        mealDao.insert(builder.withPeriod(Meal.BREAKFAST).build());
        mealDao.insert(builder.withPeriod(Meal.DINNER).build());

        assertThat(mealDao.count(), equalTo(3));
    }

    @Test
    public void testReplaceSameDateAndPeriod() throws Exception {
        MealBuilder builder = new MealBuilder();

        mealDao.insert(builder.withPeriod(Meal.LUNCH).build());
        mealDao.insert(builder.withPeriod(Meal.LUNCH).build());
        mealDao.insert(builder.withPeriod(Meal.LUNCH).build());

        assertThat(mealDao.count(), equalTo(1));
    }

    @Test
    public void testMealCreationProperties() {
        Meal meal = new MealBuilder().withCalendar(Calendar.getInstance()).withPeriod(Meal.LUNCH).build();
        long id = mealDao.insert(meal);

        Meal newMeal = mealDao.findById(id);

        assertThat(newMeal.getDate().get(Calendar.DAY_OF_YEAR), equalTo(meal.getDate().get(Calendar.DAY_OF_YEAR)));
        assertThat(newMeal.getDate().get(Calendar.MONTH), equalTo(meal.getDate().get(Calendar.MONTH)));
        assertThat(newMeal.getDate().get(Calendar.YEAR), equalTo(meal.getDate().get(Calendar.YEAR)));
        assertTrue(newMeal.isLunch());
    }

    @Test
    public void testMealCreationWithDishes() {
        Meal meal = new MealBuilder().withDishes("Beans", "Rice", "Pasta").build();

        long mealId = mealDao.insert(meal);

        assertThat(dishDao.count(), equalTo(3));

        Dish dish = dishDao.findById(meal.getDishes().get(1).getId());
        assertThat(dish.getDishName(), equalTo("Rice"));

        List<Dish> dishes = dishDao.findDishesByMealId(mealId);
        assertThat(dishes.get(1).getDishName(), equalTo("Rice"));

        Meal newMeal = mealDao.findById(mealId);
        assertThat(newMeal.getDishes().get(1).getDishName(), equalTo("Rice"));
    }

    @Test
    public void testMealDeletion() {
        Meal meal = new MealBuilder().build();
        long id = mealDao.insert(meal);
        mealDao.delete(id);

        assertThat(mealDao.count(), equalTo(0));
    }

    @Test
    public void testMealGetByDate() {
        List<Meal> meals = createMeals("2014-02-05", "2014-02-10", "2014-02-12");

        mealDao.insert(meals);

        List<Meal> queryMeals = mealDao.mealsOfTheWeek(fromStringToCalendar("2014-02-07"));
        assertThat(queryMeals.size(), equalTo(1));
        assertThat(queryMeals.get(0).getDate(), equalTo(meals.get(0).getDate()));

        List<Meal> newMeals = mealDao.mealsOfTheWeek(fromStringToCalendar("2014-02-14"));
        assertThat(newMeals.size(), equalTo(2));
        assertThat(newMeals.get(0).getDate(), equalTo(meals.get(1).getDate()));
    }

    @Test
    public void testMealDifferentYear() throws Exception {
        List<Meal> meals = createMeals("2013-02-10", "2014-02-11", "2015-02-12");
        mealDao.insert(meals);

        List<Meal> queryMeals = mealDao.mealsOfTheWeek(fromStringToCalendar("2014-02-10"));
        assertThat(queryMeals.size(), equalTo(1));
        assertThat(queryMeals.get(0).getDate(), equalTo(meals.get(1).getDate()));
    }

    @Test
    public void testMealSameValues() throws Exception {
        List<Meal> meals = createMeals("2014-02-10", "2014-02-10", "2014-02-10");
        mealDao.insert(meals);

        List<Meal> queryMeals = mealDao.mealsOfTheWeek(fromStringToCalendar("2014-02-12"));
        assertThat(queryMeals.size(), equalTo(1));
        assertThat(queryMeals.get(0).getDate(), equalTo(meals.get(0).getDate()));
    }

    @Test
    public void testNewYearWeek() throws Exception {
        List<Meal> meals = createMeals("2014-12-27", "2014-12-31", "2015-01-01", "2015-01-02", "2015-01-04", "2016-01-01");
        mealDao.insert(meals);

        List<Meal> queryMeals = mealDao.mealsOfTheWeek(fromStringToCalendar("2015-01-02"));
        assertThat(queryMeals.size(), equalTo(3));
        assertThat(queryMeals.get(0).getDate(), equalTo(meals.get(1).getDate()));

        queryMeals = mealDao.mealsOfTheWeek(fromStringToCalendar("2014-12-30"));
        assertThat(queryMeals.size(), equalTo(3));
        assertThat(queryMeals.get(1).getDate(), equalTo(meals.get(2).getDate()));
    }

    private List<Meal> createMeals(String... calendars) {

        List<Meal> meals = new ArrayList<>();

        for (String calendar : calendars) {
            meals.add(new MealBuilder().withDate(calendar).build());
        }

        return meals;
    }

    private Calendar fromStringToCalendar(String calendarStr) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            calendar.setTime(sdf.parse(calendarStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
