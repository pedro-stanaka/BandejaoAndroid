package br.uel;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBar;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import roboguice.RoboGuice;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class TestMenuDisplay {

    MainActivity mainActivity;

    @Before
    public void setupTests() {
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        RoboGuice.getInjector(Robolectric.application).injectMembers(this);
    }

    @Config(reportSdk = 10)
    @Test
    public void testCorrectDisplay() throws Exception {
        ActionBar actionBar = mainActivity.getSupportActionBar();

        assertThat(actionBar.getTabCount(), equalTo(1));
    }
}
