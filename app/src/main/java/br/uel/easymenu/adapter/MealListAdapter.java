package br.uel.easymenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.uel.easymenu.R;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealListAdapter extends BaseExpandableListAdapter {

    private List<Meal> meals = new ArrayList();

    private Context context;

    private LayoutInflater inflater;

    public MealListAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return meals.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return meals.get(groupPosition).getDishes().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return meals.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return meals.get(groupPosition).getDishes().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.parent_meal_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.meal_period);
        String periodRaw = meals.get(groupPosition).getPeriod();
        String period = context.getResources().getString(getPeriodResource(periodRaw));
        textView.setText(period);

        return convertView;
    }

    public int getPeriodResource(String period) {
        switch (period) {
            case Meal.LUNCH:
                return R.string.lunch;
            case Meal.BREAKFAST:
                return R.string.breakfast;
            case Meal.BOTH:
                return R.string.both;
            case Meal.DINNER:
                return R.string.dinner;
            default:
                throw new IllegalArgumentException(period + " is not a valid period");
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.dish_text, parent, false);
        }

        Dish dish = meals.get(groupPosition).getDishes().get(childPosition);
        TextView textView = (TextView) convertView.findViewById(R.id.dish_text);
        textView.setText(dish.getDishName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
