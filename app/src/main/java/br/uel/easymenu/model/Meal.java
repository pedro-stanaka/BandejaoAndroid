package br.uel.easymenu.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static br.uel.easymenu.utils.CalendarUtils.fromCalendarToString;
import static br.uel.easymenu.utils.CalendarUtils.fromStringToCalendar;

public class Meal implements Parcelable, Comparable<Meal> {

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
    public final static String VEGETARIAN_LUNCH = "VEGETARIAN LUNCH";
    public final static String DINNER = "DINNER";
    public final static String VEGETARIAN_DINNER = "VEGETARIAN DINNER";
    public final static String BOTH = "BOTH";

    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private DateTime date;

    private List<Dish> dishes = new ArrayList<Dish>();

    private String period;

    private University university;

    public Meal() {
    }

    public Meal(DateTime date, String period, List<Dish> dishes, University university) {
        this.date = date;
        this.period = period;
        this.dishes = dishes;
        this.university = university;
    }

    public Meal(Parcel in) {
        id = in.readLong();
        date = fromStringToCalendar(in.readString());
        setPeriod(in.readString());
        in.readTypedList(dishes, Dish.CREATOR);
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime dateTime) {
        this.date = dateTime;
    }

    public boolean compareDate(DateTime dateTime) {
        String thisDate = fromCalendarToString(this.date);
        String thatDate = fromCalendarToString(dateTime);
        return thisDate.equals(thatDate);
    }

    public String getStringDate() {
        return fromCalendarToString(this.date);
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
        if (!(period.equals(Meal.LUNCH) ||
                period.equals(Meal.BOTH) ||
                period.equals(Meal.DINNER) ||
                period.equals(Meal.BREAKFAST) ||
                period.equals(Meal.VEGETARIAN_DINNER) ||
                period.equals(Meal.VEGETARIAN_LUNCH))) {
            throw new IllegalArgumentException("Period can't have the value " + period);
        }
        this.period = period;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
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
        dest.writeString(fromCalendarToString(date));
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

    @Override
    public int compareTo(Meal another) {
        if (this.date.isAfter(another.date)) {
            return 1;
        } else if (this.date.isBefore(another.date)) {
            return -1;
        } else {
            if (rankMeal(this.period) > rankMeal(another.period)) {
                return 1;
            } else if (rankMeal(this.period) < rankMeal(another.period)) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private int rankMeal(String period) {
        switch (period) {
            case Meal.BOTH:
                return 0;
            case Meal.BREAKFAST:
                return 1;
            case Meal.LUNCH:
                return 2;
            case Meal.VEGETARIAN_LUNCH:
                return 3;
            case Meal.DINNER:
                return 4;
            case Meal.VEGETARIAN_DINNER:
                return 5;
        }
        return -1;
    }
}