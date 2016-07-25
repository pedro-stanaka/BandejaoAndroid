package br.uel.easymenu.service;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.Meal;

public class MealService {

    private final static Map<String, Integer> periodTime = new HashMap<String, Integer>() {{
        put(Meal.BREAKFAST, 10);
        put(Meal.LUNCH, 15);
        put(Meal.DINNER, 23);
        put(Meal.BOTH, 23);
    }};

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

    public Meal selectMealByTime(List<Meal> meals, Calendar calendar) {
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        PriorityQueue<Meal> queue = new PriorityQueue<>(meals.size(), new Comparator<Meal>() {
            @Override
            public int compare(Meal mealA, Meal mealB) {
                int hourDifferenceA = periodTime.get(mealA.getPeriod()) - hour;
                int hourDifferenceB = periodTime.get(mealB.getPeriod()) - hour;

                if(hourDifferenceA <= 0 && hourDifferenceB > 0) {
                    return +1;
                }

                if(hourDifferenceB <= 0 && hourDifferenceA > 0) {
                    return -1;
                }
                return hourDifferenceA - hourDifferenceB;
            }
        });
        queue.addAll(meals);
        return queue.remove();
    }

    public int selectMealByTimeIndex(List<Meal> meals, Calendar calendar) {
        Meal meal = selectMealByTime(meals, calendar);
        return meals.indexOf(meal);
    }
}
