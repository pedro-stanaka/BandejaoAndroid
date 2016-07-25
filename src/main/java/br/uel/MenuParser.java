package br.uel;

import java.util.List;

import br.uel.model.Meal;

public interface MenuParser {
    public List<Meal> parseHtml(String url);
}
