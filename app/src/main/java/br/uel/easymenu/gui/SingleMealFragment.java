package br.uel.easymenu.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.uel.easymenu.R;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class SingleMealFragment extends Fragment {

    public static String MEAL_BUNDLE = "meal_bundle";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meal_fragment, container, false);
        LinearLayout periodLayout = (LinearLayout) rootView.findViewById(R.id.layout_meals);

        Meal meal = getArguments().getParcelable(MEAL_BUNDLE);

        if (meal.getDishes().size() == 0) {
            String emptyDishes = this.getResources().getString(R.string.empty_dishes);
            addTextViewToLayout(inflater, periodLayout, emptyDishes);
        }

        for (Dish dish : meal.getDishes()) {
            addTextViewToLayout(inflater, periodLayout, dish.getDishName());
        }

        return rootView;
    }

    public void addTextViewToLayout(LayoutInflater inflater, ViewGroup viewGroup, String text) {
        TextView textView = (TextView) inflater.inflate(R.layout.dish_text, null);
        textView.setText(text);
        viewGroup.addView(textView);
    }
}
