package br.uel.easymenu.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.uel.easymenu.R;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealFragment extends Fragment {

    public static final String MEAL_ARGS = "meal_args";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meal_fragment, container, false);
        Bundle bundle = getArguments();

        ViewGroup group = (ViewGroup) rootView.findViewById(R.id.layout_dishes);

        Meal meal = bundle.getParcelable(MEAL_ARGS);

        LayoutInflater inflaterTextView = LayoutInflater.from(this.getActivity());

        for (Dish dish : meal.getDishes()) {
            TextView dishTxtView = (TextView) inflaterTextView.inflate(R.layout.dish_text, null);
            dishTxtView.setText(dish.getDishName());
            group.addView(dishTxtView);
        }

        return rootView;
    }


}
