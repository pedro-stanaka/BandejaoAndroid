package br.uel.easymenu.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.uel.easymenu.R;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealFragment extends Fragment {

    public static final String MEAL_ARGS = "meal_args";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meal_fragment, container, false);
        LayoutInflater inflaterTextView = LayoutInflater.from(this.getActivity());
        ViewGroup group = (ViewGroup) rootView.findViewById(R.id.layout_meals);

        List<Meal> meals = getArguments().getParcelableArrayList(MEAL_ARGS);

        for (Meal meal : meals) {
            LinearLayout periodLayout = (LinearLayout) inflaterTextView.inflate(R.layout.period_layout, null);
            TextView periodTextView = (TextView) periodLayout.findViewById(R.id.period_text);

            if(meal.getPeriod().equals(Meal.BOTH) && meals.size() == 1) {
                periodLayout.removeView(periodTextView);
            }
            else {
                int resourceId = getPeriodResource(meal.getPeriod());
                String period = this.getResources().getString(resourceId);
                periodTextView.setText(period);
            }

            if (meal.getDishes().size() == 0) {
                String emptyDishes = this.getResources().getString(R.string.empty_dishes);
                addTextViewToLayout(inflaterTextView, periodLayout, emptyDishes);
            }

            for (Dish dish : meal.getDishes()) {
                addTextViewToLayout(inflaterTextView, periodLayout, dish.getDishName());
            }

            group.addView(periodLayout);
        }

        return rootView;
    }

    public void addTextViewToLayout(LayoutInflater inflater, ViewGroup viewGroup, String text) {
        TextView textView = (TextView) inflater.inflate(R.layout.dish_text, null);
        textView.setText(text);
        viewGroup.addView(textView);
    }

    public int getPeriodResource(String period) {
        switch(period) {
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
}
