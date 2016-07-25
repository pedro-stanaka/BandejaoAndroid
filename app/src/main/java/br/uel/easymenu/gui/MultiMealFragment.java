package br.uel.easymenu.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import br.uel.easymenu.App;
import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealListAdapter;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.service.MealService;

public class MultiMealFragment extends Fragment {

    public static final String MEAL_BUNDLE = "meal_args";

    @Inject
    protected MealService mealService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((App) this.getContext().getApplicationContext()).component().inject(this);

        List<Meal> meals = getArguments().getParcelableArrayList(MEAL_BUNDLE);

        View expandableListLayout = inflater.inflate(R.layout.expandable_listview, container, false);
        RecyclerView recyclerView = (RecyclerView) expandableListLayout.findViewById(R.id.meals_list);
        MealListAdapter listAdapter = new MealListAdapter(this.getContext(), meals);
        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new MealDivider(this.getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        int index = mealService.selectMealByTimeIndex(meals, Calendar.getInstance());
        listAdapter.expandParent(index);

        return expandableListLayout;
    }
}
