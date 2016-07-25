package br.uel;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Test
    public void testCorrectDisplay() throws Exception {
        ActionBar actionBar = mainActivity.getActionBar();

        assertThat(actionBar.getTabCount(), equalTo(1));
    }
}
