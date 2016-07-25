package br.uel.easymenu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import javax.inject.Inject;

import br.uel.easymenu.ioc.TestApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.MealService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestJsonResponse {

    @Inject
    MealService mealService;

    private List<Meal> meals;

    @Before
    public void injectVariables() throws Exception {
        TestApp.component().inject(this);
        String mealsJson = JsonUtils.convertJsonToString("success.json");
        meals = mealService.deserializeMeal(mealsJson);
    }

    @Test
    public void mealSize() throws Exception {
        assertThat(meals.size(), equalTo(3));
    }

    @Test
    public void mealPeriods() throws Exception {
        assertThat(meals.get(0).getPeriod(), equalTo(Meal.LUNCH));
        assertThat(meals.get(1).getPeriod(), equalTo(Meal.BREAKFAST));
        assertThat(meals.get(2).getPeriod(), equalTo(Meal.BOTH));
    }

    @Test
    public void mealDishes() throws Exception {
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
}
