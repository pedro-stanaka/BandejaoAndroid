package br.uel.easymenu.ioc;

import org.robolectric.RuntimeEnvironment;

import br.uel.easymenu.TestDaoMeal;
import br.uel.easymenu.TestDaoUniversity;
import br.uel.easymenu.TestDate;
import br.uel.easymenu.TestIncomingMeal;
import br.uel.easymenu.TestIncomingUniversity;
import br.uel.easymenu.TestJsonResponse;
import br.uel.easymenu.TestMealByHour;
import br.uel.easymenu.dao.MealDao;
import dagger.Component;

public class RobolectricApp {

    public static RobolectricComponent component() {
        return DaggerRobolectricApp_RobolectricComponent
                .builder()
                .appModule(new AppModule(RuntimeEnvironment.application))
                .build();
    }

    // This method of passing mocks does not scale
    // If you have more mocks in the tests, check the library https://github.com/fabioCollini/DaggerMock
    public static RobolectricComponent mockComponent(MealDao mockDao) {
        return DaggerRobolectricApp_RobolectricComponent
                .builder()
                .appModule(new MockModule(RuntimeEnvironment.application, mockDao))
                .build();
    }

    @Component(modules = AppModule.class)
    public interface RobolectricComponent {
        void inject(TestJsonResponse testJsonResponse);

        void inject(TestIncomingMeal testIncomingMeal);

        void inject(TestDaoMeal testDaoMeal);

        void inject(TestDaoUniversity testDaoUniversity);

        void inject(TestMealByHour testMealByHour);

        void inject(TestDate testDate);

        void inject(TestIncomingUniversity testIncomingUniversity);
    }
}
