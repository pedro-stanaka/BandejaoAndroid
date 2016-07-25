package br.uel.easymenu.dao;

import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.Meal;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface MealDao extends Dao<Meal> {

    public List<Meal> mealsOfTheWeek(Calendar calendar);

    public GroupedMeals mealsOfTheWeekGroupedByDay(Calendar calendar);

}
