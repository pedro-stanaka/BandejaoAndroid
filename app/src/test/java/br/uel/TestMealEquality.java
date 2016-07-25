package br.uel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class TestMealEquality {

    @Test
    public void mealIsDifferentWithDifferentDishes() throws Exception {
        Meal meal1 = createMeal(Calendar.getInstance(), new Dish("Rice"));
        Meal meal2 = createMeal(Calendar.getInstance(), new Dish("Beans"));

        assertThat(meal1, not(meal2));
    }

    @Test
    public void mealIsEqualMealsWithDifferentTime() throws Exception {
        Meal meal1 = createMeal(Calendar.getInstance(), new Dish("Beans"));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.SECOND, 10);
        Meal meal2 = createMeal(calendar2, new Dish("Beans"));

        assertThat(meal1, equalTo(meal2));
    }

    @Test
    public void mealIsDifferentWithDifferentDate() throws Exception {
        Meal meal1 = createMeal(Calendar.getInstance(), new Dish("Beans"));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        Meal meal2 = createMeal(calendar2, new Dish("Beans"));

        assertThat(meal1, not(meal2));
    }

    @Test
    public void listOfMealsIsEquals() throws Exception {
        List<Meal> meals1 = new ArrayList<Meal>() {{
            add(createMeal(Calendar.getInstance(), new Dish("Rice")));
            add(createMeal(Calendar.getInstance(), new Dish("Beans")));
            add(createMeal(Calendar.getInstance(), new Dish("Burger")));
        }};
        List<Meal> meals2 = new ArrayList<Meal>() {{
            add(createMeal(Calendar.getInstance(), new Dish("Rice")));
            add(createMeal(Calendar.getInstance(), new Dish("Beans")));
            add(createMeal(Calendar.getInstance(), new Dish("Burger")));
        }};

        assertThat(meals1, equalTo(meals2));
    }

    @Test
    public void listOfMealsIsDifferentWithDifferentDishes() throws Exception {
        List<Meal> meals1 = new ArrayList<Meal>() {{
            add(createMeal(Calendar.getInstance(), new Dish("Rice")));
            add(createMeal(Calendar.getInstance(), new Dish("Beans")));
            add(createMeal(Calendar.getInstance(), new Dish("Burger")));
        }};
        List<Meal> meals2 = new ArrayList<Meal>() {{
            add(createMeal(Calendar.getInstance(), new Dish("Rice")));
            add(createMeal(Calendar.getInstance(), new Dish("Beans")));
            // This is different
            add(createMeal(Calendar.getInstance(), new Dish("Pizza")));
        }};

        assertThat(meals1, not(meals2));
    }

    private Meal createMeal(Calendar calendar, List<Dish> dishes) {
        Meal meal = new Meal();
        meal.setDate(calendar);
        meal.setDishes(dishes);
        meal.setPeriod(Meal.LUNCH);
        return meal;
    }

    private Meal createMeal(Calendar calendar, final Dish dish) {
        return createMeal(calendar, new ArrayList<Dish>() {{ add(dish); }});
    }
}
