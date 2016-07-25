package br.uel.model;

import java.util.ArrayList;
import java.util.List;

public class Meal {

    private String date;

    private List<String> dishes = new ArrayList<String>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getDishes() {
        return dishes;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }

    public void addDish(String dish) {
        dishes.add(dish);
    }
}