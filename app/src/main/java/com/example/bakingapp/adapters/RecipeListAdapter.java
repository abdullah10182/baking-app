package com.example.bakingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.Viewholder>{

    private static final String TAG = "RecipeListAdapter";

    private ArrayList<String> mTitles;
    private List<Recipe> mRecipes;

    public RecipeListAdapter(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes.addAll(recipes);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        Viewholder viewHolder = new Viewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.recipeTitle.setText(mRecipes.get(position).getName());

        holder.recipeListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(mRecipes.get(position).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        CardView recipeListCard;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.tv_recipe_list_item_title);
            recipeListCard = itemView.findViewById(R.id.cv_recipe_item);
        }
    }



}
