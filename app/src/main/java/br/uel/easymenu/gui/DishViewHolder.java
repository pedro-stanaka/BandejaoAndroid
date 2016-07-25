package br.uel.easymenu.gui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import br.uel.easymenu.R;
import br.uel.easymenu.adapter.MealItem;
import br.uel.easymenu.model.Dish;

import static br.uel.easymenu.utils.StringUtils.filterHtml;

public class DishViewHolder extends ChildViewHolder {

    private TextView dishNameTextView;

    private Context context;

    public DishViewHolder(View itemView, Context context) {
        super(itemView);
        this.dishNameTextView = (TextView) itemView.findViewById(R.id.dish_text);
        this.context = context;
    }

    public void bind(Dish dish) {
        String dishName = (dish.getDishName().equals(MealItem.EMPTY_DISH)) ?
                this.context.getString(R.string.empty_dishes) :
                filterHtml(dish.getDishName());

        this.dishNameTextView.setText(dishName);
    }
}
