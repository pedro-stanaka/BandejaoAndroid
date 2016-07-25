package br.uel.easymenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import br.uel.easymenu.R;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealListAdapter extends ExpandableRecyclerAdapter<MealViewHolder, DishViewHolder> {

    private LayoutInflater inflater;

    public MealListAdapter(Context context, List<Meal> meals) {
        super(MealItem.buildMealItem(meals));
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MealViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View mealView = inflater.inflate(R.layout.parent_meal_item, parentViewGroup, false);
        return new MealViewHolder(mealView);
    }

    @Override
    public DishViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View dishView = inflater.inflate(R.layout.dish_text, childViewGroup, false);
        return new DishViewHolder(dishView);
    }

    @Override
    public void onBindParentViewHolder(MealViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        MealItem mealItem = (MealItem) parentListItem;
        parentViewHolder.bind(mealItem.getMeal());
    }

    @Override
    public void onBindChildViewHolder(DishViewHolder childViewHolder, int position, Object childListItem) {
        Dish dish = (Dish) childListItem;
        childViewHolder.bind(dish);
    }
}
