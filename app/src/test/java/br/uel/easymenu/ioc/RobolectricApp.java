package br.uel.easymenu.ioc;

import org.robolectric.RuntimeEnvironment;

import br.uel.easymenu.TestMenuActivity;
import br.uel.easymenu.dao.MealDao;
import dagger.Component;

public class RobolectricApp {

    public static RobolectricComponent component() {
        return DaggerTestComponent
                .builder()
                .appModule(new AppModule(RuntimeEnvironment.application))
                .build();
    }

    // This method of passing mocks does not scale
    // If you have more mocks in the tests, check the library https://github.com/fabioCollini/DaggerMock
    public static RobolectricComponent mockComponent(MealDao mockDao) {
        return DaggerTestComponent
                .builder()
                .appModule(new MockModule(RuntimeEnvironment.application, mockDao))
                .build();
    }
}
