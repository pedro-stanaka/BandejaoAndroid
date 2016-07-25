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
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;

public class MealService {

    private final static Map<String, Integer> periodTime = new HashMap<String, Integer>() {{
        put(Meal.BREAKFAST, 10);
        put(Meal.LUNCH, 15);
        put(Meal.DINNER, 23);
        put(Meal.BOTH, 23);
    }};

    private ObjectMapper mapper;

    private MealDao mealDao;

    private UniversityDao universityDao;

    private EventBus eventBus;

    @Inject
    public MealService(ObjectMapper mapper, MealDao mealDao, EventBus eventBus, UniversityDao universityDao) {
        this.mapper = mapper;
        this.mealDao = mealDao;
        this.eventBus = eventBus;
        this.universityDao = universityDao;
    }

    public List<University> deserializeMeal(String json) {
        List<University> universities = null;

        try {
            CollectionType type = mapper.getTypeFactory().
                    constructCollectionType(List.class, University.class);
            universities = mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return universities;
    }

    public void replaceMealsFromCurrentWeek(List<University> universities) {

        for (University university : universities) {

            University persistedUniversity = universityDao.findByName(university.getName());
            if(persistedUniversity == null) {
                Log.i(App.TAG, "Inserting university: " + university.toString());
                long id = universityDao.insert(university);
                university.setId(id);
                persistedUniversity = university;

                // TODO: Move this into UniversityService and UpdateUI only once
                NetworkEvent event = new NetworkEvent(NetworkEvent.Type.SUCCESS);
                eventBus.post(event);
            }

            List<Meal> meals = university.getMeals();
            for (Meal meal : meals) {
                meal.setUniversity(persistedUniversity);
            }

            List<Meal> mealsCurrentWeek = mealDao.mealsOfTheWeek(Calendar.getInstance(), persistedUniversity);
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
                    e.printStackTrace();
                    Log.e(App.TAG, "Error in new meals persistence " + e.getMessage());
                } finally {
                    mealDao.endTransaction();
                }
            }
        }
    }

    public void replaceMealsFromCurrentWeek(List<Meal> meals, University university) {

        if(university.getId() == 0) {
            throw new IllegalArgumentException("Don't pass an unpersisted university to this method");
        }

        List<Meal> mealsCurrentWeek = mealDao.mealsOfTheWeek(Calendar.getInstance(), university);
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
                e.printStackTrace();
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
