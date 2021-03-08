package netdesigntool.com.eunions;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import netdesigntool.com.eunions.country.CountryAct;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static netdesigntool.com.eunions.country.CountryAct.COUNTRY_ISO;

//@RunWith(AndroidJUnit4.class)
public class ITest2 {

    static Intent intContAct;

    {intContAct = new Intent(getApplicationContext(), CountryAct.class);
        intContAct.putExtra(COUNTRY_ISO, "ee");
    }

//    @BeforeClass
//    public void befor(){
//
//        intContAct.putExtra(COUNTRY_ISO, "ee");
//
//
//    }


    @Rule
    public ActivityScenarioRule<CountryAct> actRule = new ActivityScenarioRule<CountryAct>(intContAct);

    @Test
    public void testCountryActGetDataFromInternet() {

        try {
            Thread.sleep(3000);
        }catch (InterruptedException ignored){}

        Espresso.onView(withText(R.string.density)).check(ViewAssertions.matches(isDisplayed()));
    }
}
