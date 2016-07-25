package br.uel.easymenu.adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealItem implements ParentListItem {

    public static final String EMPTY_DISH = "EMPTY_DISH";

    private Meal meal;

    public MealItem(Meal meal) {
        this.meal = meal;
    }

    public static List<MealItem> buildMealItem(List<Meal> meals) {
        List<MealItem> mealItems = new ArrayList<>(meals.size());
        for (Meal meal : meals) {

            // Add empty dish because it will be hard to add this special case with the way ExpandableRecyclerView works
            if (meal.getDishes().size() == 0) {
                meal.addDish(new Dish(EMPTY_DISH));
            }

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
