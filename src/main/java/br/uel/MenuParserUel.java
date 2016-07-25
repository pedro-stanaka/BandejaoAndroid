package br.uel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.uel.model.Meal;

public class MenuParserUel implements MenuParser {

//    private final static String URL = ";
    private final static int REQUEST_TIMEOUT = 5000;

    @Override
    public List<Meal> parseHtml(String url) {


        Document menuPage = createNewDocument(url);

        List<Meal> meals = iterateMeals(menuPage);

        return meals;
    }

    private List<Meal> iterateMeals(Document menuPage) {

        List<Meal> meals = new ArrayList<Meal>();
        menuPage.select(":containsOwn(\u00a0)").remove();

        for (Element mealElement : menuPage.select("#conteudo2CT table td")) {


            meals.add(iterateDishes(mealElement));
        }
        return null;
    }

    private Meal iterateDishes(Element mealElement) {
        Meal meal = new Meal();
        for (Element infoElement : mealElement.select("p")) {
            String info = infoElement.text().trim();
            if (info.contains("feira")) {
                meal.setDate(parseDate(info));
                System.out.println("Data => " + info);
            } else {
                if (!info.equals("")) {
                    System.out.println(info);
                    meal.addDish(info);
                }
            }

        }
        return meal;
    }

    //TODO
    private String parseDate(String rawDate) {

        return rawDate;
    }

    private Document createNewDocument(String url) {

        Document menuPage = null;
        try {
            // Try to get the page or the file in a maximum time of REQUEST_TIMEOUT
            menuPage = Jsoup.parse(new URL(url), REQUEST_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuPage;
    }


}
