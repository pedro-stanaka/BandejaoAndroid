package br.uel.easymenu.ioc;

import org.robolectric.RuntimeEnvironment;

import br.uel.easymenu.TestIncomingMeal;
import br.uel.easymenu.TestJsonResponse;
import br.uel.easymenu.TestMenuActivity;
import dagger.Component;

@Component(modules = AppModule.class)
public interface TestComponent {

    public void inject(TestMenuActivity testMenuActivity);

    public void inject(TestJsonResponse testJsonResponse);

    public void inject(TestIncomingMeal testIncomingMeal);
}
