package com.example.bakingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.example.bakingapp.widgets.RecipeWidgetProvider;
import com.example.bakingapp.widgets.RecipeWidgetService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailActivity";

    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private Step mStep;
    @Nullable
    @BindView(R.id.v_divider_tablet_land)
    public View mFragmentDivider;
    private static final String RECIPE_PREFERENCES = "myPrefrences";
    private boolean mFromWidget = false;

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
        setRecipeDataInSharedPreferences();
        RecipeWidgetService.startActionUpdateIngredientsWidget(this);
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

        //if came in through widget click
        if(jsonSelectedRecipe == null){
            getDataFromSharedPreferences();
            mFromWidget = true;
            return;
        }
        mRecipe = gson.fromJson(jsonSelectedRecipe, Recipe.class);
        mSteps = mRecipe.getSteps();
        mStep =(Step) mIntent.getSerializableExtra("step");
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
        for (int i = 0; i < mSteps.size(); i++) {
            mSteps.get(i).setStedId(i);
        }
        return mSteps;
    }

    public void setTitleBarText(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!mFromWidget) return false;
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onGoToMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGoToMainActivity() {
        Intent intent = new Intent(RecipeDetailActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Nullable
    public View getFragmentDivider() {
        return mFragmentDivider;
    }

    public void onClickCalled(int position) {
        initStepDetailFragment(position);
    }

    public void setRecipeDataInSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(RECIPE_PREFERENCES, MODE_PRIVATE).edit();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String jsonSelectedRecipe = gson.toJson(mRecipe);

        editor.putString("selectedRecipe", jsonSelectedRecipe);
        editor.commit();
    }

    public void getDataFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(RECIPE_PREFERENCES, MODE_PRIVATE);
        String selectedRecipe = prefs.getString("selectedRecipe", "");

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        mRecipe = gson.fromJson(selectedRecipe, Recipe.class);
        mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();

    }
}
