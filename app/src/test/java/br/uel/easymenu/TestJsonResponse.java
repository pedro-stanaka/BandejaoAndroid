package br.uel.easymenu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import javax.inject.Inject;

import br.uel.easymenu.ioc.RobolectricApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.Serializer;

import static br.uel.easymenu.CalendarFactory.monday;
import static br.uel.easymenu.utils.CalendarUtils.fromCalendarToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestJsonResponse {

    @Inject
    Serializer serializer;

    private List<Meal> meals;

    @Before
    public void injectVariables() throws Exception {
        RobolectricApp.component().inject(this);
        String mealsJson = JsonUtils.convertJsonToString("meals.json");
        meals = serializer.deserialize(mealsJson, Meal.class);
    }

    @Test
    public void mealSize() throws Exception {
        assertThat(meals.size(), equalTo(3));
    }

    @Test
    public void mealPeriods() throws Exception {
        assertThat(meals.get(0).getPeriod(), equalTo(Meal.BREAKFAST));
        assertThat(meals.get(1).getPeriod(), equalTo(Meal.LUNCH));
        assertThat(meals.get(2).getPeriod(), equalTo(Meal.DINNER));
    }

    @Test
    public void mealDishes() throws Exception {
        assertThat(meals.get(0).getDishes().get(0).getDishName(), equalTo("Burger"));
        assertThat(meals.get(1).getDishes().get(0).getDishName(), equalTo("Lettuce"));
        assertThat(meals.get(2).getDishes().get(0).getDishName(), equalTo("Rice"));
    }

    @Test
    public void mealDate() throws Exception {
        assertThat(meals.get(0).getStringDate(), equalTo(fromCalendarToString(monday())));
        assertThat(meals.get(0).getStringDate(), equalTo(fromCalendarToString(monday())));
        assertThat(meals.get(0).getStringDate(), equalTo(fromCalendarToString(monday())));
    }
}
