package br.uel;

import android.webkit.URLUtil;

import br.uel.model.Dish;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uel.model.Meal;

public class MenuParserUel implements MenuParser {

//    private final static String URL = ";
    private final static int REQUEST_TIMEOUT = 5000;

    static final String FILE_BASE = "file:";
    @Override
    public List<Meal> parseHtml(URL url) {


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

        return meals;
    }

    private Meal iterateDishes(Element mealElement) {
        Meal meal = new Meal();
        for (Element infoElement : mealElement.select("p")) {
            String info = infoElement.text().trim();
            if (info.contains("feira")) {
                meal.setDate(parseDate(info));
            } else {
                if (!info.equals("")) {
                    Dish dish = new Dish();
                    dish.setDishName(info);
                    meal.addDish(dish);
                }
            }

        }
        return meal;
    }

    private Calendar parseDate(String rawDate) {
        String [] dates = rawDate.split(" ");
        String dateString = dates[dates.length - 1];

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    private Document createNewDocument(URL url) {

        Document menuPage = null;
        try {
            // Try to get the page or the file in a maximum time of REQUEST_TIMEOUT
            if (isFileURl(url.toString())) {
                menuPage = Jsoup.parse(new File(url.getPath()), null);
            }
            else{
                menuPage = Jsoup.connect(url.toString()).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuPage;
    }

    private boolean isFileURl(String url) {
        return (null != url) && (url.startsWith(FILE_BASE));
    }
}
