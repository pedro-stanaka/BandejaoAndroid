package br.uel.easymenu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import br.uel.easymenu.model.Meal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestMealEquality {

    @Test
    public void mealIsDifferentWithDifferentDishes() throws Exception {
        MealBuilder builder = new MealBuilder();

        Meal meal = builder.withDishes("Rice").build();
        Meal otherMeal = builder.withDishes("Beans").build();

        assertThat(meal, not(otherMeal));
    }

    @Test
    public void mealIsDifferentWithDifferentPeriod() throws Exception {
        MealBuilder builder = new MealBuilder();
        Meal meal = builder.withPeriod(Meal.LUNCH).build();
        Meal otherMeal = builder.withPeriod(Meal.BREAKFAST).build();

        assertThat(meal, not(otherMeal));
    }

    @Test
    public void mealIsEqualMealsWithDifferentTime() throws Exception {
        MealBuilder builder = new MealBuilder();

        Meal meal = builder.build();
        Meal otherMeal = builder.addSeconds(10).build();

        assertThat(meal, equalTo(otherMeal));
    }

    @Test
    public void mealIsDifferentWithDifferentDate() throws Exception {
        Meal meal = new MealBuilder().build();
        Meal otherMeal = new MealBuilder().addDays(1).build();

        assertThat(meal, not(otherMeal));
    }

    @Test
    public void listOfMealsIsEquals() throws Exception {
        List<Meal> meals = MealBuilder.createFakeMeals();
        List<Meal> otherMeals = MealBuilder.createFakeMeals();

        assertThat(meals, equalTo(otherMeals));
    }

    @Test
    public void listOfMealsIsDifferentWithDifferentDishes() throws Exception {
        List<Meal> meals = MealBuilder.createFakeMeals();
        List<Meal> otherMeals = MealBuilder.createFakeMeals();
        otherMeals.remove(2);

        assertThat(meals, not(otherMeals));
    }
}
