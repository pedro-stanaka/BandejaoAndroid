package br.uel.easymenu;

import android.database.sqlite.SQLiteException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.ioc.TestApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.MealService;
import br.uel.easymenu.tables.DbHelper;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestIncomingMeal {

    @Inject
    MealDao mealDao;

    @Inject
    MealService mealService;

    @Before
    public void setupTests() {
        DbHelper.resetConnection();
        TestApp.component().inject(this);
    }

    @After
    public void closeDatabase() {
        DbHelper.getInstance(RuntimeEnvironment.application).getWritableDatabase().close();
    }

    @Test
    public void testPersistNewMeals() throws Exception {
        List<Meal> meals = MealBuilder.createFakeMeals();
        mealDao.insert(meals);
        assertEquals(mealDao.count(), meals.size());
    }

    @Test
    public void testReplaceOldMeals() throws Exception {
        List<Meal> meals = MealBuilder.createFakeMeals();
        mealDao.insert(meals);
        meals.remove(0);
        mealService.replaceMealsFromCurrentWeek(meals);
        assertEquals(mealDao.count(), meals.size());
    }

    @Test
    public void twoEqualMealsShouldNotBeReplaced() {
        List<Meal> firstMeals =  MealBuilder.createFakeMeals();
        List<Meal> secondMeals =  MealBuilder.createFakeMeals();

        mealDao.insert(firstMeals);
        mealService.replaceMealsFromCurrentWeek(secondMeals);
        // It shouldn't remove the first meals and insert the new swapped one
        assertEquals(firstMeals.get(0).getId(), mealDao.fetchAll().get(0).getId());
    }

    @Test
    public void mealsShouldNotBeReplacedWithDifferentOrder() throws Exception {
        List<Meal> firstMeals = MealBuilder.createFakeMeals();
        List<Meal> swappedMeals = MealBuilder.createFakeMeals();

        Collections.swap(swappedMeals, 0, 2);
        Collections.swap(swappedMeals, 1, 2);
        mealDao.insert(firstMeals);
        mealService.replaceMealsFromCurrentWeek(swappedMeals);
        assertEquals(firstMeals.get(0).getId(), mealDao.fetchAll().get(0).getId());
    }

    @Test
    public void testDatabaseRollback() throws Exception {
        // Populating the database first
        List<Meal> firstMeals = MealBuilder.createFakeMeals();
        mealDao.insert(firstMeals);
        int mealsSize = mealDao.count();

        // Injection of mock
        MealDao mealDaoMock = mock(MealDao.class);
        doThrow(new SQLiteException()).when(mealDaoMock).insert(anyList());

        TestApp.mockComponent(mealDaoMock).inject(this);

        List<Meal> exceptionMeals = MealBuilder.createFakeMeals();
        // Removing to confirm the rollback
        exceptionMeals.remove(0);
        mealService.replaceMealsFromCurrentWeek(exceptionMeals);

        assertEquals(mealsSize, firstMeals.size());
    }
}
