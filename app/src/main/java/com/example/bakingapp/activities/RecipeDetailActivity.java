package com.example.bakingapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;
import com.example.bakingapp.fragments.IngredientsStepsFragment;
import com.example.bakingapp.fragments.RecipeListFragment;
import com.example.bakingapp.fragments.StepDetailFragment;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    @Nullable
    @BindView(R.id.v_divider_tablet_land)
    public View mFragmentDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        initIngredientsStepsFragment();
        if(mFragmentDivider != null && savedInstanceState == null){
            initStepDetailFragment(0);
        }
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

    public void initStepDetailFragment(int position)  {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle arguments = new Bundle();
        arguments.putInt("position", position);
        stepDetailFragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_detail_container, stepDetailFragment)
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

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public void setTitleBarText(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }

    @Nullable
    public View getFragmentDivider() {
        return mFragmentDivider;
    }

    public void onClickCalled(int position) {
        initStepDetailFragment(position);
    }
}
