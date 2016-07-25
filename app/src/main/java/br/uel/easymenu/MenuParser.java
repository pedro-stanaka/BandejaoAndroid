package br.uel.easymenu;

import java.net.URL;
import java.util.List;

import br.uel.easymenu.model.Meal;

public interface MenuParser {
    public List<Meal> parseHtml(URL url);
}
