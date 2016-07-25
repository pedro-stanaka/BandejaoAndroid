package br.uel.easymenu.utils;

import br.uel.easymenu.R;
import br.uel.easymenu.model.Meal;

public class ResourceUtils {

    public static int getPeriodResourceId(String period) {
         switch (period) {
            case Meal.LUNCH:
                return R.string.lunch;
            case Meal.BREAKFAST:
                return R.string.breakfast;
            case Meal.BOTH:
                return R.string.both;
            case Meal.DINNER:
                return R.string.dinner;
            default:
                throw new IllegalArgumentException(period + " is not a valid period");
        }
    }
}
