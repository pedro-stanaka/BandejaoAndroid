package br.uel.easymenu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;

import java.util.List;

import br.uel.easymenu.App;
import br.uel.easymenu.R;
import br.uel.easymenu.gui.DishViewHolder;
import br.uel.easymenu.gui.MealViewHolder;
import br.uel.easymenu.model.Dish;
import br.uel.easymenu.model.Meal;

public class MealListAdapter extends ExpandableRecyclerAdapter<MealViewHolder, DishViewHolder> {

    public static final String MEAL_TAG = "MEAL";
    public static final String DISH_TAG = "DISH";

    private Context context;

    private LayoutInflater inflater;

    public MealListAdapter(Context context, List<Meal> meals, final RecyclerView recyclerView) {
        super(MealItem.buildMealItem(meals));
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        // The position passed to ExpandCollapseListener is the parent position
        // To get the real position, we have to iterate over all the elements and check if it matches the parent element
        // We should pass a listener, instead of a RecyclerView here, but this adapter won't be reused, so it's okay
        this.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                int count = 0;
                ParentListItem item = MealListAdapter.this.getParentItemList().get(position);

                for (Object object : MealListAdapter.this.mItemList) {
                    if (object instanceof ParentWrapper) {
                        ParentWrapper wrapper = (ParentWrapper) object;
                        if (wrapper.getParentListItem() == item) {
                            break;
                        }
                    }
                    count++;
                }
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(count, 0);
            }

            @Override
            public void onListItemCollapsed(int position) { }
        });
    }

    @Override
    public MealViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View mealView = inflater.inflate(R.layout.parent_meal_item, parentViewGroup, false);
        mealView.setTag(MEAL_TAG);
        return new MealViewHolder(mealView);
    }

    @Override
    public DishViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View dishView = inflater.inflate(R.layout.dish_text, childViewGroup, false);
        dishView.setTag(DISH_TAG);
        return new DishViewHolder(dishView, context);
    }

    @Override
    public void onBindParentViewHolder(MealViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        MealItem mealItem = (MealItem) parentListItem;
        parentViewHolder.bind(mealItem.getMeal());
    }

    @Override
    public void onBindChildViewHolder(DishViewHolder childViewHolder, int position, Object childListItem) {
        Dish dish = (Dish) childListItem;
        childViewHolder.bind(dish);
    }
}
