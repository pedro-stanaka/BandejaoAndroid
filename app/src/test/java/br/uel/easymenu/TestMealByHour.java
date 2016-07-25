package br.uel.easymenu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.uel.easymenu.ioc.RobolectricApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.MealService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestMealByHour {

    @Inject
    MealService mealService;

    @Before
    public void inject() {
        RobolectricApp.component().inject(this);
    }

    @Test
    public void testMealSelectionByHourOfDay() throws Exception {
        List<Meal> meals = createMealsWithDifferentPeriods();
        assertMealsTime(meals);
    }

    @Test
    public void testMealSelectionByHourOfDayIndex() throws Exception {
        List<Meal> meals = createMealsWithDifferentPeriods();
        Collections.swap(meals, 1, 2);
        Collections.swap(meals, 0, 2);
        assertMealsTime(meals);
    }

    private void assertMealsTime(List<Meal> meals) {
        assertMealTime(meals, Meal.BREAKFAST, 7);
        assertMealTime(meals, Meal.LUNCH, 10);
        assertMealTime(meals, Meal.LUNCH, 13);
        assertMealTime(meals, Meal.DINNER, 15);
        assertMealTime(meals, Meal.DINNER, 20);
        assertMealTime(meals, Meal.BREAKFAST, 23);
    }

    private void assertMealTime(List<Meal> meals, String period, int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        Meal meal = mealService.selectMealByTime(meals, calendar);
        assertThat(meal.getPeriod(), equalTo(period));
    }

    private List<Meal> createMealsWithDifferentPeriods() throws Exception {
        Meal mealBreakfast = new MealBuilder().withPeriod(Meal.BREAKFAST).build();
        Meal mealLunch = new MealBuilder().withPeriod(Meal.LUNCH).build();
        Meal mealDinner = new MealBuilder().withPeriod(Meal.DINNER).build();

        return Arrays.asList(mealBreakfast, mealLunch, mealDinner);
    }
}
