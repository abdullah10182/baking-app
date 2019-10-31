package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bakingapp.adapters.RecipeListAdapter;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.rest.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String mBaseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private RecyclerView mRecyclerView;
    private RecipeListAdapter mRecipeListAdapter;
    private GridLayoutManager mLayoutManager;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_recipes_list);
        mLayoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeListAdapter = new RecipeListAdapter(mRecipes);
        mRecyclerView.setAdapter(mRecipeListAdapter);

        getRecipes();
    }


    public void getRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Recipe>> call = jsonPlaceHolderApi.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Code:" + response.code());
                    return;
                }

                List<Recipe> recipes = response.body();
                System.out.println(recipes);
                mRecipeListAdapter.setRecipes(recipes);
                mRecipeListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
