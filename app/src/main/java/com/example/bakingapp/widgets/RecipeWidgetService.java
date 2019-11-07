package com.example.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.bakingapp.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.Nullable;

public class RecipeWidgetService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET_INGREDIENT = "com.example.bakingapp.action.update_ingredients_widget";
    private static final String RECIPE_PREFERENCES = "myPrefrences";

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    public static void startActionUpdateIngredientsWidget(Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_INGREDIENT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            final String action = intent.getAction();
            if(ACTION_UPDATE_WIDGET_INGREDIENT.equals(action)) {
                handleActionUpdateIngredientsWidget();
            }
        }
    }

    private void handleActionUpdateIngredientsWidget() {
        SharedPreferences prefs = this.getSharedPreferences(RECIPE_PREFERENCES, MODE_PRIVATE);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String selectedRecipe = prefs.getString("selectedRecipe", "");

        Recipe recipe = gson.fromJson(selectedRecipe, Recipe.class);
        String test = recipe.getName();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateIngredientsWidget(this, appWidgetManager, test, appWidgetIds);

    }
}
