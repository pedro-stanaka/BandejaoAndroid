package br.uel.easymenu.dao;

import br.uel.easymenu.model.Meal;

import java.util.Calendar;
import java.util.List;

public interface MealDao extends Dao<Meal> {

    public List<Meal> mealsOfTheWeek(Calendar calendar);

}
