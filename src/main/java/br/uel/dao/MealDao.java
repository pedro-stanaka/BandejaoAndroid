package br.uel.dao;

import br.uel.model.Meal;

import java.util.Calendar;
import java.util.List;

public interface MealDao extends Dao<Meal> {

    public List<Meal> mealsOfTheWeek(Calendar calendar);

    public List<Meal> mealsOfThisWeek();
}
