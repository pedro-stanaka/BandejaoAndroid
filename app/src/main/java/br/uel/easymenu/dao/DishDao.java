package br.uel.easymenu.dao;

import java.util.List;

import br.uel.easymenu.model.Dish;

public interface DishDao extends Dao<Dish> {

    public List<Dish> findDishesByMealId(long mealId);

}
