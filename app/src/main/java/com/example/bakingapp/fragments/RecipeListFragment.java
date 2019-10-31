package com.example.bakingapp.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeListAdapter;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.rest.JsonPlaceHolderApi;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeListFragment extends Fragment {

    private String mBaseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private RecyclerView mRecyclerView;
    private RecipeListAdapter mRecipeListAdapter;
    private GridLayoutManager mLayoutManager;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private ImageView mHeroImage;

    public RecipeListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        Context context = rootView.getContext();

        mRecyclerView = rootView.findViewById(R.id.rv_recipes_list);
        mLayoutManager = new GridLayoutManager(context,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeListAdapter = new RecipeListAdapter(mRecipes);
        mRecyclerView.setAdapter(mRecipeListAdapter);

        getRecipes();

        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar_layout);
        mHeroImage = rootView.findViewById(R.id.iv_cupcake_image);
        appBarListener(appBarLayout);

        return rootView;
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

                System.out.println("Code:" + response.code());
                List<Recipe> recipes = response.body();
                recipes.addAll(recipes);
                recipes.addAll(recipes);
                recipes.addAll(recipes);
                System.out.println("------------ "+recipes);
                mRecipeListAdapter.setRecipes(recipes);

                mRecipeListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void appBarListener(AppBarLayout appBarLayout) {

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    AnimatorSet set = new AnimatorSet();
                    int widthOfScreen = appBarLayout.getWidth();
                    int widthHeroImage = mHeroImage.getWidth();
                    set.playTogether(
                            ObjectAnimator.ofFloat(mHeroImage, "translationX", ((widthOfScreen/2f) - (widthHeroImage/2.5f))).setDuration(300),
                            ObjectAnimator.ofFloat(mHeroImage, "rotation", 15).setDuration(500),
                            ObjectAnimator.ofFloat(mHeroImage, "scaleY", .8f).setDuration(500),
                            ObjectAnimator.ofFloat(mHeroImage, "scaleX", .8f).setDuration(500)
                    );
                    set.start();
                } else if (verticalOffset == 0) {
                    // If expanded, then do this
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(
                        ObjectAnimator.ofFloat(mHeroImage, "translationX", 0f).setDuration(300),
                        ObjectAnimator.ofFloat(mHeroImage, "rotation", 0).setDuration(500),
                            ObjectAnimator.ofFloat(mHeroImage, "scaleY", 1f).setDuration(500),
                            ObjectAnimator.ofFloat(mHeroImage, "scaleX", 1f).setDuration(500)
                    );
                    set.start();
                } else {
                    // Somewhere in between
                    // Do according to your requirement
//                    AnimatorSet set = new AnimatorSet();
//                    set.playTogether(
//                            ObjectAnimator.ofFloat(mHeroImage, "translationX", 0f).setDuration(300),
//                            ObjectAnimator.ofFloat(mHeroImage, "rotation", 0).setDuration(500)
//                    );
//                    set.start();
                }
            }
        });
    }
}
