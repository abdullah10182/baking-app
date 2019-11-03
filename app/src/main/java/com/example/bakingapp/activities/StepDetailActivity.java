package com.example.bakingapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.bakingapp.R;
import com.example.bakingapp.fragments.IngredientsStepsFragment;
import com.example.bakingapp.fragments.StepDetailFragment;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity {

    private Step mStep;
    private ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        getDataFromIntent();
        initStepDetailFragment();
        initActionBar();
    }

    public void initStepDetailFragment() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_detail_container, stepDetailFragment)
                .commit();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mStep.getShortDescription());
    }

    public void getDataFromIntent() {
        Intent intent = getIntent();
        mStep = (Step) intent.getSerializableExtra("step");

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String stepsJson = intent.getStringExtra("steps");
        Type listType = new TypeToken<ArrayList<Step>>(){}.getType();
        mSteps = new Gson().fromJson(stepsJson, listType);
    }

    public void setTitleBarText(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public Step getStep() {
        return mStep;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }
}
