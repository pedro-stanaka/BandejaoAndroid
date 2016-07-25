package br.uel.easymenu.ioc;

import br.uel.easymenu.gcm.AppGcmListenerService;
import br.uel.easymenu.gui.MainActivity;
import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.gui.MultiMealFragment;
import br.uel.easymenu.scheduler.BackgroundService;
import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(MenuActivity menuActivity);

    void inject(MultiMealFragment fragment);

    void inject(BackgroundService backgroundService);

    void inject(AppGcmListenerService service);
}
