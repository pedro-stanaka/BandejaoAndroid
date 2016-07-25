package br.uel.model;

import java.util.*;

public class Meal {

    private long id;

    private Calendar date;

    private List<Dish> dishes = new ArrayList<Dish>();

    public Meal() { }

    public Meal(long id, Calendar date) {
        this.id = id;
        this.date = date;
    }

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

    public List<Dish> getDishes() {
        return dishes;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", date=" + date +
                ", dishes=" + dishes +
                '}';
    }
}