package com.example.bakingapp;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;




@RunWith(AndroidJUnit4.class)
public class SelectRecipeBasicTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

//    @Test
//    public void clickRecipe() {
//        onView(withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//        onView(withId(R.id.tv_ingredients_title)).check(matches(withText("Ingredients")));
//    }

    @Test
    public void clickGridViewItem_OpensOrderActivity() {
        SystemClock.sleep(500);
        onView(withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //onView(withId(R.id.tv_ingredients_title)).check(matches(withText("Ingredients")));
        onView(withId(R.id.rv_ingredients_list)).check(matches(hasItem(hasDescendant(withText(containsString("Bittersweet"))))));
        SystemClock.sleep(500);
    }

    public static Matcher<View> hasItem(Matcher<View> matcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override public void describeTo(Description description) {
                description.appendText("has item: ");
                matcher.describeTo(description);
            }

            @Override protected boolean matchesSafely(RecyclerView view) {
                RecyclerView.Adapter adapter = view.getAdapter();
                for (int position = 0; position < adapter.getItemCount(); position++) {
                    int type = adapter.getItemViewType(position);
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(view, type);
                    adapter.onBindViewHolder(holder, position);
                    if (matcher.matches(holder.itemView)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

}