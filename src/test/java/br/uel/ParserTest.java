package br.uel;

import java.net.URISyntaxException;
import java.net.URL;

import br.uel.model.Dish;
import br.uel.model.Meal;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ParserTest {

    private MenuParser menuParser = new MenuParserUel();

    private List<Meal> meals;

    @Before
    public void prepareMeals() {
        URL url = getClass().getResource("/menu_page.html");
        System.out.println(url.getPath());
        meals = menuParser.parseHtml(url);
    }

    @Test
    public void shouldHaveMealsSize() {
        assertEquals(meals.size(), 6);
    }

    @Test
    public void shouldReturnCorrectDate() {
        Date date = meals.get(0).getDate().getTime();
        assertThat(date, is(createCalendar("10/03/14")));
        assertThat(date, is(createCalendar("10/03/14")));
    }

    private Date createCalendar(String dateString) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal.getTime();
    }

    @Test
    public void shouldReturnCorrectDishes() {
        List<Dish> dishes = meals.get(0).getDishes();

        assertEquals(dishes.get(0).getDishName(), "Arroz");
        assertEquals(dishes.get(1).getDishName(), "Feijão");
    }
}
