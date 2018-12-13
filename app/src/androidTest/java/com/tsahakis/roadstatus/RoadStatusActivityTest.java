package com.tsahakis.roadstatus;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tsahakis.roadstatus.ui.RoadStatusActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class RoadStatusActivityTest {

    @Rule
    public ActivityTestRule<RoadStatusActivity> mRule = new ActivityTestRule<>(RoadStatusActivity.class);

    @Before
    public void registerResource() {
        CountingIdlingResource idlingResource = mRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void make_request_empty_road_id_text() {
        // given
        onView(withId(R.id.road_id)).perform(typeText(""));

        // when
        onView(withId(R.id.button_request)).perform(click());

        // then
        onView(withId(R.id.result_container)).check(matches(isDisplayed()));
        onView(withId(R.id.error_container)).check(matches(not(isDisplayed())));
    }

    @Test
    public void make_request_valid_road_id_text() {
        // given
        onView(withId(R.id.road_id)).perform(typeText("a1"));

        // when
        onView(withId(R.id.button_request)).perform(click());

        // then
        onView(withId(R.id.result_container)).check(matches(isDisplayed()));
        onView(withId(R.id.error_container)).check(matches(not(isDisplayed())));
    }

    @Test
    public void make_request_invalid_road_id_text() {
        // given
        onView(withId(R.id.road_id)).perform(typeText("foo"));

        // when
        onView(withId(R.id.button_request)).perform(click());

        // then
        onView(withId(R.id.result_container)).check(matches(not(isDisplayed())));
        onView(withId(R.id.error_container)).check(matches(isDisplayed()));
        onView(withId(R.id.error_text)).check(matches(withText(R.string.please_enter_valid_road_id)));
    }

    @After
    public void unregisterResource() {
        CountingIdlingResource idlingResource = mRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
