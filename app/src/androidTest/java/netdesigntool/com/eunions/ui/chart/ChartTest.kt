package netdesigntool.com.eunions.ui.chart

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.util.launchHiltFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@UninstallModules(DiFbProvider::class)
@HiltAndroidTest
@MediumTest
class ChartTest {

    /*@Inject
    lateinit var vModel: ChartVM*/

    /*@Inject
    lateinit var vm: ChartVM*/


    /*@BindValue
    var vModel: ChartVM = ChartVM(fbA)*/

    /*@BindValue
    var vModel: ChartVM = ChartVM()*/

    /*@BindValue
    var vModel: ChartViewModel = ChartVMt()*/



    //--------------------------------------------------------
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()

        launchHiltFragment { ChartFragment() }
    }



    @Before
    fun a(){

        //(vModel.ldWHI as MutableLiveData<Map<String,Number>>).value = whiData

        /*val f = { _: Map<String,Number> -> }
        requestWHI("","", f)
        requestRankWHI("","", f)*/
    }


    @Test
    fun testChartFragment() {
        println( "vm.whiSize == 1234567 ")
        Espresso.onView(withId(R.id.lChart))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    private val whiData = hashMapOf<String, Number>(
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

}