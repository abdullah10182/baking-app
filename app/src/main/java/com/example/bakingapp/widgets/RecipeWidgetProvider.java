package com.example.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.bakingapp.MainActivity;
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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        System.out.println("updateAppWidget");

        if(recipe != null && recipe.getName() != null) {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        } else{
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.iv_widget_cupcake_image, pendingIntent);
        }

        if(recipe != null && recipe.getName() != null){
            String ingredientsTitle = recipe.getName();
            String ingredientsList = "";
            for(Ingredient ingredient : recipe.getIngredients()) {
                ingredientsList += "- ";
                ingredientsList += ingredient.getQuantity() + " ";
                ingredientsList += ingredient.getMeasure() + " ";
                ingredientsList += ingredient.getIngredient() + "\n";
            }
            views.setTextViewText(R.id.tv_ingredients_title_widget, ingredientsTitle);
            views.setTextViewText(R.id.tv_ingredients_widget, ingredientsList);
            views.setViewVisibility(R.id.tv_ingredients_title_widget, View.VISIBLE);
            views.setViewVisibility(R.id.tv_ingredients_widget, View.VISIBLE);
            views.setViewVisibility(R.id.iv_widget_cupcake_image, View.GONE);
        } else {
            views.setViewVisibility(R.id.iv_widget_cupcake_image, View.VISIBLE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    public static void updateIngredientsWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId );
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Recipe recipe = new Recipe();
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId );
        }
        //RecipeWidgetService.startActionUpdateIngredientsWidget(context);
        System.out.println("onUpdate");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
        views.setOnClickPendingIntent(R.id.iv_widget_cupcake_image, pendingIntent2);
    }

    @Override
        // Enter relevant functionality for when the first widget is created
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }


}

