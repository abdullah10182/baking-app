package com.example.bakingapp.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeDetailActivity;
import com.example.bakingapp.adapters.RecipeListAdapter;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.rest.JsonPlaceHolderApi;
import com.example.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.test.espresso.IdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeListFragment extends Fragment {

    private String mBaseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    @BindView(R.id.rv_recipes_list)
    public RecyclerView mRecyclerView;
    private RecipeListAdapter mRecipeListAdapter;
    private GridLayoutManager mLayoutManager;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private ImageView mHeroImage;
    @BindView(R.id.pb_recipes_list)
    public ProgressBar mProgressBarRecipeList;
    @BindView(R.id.btn_retry_connection)
    public Button mRetryConnection;
    private Context mContext;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public RecipeListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        Context context = rootView.getContext();
        ButterKnife.bind(this, rootView);
        getIdlingResource();

        initRecipeRecycleView(rootView, context);

        if(savedInstanceState == null) {
            getRecipes(context);
        } else {
            setRecipesFromState(savedInstanceState);
        }
        initAppBar(rootView);

        return rootView;
    }

    public void initRecipeRecycleView(View rootView, Context context) {
        MainActivity mainActivity = (MainActivity) getActivity();
        boolean isTablet = mainActivity.isTablet();
        boolean isLandscpae = mainActivity.isLandscape();
        int spanCount = 2;
        if(isTablet && isLandscpae) {
            spanCount = 4;
        } else if(isTablet) {
            spanCount = 3;
        }
        mLayoutManager = new GridLayoutManager(context,spanCount);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeListAdapter = new RecipeListAdapter(context, mRecipes);
        mRecyclerView.setAdapter(mRecipeListAdapter);
    }

    public void getRecipes(Context context) {

        if(!NetworkUtils.isNetworkAvailable(context)) {
            errorLoadingData(context);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Recipe>> call = jsonPlaceHolderApi.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                //SystemClock.sleep(1500);
                System.out.println("retro");
                if(!response.isSuccessful()) {
                    System.out.println("Code:" + response.code());
                    return;
                }

                System.out.println("Code:" + response.code());
                mProgressBarRecipeList.setVisibility(View.INVISIBLE);
                List<Recipe> recipes = response.body();
                recipes.addAll(recipes);
                recipes.addAll(recipes);
                recipes.addAll(recipes);
                mRecipeListAdapter.setRecipes(recipes);
                mRecipeListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                System.out.println(t.getMessage());
            }

        });
        mIdlingResource.setIdleState(true);
    }

    public void initAppBar(View rootView) {
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar_layout);
        mHeroImage = rootView.findViewById(R.id.iv_cupcake_image);
        appBarListener(appBarLayout);
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

    public void errorLoadingData(Context context){
        mProgressBarRecipeList.setVisibility(View.INVISIBLE);
        mRetryConnection.setVisibility(View.VISIBLE);
        Toast.makeText(context, "No internet connection, cannot retrieve recipes at the moment", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_retry_connection)
    void retryButtonClicked(Button button){
        mProgressBarRecipeList.setVisibility(View.VISIBLE);
        mRetryConnection.setVisibility(View.GONE);
        getRecipes(mContext);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String jsonRecipes = gson.toJson(mRecipes);
        outState.putString("recipes", jsonRecipes);
    }

    public void setRecipesFromState(Bundle savedInstanceState) {
        mProgressBarRecipeList.setVisibility(View.INVISIBLE);
        String jsonRecipes =savedInstanceState.getString("recipes");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        TypeToken<ArrayList<Recipe>> token = new TypeToken<ArrayList<Recipe>>() {};
        ArrayList<Recipe> recipes = gson.fromJson(jsonRecipes, token.getType());
        mRecipeListAdapter.setRecipes(recipes);
        mRecipeListAdapter.notifyDataSetChanged();
    }

    @VisibleForTesting
    private void getIdlingResource() {
        if (getActivity() != null) {
            mIdlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getIdlingResource();
            mIdlingResource.setIdleState(false);
        }
    }

}
