package br.uel;

import android.support.v7.app.ActionBar;

import br.uel.easymenu.BuildConfig;
import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import br.uel.easymenu.tables.DbHelper;
import roboguice.RoboGuice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestMenuDisplay {

    private MenuActivity menuActivity;

    @Before
    public void setupTests() {
        menuActivity = Robolectric.buildActivity(MenuActivity.class).create().get();
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
    }

    @Test
    public void testCorrectDisplay() throws Exception {
        ActionBar actionBar = menuActivity.getSupportActionBar();

        assertThat(actionBar.getTabCount(), equalTo(1));
    }

    @Test
    public void testJsonFormatter() throws Exception {
        List<Meal> meals = new ArrayList<>();

        Meal meal = new Meal(Calendar.getInstance());

        Dish dish1 = new Dish("Beans");
        Dish dish2 = new Dish("Rice");
        Dish dish3 = new Dish("Pasta");

        meal.addDish(dish1);
        meal.addDish(dish2);
        meal.addDish(dish3);

        meals.add(meal);

        meal = new Meal(Calendar.getInstance());

        dish1 = new Dish("Beans");
        dish2 = new Dish("Rice");
        dish3 = new Dish("Pasta");

        meal.addDish(dish1);
        meal.addDish(dish2);
        meal.addDish(dish3);
        meals.add(meal);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(meals);
    }

    @After
    public void cleanDatabase() {
        DbHelper helper = DbHelper.getInstance(RuntimeEnvironment.application);
        helper.close();
    }
}
