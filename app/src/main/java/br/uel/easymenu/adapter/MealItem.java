package br.uel.easymenu.adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealItem implements ParentListItem {

    private Meal meal;

    public MealItem(Meal meal) {
        this.meal = meal;
    }

    public static List<MealItem> buildMealItem(List<Meal> meals) {
        List<MealItem> mealItems = new ArrayList<>(meals.size());
        for(Meal meal: meals) {
            mealItems.add(new MealItem(meal));
        }
        return mealItems;
    }

    public Meal getMeal() {
        return meal;
    }

    @Override
    public List<Dish> getChildItemList() {
        return meal.getDishes();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
