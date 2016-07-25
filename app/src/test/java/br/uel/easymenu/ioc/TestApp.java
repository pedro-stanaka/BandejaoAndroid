package br.uel.easymenu.ioc;

import org.robolectric.RuntimeEnvironment;

import br.uel.easymenu.TestMenuActivity;
import br.uel.easymenu.dao.MealDao;
import dagger.Component;
import dagger.Module;

public class TestApp {

    @Component(modules = AppModule.class)
    public interface MockHttpComponent extends AppComponent{
        public void inject(TestMenuActivity testMenuActivity);
    }

    public static TestComponent component() {
        return DaggerTestComponent
                .builder()
                .appModule(new AppModule(RuntimeEnvironment.application))
                .build();
    }

    // This method of passing mocks does not scale
    // If you have more mocks in the tests, check the library https://github.com/fabioCollini/DaggerMock
    public static TestComponent mockComponent(MealDao mockDao) {
        return DaggerTestComponent
                .builder()
                .appModule(new MockModule(RuntimeEnvironment.application, mockDao))
                .build();
    }
}
