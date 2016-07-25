package br.uel.easymenu.gui;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import br.uel.easymenu.R;
import br.uel.easymenu.model.Meal;

public class MealViewHolder extends ParentViewHolder {

    private static final float ROTATED_POSITION = 180f;
    private static final float INITIAL_POSITION = 0.0f;

    private TextView periodTextView;

    private ImageView periodView;

    public MealViewHolder(View itemView) {
        super(itemView);
        this.periodTextView = (TextView) itemView.findViewById(R.id.period_name);
        this.periodView = (ImageView) itemView.findViewById(R.id.expand_image);
    }

    public void bind(Meal meal) {
        int resourceId = getPeriodResource(meal.getPeriod());
        String periodText = periodTextView.getContext().getString(resourceId);
        this.periodTextView.setText(periodText);
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if (expanded) {
            periodView.setRotation(ROTATED_POSITION);
        } else {
            periodView.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        periodView.startAnimation(rotateAnimation);
    }

    private int getPeriodResource(String period) {
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
