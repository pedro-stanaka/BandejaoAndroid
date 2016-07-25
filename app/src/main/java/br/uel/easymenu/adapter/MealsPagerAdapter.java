package br.uel.easymenu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import br.uel.easymenu.gui.MultiMealFragment;
import br.uel.easymenu.gui.SingleMealFragment;
import br.uel.easymenu.model.GroupedMeals;
import br.uel.easymenu.model.Meal;
import br.uel.easymenu.utils.CalendarUtils;

public class MealsPagerAdapter extends FragmentStatePagerAdapter {

    private GroupedMeals groupedMeals;

    public MealsPagerAdapter(FragmentManager fm, GroupedMeals meals) {
        super(fm);
        this.groupedMeals = meals;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        Fragment fragment;

        ArrayList<Meal> meals = groupedMeals.getMealsByIndex(position);
        if (meals.size() == 1 && meals.get(0).isBoth()) {
            bundle.putParcelable(SingleMealFragment.MEAL_BUNDLE, meals.get(0));
            fragment = new SingleMealFragment();
        } else {
            bundle.putParcelableArrayList(MultiMealFragment.MEAL_BUNDLE, meals);
            fragment = new MultiMealFragment();
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return groupedMeals.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar calendar = groupedMeals.getDateByIndex(position);
        return CalendarUtils.dayOfWeekName(calendar) + "  " + CalendarUtils.simpleLocaleFormat(calendar);
    }
}
