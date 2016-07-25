package br.uel.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Meal implements Parcelable {

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

    private long id;

    //    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT")
    private Calendar date;

    private List<Dish> dishes = new ArrayList<Dish>();

    public Meal() {
    }

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

    public void setDate(Calendar date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(long id) {
        this.id = id;
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

}