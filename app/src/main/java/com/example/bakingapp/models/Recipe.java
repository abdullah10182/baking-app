package com.example.bakingapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    @SerializedName("id")
    private int recipeId;
    private  String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;


    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }
}
