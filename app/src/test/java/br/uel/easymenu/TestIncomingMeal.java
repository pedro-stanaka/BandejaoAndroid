package br.uel.easymenu;

import android.database.sqlite.SQLiteException;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.dao.SqliteMealDao;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.MealService;
import br.uel.easymenu.tables.DbHelper;
import roboguice.RoboGuice;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestIncomingMeal {

    private MealDao mealDaoMock = mock(MealDao.class);

    @Inject
    private MealDao mealDao;

    @Inject
    private MealService mealService;

    @Before
    public void setupTests() {
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
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
    public void testDatabaseRollback() throws Exception {
        // Injection of mock
        Module module = Modules.override(RoboGuice.newDefaultRoboModule(RuntimeEnvironment.application)).with(new TestModule());
        RoboGuice.setBaseApplicationInjector(RuntimeEnvironment.application, Stage.DEVELOPMENT, module);
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
        doThrow(new SQLiteException()).when(mealDaoMock).insert(anyList());

        // Instance Variable Dao is now a mock.
        // Therefore, we have to create one manually
        MealDao manualDao = new SqliteMealDao(RuntimeEnvironment.application);
        List<Meal> firstMeals = MealBuilder.createFakeMeals();
        manualDao.insert(firstMeals);

        List<Meal> exceptionMeals = MealBuilder.createFakeMeals();
        // Removing to confirm the rollback
        exceptionMeals.remove(0);
        mealService.replaceMealsFromCurrentWeek(exceptionMeals);

        assertEquals(manualDao.count(), firstMeals.size());

        RoboGuice.util.reset();
    }

    public class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(MealDao.class).toInstance(mealDaoMock);
        }
    }
}
