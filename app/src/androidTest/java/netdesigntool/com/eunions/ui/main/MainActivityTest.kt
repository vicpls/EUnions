package netdesigntool.com.eunions.ui.main

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.espresso.action.GeneralLocation
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.chart.DiFbProvider
import netdesigntool.com.eunions.ui.screens.AboutActSc
import netdesigntool.com.eunions.ui.screens.CountryActSc
import netdesigntool.com.eunions.ui.screens.DescFragComposeSc
import netdesigntool.com.eunions.ui.screens.FrOtherCountryListCompSc
import netdesigntool.com.eunions.ui.screens.MainActivitySc
import netdesigntool.com.eunions.ui.screens.MainActivitySc.fab
import netdesigntool.com.eunions.ui.screens.MainActivitySc.menuAbout
import netdesigntool.com.eunions.ui.screens.MainActivitySc.middleFlexBox
import netdesigntool.com.eunions.ui.screens.MainActivitySc.pressBack
import org.junit.Rule
import org.junit.Test

@UninstallModules(DiFbProvider::class)
@HiltAndroidTest
class MainActivityTest: TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @Test
    fun test() = run {

        step("Start MainActivity, EU click.") {

            MainActivitySc {
                EUFlag {
                    isVisible()
                    click()
                }
            }
        }

        step("Is EU description visible?") {
            DescFragComposeSc(composeTestRule).euDesc {
                assertIsDisplayed()
                //performClick()
            }
        }

        step("Is Shengen description visible?") {
            MainActivitySc {
                SchengenFlag {
                    isVisible()
                    click()
                }
            }

            DescFragComposeSc(composeTestRule).schenDesc {
                assertIsDisplayed()
                performClick()
            }
        }

        step("Is About activity visible?") {
            menuAbout {
                isVisible()
                click()
            }

            AboutActSc {
                appLogo {
                    isVisible()
                }
            }
            pressBack()
        }

        step("Click FAB. Is country list visible?") {
            fab {
                isVisible()
                click()
            }

            FrOtherCountryListCompSc(composeTestRule)
                .countryList.assertIsDisplayed()

            pressBack()
        }

        step("Click on first country. Is activity for Austria visible?") {
            middleFlexBox {
                isVisible()
                hasDescendant { withText(R.string.at) }
                click(GeneralLocation.TOP_CENTER)
            }

            CountryActSc{
                flag.isVisible()
                countryName.matches { withText(R.string.at) }
            }
        }

    }
}