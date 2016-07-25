package br.uel.easymenu.ioc;

import android.content.Context;

import javax.inject.Named;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.service.DefaultResponseHandler;
import br.uel.easymenu.service.MealService;

public class MockModule extends AppModule {

    private MealDao mealDaoMock;

    public MockModule(Context context, MealDao mealServiceMock) {
        super(context);
        this.mealDaoMock = mealServiceMock;
    }

    @Override
    public MealService provideMealService(@Named("url.weekly_meals") String url,
                                          MealDao mealDao,
                                          DefaultResponseHandler meals) {
        return new MealService(url, mealDao, meals);
    }

}
