package com.example.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeDetailActivity;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;
    @BindView(R.id.tv_ingredients_widget)
    public TextView mIngredientsWidget;
//    @BindView(R.id.iv_widget_cupcake_image)
//    public ImageView mImageViewWidgetCupcake;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String ingredients, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //views.setOnClickPendingIntent(R.id.iv_widget_cupcake_image, pendingIntent);
        views.setTextViewText(R.id.tv_ingredients_widget, ingredients);
        System.out.println("updateAppWidget");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    public static void updateIngredientsWidget(Context context, AppWidgetManager appWidgetManager, String ingredients, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
        System.out.println(ingredients);
            updateAppWidget(context, appWidgetManager, ingredients, appWidgetId );
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RecipeWidgetService.startActionUpdateIngredientsWidget(context);
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        System.out.println("onreice");

    }

    public void setDataFromSharedPreferences(SharedPreferences prefs) {

        String selectedRecipe = prefs.getString("selectedRecipe", "");

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        mRecipe = gson.fromJson(selectedRecipe, Recipe.class);
        //mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();

        System.out.println("setDataFromSharedPreferences" + mRecipe.getName());
        mIngredientsWidget.setText(mRecipe.getName());

        //mImageViewWidgetCupcake.setVisibility(View.GONE);

    }
}

