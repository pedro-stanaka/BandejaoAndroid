package br.uel.easymenu;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;
import br.uel.easymenu.utils.CalendarUtils;

public class MealBuilder {

    DateTime dateTime = DateTime.now();
    String period = Meal.LUNCH;
    List<Dish> dishes = new ArrayList<Dish>();
    University university = new UniversityBuilder().build();

    public static List<Meal> createFakeMeals() {
        return createFakeMeals(DateTime.now());
    }

    public static List<Meal> createFakeMeals(DateTime... dateTimes) {
        ArrayList<Meal> fakeMeals = new ArrayList<>();
        final University university = new UniversityBuilder().withName("Name").build();
        for (final DateTime dateTime : dateTimes) {
            fakeMeals.addAll(
                    new ArrayList<Meal>() {{
                        add(new MealBuilder().withCalendar(dateTime).withUniversity(university)
                                .withDishes("Rice", "Pizza").withPeriod(Meal.LUNCH).build());
                        add(new MealBuilder().withCalendar(dateTime).withUniversity(university)
                                .withDishes("Beans").withPeriod(Meal.BOTH).build());
                        add(new MealBuilder().withCalendar(dateTime).withUniversity(university)
                                .withDishes("Burger").withPeriod(Meal.BREAKFAST).build());
                    }});
        }
        return fakeMeals;
    }

    public MealBuilder withPeriod(String period) {
        this.period = period;
        return this;
    }

    public MealBuilder withDate(String date) {
        this.dateTime = CalendarUtils.fromStringToCalendar(date);
        return this;
    }

    public MealBuilder addSeconds(int seconds) {
        dateTime.plusSeconds(seconds);
        return this;
    }

    public MealBuilder addDays(int days) {
        dateTime = dateTime.plusDays(days);
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

    public MealBuilder withCalendar(DateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public MealBuilder withUniversity(University university) {
        this.university = university;
        return this;
    }

    public Meal build() {
        return new Meal(dateTime, period, dishes, university);
    }
}
