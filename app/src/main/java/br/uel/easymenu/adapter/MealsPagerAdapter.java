package br.uel.easymenu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.uel.easymenu.gui.MealFragment;
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
        ArrayList<Meal> meals = groupedMeals.getMealsByIndex(position);
        bundle.putParcelableArrayList(MealFragment.MEAL_ARGS, meals);

        Fragment mealFragment = new MealFragment();
        mealFragment.setArguments(bundle);
        return mealFragment;
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
