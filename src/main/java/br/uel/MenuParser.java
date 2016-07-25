package br.uel;

import br.uel.model.Meal;

import java.util.List;

public interface MenuParser {
    public List<Meal> parseHtml(String url);
}
