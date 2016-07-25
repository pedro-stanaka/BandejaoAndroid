package br.uel;

import br.uel.model.Meal;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private MenuParser menuParser = new MenuParserUel();

    @Test
    public void testMeals() {
        List<Meal> meals = menuParser.parseHtml("http://www.uel.br/ru/pages/cardapio.php");
            
    }
}
