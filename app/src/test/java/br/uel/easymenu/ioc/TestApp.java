package br.uel.easymenu.ioc;

import org.robolectric.RuntimeEnvironment;

import br.uel.easymenu.dao.MealDao;

public class TestApp {

    public static TestComponent component() {
        return DaggerTestComponent
                .builder()
                .appModule(new AppModule(RuntimeEnvironment.application))
                .build();
    }

    // This method of passing mocks does not scale
    // If you have more mocks into the test, check the library https://github.com/fabioCollini/DaggerMock
    public static TestComponent mockComponent(MealDao mockDao) {
        return DaggerTestComponent
                .builder()
                .appModule(new MockModule(RuntimeEnvironment.application, mockDao))
                .build();
    }
}
