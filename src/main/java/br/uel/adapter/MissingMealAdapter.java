package br.uel.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.uel.R;

public class MissingMealAdapter extends FragmentStatePagerAdapter {

    public MissingMealAdapter(FragmentManager fm) {
        super(fm);
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
//        TODO: I18n this and put in resource
        return "Cardápio Inexistente";
    }

    public static class MissingMealFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            TODO: i18n this and put in resource
            return inflater.inflate(R.layout.missing_meal_fragment, null);
        }
    }

}
