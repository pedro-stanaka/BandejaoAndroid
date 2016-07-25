package br.uel.easymenu.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealListAdapter;

public class MealDivider extends RecyclerView.ItemDecoration {

    private Context context;

    private Drawable divider;

    public MealDivider(Context context) {
        this.context = context;
        this.divider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        if (parent.getChildCount() == 1) {
            drawLine(parent.getChildAt(0), c, right, left);
        }

        for (int i = 0, j = 1; j < parent.getChildCount(); i++, j++) {
            View firstView = parent.getChildAt(i);
            View secondView = parent.getChildAt(j);

            boolean dishBeforeMeal = firstView.getTag().equals(MealListAdapter.DISH_TAG) &&
                    secondView.getTag().equals(MealListAdapter.MEAL_TAG);
            boolean lastDish = secondView.getTag().equals(MealListAdapter.DISH_TAG) &&
                    j == (parent.getChildCount() - 1);
            boolean mealWithoutDishes = firstView.getTag().equals(MealListAdapter.MEAL_TAG)
                    && secondView.getTag().equals(MealListAdapter.MEAL_TAG);
            boolean lastMeal = secondView.getTag().equals(MealListAdapter.MEAL_TAG)
                    && j == (parent.getChildCount() - 1);

            if (dishBeforeMeal || mealWithoutDishes || lastDish || lastMeal) {

                if (dishBeforeMeal || mealWithoutDishes) {
                    drawLine(firstView, c, right, left);
                }

                if (lastDish || lastMeal) {
                    drawLine(secondView, c, right, left);
                }
            }
        }
    }

    private void drawLine(View view, Canvas canvas, int right, int left) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        int top = view.getBottom() + params.bottomMargin;
        int bottom = top + divider.getIntrinsicHeight();

        divider.setBounds(left, top, right, bottom);
        divider.draw(canvas);
    }
}
