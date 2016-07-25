package br.uel.easymenu.service;

import android.util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.inject.Inject;
import javax.inject.Named;

import br.uel.easymenu.App;
import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;
import br.uel.easymenu.service.DefaultResponseHandler.Action;

public class MealService {

    private final static Map<String, Integer> periodTime = new HashMap<String, Integer>() {{
        put(Meal.BREAKFAST, 10);
        put(Meal.LUNCH, 15);
        put(Meal.DINNER, 23);
        put(Meal.BOTH, 23);
    }};

    private String urlWeeklyMeals;

    private MealDao mealDao;

    private DefaultResponseHandler<Meal> handler;

    @Inject
    public MealService(@Named("url.weekly_meals") String urlWeeklyMeals,
                       MealDao mealDao,
                       DefaultResponseHandler<Meal> handler) {
        this.urlWeeklyMeals = urlWeeklyMeals;
        this.mealDao = mealDao;
        this.handler = handler;
    }

    public void makeRequest(final University university) {
        Action<Meal> action = new Action<Meal>() {
            @Override
            public boolean makeBusiness(List<Meal> meals) {
                return matchMeals(meals, university);
            }
        };
        handler.makeRequest(urlWeeklyMeals, Meal.class, action);
    }

    public boolean matchMeals(List<Meal> meals, University university) {

        if(university.getId() == 0) {
            throw new IllegalArgumentException("Don't pass a non-persisted university to this method " + university);
        }

        List<Meal> mealsCurrentWeek = mealDao.mealsOfTheWeek(Calendar.getInstance(), university);
        Collections.sort(meals);

        return replaceMeals(meals, mealsCurrentWeek);
    }

    private boolean replaceMeals(List<Meal> newMeals, List<Meal> oldMeals) {
        boolean changed = false;
        if (!newMeals.equals(oldMeals)) {
            changed = true;

            mealDao.beginTransaction();
            try {
                Log.i(App.TAG, "Deleting " + oldMeals.size() + " meals in the database: " + oldMeals);

                for (Meal meal : newMeals) {
                    mealDao.delete(meal.getId());
                }
                Log.i(App.TAG, "Inserting " + newMeals.size() + " new meals in the database: " + newMeals);
                mealDao.insert(newMeals);

                mealDao.setTransactionSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(App.TAG, "Error in new meals persistence " + e.getMessage());
            } finally {
                mealDao.endTransaction();
            }
        }
        return changed;
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
