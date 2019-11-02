package com.example.bakingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.bakingapp.R;
import com.example.bakingapp.fragments.IngredientsStepsFragment;
import com.example.bakingapp.fragments.StepDetailFragment;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        initStepDetailFragment();
    }

    public void initStepDetailFragment() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_detail_container, stepDetailFragment)
                .commit();
    }
}
