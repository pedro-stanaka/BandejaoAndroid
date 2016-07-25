package br.uel.easymenu.ioc;

import br.uel.easymenu.gui.MainActivity;
import br.uel.easymenu.gui.MenuActivity;
import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(MenuActivity menuActivity);
}
