package br.uel.service;

import br.uel.dao.MealDao;
import br.uel.model.Meal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.List;

public class MealService {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private MealDao mealDao;

    public List<Meal> deserializeMeal(String json) {
        List<Meal> meals = null;

        CollectionType type = mapper.getTypeFactory().
                constructCollectionType(List.class, Meal.class);
        try {
            meals = mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return meals;
    }

    public void persistMeals(List<Meal> meals) {
        mealDao.insert(meals);
    }
}
