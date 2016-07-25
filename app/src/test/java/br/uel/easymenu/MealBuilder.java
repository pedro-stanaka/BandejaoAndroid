package br.uel.easymenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealBuilder {

    Calendar calendar = Calendar.getInstance();
    String period = Meal.LUNCH;
    List<Dish> dishes = new ArrayList<Dish>();

    public MealBuilder withPeriod(String period) {
        this.period = period;
        return this;
    }

    public MealBuilder withDate(String date)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public MealBuilder withDishes(String...dishNames) {
        List<Dish> dishes = new ArrayList<>();
        for(String dishName : dishNames) {
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

    public static List<Meal> createFakeMeals() {
        return new ArrayList<Meal>() {{
            add(new MealBuilder().withDishes("Rice").withPeriod(Meal.LUNCH).build());
            add(new MealBuilder().withDishes("Beans").withPeriod(Meal.BOTH).build());
            add(new MealBuilder().withDishes("Burger").withPeriod(Meal.BREAKFAST).build());
        }};
    }
}
