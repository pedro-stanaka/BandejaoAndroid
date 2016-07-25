package br.uel.easymenu.service;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.uel.easymenu.App;
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.model.University;

public class UniversityService {

    private UniversityDao universityDao;

    private String urlWeeklyUniversities;

    private MealService mealService;

    private DefaultResponseHandler<University> handler;

    @Inject
    public UniversityService(@Named("url.weekly_meals") String urlWeeklyUniversities,
                             UniversityDao universityDao,
                             MealService mealService,
                             DefaultResponseHandler<University> handler) {
        this.urlWeeklyUniversities = urlWeeklyUniversities;
        this.universityDao = universityDao;
        this.mealService = mealService;
        this.handler = handler;
    }

    public void syncUniversitiesWithServer() {
        DefaultResponseHandler.Action<University> action = new DefaultResponseHandler.Action<University>() {
            @Override
            public boolean makeBusiness(List<University> universities) {
                return matchUniversity(universities);
            }
        };
        handler.makeRequest(urlWeeklyUniversities, University.class, action);
    }

    // TODO: Documentation
    public boolean matchUniversity(List<University> universities) {
        boolean changed = false;
        for(University university : universities) {
            University persistedUniversity = universityDao.findByName(university.getName());

            if(persistedUniversity == null) {
                changed = true;
                Log.i(App.TAG, "Inserting university: " + university.toString());
                long id = universityDao.insert(university);
                university.setId(id);
                persistedUniversity = university;
            }

            boolean changedMeal = mealService.matchMeals(university.getMeals(), persistedUniversity);
            if(changedMeal) changed = true;
        }
        return changed;
    }

    // TODO: Documentation
    public University selectUniversity(University currentUniversity) {
        if (universityDao.count() == 1) {
            currentUniversity =  universityDao.fetchAll().get(0);
        }
        else if (universityDao.count() > 1) {
            if (currentUniversity == null) {
                currentUniversity = universityDao.orderByName().get(0);
            }
            else {
                // Current University may not exist in the database if it was deleted
                University university = universityDao.findByName(currentUniversity.getName());
                if (university == null) {
                    currentUniversity = universityDao.orderByName().get(0);
                }
            }
        }
        return currentUniversity;
    }
}
