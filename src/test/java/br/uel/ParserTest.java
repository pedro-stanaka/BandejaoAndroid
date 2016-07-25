package br.uel;

import org.junit.Test;

import java.net.URL;
import java.util.List;

import br.uel.model.Meal;

public class ParserTest {

    private MenuParser menuParser = new MenuParserUel();

    @Test
    public void testMeals() {

        URL pageFileUrl = getClass().getResource("/");
        List<Meal> meals = menuParser.parseHtml("http://www.uel.br/ru/pages/cardapio.php");

    }
}
