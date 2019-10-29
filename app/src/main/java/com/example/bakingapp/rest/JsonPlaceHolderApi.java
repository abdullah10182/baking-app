package com.example.bakingapp.rest;

import com.example.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
