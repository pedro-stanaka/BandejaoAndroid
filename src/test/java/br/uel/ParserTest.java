package br.uel;

import java.net.URISyntaxException;
import java.net.URL;

import br.uel.model.Meal;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private MenuParser menuParser = new MenuParserUel();

    private List<Meal> meals;

    @Before
    public void prepareMeals() {

        URL url = getClass().getResource("/menu_page.html");

        meals = menuParser.parseHtml(url.toString());
    }

    @Test
    public void dateWithCorrectFormat() {
        System.out.println(meals.size());
    }
}
