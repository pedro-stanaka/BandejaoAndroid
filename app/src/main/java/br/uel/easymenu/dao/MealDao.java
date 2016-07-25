package br.uel.easymenu.dao;

import org.joda.time.DateTime;

import java.util.List;

import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;

public interface MealDao extends Dao<Meal> {

    public List<Meal> mealsOfTheWeek(DateTime dateTime, University university);

    public GroupedMeals mealsOfTheWeekGroupedByDay(DateTime dateTime, University university);

}
