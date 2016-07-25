package br.uel.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

public class Meal implements Parcelable {

    private long id;

    private Calendar date;

    private List<Dish> dishes = new ArrayList<Dish>();

    public Meal() { }

    public Meal(Calendar date) {
        this.date = date;
    }

    public Meal(Parcel in) {
        id = in.readLong();
        date = (Calendar) in.readSerializable();
        in.readTypedList(dishes, Dish.CREATOR);

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

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", date=" + date.getTimeInMillis() +
                ", dishes=" + dishes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeSerializable(date);

        dest.writeTypedList(dishes);
    }

    public static final Meal.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {

        @Override
        public Meal createFromParcel(Parcel source) {
            return new Meal(source);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };
}