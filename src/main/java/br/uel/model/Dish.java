package br.uel.model;

public class Dish {

    private long id;

    private String dishName;

    private Meal meal;

    public Dish() { }

    public Dish(String dishName) {
        this.dishName = dishName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "dishName='" + dishName + '\'' +
                ", id=" + id +
                '}';
    }
}
