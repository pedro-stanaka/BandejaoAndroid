package br.uel.model;

public class Dish {

    private long id;

    private String dishName;

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

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", dishName='" + dishName + '\'' +
                '}';
    }
}
