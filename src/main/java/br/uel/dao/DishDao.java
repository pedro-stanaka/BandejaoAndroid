package br.uel.dao;

import br.uel.model.Dish;

import java.util.List;

public interface DishDao extends Dao<Dish> {

    public List<Dish> findDishesByMealId(long mealId);

}
