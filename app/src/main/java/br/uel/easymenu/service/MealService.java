package br.uel.easymenu.service;

import android.util.Log;
import br.uel.easymenu.App;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.Meal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.Calendar;
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

        if(meals == null) {
            throw new RuntimeException("Json not valid: " + json);
        }

        return meals;
    }

    public void replaceMealsFromCurrentWeak(List<Meal> meals) {
        List<Meal> mealsCurrentWeek = mealDao.mealsOfTheWeek(Calendar.getInstance());

        Log.d(App.TAG, "Deleting " + meals.size() + " meals in the database: " + mealsCurrentWeek);

        for (Meal meal : mealsCurrentWeek) {
            mealDao.delete(meal.getId());
        }

        Log.d(App.TAG, "Inserting " + meals.size() + " new meals in the database: " + meals);
        mealDao.insert(meals);
    }
}
