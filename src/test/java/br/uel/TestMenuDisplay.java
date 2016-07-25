package br.uel;

import android.support.v7.app.ActionBar;
import br.uel.gui.MenuActivity;
import br.uel.model.Dish;
import br.uel.model.Meal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import roboguice.RoboGuice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class TestMenuDisplay {

    private MenuActivity menuActivity;

    @Before
    public void setupTests() {
        menuActivity = Robolectric.buildActivity(MenuActivity.class).create().get();
        RoboGuice.getInjector(Robolectric.application).injectMembers(this);
    }

    @Config(reportSdk = 10)
    @Test
    public void testCorrectDisplay() throws Exception {
        ActionBar actionBar = menuActivity.getSupportActionBar();

        assertThat(actionBar.getTabCount(), equalTo(1));
    }

    @Test
    public void testJsonFormatter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

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
        System.out.println(json);
    }
}
