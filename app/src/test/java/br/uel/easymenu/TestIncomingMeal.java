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
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.ioc.RobolectricApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;
import br.uel.easymenu.service.MealService;
import br.uel.easymenu.tables.DbHelper;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
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

    @Inject
    UniversityDao universityDao;

    @Before
    public void setupTests() {
        DbHelper.resetConnection();
        RobolectricApp.component().inject(this);
    }

    @After
    public void closeDatabase() {
        DbHelper.getInstance(RuntimeEnvironment.application).getWritableDatabase().close();
    }

    @Test
    public void testPersistNewMeals() throws Exception {
        List<Meal> meals = MealBuilder.createFakeMeals();
        mealDao.insert(meals);
        assertThat(mealDao.count(), equalTo(meals.size()));
    }

    @Test
    public void testReplaceOldMeals() throws Exception {
        University university = UniversityBuilder.createFakeUniversty();
        List<Meal> meals = university.getMeals();
        universityDao.insertWithMeals(university);
        meals.remove(0);

        mealService.matchMeals(meals, university);
        assertThat(mealDao.count(), equalTo(university.getMeals().size()));
    }

    @Test
    public void twoEqualMealsShouldNotBeReplaced() {
        University university = UniversityBuilder.createFakeUniversty();
        universityDao.insertWithMeals(university);
        List<Meal> firstMeals = university.getMeals();

        List<Meal> secondMeals =  MealBuilder.createFakeMeals();

        mealService.matchMeals(secondMeals, university);
        // It shouldn't remove the first meals and insert the new swapped one
        assertThat(firstMeals.get(0).getId(), equalTo(mealDao.fetchAll().get(0).getId()));
    }

    @Test
    public void mealsShouldNotBeReplacedWithDifferentOrder() throws Exception {
        University university = UniversityBuilder.createFakeUniversty();
        universityDao.insertWithMeals(university);
        List<Meal> firstMeals = university.getMeals();

        List<Meal> swappedMeals = MealBuilder.createFakeMeals();
        Collections.swap(swappedMeals, 0, 2);
        Collections.swap(swappedMeals, 1, 2);

        mealService.matchMeals(swappedMeals, university);
        assertThat(firstMeals.get(0).getId(), equalTo(mealDao.fetchAll().get(0).getId()));
    }

    @Test
    public void testDatabaseRollback() throws Exception {
        // Populating the database first
        University university = UniversityBuilder.createFakeUniversty();
        universityDao.insertWithMeals(university);
        int initialMealsSize = university.getMeals().size();

        // Injection of mock
        MealDao mealDaoMock = mock(MealDao.class);
        doThrow(new SQLiteException()).when(mealDaoMock).insert(anyList());
        RobolectricApp.mockComponent(mealDaoMock).inject(this);

        List<Meal> exceptionMeals = MealBuilder.createFakeMeals();
        // Removing to confirm the rollback
        exceptionMeals.remove(0);
        mealService.matchMeals(exceptionMeals, university);

        assertEquals(initialMealsSize, university.getMeals().size());
    }
}
