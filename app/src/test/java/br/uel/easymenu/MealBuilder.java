package br.uel.easymenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.utils.CalendarUtils;

public class MealBuilder {

    Calendar calendar = Calendar.getInstance();
    String period = Meal.LUNCH;
    List<Dish> dishes = new ArrayList<Dish>();

    public static List<Meal> createFakeMeals() {
        return createFakeMeals(Calendar.getInstance());
    }

    public static List<Meal> createFakeMeals(Calendar... calendars) {
        ArrayList<Meal> fakeMeals = new ArrayList<>();
        for (final Calendar calendar : calendars) {
            fakeMeals.addAll(
                    new ArrayList<Meal>() {{
                        add(new MealBuilder().withCalendar(calendar).withDishes("Rice", "Pizza").withPeriod(Meal.LUNCH).build());
                        add(new MealBuilder().withCalendar(calendar).withDishes("Beans").withPeriod(Meal.BOTH).build());
                        add(new MealBuilder().withCalendar(calendar).withDishes("Burger").withPeriod(Meal.BREAKFAST).build());
                    }});
        }
        return fakeMeals;
    }

    public MealBuilder withPeriod(String period) {
        this.period = period;
        return this;
    }

    public MealBuilder withDate(String date) {
        this.calendar = CalendarUtils.fromStringToCalendar(date);
        return this;
    }

    public MealBuilder addSeconds(int seconds) {
        calendar.add(Calendar.SECOND, seconds);
        return this;
    }

    public MealBuilder addDays(int days) {
        calendar.add(Calendar.DATE, days);
        return this;
    }

    public MealBuilder withDishes(String... dishNames) {
        List<Dish> dishes = new ArrayList<>();
        for (String dishName : dishNames) {
            dishes.add(new Dish(dishName));
        }
        this.dishes = dishes;

        return this;
    }

    public MealBuilder withCalendar(Calendar calendar) {
        this.calendar = calendar;
        return this;
    }

    public Meal build() {
        return new Meal(calendar, period, dishes);
    }
}
