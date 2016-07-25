package br.uel.easymenu.dao;

import java.util.Calendar;
import java.util.List;

import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.Meal;

public interface MealDao extends Dao<Meal> {

    public List<Meal> mealsOfTheWeek(Calendar calendar);

    public GroupedMeals mealsOfTheWeekGroupedByDay(Calendar calendar);

}
