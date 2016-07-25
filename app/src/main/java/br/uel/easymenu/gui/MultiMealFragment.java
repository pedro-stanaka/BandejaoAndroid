package br.uel.easymenu.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealListAdapter;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MultiMealFragment extends Fragment {

    public static final String MEAL_BUNDLE = "meal_args";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.expandable_listview, container, false);

        List<Meal> meals = getArguments().getParcelableArrayList(MEAL_BUNDLE);

        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.periods);
        MealListAdapter adapter = new MealListAdapter(this.getContext(), meals);
        expandableListView.setAdapter(adapter);

        return rootView;
    }

    public void addTextViewToLayout(LayoutInflater inflater, ViewGroup viewGroup, String text) {
        TextView textView = (TextView) inflater.inflate(R.layout.dish_text, null);
        textView.setText(text);
        viewGroup.addView(textView);
    }


}
