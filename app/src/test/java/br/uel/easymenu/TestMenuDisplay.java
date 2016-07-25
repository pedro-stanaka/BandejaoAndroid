package br.uel.easymenu;

import android.support.v7.app.ActionBar;

import br.uel.easymenu.gui.MenuActivity;
import br.uel.easymenu.model.Meal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import br.uel.easymenu.tables.DbHelper;
import roboguice.RoboGuice;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestMenuDisplay {

    private MenuActivity menuActivity;

    @Before
    public void setupTests() {
        menuActivity = Robolectric.buildActivity(MenuActivity.class).create().get();
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
    }

    @Test
    public void testCorrectDisplay() throws Exception {
        ActionBar actionBar = menuActivity.getSupportActionBar();

        assertThat(actionBar.getTabCount(), equalTo(1));
    }

    @Test
    public void testJsonFormatter() throws Exception {
        List<Meal> meals = new ArrayList<Meal>() {{
            add(new MealBuilder().withDishes("Beans", "Rice", "Pasta").build());
            add(new MealBuilder().withDishes("Beans", "Rice", "Pasta").build());
        }};

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(meals);
        //TODO: Finish this test
    }

    @After
    public void cleanDatabase() {
        DbHelper helper = DbHelper.getInstance(RuntimeEnvironment.application);
        helper.close();
    }
}
