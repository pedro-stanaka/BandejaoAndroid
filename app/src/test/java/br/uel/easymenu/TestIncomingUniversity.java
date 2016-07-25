package br.uel.easymenu;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import br.uel.easymenu.dao.MealDao;
import br.uel.easymenu.dao.UniversityDao;
import br.uel.easymenu.ioc.RobolectricApp;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.model.University;
import br.uel.easymenu.service.UniversityService;
import br.uel.easymenu.tables.DbHelper;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TestIncomingUniversity {
    
    @Inject
    UniversityDao universityDao;
    
    @Inject
    UniversityService universityService;

    @Inject
    MealDao mealDao;

    @Before
    public void setupTests() {
        DbHelper.resetConnection();
        RobolectricApp.component().inject(this);
    }

    @After
    public void closeDatabase() {
        DbHelper.getInstance(RuntimeEnvironment.application).getWritableDatabase().close();
    }
    @Test
    public void universityInsertion() throws Exception {
        University university = UniversityBuilder.createFakeUniversty();
        boolean updateUi = universityService. matchUniversity(singletonList(university));
        assertThat(universityDao.count(), equalTo(1));
        assertThat(mealDao.count(), equalTo(university.getMeals().size()));
        assertThat(updateUi, equalTo(true));
    }

    @Test
    public void universtiyMealInsertion() throws Exception {
        University university = UniversityBuilder.createFakeUniversty();
        universityDao.insertWithMeals(university);
        List<Meal> meals = university.getMeals();
        meals.remove(0);

        boolean updateUi = universityService.matchUniversity(singletonList(university));
        assertThat(mealDao.count(), equalTo(meals.size()));
        assertThat(updateUi, equalTo(updateUi));
    }

    @Test
    public void universityDeletion() throws Exception {
        University university1 = new UniversityBuilder().withName("University1").build();
        University university2 = new UniversityBuilder().withName("University2").build();
        University university3 = new UniversityBuilder().withName("University3").build();

        universityDao.insert(Arrays.asList(university1, university2));
        boolean updateUi = universityService.matchUniversity(singletonList(university3));

        assertThat(universityDao.count(), equalTo(1));
        assertThat(universityDao.fetchAll().get(0).getName(), equalTo(university3.getName()));
        assertThat(updateUi, equalTo(updateUi));
    }

    @Test
    public void sameUniversityShouldNotUpdateAnything() throws Exception {
        University university = UniversityBuilder.createFakeUniversty();
        universityDao.insertWithMeals(university);
        boolean updateUi = universityService.matchUniversity(singletonList(university));
        assertThat(updateUi, equalTo(false));
        assertThat(universityDao.count(), equalTo(1));
    }

    @Test
    public void everyOperationsShouldBehaveCorrectly() throws Exception {
        University databaseUniversity = new UniversityBuilder().withName("Db").build();
        University databaseServerUniversity = new UniversityBuilder().withName("DbServer")
                .withMeals(MealBuilder.createFakeMeals()).build();
        University serverUniversity = new UniversityBuilder().withName("Server").build();

        universityDao.insertWithMeals(databaseUniversity);
        universityDao.insertWithMeals(databaseServerUniversity);

        databaseServerUniversity.getMeals().remove(0);

        boolean updateUi = universityService.matchUniversity(Arrays.asList(databaseServerUniversity, serverUniversity));
        assertThat(updateUi, equalTo(updateUi));

        assertThat(universityDao.count(), equalTo(2));
        assertThat(universityDao.fetchAll().get(0).getName(), equalTo(databaseServerUniversity.getName()));
        assertThat(universityDao.fetchAll().get(1).getName(), equalTo(serverUniversity.getName()));

        assertThat(mealDao.count(), equalTo(databaseServerUniversity.getMeals().size()));
    }
}