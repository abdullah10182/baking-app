package com.example.bakingapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    private int recipeId;
    private  String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;


    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }
}
