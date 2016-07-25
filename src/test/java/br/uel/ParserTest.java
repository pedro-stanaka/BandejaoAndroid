package br.uel;

import java.net.URISyntaxException;
import java.net.URL;

import br.uel.model.Meal;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals(meals.get(0).getDate(), "10/03/14");
        assertEquals(meals.get(1).getDate(), "11/03/14");
    }

    @Test
    public void shouldReturnCorrectDishes() {
        List<String> dishes = meals.get(0).getDishes();

        assertEquals(dishes.get(0), "Arroz");
        assertEquals(dishes.get(1), "Feijão");
    }
}
