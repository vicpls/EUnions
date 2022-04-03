package netdesigntool.com.eunions;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import netdesigntool.com.eunions.ui.main.MainActivity;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    //Is Flag icon viewed?
    @Test
    public void isFlagViewed(){

        Matcher<View> m = allOf(withId(R.id.ivFlag), withParent(withTagValue(is("at"))));

        Espresso.onView(m).check(matches(isDisplayed()));

    }

    // Calling About Activity
    @Test
    public void callAboutAct(){

        Espresso.onView(withId(R.id.about)).perform(click());

        Espresso.onView(withText("Credit")).check(matches(isDisplayed()));
    }

    // Calling CountryActivity and getting Info from I
    @Test
    public void callCountryAct(){

        Espresso.onView(ViewMatchers.withTagValue(Matchers.is("at"))).perform(click());

        try {
            Thread.sleep(5000);
        }catch (InterruptedException ignored){}


        //wait for Activity started. Работает не устойчиво.
        /*await().atMost(5, SECONDS)
                .ignoreExceptions()
                .untilAsserted(() ->
                                Espresso.onView(ViewMatchers.withText(R.string.density))
                                        .check(matches(isDisplayed())));*/

        Espresso.onView(withId(R.id.tvCountryName)).check(matches(isDisplayed()));

        // Did info get from Wiki about area and population?
        Espresso.onView(withText(R.string.density)).check(matches(isDisplayed()));

        // Chart is Displayed?
        Espresso.onView(withId(R.id.lChart)).check(matches(isDisplayed()));
    }



}
