package br.uel.model;

import java.util.*;

public class Meal {

    private long id;

    private Calendar date;

    private List<String> dishes = new ArrayList<String>();

    public Calendar getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Calendar date) {
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