package br.uel.easymenu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import br.uel.easymenu.gui.MealFragment;
import br.uel.easymenu.model.Meal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MealsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Meal> meals;

    public MealsPagerAdapter(FragmentManager fm, List<Meal> meals) {
        super(fm);
        this.meals = meals;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mealFragment = new MealFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(MealFragment.MEAL_ARGS, meals.get(position));
        mealFragment.setArguments(bundle);

        return mealFragment;
    }

    @Override
    public int getCount() {
        return meals.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar calendar = meals.get(position).getDate();

        DateFormat dateFormatDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        DateFormat dateFormatDayOfWeek = new SimpleDateFormat("E", Locale.getDefault());

        return dateFormatDayOfWeek.format(calendar.getTime()) + "  "  +dateFormatDate.format(calendar.getTime());
    }
}
