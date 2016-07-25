package br.uel.easymenu.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.model.Meal;
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
        handler.makeRequest(urlWeeklyUniversities, University.class, new DefaultResponseHandler.Action<University>() {
            @Override
            public boolean makeBusiness(List<University> universities) {
                return matchUniversity(universities);
            }
        });
    }

    // TODO: Documentation
    // We don't override equals
    public boolean matchUniversity(List<University> serverUniversities) {
        List<String> serverUniversityNames = listOfUniversityNames(serverUniversities);
        List<University> mealsInDbAndServer = universityDao.inNamesList(serverUniversityNames);

        boolean isInsertion = insertUniversities(mealsInDbAndServer, serverUniversities);
        boolean isMealsChanged = syncMealsServer(mealsInDbAndServer, serverUniversities);
        boolean isDeleted = deleteNonServerUniversity(serverUniversityNames);

        return isInsertion || isMealsChanged || isDeleted;
    }

    private boolean insertUniversities(List<University> mealsInDbAndServer, List<University> serverUniversities) {
        boolean changed = false;
        List<String> dbListNames = listOfUniversityNames(mealsInDbAndServer);

        for (University serverUniversity : serverUniversities) {
            if(!dbListNames.contains(serverUniversity.getName())) {
                universityDao.insert(serverUniversity);
                changed = true;
            }
        }
        return changed;
    }

    private boolean syncMealsServer(List<University> mealsInDbAndServer, List<University> incomingUniversities) {
        boolean changed = false;

        for (University databaseUniversity : mealsInDbAndServer) {
            for(University serverUniversity : incomingUniversities) {
                if(serverUniversity.getName().equals(databaseUniversity.getName())) {
                    serverUniversity.setId(databaseUniversity.getId());
                }
            }
        }

        for(University university : incomingUniversities) {
            boolean changedMeal = mealService.matchMeals(university.getMeals(), university);
            if(changedMeal) changed = true;
        }
        return changed;
    }

    private boolean deleteNonServerUniversity(List<String> incomingUniversityNames) {
        boolean changed = false;
        List<University> mealsInDbAndServer = universityDao.notInNamesList(incomingUniversityNames);
        for(University university : mealsInDbAndServer) {
            universityDao.delete(university.getId());
            changed = true;
        }
        return changed;
    }

    private List<String> listOfUniversityNames(List<University> universities) {
        List<String> listUniversityNames = new ArrayList<>(universities.size());
        for (University university : universities) {
            listUniversityNames.add(university.getName());
        }
        return listUniversityNames;
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
