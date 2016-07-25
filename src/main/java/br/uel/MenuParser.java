package br.uel;

import java.net.URL;
import java.util.List;

import br.uel.model.Meal;

public interface MenuParser {
    public List<Meal> parseHtml(URL url);
}
