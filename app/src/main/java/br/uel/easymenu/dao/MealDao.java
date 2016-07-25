package br.uel.easymenu.dao;

import java.util.Calendar;
import java.util.List;

import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;

public interface MealDao extends Dao<Meal> {

    public List<Meal> mealsOfTheWeek(Calendar calendar, University university);

    public GroupedMeals mealsOfTheWeekGroupedByDay(Calendar calendar, University university);

}
