package com.example.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeDetailActivity;
import com.example.bakingapp.adapters.IngredientsListAdapter;
import com.example.bakingapp.adapters.StepsListAdapter;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsStepsFragment extends Fragment {

    private Recipe mRecipe;

    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    private RecyclerView mRecyclerViewIgredients;
    private RecyclerView.LayoutManager mLayoutManagerIngredients;
    private IngredientsListAdapter mIngredientsListAdapter;

    private RecyclerView mRecyclerViewSteps;
    private RecyclerView.LayoutManager mLayoutManagerSteps;
    private StepsListAdapter mStepsListAdapter;

    @Nullable
    @BindView(R.id.v_divider_tablet_land)
    public View mFragmentDivider;


    public IngredientsStepsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients_steps_container, container, false);
        Context context = rootView.getContext();
        ButterKnife.bind(this, rootView);

        getDataFromContainerActivity();
        initIngredientsRecycleView(rootView, context);
        initStepsRecycleView(rootView, context);

        if(mFragmentDivider != null)
        mFragmentDivider.setVisibility(View.GONE);

        return rootView;
    }

    public void getDataFromContainerActivity() {
        RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) getActivity();
        mRecipe = recipeDetailActivity.getRecipe();
        mIngredients = recipeDetailActivity.getIngredients();
        mSteps = recipeDetailActivity.getSteps();
    }

    public void  initIngredientsRecycleView(View rootView, Context context) {
        mRecyclerViewIgredients = rootView.findViewById(R.id.rv_ingredients_list);
        mLayoutManagerIngredients = new LinearLayoutManager(context);
        mRecyclerViewIgredients.setLayoutManager(mLayoutManagerIngredients);
        mIngredientsListAdapter = new IngredientsListAdapter(context, mIngredients);
        mRecyclerViewIgredients.setAdapter(mIngredientsListAdapter);

        mIngredientsListAdapter.setIngredients(mIngredients);
        mIngredientsListAdapter.notifyDataSetChanged();
    }

    public void initStepsRecycleView(View rootView, Context context) {
        mRecyclerViewSteps = rootView.findViewById(R.id.rv_steps_list);
        mLayoutManagerSteps = new LinearLayoutManager(context);
        mRecyclerViewSteps.setLayoutManager(mLayoutManagerSteps);
        RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) getActivity();
        recipeDetailActivity.getFragmentDivider();
        boolean masterDetailLayout  = recipeDetailActivity.getFragmentDivider() != null ? true : false;
        mStepsListAdapter = new StepsListAdapter(context, mSteps, masterDetailLayout);
        mRecyclerViewSteps.setAdapter(mStepsListAdapter);

        mStepsListAdapter.setIngredients(mSteps);
        mStepsListAdapter.notifyDataSetChanged();
    }

    public boolean getMasterDetailLayout() {

        if(mFragmentDivider != null) {
            return true;
        } else {
            return false;
        }
    }
}
