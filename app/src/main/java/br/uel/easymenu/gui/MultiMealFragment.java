package br.uel.easymenu.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.uel.easymenu.R;
import br.uel.easymenu.adapter.LastDishDivider;
import br.uel.easymenu.adapter.MealListAdapter;
import br.uel.easymenu.model.Meal;

public class MultiMealFragment extends Fragment {

    public static final String MEAL_BUNDLE = "meal_args";

    private MealListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<Meal> meals = getArguments().getParcelableArrayList(MEAL_BUNDLE);

        View expandableListLayout = inflater.inflate(R.layout.expandable_listview, container, false);
        RecyclerView recyclerView = (RecyclerView) expandableListLayout.findViewById(R.id.meals_list);
        listAdapter = new MealListAdapter(this.getContext(), meals);
        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new LastDishDivider(this.getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return expandableListLayout;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        listAdapter.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listAdapter.onSaveInstanceState(outState);
    }


}
