package netdesigntool.com.eunions;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {


    /*@Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);*/

    /* Instantiate an IntentsTestRule object. */
    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);



    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("netdesigntool.com.eunions", appContext.getPackageName());
    }*/


    //Is Flag icon viewed?
    @Test
    public void isFlagViewed(){

        Matcher<View> m = allOf(withId(R.id.ivFlag), withParent(withTagValue(is("at"))));

        Espresso.onView(m).check(ViewAssertions.matches(isDisplayed()));

    }

    // Calling About Activity
    @Test
    public void callAboutAct(){

        Espresso.onView(withId(R.id.about)).perform(click());

        Intents.intended(hasComponent(hasShortClassName("com.mcsoft.aboutactivity.AboutActivity")));

    }

    // Calling CountryActivity and getting Info from I
    @Test
    public void callCountryAct(){

        Espresso.onView(ViewMatchers.withTagValue(Matchers.is("at"))).perform(click());

        Intents.intended(hasComponent(hasShortClassName("netdesigntool.com.eunions.CountryAct")));

        try {
            Thread.sleep(3000);
        }catch (InterruptedException ignored){}

        //await().atMost(Duration.TWO_SECONDS).until();

        // Did info get from Internet about area and population?
        Espresso.onView(withText(R.string.density)).check(ViewAssertions.matches(isCompletelyDisplayed()));

        Espresso.onView(withId(R.id.tvCountryName)).check(ViewAssertions.matches(isDisplayed()));
    }



}
