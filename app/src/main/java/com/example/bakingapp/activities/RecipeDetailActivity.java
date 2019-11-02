package com.example.bakingapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.example.bakingapp.fragments.IngredientsStepsFragment;
import com.example.bakingapp.fragments.RecipeListFragment;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private List<Ingredient> mIngredients;
    private  List<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        initIngredientsStepsFragment();
        setRecipeFromIntent();
        initActionBar();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void initIngredientsStepsFragment() {
        IngredientsStepsFragment ingredientsStepsFragment = new IngredientsStepsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_ingredients_steps_container, ingredientsStepsFragment)
                .commit();
    }

    public void setRecipeFromIntent() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        Intent mIntent = getIntent();
        String jsonSelectedRecipe = mIntent.getStringExtra("recipe");
        mRecipe = gson.fromJson(jsonSelectedRecipe, Recipe.class);
        mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mRecipe.getName());
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }
}
