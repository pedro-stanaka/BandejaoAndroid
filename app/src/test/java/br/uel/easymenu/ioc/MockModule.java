package br.uel.easymenu.ioc;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.greenrobot.eventbus.EventBus;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.service.MealService;

public class MockModule extends AppModule {

    private MealDao mealDaoMock;

    public MockModule(Context context, MealDao mealServiceMock) {
        super(context);
        this.mealDaoMock = mealServiceMock;
    }

    @Override
    public MealService provideMealService(ObjectMapper mapper, MealDao mealDao, EventBus eventBus, UniversityDao universityDao) {
        return new MealService(mapper, this.mealDaoMock, eventBus, universityDao);
    }

}
