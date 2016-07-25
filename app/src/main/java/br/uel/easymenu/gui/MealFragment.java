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

        // TODO: Don't display BOTH
        for (Meal meal : meals) {
            LinearLayout periodLayout = (LinearLayout) inflaterTextView.inflate(R.layout.period_layout, null);

            TextView periodText = (TextView) periodLayout.findViewById(R.id.period_text);
            periodText.setText(meal.getPeriod());

            for (Dish dish : meal.getDishes()) {
                TextView dishTxtView = (TextView) inflaterTextView.inflate(R.layout.dish_text, null);
                dishTxtView.setText(dish.getDishName());
                periodLayout.addView(dishTxtView);
            }

            group.addView(periodLayout);
        }

        return rootView;
    }
}
