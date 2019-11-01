package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

import com.example.bakingapp.fragments.RecipeListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        initRecipeListFragment();
    }

    public void initRecipeListFragment() {
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_recipe_list_container, recipeListFragment)
                .commit();
    }

}
