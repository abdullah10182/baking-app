package com.example.bakingapp;

import android.os.SystemClock;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


@RunWith(AndroidJUnit4.class)
public class TestDisplayRecipeDetailTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
        getInstrumentation().waitForIdleSync();
    }

//    @Test
//    public void clickRecipe() {
//        onView(withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//        onView(withId(R.id.tv_ingredients_title)).check(matches(withText("Ingredients")));
//    }

//    @Test
//    public void clickGridViewItem_OpensOrderActivity() {
//        SystemClock.sleep(500);
//        onView(withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//
//        //onView(withId(R.id.tv_ingredients_title)).check(matches(withText("Ingredients")));
//        //onView(withId(R.id.rv_ingredients_list)).check(matches(hasItem(hasDescendant(withText(containsString("Bittersweet"))))));
//        onView(withId(R.id.rv_ingredients_list)).check(matches(hasItems(isDisplayed())));
//        onView(withId(R.id.rv_steps_list)).check(matches(hasItems(isDisplayed())));
//        //SystemClock.sleep(500);
//    }

    @Test
    public void displaysRecipeList() {
        onView(withId(R.id.rv_recipes_list))
                .check(matches(hasMinimumChildCount(1)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


        onView(withId(R.id.rv_steps_list))
                .check(matches(hasMinimumChildCount(1)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        SystemClock.sleep(1500);

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
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