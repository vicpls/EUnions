package netdesigntool.com.eunions.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.util.launchHiltFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@MediumTest
class ChartTestFakeVM {

    val whiData = hashMapOf<String, Number>(
        Pair("2010", 5),
        Pair("2011", 5.5),
        Pair("2012", 6),
        Pair("2013", 6.3),
        Pair("2014", 6.4),
        Pair("2015", 6.5),
        Pair("2016", 6.2),
        Pair("2017", 5.9),
        Pair("2018", 5.5),
        Pair("2019", 5),
        Pair("2020", 4.5),
        Pair("2021", 4.3),
        Pair("2022", 4.1)
    )

    val ldWhiT: LiveData<Map<String, Number>> = MutableLiveData(whiData)
    val ldLastYearT: LiveData<Float> = MutableLiveData(2022f)

    @BindValue
    @JvmField
    var vModel: ChartVM = mock()

    @Before
    fun initMockChartVM(){
        `when`(vModel.ldWHI).thenReturn(ldWhiT)
        `when`(vModel.ldLastYear).thenReturn(ldLastYearT)
    }

    //--------------------------------------------------------
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()

        launchHiltFragment { ChartFragment() }
    }


    @Test
    fun testChartFragment() {
        Espresso.onView(withId(R.id.lChart))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}