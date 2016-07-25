package br.uel.easymenu;

import com.google.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.MealService;
import roboguice.RoboGuice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestJsonResponse {

    @Inject
    private MealService mealService;

    private List<Meal> meals;

    @Before
    public void injectVariables() {
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
        String mealsJson = convertStreamToString(getClass().getResourceAsStream("/success.json"));
        meals = mealService.deserializeMeal(mealsJson);
    }

    @Test
    public void mealSize() {
        assertThat(meals.size(), equalTo(3));
    }

    @Test
    public void mealPeriods() {
        assertThat(meals.get(0).getPeriod(), equalTo(Meal.LUNCH));
        assertThat(meals.get(1).getPeriod(), equalTo(Meal.BREAKFAST));
        assertThat(meals.get(2).getPeriod(), equalTo(Meal.BOTH));
    }

    @Test
    public void mealDishes() {
        assertThat(meals.get(0).getDishes().get(1).getDishName(), equalTo("Burger"));
        assertThat(meals.get(1).getDishes().get(1).getDishName(), equalTo("Lettuce"));
        assertThat(meals.get(2).getDishes().get(0).getDishName(), equalTo("Rice"));
    }

    @Test
    public void mealDate() throws Exception {
        assertThat(meals.get(0).getStringDate(), equalTo("2016-04-15"));
        assertThat(meals.get(1).getStringDate(), equalTo("2016-04-15"));
        assertThat(meals.get(2).getStringDate(), equalTo("2016-04-16"));
    }

    public String convertStreamToString(java.io.InputStream is) {
        String result = null;
        try {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
