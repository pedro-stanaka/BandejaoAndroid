package br.uel.easymenu;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.ioc.RobolectricApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.utils.CalendarUtils;

import static br.uel.easymenu.CalendarFactory.monday;
import static br.uel.easymenu.utils.CalendarUtils.dayOfWeekName;
import static br.uel.easymenu.utils.CalendarUtils.fromCalendarToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestDate {

    @Inject
    MealDao mealDao;

    @Test
    public void testDateConversion() throws Exception {
        DateTime dateTime = monday().withHourOfDay(23).withMinuteOfHour(0);

        DecimalFormat df = new DecimalFormat("00");
        String day = df.format(dateTime.getDayOfMonth());
        String convertedDay = fromCalendarToString(dateTime).split("-")[2];
        assertThat(day, equalTo(convertedDay));
    }

    @Test
    public void testShortDate() throws Exception {
        RobolectricApp.component().inject(this);
        Meal meal = new MealBuilder().withDate(fromCalendarToString(monday())).build();
        mealDao.insert(meal);

        Meal mealDb = mealDao.fetchAll().get(0);

        String dayName = dayOfWeekName(mealDb.getDate(), Locale.US);
        assertThat(dayName, equalTo("Mon"));
    }

}
