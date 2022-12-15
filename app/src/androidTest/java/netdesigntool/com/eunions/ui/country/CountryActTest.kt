package netdesigntool.com.eunions.ui.country

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import netdesigntool.com.eunions.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryActTest {

    private lateinit var scenario: ActivityScenario<CountryAct>

    @Before
    fun setUp() {

        TODO("Mock WiKi provider")

        val intent = Intent(ApplicationProvider.getApplicationContext(), CountryAct::class.java)
            .putExtra(CountryAct.COUNTRY_ISO, "ee")
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun isFlagViewed(){
        try {
            Thread.sleep(5000);
        }catch (_ :InterruptedException){}

        Espresso.onView(ViewMatchers.withId(R.id.ivFlag))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun tearDown() {
        scenario.close()
    }

}