package com.example.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeDetailActivity;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.Viewholder>{

    private static final String TAG = "IngredientsListAdapter";

    private List<Ingredient> mIngredients;
    private Context mContext;

    public IngredientsListAdapter(Context context, List<Ingredient> ingredients) {
        mIngredients = ingredients;
        mContext = context;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        System.out.println("----" + mIngredients);
        this.mIngredients = ingredients;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_list_item, parent, false);
        Viewholder viewHolder = new Viewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.ingredientTitle.setText(mIngredients.get(position).getIngredient());

    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView ingredientTitle;
        CardView ingredientListCard;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ingredientTitle = itemView.findViewById(R.id.tv_ingredient_list_item_title);
        }
    }




}
