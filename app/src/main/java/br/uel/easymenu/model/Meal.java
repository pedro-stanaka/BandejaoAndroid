package br.uel.easymenu.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import br.uel.easymenu.utils.CalendarUtils;

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

    @JsonProperty("meal_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private java.util.Calendar date;

    private List<Dish> dishes = new ArrayList<Dish>();

    private String period;

    public Meal() {
    }

    public Meal(java.util.Calendar date, String period, List<Dish> dishes) {
        this.date = date;
        this.period = period;
        this.dishes = dishes;
    }

    public Meal(Parcel in) {
        id = in.readLong();
        date = (java.util.Calendar) in.readSerializable();
        period = in.readString();
        in.readTypedList(dishes, Dish.CREATOR);
    }

    public java.util.Calendar getDate() {
        return date;
    }

    public void setDate(java.util.Calendar date) {
        this.date = date;
    }

    public boolean compareDate(java.util.Calendar compareDate) {
        String thisDate = CalendarUtils.fromCalendarToString(this.date);
        String thatDate = CalendarUtils.fromCalendarToString(compareDate);
        return thisDate.equals(thatDate);
    }

    public String getStringDate() {
        return CalendarUtils.fromCalendarToString(this.date);
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
                ", date=" + CalendarUtils.fromCalendarToString(date) +
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
        if (date != null ? !this.compareDate(meal.date) : meal.date != null) return false;
        if (period != null ? !period.equals(meal.period) : meal.period != null) return false;
        return !(dishes != null ? !dishes.equals(meal.dishes) : meal.dishes != null);
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (dishes != null ? dishes.hashCode() : 0);
        return result;
    }
}