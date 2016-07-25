package br.uel.easymenu.service;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealService {

    private ObjectMapper mapper;

    private MealDao mealDao;

    private EventBus eventBus;

    @Inject
    public MealService(ObjectMapper mapper, MealDao mealDao, EventBus eventBus) {
        this.mapper = mapper;
        this.mealDao = mealDao;
        this.eventBus = eventBus;
    }

    public List<Meal> deserializeMeal(String json) {
        List<Meal> meals = null;

        try {
            CollectionType type = mapper.getTypeFactory().
                    constructCollectionType(List.class, Meal.class);
            meals = mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return meals;
    }

    public void replaceMealsFromCurrentWeek(List<Meal> meals) {
        List<Meal> mealsCurrentWeek = mealDao.mealsOfTheWeek(Calendar.getInstance());
        Collections.sort(meals);

        if (!meals.equals(mealsCurrentWeek)) {
            mealDao.beginTransaction();
            try {
                Log.i(App.TAG, "Deleting " + meals.size() + " meals in the database: " + mealsCurrentWeek);

                for (Meal meal : mealsCurrentWeek) {
                    mealDao.delete(meal.getId());
                }
                mealDao.insert(meals);

                Log.i(App.TAG, "Inserting " + meals.size() + " new meals in the database: " + meals);
                mealDao.setTransactionSuccess();

                // Updating UI
                NetworkEvent event = new NetworkEvent(NetworkEvent.Type.SUCCESS);
                eventBus.post(event);
            } catch (Exception e) {
                Log.e(App.TAG, "Error in new meals persistence " + e.getMessage());
            } finally {
                mealDao.endTransaction();
            }
        }
    }
}
