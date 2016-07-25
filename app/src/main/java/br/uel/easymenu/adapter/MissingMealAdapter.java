package br.uel.easymenu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.uel.easymenu.R;

public class MissingMealAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public MissingMealAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return new MissingMealFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(R.string.nonexistent_meals_title);
    }

    public static class MissingMealFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.missing_meal_fragment, null);
        }
    }

}
