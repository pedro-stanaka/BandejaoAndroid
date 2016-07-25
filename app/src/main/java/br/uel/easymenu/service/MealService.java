package br.uel.easymenu.service;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import br.uel.easymenu.App;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.Meal;

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

    public void replaceMealsFromCurrentWeek(List<Meal> meals) {
        List<Meal> mealsCurrentWeek = mealDao.mealsOfTheWeek(Calendar.getInstance());

        if (mealsCurrentWeek != meals) {

            mealDao.beginTransaction();
            try {
                Log.i(App.TAG, "Deleting " + meals.size() + " meals in the database: " + mealsCurrentWeek);

                for (Meal meal : mealsCurrentWeek) {
                    mealDao.delete(meal.getId());
                }
                mealDao.insert(meals);

                Log.i(App.TAG, "Inserting " + meals.size() + " new meals in the database: " + meals);
                mealDao.setTransactionSuccess();
            } catch (Exception e) {
                Log.e(App.TAG, "Error in new meals persistence " + e.getMessage());
            } finally {
                mealDao.endTransaction();
            }
        }
    }
}
