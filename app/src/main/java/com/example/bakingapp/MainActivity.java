package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

import com.example.bakingapp.fragments.RecipeListFragment;

public class MainActivity extends AppCompatActivity {

    private RecipeListFragment mRecipeListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        if(savedInstanceState == null){
            initRecipeListFragment();
        }
    }

    public void initRecipeListFragment() {
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_recipe_list_container, recipeListFragment)
                .commit();
    }

    public boolean isTablet() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLandscape() {
        boolean isLandscape = getResources().getBoolean(R.bool.isLandscape);
        if (isLandscape) {
            return true;
        } else {
            return false;
        }
    }
}
