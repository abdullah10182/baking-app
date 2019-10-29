package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.rest.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String mBaseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private TextView mTextResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextResult = findViewById(R.id.tv_recipe_result);
        mTextResult.setText("");

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
                    mTextResult.setText("Code:" + response.code());
                    return;
                }

                List<Recipe> recipes = response.body();
                List<Ingredient> ingredients = new ArrayList<>();

                for (Recipe recipe: recipes) {
                    ingredients = recipe.getIngredients();
                    String content = "";
                    content += "id: " + recipe.getRecipeId() + "\n";
                    content += "name: " + recipe.getName() + "\n";
                    content += "Ingredients: \n";

                    for(Ingredient ingredient : ingredients) {
                        content += ingredient.getIngredient() + "\n";
                        content += "--------------------------------" + "\n";
                    }


                    mTextResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mTextResult.setText(t.getMessage());
            }
        });
    }
}
