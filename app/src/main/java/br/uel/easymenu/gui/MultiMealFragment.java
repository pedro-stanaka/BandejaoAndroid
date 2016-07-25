package br.uel.easymenu.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.List;

import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealListAdapter;
import br.uel.easymenu.model.Meal;

public class MultiMealFragment extends Fragment {

    public static final String MEAL_BUNDLE = "meal_args";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<Meal> meals = getArguments().getParcelableArrayList(MEAL_BUNDLE);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.meal_fragment, container, false);
        LinearLayout mealLayout = (LinearLayout) rootView.findViewById(R.id.layout_meals);

        View expandableListLayout = inflater.inflate(R.layout.expandable_listview, container, false);
        ExpandableListView expandableListView = (ExpandableListView) expandableListLayout.findViewById(R.id.periods);
        MealListAdapter adapter = new MealListAdapter(this.getContext(), meals);
        expandableListView.setAdapter(adapter);

        mealLayout.addView(expandableListLayout);
        return rootView;
    }
}
