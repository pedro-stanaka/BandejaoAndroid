package br.uel.easymenu.adapter;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import br.uel.easymenu.R;
import br.uel.easymenu.model.Dish;

public class DishViewHolder extends ChildViewHolder {

    private TextView dishNameTextView;

    public DishViewHolder(View itemView) {
        super(itemView);
        this.dishNameTextView = (TextView) itemView.findViewById(R.id.dish_text);
    }

    public void bind(Dish dish) {
        this.dishNameTextView.setText(dish.getDishName());
    }
}
