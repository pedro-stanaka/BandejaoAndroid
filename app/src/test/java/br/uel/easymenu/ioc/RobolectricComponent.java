package br.uel.easymenu.ioc;

import br.uel.easymenu.TestDaoMeal;
import br.uel.easymenu.TestIncomingMeal;
import br.uel.easymenu.TestJsonResponse;
import br.uel.easymenu.TestMenuActivity;
import br.uel.easymenu.TestDaoUniversity;
import dagger.Component;

@Component(modules = AppModule.class)
public interface RobolectricComponent {

    public void inject(TestMenuActivity testMenuActivity);

    public void inject(TestJsonResponse testJsonResponse);

    public void inject(TestIncomingMeal testIncomingMeal);

    public void inject(TestDaoMeal testDaoMeal);

    public void inject(TestDaoUniversity testDaoUniversity);
}
