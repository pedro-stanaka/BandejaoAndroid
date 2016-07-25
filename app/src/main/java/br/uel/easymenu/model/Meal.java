package br.uel.easymenu.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    public final static String BREAKFAST = "BREAKFAST";
    public final static String LUNCH = "LUNCH";
    public final static String DINNER = "DINNER";
    public final static String BOTH = "BOTH";

    private long id;

    private Calendar date;

    private List<Dish> dishes = new ArrayList<Dish>();

   private String period;

    public Meal() {
    }

    public Meal(Calendar date, String period) {
        this.date = date;
        this.period = period;
    }

    public Meal(Parcel in) {
        id = in.readLong();
        date = (Calendar) in.readSerializable();
        period = in.readString();
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isBreakfast() {
        return period.equals("BREAKFAST");
    }

    public boolean isLunch() {
        return period.equals("LUNCH");
    }

    public boolean isDinner() {
        return period.equals("DINNER");
    }

    public boolean isBoth() {
        return period.equals("BOTH");
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", date=" + date +
                ", dishes=" + dishes +
                ", period='" + period + '\'' +
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
        dest.writeString(period);

        dest.writeTypedList(dishes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meal meal = (Meal) o;

        // Compare the Date without Time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        if(date != null ? !sdf.format(date.getTime()).equals(sdf.format(meal.date.getTime())) : meal.date != null) return false;
        if(period != null ? !period.equals(meal.period) : meal.period != null) return false;
        return !(dishes != null ? !dishes.equals(meal.dishes) : meal.dishes != null);
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (dishes != null ? dishes.hashCode() : 0);
        return result;
    }
}