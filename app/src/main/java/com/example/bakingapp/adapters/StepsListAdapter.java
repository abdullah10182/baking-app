package com.example.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.StepDetailActivity;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.Viewholder>{

    private static final String TAG = "StepsListAdapter";

    private List<Step> mSteps;
    private Context mContext;

    public StepsListAdapter(Context context, List<Step> steps) {
        mSteps = steps;
        mContext = context;
    }

    public void setIngredients(List<Step> steps) {
        this.mSteps = steps;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_item, parent, false);
        Viewholder viewHolder = new Viewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        String text = mSteps.get(position).getShortDescription();
        holder.stepTitle.setText(text);


        holder.stepListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchStepDetailActivity(mSteps ,mSteps.get(position));
            }
        });
    }

    public void launchStepDetailActivity(List<Step> steps, Step selectedStep) {
        Intent intent = new Intent(mContext, StepDetailActivity.class);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String stepsJson = gson.toJson(steps);

        intent.putExtra("steps", stepsJson);
        intent.putExtra("step", selectedStep);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView stepTitle;
        LinearLayout stepListItem;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            stepTitle = itemView.findViewById(R.id.tv_steps_list_item_title);
            stepListItem = itemView.findViewById(R.id.ll_step_list_item);
        }
    }




}
